package com.easylive.service.impl;


import java.util.Date;
import java.util.List;

import com.easylive.entity.constants.Constants;
import com.easylive.entity.enums.CommentTopTypeEnum;
import com.easylive.entity.enums.ResponseCodeEnum;
import com.easylive.entity.enums.UserActionTypeEnum;
import com.easylive.entity.po.UserInfo;
import com.easylive.entity.po.VideoInfo;
import com.easylive.entity.query.SimplePage;
import com.easylive.entity.enums.PageSize;
import com.easylive.entity.query.UserInfoQuery;
import com.easylive.entity.query.VideoInfoQuery;
import com.easylive.exception.BusinessException;
import com.easylive.mapper.UserInfoMapper;
import com.easylive.mapper.VideoCommentMapper;
import com.easylive.mapper.VideoInfoMapper;
import com.easylive.service.VideoCommentService;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.VideoComment;
import com.easylive.entity.query.VideoCommentQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
/**
 * @Description: 评论 业务接口实现
 * @Author: false
 * @Date: 2026/02/10 21:56:04
 */
@Service("VideoCommentMapper")
public class VideoCommentServiceImpl implements VideoCommentService{

	@Resource
	private VideoCommentMapper<VideoComment, VideoCommentQuery> videoCommentMapper;

    @Resource
    private VideoInfoMapper<VideoInfo, VideoInfoQuery> videoInfoMapper;

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	/**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<VideoComment> findListByParam(VideoCommentQuery query) {
        if (query.getLoadChildren()!=null && query.getLoadChildren()) {
            return videoCommentMapper.selectListWithChildren(query);
        }
		return this.videoCommentMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(VideoCommentQuery query) {
		return this.videoCommentMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<VideoComment> findListByPage(VideoCommentQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<VideoComment> list = this.findListByParam(query);
		PaginationResultVO<VideoComment> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(VideoComment bean) {
		return this.videoCommentMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<VideoComment> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.videoCommentMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<VideoComment> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.videoCommentMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 CommentId 查询
 	 */
	@Override
	public VideoComment getVideoCommentByCommentId(Integer commentId) {
		return this.videoCommentMapper.selectByCommentId(commentId);}

	/**
 	 * 根据 CommentId 更新
 	 */
	@Override
	public Integer updateVideoCommentByCommentId(VideoComment bean, Integer commentId) {
		return this.videoCommentMapper.updateByCommentId(bean, commentId);}

	/**
 	 * 根据 CommentId 删除
 	 */
	@Override
	public Integer deleteVideoCommentByCommentId(Integer commentId) {
		return this.videoCommentMapper.deleteByCommentId(commentId);}

    /**
     *
     * @param comment  当前评论的信息
     * @param replyCommentId  被回复的评论ID
     */
    @Override
    public void postComment(VideoComment comment, Integer replyCommentId) {
        VideoInfo videoInfo = videoInfoMapper.selectByVideoId(comment.getVideoId());
        if (videoInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (videoInfo.getInteraction()!=null && videoInfo.getInteraction().contains(Constants.ZERO.toString())) {
            throw new BusinessException("UP主已关闭评论区");
        }
        if (replyCommentId != null) {  // 我的评论是二级评论
            // 被回复的评论
            VideoComment replyComment = getVideoCommentByCommentId(replyCommentId);
            if (replyComment == null || !replyComment.getVideoId().equals(comment.getVideoId())) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            if (replyComment.getCommentId()==0) { // 被回复的评论是一级评论
                comment.setPCommentId(replyCommentId);
            } else {// 二级评论
                comment.setPCommentId(replyComment.getCommentId());
                comment.setReplyUserId(replyComment.getUserId());
            }
            // 被回复的用户的信息
            UserInfo userInfo = userInfoMapper.selectByUserId(replyComment.getUserId());
            comment.setReplyNickName(userInfo.getNickName());
            comment.setReplyAvatar(userInfo.getAvatar());
        } else {
            comment.setPCommentId(Constants.ZERO);
        }
        comment.setPostTime(new Date());
        comment.setVideoUserId(videoInfo.getUserId());
        videoCommentMapper.insert(comment);

        // 只计算一级评论
        if (comment.getPCommentId() == 0) {
            videoInfoMapper.updateCountInfo(comment.getVideoId(), UserActionTypeEnum.VIDEO_COMMENT.getField(), Constants.ONE);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void topComment(Integer commentId, String userId) {
        this.cancelTopComment(commentId, userId);
        VideoComment videoComment = getVideoCommentByCommentId(commentId);
        videoComment.setTopType(CommentTopTypeEnum.TOP.getType());
        videoCommentMapper.updateByCommentId(videoComment, commentId);

    }

    @Override
    public void cancelTopComment(Integer commentId, String userId) {
        VideoComment dbVideoComment = videoCommentMapper.selectByCommentId(commentId);
        if (dbVideoComment == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        VideoInfo videoInfo = videoInfoMapper.selectByVideoId(dbVideoComment.getVideoId());
        if (videoInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (!videoInfo.getUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        // 将原有的评论设置为为置顶
        VideoComment videoComment =new VideoComment();
        videoComment.setTopType(CommentTopTypeEnum.NO_TOP.getType());

        VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
        videoCommentQuery.setTopType(CommentTopTypeEnum.TOP.getType());
        videoCommentQuery.setVideoId(dbVideoComment.getVideoId());
        videoCommentMapper.updateByParam(videoComment, videoCommentQuery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Integer commentId, String userId) {
        VideoComment videoComment = getVideoCommentByCommentId(commentId);
        if (videoComment == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        VideoInfo videoInfo = videoInfoMapper.selectByVideoId(videoComment.getVideoId());
        if (videoInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if (userId != null && !videoInfo.getUserId().equals(userId) && !videoComment.getUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        videoCommentMapper.deleteByCommentId(commentId);
        if (videoComment.getPCommentId() == Constants.ZERO) {
            videoInfoMapper.updateCountInfo(videoInfo.getVideoId(), UserActionTypeEnum.VIDEO_COMMENT.getField(), -Constants.ONE);
            // 删除二级评论
            VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
            videoCommentQuery.setPCommentId(commentId);
            videoCommentMapper.deleteByParam(videoCommentQuery);
        }
    }
}