package com.easylive.service.impl;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.easylive.component.RedisComponent;
import com.easylive.entity.config.AppConfig;
import com.easylive.entity.constants.Constants;
import com.easylive.entity.dto.SysSettingDto;
import com.easylive.entity.dto.UploadingFileDto;
import com.easylive.entity.enums.*;
import com.easylive.entity.po.VideoInfo;
import com.easylive.entity.po.VideoInfoFile;
import com.easylive.entity.po.VideoInfoFilePost;
import com.easylive.entity.query.*;
import com.easylive.exception.BusinessException;
import com.easylive.mapper.VideoInfoFileMapper;
import com.easylive.mapper.VideoInfoFilePostMapper;
import com.easylive.mapper.VideoInfoMapper;
import com.easylive.mapper.VideoInfoPostMapper;
import com.easylive.service.VideoInfoPostService;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.VideoInfoPost;
import com.easylive.utils.CopyTools;
import com.easylive.utils.FFmpegUtils;
import com.easylive.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
/**
 * @Description: 视频信息 业务接口实现
 * @Author: false
 * @Date: 2026/02/05 21:42:17
 */
@Service("VideoInfoPostMapper")
@Slf4j
public class VideoInfoPostServiceImpl implements VideoInfoPostService{


    @Autowired
    private RedisComponent redisComponent;

    @Resource
    private VideoInfoMapper<VideoInfo, VideoInfoQuery> videoInfoMapper;

    @Resource
    private VideoInfoFileMapper<VideoInfoFile, VideoInfoFileQuery> videoInfoFileMapper;

	@Resource
	private VideoInfoPostMapper<VideoInfoPost, VideoInfoPostQuery> videoInfoPostMapper;

    @Resource
    private VideoInfoFilePostMapper<VideoInfoFilePost, VideoInfoFilePostQuery> videoInfoFilePostMapper;

    @Resource
    private AppConfig appConfig;

    @Resource
    private FFmpegUtils fFmpegUtils;

    /**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<VideoInfoPost> findListByParam(VideoInfoPostQuery query) {
		return this.videoInfoPostMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(VideoInfoPostQuery query) {
        Integer result = this.videoInfoPostMapper.selectCount(query);
		return result == null ? 0 : result;	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<VideoInfoPost> findListByPage(VideoInfoPostQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<VideoInfoPost> list = this.findListByParam(query);
		PaginationResultVO<VideoInfoPost> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(VideoInfoPost bean) {
		return this.videoInfoPostMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<VideoInfoPost> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.videoInfoPostMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<VideoInfoPost> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.videoInfoPostMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 VideoId 查询
 	 */
	@Override
	public VideoInfoPost getVideoInfoPostByVideoId(String videoId) {
		return this.videoInfoPostMapper.selectByVideoId(videoId);}

	/**
 	 * 根据 VideoId 更新
 	 */
	@Override
	public Integer updateVideoInfoPostByVideoId(VideoInfoPost bean, String videoId) {
		return this.videoInfoPostMapper.updateByVideoId(bean, videoId);}

	/**
 	 * 根据 VideoId 删除
 	 */
	@Override
	public Integer deleteVideoInfoPostByVideoId(String videoId) {
		return this.videoInfoPostMapper.deleteByVideoId(videoId);}

