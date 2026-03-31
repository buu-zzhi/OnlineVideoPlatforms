package com.easylive.web.controller;

import com.easylive.web.annotation.GlobalInterceptor;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.easylive.entity.dto.TokenUserInfoDto;
import com.easylive.entity.enums.ResponseCodeEnum;
import com.easylive.entity.enums.VideoStatusEnum;
import com.easylive.entity.po.VideoInfoFilePost;
import com.easylive.entity.po.VideoInfoPost;
import com.easylive.entity.query.VideoInfoFilePostQuery;
import com.easylive.entity.query.VideoInfoPostQuery;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.vo.ResponseVO;
import com.easylive.entity.vo.VideoPostEditInfoVO;
import com.easylive.entity.vo.VideoStatusCountInfoVO;
import com.easylive.exception.BusinessException;
import com.easylive.service.VideoInfoFilePostService;
import com.easylive.service.VideoInfoPostService;
import com.easylive.service.VideoInfoService;
import com.easylive.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import com.easylive.entity.config.AppConfig;
import com.easylive.entity.constants.Constants;
import com.easylive.utils.FFmpegUtils;

@RestController
@RequestMapping("/ucenter")
@Validated
@Slf4j
@GlobalInterceptor(checkLogin = true)
public class UcenterVideoPostController extends ABaseController {
    @Resource
    private VideoInfoPostService videoInfoPostService;

    @Resource
    private VideoInfoFilePostService  videoInfoFilePostService;

    @Resource
    private VideoInfoService videoInfoService;

    @Resource
    private DashScopeApi dashScopeApi;

    @Resource
    private AppConfig appConfig;

    @Resource
    private FFmpegUtils fFmpegUtils;

    @Value("${spring.ai.dashscope.chat.options.model:qwen-vl-plus-2025-05-07}")
    private String dashscopeModel;

    @RequestMapping("/analyzeVideo")
    public ResponseVO analyzeVideo(@NotEmpty String filePath) {
        String tempFilePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER + filePath;
        File tempDir = new File(tempFilePath);
        File[] chunkFiles = tempDir.listFiles(File::isFile);
        if (chunkFiles == null || chunkFiles.length == 0) {
            throw new BusinessException("文件不存在");
        }
        Arrays.sort(chunkFiles, Comparator.comparing(File::getName));

        File firstChunk = chunkFiles[0];
        String framePath = null;
        try {
            framePath = fFmpegUtils.extractKeyFrame(firstChunk.getAbsolutePath());
            File frameFile = new File(framePath);
            if (!frameFile.exists() || frameFile.length() == 0) {
                throw new BusinessException("视频关键帧提取失败");
            }

            String promptText = "请分析这张视频关键帧图像，并按如下格式返回，不要返回其余无关信息：\n"
                    + "视频名称：[提取或生成的100字以内视频名称]\n"
                    + "标签：[根据画面内容生成的标签，最多300字，逗号分隔]\n"
                    + "简介：[根据画面提取的视频简介，最多2000字]";

            String imageDataUri = buildImageDataUri(frameFile);
            List<DashScopeApi.ChatCompletionMessage.MediaContent> contents = Arrays.asList(
                    new DashScopeApi.ChatCompletionMessage.MediaContent("image", null, imageDataUri, null),
                    new DashScopeApi.ChatCompletionMessage.MediaContent(promptText)
            );

            DashScopeApi.ChatCompletionMessage message = new DashScopeApi.ChatCompletionMessage(
                    contents,
                    DashScopeApi.ChatCompletionMessage.Role.USER
            );
            DashScopeApi.ChatCompletionRequest request = new DashScopeApi.ChatCompletionRequest(
                    dashscopeModel,
                    new DashScopeApi.ChatCompletionRequestInput(List.of(message)),
                    new DashScopeApi.ChatCompletionRequestParameter(),
                    Boolean.FALSE,
                    Boolean.TRUE
            );
            long startTime = System.currentTimeMillis();
            DashScopeApi.ChatCompletion response = dashScopeApi.chatCompletionEntity(request).getBody();
            if (response == null || response.output() == null) {
                throw new BusinessException("AI分析结果为空");
            }
            long endTime = System.currentTimeMillis();

            String responseText = response.output().text();
            if ((responseText == null || responseText.trim().isEmpty())
                    && response.output().choices() != null
                    && !response.output().choices().isEmpty()
                    && response.output().choices().get(0).message() != null) {
                responseText = response.output().choices().get(0).message().content();
            }
            if (responseText == null || responseText.trim().isEmpty()) {
                throw new BusinessException("AI分析结果为空");
            }

            Map<String, String> result = parseAnalyzeResult(responseText);
            result.put("Model Consume Time:", String.valueOf((endTime - startTime) / 1000));
            return getSuccessResponseVO(result);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("视频AI分析失败, model={}, framePath={}", dashscopeModel, framePath, e);
            throw new BusinessException("视频分析失败，请检查模型配置或图片格式");
        } finally {
            deleteFrameImage(framePath);
        }
    }

    private Map<String, String> parseAnalyzeResult(String responseText) {
        Map<String, String> data = new HashMap<>();
        String videoName = "";
        String tags = "";
        String introduction = "";

        for (String line : responseText.split("\n")) {
            if (line.contains("视频名称：")) {
                videoName = line.substring(line.indexOf("视频名称：") + 5).trim();
            } else if (line.contains("标签：")) {
                tags = line.substring(line.indexOf("标签：") + 3).trim();
            } else if (line.contains("简介：")) {
                introduction = line.substring(line.indexOf("简介：") + 3).trim();
            }
        }

        data.put("videoName", videoName.length() > 100 ? videoName.substring(0, 100) : videoName);
        data.put("tags", tags.length() > 300 ? tags.substring(0, 300) : tags);
        data.put("introduction", introduction.length() > 2000 ? introduction.substring(0, 2000) : introduction);
        return data;
    }

