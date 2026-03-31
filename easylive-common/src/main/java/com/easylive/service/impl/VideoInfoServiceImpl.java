package com.easylive.service.impl;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.easylive.component.EsSearchComponent;
import com.easylive.component.RedisComponent;
import com.easylive.entity.config.AppConfig;
import com.easylive.entity.constants.Constants;
import com.easylive.entity.dto.SysSettingDto;
import com.easylive.entity.enums.ResponseCodeEnum;
import com.easylive.entity.enums.UserActionTypeEnum;
import com.easylive.entity.enums.VideoRecommendType;
import com.easylive.entity.po.*;
import com.easylive.entity.query.*;
import com.easylive.entity.enums.PageSize;
import com.easylive.exception.BusinessException;
import com.easylive.mapper.*;
import com.easylive.service.UserInfoService;
import com.easylive.service.VideoInfoFilePostService;
import com.easylive.service.VideoInfoService;
import com.easylive.entity.vo.PaginationResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
/**
 * @Description: 视频信息 业务接口实现
 * @Author: false
 * @Date: 2026/02/05 21:42:17
 */
@Service("VideoInfoMapper")
@Slf4j
public class VideoInfoServiceImpl implements VideoInfoService{

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    private RedisComponent redisComponent;

    @Resource
    private AppConfig appconfig;

	@Resource
	private VideoInfoMapper<VideoInfo, VideoInfoQuery> videoInfoMapper;

    @Resource
    private VideoInfoPostMapper<VideoInfoPost, VideoInfoPostQuery> videoInfoPostMapper;

    @Autowired
    private VideoInfoFileMapper<VideoInfoFile, VideoInfoFileQuery> videoInfoFileMapper;

    @Resource
    private VideoInfoFilePostMapper<VideoInfoFilePost, VideoInfoFilePostQuery> videoInfoFilePostMapper;

    @Resource
    private VideoDanmuMapper<VideoDanmu, VideoDanmuQuery> videoDanmuMapper;

    @Resource
    private VideoCommentMapper<VideoComment, VideoCommentQuery> videoCommentMapper;
    @Autowired
    private EsSearchComponent esSearchComponent;
    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    /**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<VideoInfo> findListByParam(VideoInfoQuery query) {
		return this.videoInfoMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(VideoInfoQuery query) {
		return this.videoInfoMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<VideoInfo> findListByPage(VideoInfoQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<VideoInfo> list = this.findListByParam(query);
		PaginationResultVO<VideoInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(VideoInfo bean) {
		return this.videoInfoMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<VideoInfo> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.videoInfoMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<VideoInfo> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.videoInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 VideoId 查询
 	 */
	@Override
	public VideoInfo getVideoInfoByVideoId(String videoId) {
		return this.videoInfoMapper.selectByVideoId(videoId);}

	/**
 	 * 根据 VideoId 更新
 	 */
	@Override
	public Integer updateVideoInfoByVideoId(VideoInfo bean, String videoId) {
		return this.videoInfoMapper.updateByVideoId(bean, videoId);}

	/**
 	 * 根据 VideoId 删除
 	 */
	@Override
	public Integer deleteVideoInfoByVideoId(String videoId) {
		return this.videoInfoMapper.deleteByVideoId(videoId);}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeInteraction(String videoId, String userId, String interaction) {
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setInteraction(interaction);
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setUserId(userId);
        videoInfoQuery.setVideoId(videoId);
        videoInfoMapper.updateByParam(videoInfo, videoInfoQuery);

        VideoInfoPost videoInfoPost = new VideoInfoPost();
        videoInfoPost.setInteraction(interaction);
        VideoInfoPostQuery videoInfoPostQuery = new VideoInfoPostQuery();
        videoInfoPostQuery.setUserId(userId);
        videoInfoPostQuery.setVideoId(videoId);
        videoInfoPostMapper.updateByParam(videoInfoPost, videoInfoPostQuery);
    }

    @Override
    public void deleteVideo(String videoId, String userId) {
        VideoInfoPost dbInfoPost = videoInfoPostMapper.selectByVideoId(videoId);

        // 管理员有可能调用这个接口 传进来的userId为Null
        if (dbInfoPost == null || userId != null && !dbInfoPost.getUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        videoInfoMapper.deleteByVideoId(videoId);
        videoInfoPostMapper.deleteByVideoId(videoId);

        SysSettingDto sysSettingDto = redisComponent.getSysSettingDto();
        // 减去用户的硬币
        userInfoMapper.updateCoinCountInfo(dbInfoPost.getUserId(), -sysSettingDto.getPostVideoCoinCount());

        // 删除es
        esSearchComponent.delDoc(videoId);

        // 异步删除视频
        executorService.execute(()-> {
            // 删除分P文件
            VideoInfoFileQuery videoInfoFileQuery = new VideoInfoFileQuery();
            videoInfoFileQuery.setVideoId(videoId);
            List<VideoInfoFile> videoInfoFileList = videoInfoFileMapper.selectList(videoInfoFileQuery);
            videoInfoFileMapper.deleteByParam(videoInfoFileQuery);
            VideoInfoFilePostQuery videoInfoFilePostQuery = new VideoInfoFilePostQuery();
            videoInfoFilePostQuery.setVideoId(videoId);
            videoInfoFilePostMapper.deleteByParam(videoInfoFilePostQuery);

            // 删除弹幕
            VideoDanmuQuery videoDanmuQuery = new VideoDanmuQuery();
            videoDanmuQuery.setVideoId(videoId);
            videoDanmuMapper.deleteByParam(videoDanmuQuery);

            // 删除评论
            VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
            videoCommentQuery.setVideoId(videoId);
            videoCommentMapper.deleteByParam(videoCommentQuery);

            // 删除文件
            for (VideoInfoFile videoInfoFile : videoInfoFileList) {
                try {
                    FileUtils.deleteDirectory(new File(appconfig.getProjectFolder() + Constants.FILE_FOLDER + videoInfoFile.getFilePath()));
                } catch (IOException e) {
                    log.error("删除文件失败，文件路径{}", videoInfoFile.getFilePath());
                }
            }
        });
    }

    @Override
    public void addReadCount(String videoId) {
        videoInfoMapper.updateCountInfo(videoId, UserActionTypeEnum.VIDEO_PLAY.getField(), 1);
    }

    @Override
    public void recommendVideo(String videoId) {
        VideoInfo videoInfo = videoInfoMapper.selectByVideoId(videoId);
        if (videoInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        Integer recommendType = null;
        if (VideoRecommendType.RECOMMEND.getStatus().equals(videoInfo.getRecommendType())) {
            recommendType = VideoRecommendType.NO_RECOMMEND.getStatus();
        } else {
            recommendType = VideoRecommendType.RECOMMEND.getStatus();
        }
        VideoInfo updateVideoInfo = new VideoInfo();
        updateVideoInfo.setRecommendType(recommendType);
        videoInfoMapper.updateByVideoId(updateVideoInfo, videoId);
    }
}