    /**
     *
     * @param videoInfoPost 有videoId表示修改，没有表示添加
     * @param uploadFileList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveVideoInfo(VideoInfoPost videoInfoPost, List<VideoInfoFilePost> uploadFileList) {
        if (uploadFileList.size() > redisComponent.getSysSettingDto().getVideoPCount()) {
            // 分P数量超出
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (!StringTools.isEmpty(videoInfoPost.getVideoId())) {
            //传进来的对象不在数据库中
            VideoInfoPost videoInfoPostDb = videoInfoPostMapper.selectByVideoId(videoInfoPost.getVideoId());
            if (videoInfoPostDb == null) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            // 判断状态是否支持修改操作
            if (ArrayUtils.contains(new Integer[]{VideoStatusEnum.STATUS2.getStatus(), VideoStatusEnum.STATUS0.getStatus()}, videoInfoPostDb.getStatus())) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
        }

        Date curDate = new Date();
        String videoId = videoInfoPost.getVideoId();
        List<VideoInfoFilePost> deleteFileList = new ArrayList<>();
        List<VideoInfoFilePost> addFileList = uploadFileList;

        // if-else只修改 videoInfoPost 稿件的信息
        if (StringTools.isEmpty(videoId)){
            // videoId不存在，表示新增
            videoId = StringTools.getRandomString(Constants.length_10);
            videoInfoPost.setVideoId(videoId);
            videoInfoPost.setCreateTime(curDate);
            videoInfoPost.setLastUpdateTime(curDate);
            videoInfoPost.setStatus(VideoStatusEnum.STATUS0.getStatus());
            videoInfoPostMapper.insert(videoInfoPost);
        } else {
            // 查找被删除的文件 并且判断文件名字有没有修改
            VideoInfoFilePostQuery filePostQuery = new VideoInfoFilePostQuery();
            filePostQuery.setVideoId(videoId);
            filePostQuery.setUserId(videoInfoPost.getUserId());
            List<VideoInfoFilePost> dbInfoFileList = videoInfoFilePostMapper.selectList(filePostQuery);
            /**
             * 以下代码将转换为以 uploadId 为 key，
             * 以 VideoInfoFilePost 为 value，其中Function.identity 等价于 item->item
             * (data1, data2) -> data2)表示若 uploadId存在重复值，采取后覆盖原则
             */
            Map<String, VideoInfoFilePost> uploadFileMap = uploadFileList.stream().collect(Collectors.toMap(item->item.getUploadId(), Function.identity(),
                    (data1, data2) -> data2));
            Boolean updateFileName = false;
            for (VideoInfoFilePost fileInfo : dbInfoFileList) {
                VideoInfoFilePost updateFile = uploadFileMap.get(fileInfo.getUploadId());
                if (null == updateFile) {
                    deleteFileList.add(fileInfo);
                } else if (!updateFile.getFileName().equals(fileInfo.getFileName())) {
                    updateFileName = true;
                }
            }

            // 找出新增加的文件
            addFileList = uploadFileList.stream().filter(item->item.getFileId()==null).collect(Collectors.toList());