    private String buildImageDataUri(File frameFile) throws IOException {
        byte[] imageBytes = Files.readAllBytes(frameFile.toPath());
        String mimeType = Files.probeContentType(frameFile.toPath());
        if (mimeType == null || mimeType.trim().isEmpty()) {
            mimeType = "image/jpeg";
        }
        return "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(imageBytes);
    }

    private void deleteFrameImage(String framePath) {
        if (framePath == null || framePath.trim().isEmpty()) {
            return;
        }
        File frameFile = new File(framePath);
        if (frameFile.exists() && !frameFile.delete()) {
            log.warn("删除关键帧图片失败: {}", framePath);
        }
    }

    /**
     * 发布视频
     */
    @RequestMapping("/postVideo")
    public ResponseVO postVideo(String videoId,
                                @NotEmpty String videoCover,
                                @NotEmpty @Size(max=100) String videoName,
                                @NotNull Integer pCategoryId,
                                Integer categoryId,
                                @NotNull Integer postType,
                                @NotEmpty @Size(max=300) String tags,
                                @Size(max=2000) String introduction,
                                @Size(max=3) String interaction,
                                @NotEmpty String uploadFileList) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        List<VideoInfoFilePost> filePostList = JsonUtils.convertJsonArray2List(uploadFileList, VideoInfoFilePost.class);

        VideoInfoPost videoInfoPost = new VideoInfoPost();
        videoInfoPost.setVideoId(videoId);
        videoInfoPost.setVideoName(videoName);
        videoInfoPost.setVideoCover(videoCover);
        videoInfoPost.setPCategoryId(pCategoryId);
        videoInfoPost.setCategoryId(categoryId);
        videoInfoPost.setPostType(postType);
        videoInfoPost.setTags(tags);
        videoInfoPost.setIntroduction(introduction);
        videoInfoPost.setInteraction(interaction);
        videoInfoPost.setUserId(tokenUserInfoDto.getUserId());
        videoInfoPostService.saveVideoInfo(videoInfoPost, filePostList);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadVideoList")
    public ResponseVO loadVideoList(Integer status, Integer pageNo, String videoNameFuzzy) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoInfoPostQuery videoInfoQuery =  new VideoInfoPostQuery();
        videoInfoQuery.setUserId(tokenUserInfoDto.getUserId());
        videoInfoQuery.setOrderBy("v.create_time desc");
        videoInfoQuery.setPageNo(pageNo);
        if (null != status) {
            // 查询已通过状态 不包括
            if (status == -1) {
                videoInfoQuery.setExcludeStatusArray(new Integer[] {VideoStatusEnum.STATUS3.getStatus(), VideoStatusEnum.STATUS4.getStatus()});
            } else {
                videoInfoQuery.setStatus(status);
            }
        }
        videoInfoQuery.setVideoNameFuzzy(videoNameFuzzy);
        videoInfoQuery.setQueryCountInfo(true);
        PaginationResultVO resultVO = videoInfoPostService.findListByPage(videoInfoQuery);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/getVideoCountInfo")
    public ResponseVO getVideoCountInfo() {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoInfoPostQuery videoInfoQuery =  new VideoInfoPostQuery();
        videoInfoQuery.setUserId(tokenUserInfoDto.getUserId());
        videoInfoQuery.setStatus(VideoStatusEnum.STATUS3.getStatus());
        Integer auditPassCount = videoInfoPostService.findCountByParam(videoInfoQuery);

        videoInfoQuery.setStatus(VideoStatusEnum.STATUS4.getStatus());
        Integer auditFailCount = videoInfoPostService.findCountByParam(videoInfoQuery);

        videoInfoQuery.setStatus(null);
        videoInfoQuery.setExcludeStatusArray(new Integer[] {VideoStatusEnum.STATUS3.getStatus(), VideoStatusEnum.STATUS4.getStatus()});
        Integer inProgressCount = videoInfoPostService.findCountByParam(videoInfoQuery);

        VideoStatusCountInfoVO videoStatusCountInfoVO = VideoStatusCountInfoVO.builder()
                .auditPassCount(auditPassCount)
                .auditFailCount(auditFailCount)
                .inProgressCount(inProgressCount)
                .build();
        return getSuccessResponseVO(videoStatusCountInfoVO);
    }

    @RequestMapping("/getVideoByVideoId")
    public ResponseVO getVideoByVideoId(@NotEmpty String videoId) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoInfoPost videoInfoPost = videoInfoPostService.getVideoInfoPostByVideoId(videoId);
        if (videoInfoPost == null || !videoInfoPost.getUserId().equals(tokenUserInfoDto.getUserId())) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        VideoInfoFilePostQuery videoInfoFilePostQuery = new VideoInfoFilePostQuery();
        videoInfoFilePostQuery.setVideoId(videoId);
        videoInfoFilePostQuery.setOrderBy("file_index asc");
        List<VideoInfoFilePost> videoInfoFilePostList = videoInfoFilePostService.findListByParam(videoInfoFilePostQuery);
        VideoPostEditInfoVO resultVO = new VideoPostEditInfoVO(videoInfoPost, videoInfoFilePostList);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/saveVideoInteraction")
    public ResponseVO saveVideoInteraction(@NotEmpty String videoId, String interaction) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        videoInfoService.changeInteraction(videoId,tokenUserInfoDto.getUserId(), interaction);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/deleteVideo")
    public ResponseVO deleteVideo(@NotEmpty String videoId) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        videoInfoService.deleteVideo(videoId, tokenUserInfoDto.getUserId());
        return getSuccessResponseVO(null);
    }
}