            // 更新稿件的信息
            videoInfoPost.setLastUpdateTime(curDate);
            Boolean changeVideoInfo =changeVideoInfo(videoInfoPost);
            if (addFileList != null &&!addFileList.isEmpty()){
                // 视频有新增就转码
                videoInfoPost.setStatus(VideoStatusEnum.STATUS0.getStatus());
            } else if (changeVideoInfo||updateFileName) {
                // 视频无新增但是其他内容有更改
                videoInfoPost.setStatus(VideoStatusEnum.STATUS2.getStatus());
            }
            videoInfoPostMapper.updateByVideoId(videoInfoPost,videoInfoPost.getVideoId());
        }
        // 接下来是对文件的操作

        // 删除不存在的视频信息  并添加视频路径到消息队列
        if (!deleteFileList.isEmpty()) {
            List<String> delFileList = deleteFileList.stream().map(item->item.getFileId()).collect(Collectors.toList());
            videoInfoFilePostMapper.deleteBatchByFileId(delFileList, videoInfoPost.getUserId());

            List<String> delFilePathList = deleteFileList.stream().map(item->item.getFilePath()).collect(Collectors.toList());
            redisComponent.addFile2DelQueue(videoId, delFilePathList);
        }

        // 更新 uploadFileList 中的数据并添加到数据库中
        Integer index = 1;
        for (VideoInfoFilePost videoInfoFile : uploadFileList) {
            videoInfoFile.setFileIndex(index++);
            videoInfoFile.setVideoId(videoId);
            videoInfoFile.setUserId(videoInfoPost.getUserId());
            if (videoInfoFile.getFileId() == null) {
                videoInfoFile.setFileId(StringTools.getRandomString(Constants.length_20));
                videoInfoFile.setUpdateType(VideoFileUpdateTypeEnum.UPDATE.getStatus());
                videoInfoFile.setTransferResult(VideoFileTransferResultEnum.TRANSFER.getStatus());
            }
        }
        videoInfoFilePostMapper.insertOrUpdateBatch(uploadFileList);

        //  更新新增文件信息并添加到消息队列中
        if (addFileList!=null && !addFileList.isEmpty()){
            for (VideoInfoFilePost file : addFileList) {
                file.setUserId(videoInfoPost.getUserId());
                file.setVideoId(videoId);
            }
            redisComponent.addFile2TransferQueue(addFileList);
        }
    }

    /**
     * 判断对象与数据库中的对象是否有变动
     * @param videoInfoPost 前端传递过来的对象
     * @return
     */
    private Boolean changeVideoInfo(VideoInfoPost videoInfoPost) {
        VideoInfoPost dbInfo = videoInfoPostMapper.selectByVideoId(videoInfoPost.getVideoId());
        // 标题 封面 标签 简介
        return !videoInfoPost.getVideoName().equals(dbInfo.getVideoName())
                || !videoInfoPost.getVideoCover().equals(dbInfo.getVideoCover())
                || !videoInfoPost.getTags().equals(dbInfo.getTags())
                || !videoInfoPost.getIntroduction().equals(dbInfo.getIntroduction()==null?"":dbInfo.getIntroduction());
    }

    @Override
    public void transferVideoFile(VideoInfoFilePost videoInfoFilePost) {
        VideoInfoFilePost updateFilePost = new VideoInfoFilePost();
        try {
            UploadingFileDto fileDto = redisComponent.getUploadVideoFile(videoInfoFilePost.getUserId(), videoInfoFilePost.getUploadId());
            String tempFilePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER+Constants.FILE_FOLDER_TEMP + fileDto.getFilePath();
            File tempFile = new File(tempFilePath);

            String targetFilePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_VIDEO + fileDto.getFilePath();
            File targetFile = new File(targetFilePath);

            FileUtils.copyDirectory(tempFile, targetFile);

            // 删除临时目录 和 redis 记录
            FileUtils.forceDelete(tempFile);
            redisComponent.delVideoFileInfo(videoInfoFilePost.getUserId(), videoInfoFilePost.getUploadId());

            // 合并文件
            String completeVideo = targetFilePath+Constants.TEMP_VIDEO_NAME;
            union(targetFilePath, completeVideo, true);

            // 获取播放时长
            Integer duration = fFmpegUtils.getVideoInfoDuration(completeVideo);
            updateFilePost.setDuration(duration);
            updateFilePost.setFileSize(new File(completeVideo).length());
            updateFilePost.setFilePath(Constants.FILE_VIDEO+ fileDto.getFilePath());
            updateFilePost.setTransferResult(VideoFileTransferResultEnum.SUCCESS.getStatus());

            convertVideo2Ts(completeVideo);

        } catch (Exception e) {
            log.error("文件转码失败", e);
            updateFilePost.setTransferResult(VideoFileTransferResultEnum.FAIL.getStatus());
        } finally {
            // 更新 filePost 中的信息
            videoInfoFilePostMapper.updateByUploadIdAndUserId(updateFilePost, videoInfoFilePost.getUploadId(), videoInfoFilePost.getUserId());
            // 查找数据表中是否存在转码失败的文件
            VideoInfoFilePostQuery filePostQuery = new VideoInfoFilePostQuery();
            filePostQuery.setVideoId(videoInfoFilePost.getVideoId());
            filePostQuery.setTransferResult(VideoFileTransferResultEnum.FAIL.getStatus());
            Integer failCount = videoInfoFilePostMapper.selectCount(filePostQuery);
            if (failCount > 0) {
                // 如果有转码失败的文件 直接Video更新状态
                VideoInfoPost videoUpdate = new VideoInfoPost();
                videoUpdate.setStatus(VideoStatusEnum.STATUS1.getStatus());
                videoInfoPostMapper.updateByVideoId(videoUpdate, videoInfoFilePost.getVideoId());
                return;
            }
            filePostQuery.setTransferResult(VideoFileTransferResultEnum.TRANSFER.getStatus());
            Integer transferCount = videoInfoFilePostMapper.selectCount(filePostQuery);
            // 如果全部转码完成 更新Video状态
            if (transferCount == 0) {
                Integer duration = videoInfoFilePostMapper.sumDuration(videoInfoFilePost.getVideoId());
                VideoInfoPost videoUpdate = new VideoInfoPost();
                videoUpdate.setStatus(VideoStatusEnum.STATUS2.getStatus());
                videoUpdate.setDuration(duration);
                videoInfoPostMapper.updateByVideoId(videoUpdate, videoInfoFilePost.getVideoId());
            }
        }
    }


    private void convertVideo2Ts(String completeVideo) {
        File videoFile = new File(completeVideo);
        File tsFolder = videoFile.getParentFile();
        String codec = fFmpegUtils.getVideoCodec(completeVideo);
        if (Constants.VIDEO_CODE_HEVC.equals(codec)) {
            /**
             * 转码视频文件
             * completeVideo renameTo newFileName
             * ffmpeg -> newFileName generate completeVideo
             * delete newFileName
             */
            String newFileName = completeVideo + Constants.VIDEO_CODE_TEMP_FILE_SUFFIX;
            new File(completeVideo).renameTo(new File(newFileName));
            fFmpegUtils.convertHevc2Mp4(newFileName, completeVideo);
            new File(newFileName).delete();
        }
        // 此时 completeVideo 存放的是编码为 h264 的文件
        fFmpegUtils.convertVideo2Ts(tsFolder, completeVideo);
        videoFile.delete();
    }

    /**
     * 将分片的文件合并成一个mp4文件
     * @param dirPath
     * @param toFilePath 合并到的文件
     * @param delSource 是否删除源文件
     */
    private void union(String dirPath, String toFilePath, Boolean delSource) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            throw new BusinessException("目录不存在");
        }
        File fileList[] = dir.listFiles();
        File targetFile = new File(toFilePath);
        try (RandomAccessFile writeFile = new RandomAccessFile(targetFile, "rw")) {
            byte[] b = new byte[1024 * 10];
            // 创建读块文件的对象
            for (int i = 0; i < fileList.length; i++) {
                int len = -1;
                File chunkFile = new File(dirPath + File.separator + i);
                RandomAccessFile readFile = null;
                try{
                    readFile = new RandomAccessFile(chunkFile, "r");
                    while ((len = readFile.read(b)) != -1) {
                        writeFile.write(b, 0, len);
                    }
                } catch (Exception e) {
                    log.error("合并分片失败", e);
                    throw new BusinessException("合并文件失败");
                } finally {
                    readFile.close();
                }
            }
        } catch (Exception e) {
            throw new BusinessException("合并文件" + dirPath + "出错了");
        } finally {
            if (delSource) {
                for (int i = 0; i < fileList.length; i++) {
                    fileList[i].delete();
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditVideo(String videoId, Integer status, String reason) {
        VideoStatusEnum videoStatusEnum = VideoStatusEnum.getByStatus(status);
        if (videoStatusEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        /**
         * 修改状态的时候必须是由一个状态(status2)修改为另一个状态(status3)
         * 才能保证并发操作下成功操作数据
         * set status=3 where video_id= and status=2
         */

        // 最终状态
        VideoInfoPost videoInfoPost = new VideoInfoPost();
        videoInfoPost.setStatus(status);
        // 限制条件
        VideoInfoPostQuery videoInfoPostQuery = new VideoInfoPostQuery();
        videoInfoPostQuery.setStatus(VideoStatusEnum.STATUS2.getStatus());
        videoInfoPostQuery.setVideoId(videoId);
        Integer auditCount = videoInfoPostMapper.updateByParam(videoInfoPost, videoInfoPostQuery);
        if (auditCount == 0) {
            throw new BusinessException("审核失败，请稍后重试");
        }

        // 同上，根据限制条件更新最终状态
        VideoInfoFilePost videoInfoFilePost = new VideoInfoFilePost();
        videoInfoFilePost.setUpdateType(VideoFileUpdateTypeEnum.NO_UPDATE.getStatus());
        VideoInfoFilePostQuery filePostQuery = new VideoInfoFilePostQuery();
        filePostQuery.setVideoId(videoId);
        videoInfoFilePostMapper.updateByParam(videoInfoFilePost, filePostQuery);

        if (VideoStatusEnum.STATUS4 == videoStatusEnum) {
            // 审核不通过
            return;
        }

        VideoInfo dbVideoInfo = videoInfoMapper.selectByVideoId(videoId);
        if (dbVideoInfo == null) {
            // 第一次发布视频
            SysSettingDto sysSettingDto = redisComponent.getSysSettingDto();
            // TODO 给用户加积分
        }

        // 更新发布信息到正式表
        VideoInfoPost infoPost = videoInfoPostMapper.selectByVideoId(videoId);
        VideoInfo videoInfo = CopyTools.copy(infoPost, VideoInfo.class);
        videoInfoMapper.insertOrUpdate(videoInfo);

        // 更新文件到正式表  先删除再添加
        VideoInfoFileQuery videoInfoFileQuery = new VideoInfoFileQuery();
        videoInfoFileQuery.setVideoId(videoId);
        videoInfoFileMapper.deleteByParam(videoInfoFileQuery);

        VideoInfoFilePostQuery videoInfoFilePostQuery =  new VideoInfoFilePostQuery();
        videoInfoFilePostQuery.setVideoId(videoId);
        List<VideoInfoFilePost> videoInfoFilePostList = videoInfoFilePostMapper.selectList(videoInfoFilePostQuery);
        List<VideoInfoFile> videoInfoFileList = CopyTools.copyList(videoInfoFilePostList, VideoInfoFile.class);
        videoInfoFileMapper.insertBatch(videoInfoFileList);

        // 删除文件
        List<String> filePathList = redisComponent.getDelFileList(videoId);
        if (filePathList != null) {
            for (String path : filePathList) {
                File file = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER + path);
                if (file.exists()) {
                    try {
                        FileUtils.deleteDirectory(file);
                    } catch (IOException e) {
                        log.error("删除文件失败", e);
                    }
                }
            }
        }
        redisComponent.cleanDelFileList(videoId);
        // TODO 保存信息到es
    }
}