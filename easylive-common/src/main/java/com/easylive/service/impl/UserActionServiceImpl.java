package com.easylive.service.impl;


import java.util.Date;
import java.util.List;

import com.easylive.component.EsSearchComponent;
import com.easylive.entity.constants.Constants;
import com.easylive.entity.enums.ResponseCodeEnum;
import com.easylive.entity.enums.SearchOrderTypeEnum;
import com.easylive.entity.enums.UserActionTypeEnum;
import com.easylive.entity.po.UserInfo;
import com.easylive.entity.po.VideoComment;
import com.easylive.entity.po.VideoInfo;
import com.easylive.entity.query.*;
import com.easylive.entity.enums.PageSize;
import com.easylive.exception.BusinessException;
import com.easylive.mapper.UserActionMapper;
import com.easylive.mapper.UserInfoMapper;
import com.easylive.mapper.VideoCommentMapper;
import com.easylive.mapper.VideoInfoMapper;
import com.easylive.service.UserActionService;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.UserAction;
import com.easylive.service.VideoInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
/**
 * @Description: 用户行为 点赞、评论 业务接口实现
 * @Author: false
 * @Date: 2026/02/10 21:56:04
 */
@Service("UserActionMapper")
public class UserActionServiceImpl implements UserActionService{

	@Resource
	private UserActionMapper<UserAction, UserActionQuery> userActionMapper;

    @Resource
    private VideoInfoMapper<VideoInfo, VideoInfoQuery> videoInfoMapper;
    @Autowired
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Resource
    private VideoCommentMapper<VideoComment, VideoCommentQuery> videoCommentMapper;
    @Autowired
    private EsSearchComponent esSearchComponent;

    /**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<UserAction> findListByParam(UserActionQuery query) {
		return this.userActionMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(UserActionQuery query) {
		return this.userActionMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<UserAction> findListByPage(UserActionQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserAction> list = this.findListByParam(query);
		PaginationResultVO<UserAction> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(UserAction bean) {
		return this.userActionMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<UserAction> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.userActionMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<UserAction> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.userActionMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 ActionId 查询
 	 */
	@Override
	public UserAction getUserActionByActionId(Integer actionId) {
		return this.userActionMapper.selectByActionId(actionId);}

	/**
 	 * 根据 ActionId 更新
 	 */
	@Override
	public Integer updateUserActionByActionId(UserAction bean, Integer actionId) {
		return this.userActionMapper.updateByActionId(bean, actionId);}

	/**
 	 * 根据 ActionId 删除
 	 */
	@Override
	public Integer deleteUserActionByActionId(Integer actionId) {
		return this.userActionMapper.deleteByActionId(actionId);}

	/**
 	 * 根据 VideoIdAndCommentIdAndActionTypeAndUserId 查询
 	 */
	@Override
	public UserAction getUserActionByVideoIdAndCommentIdAndActionTypeAndUserId(String videoId, Integer commentId, Integer actionType, String userId) {
		return this.userActionMapper.selectByVideoIdAndCommentIdAndActionTypeAndUserId(videoId, commentId, actionType, userId);}

	/**
 	 * 根据 VideoIdAndCommentIdAndActionTypeAndUserId 更新
 	 */
	@Override
	public Integer updateUserActionByVideoIdAndCommentIdAndActionTypeAndUserId(UserAction bean, String videoId, Integer commentId, Integer actionType, String userId) {
		return this.userActionMapper.updateByVideoIdAndCommentIdAndActionTypeAndUserId(bean, videoId, commentId, actionType, userId);}

	/**
 	 * 根据 VideoIdAndCommentIdAndActionTypeAndUserId 删除
 	 */
	@Override
    @Transactional(rollbackFor = Exception.class)
	public Integer deleteUserActionByVideoIdAndCommentIdAndActionTypeAndUserId(String videoId, Integer commentId, Integer actionType, String userId) {
		return this.userActionMapper.deleteByVideoIdAndCommentIdAndActionTypeAndUserId(videoId, commentId, actionType, userId);}

    public void saveAction(UserAction bean) {
        VideoInfo videoInfo = videoInfoMapper.selectByVideoId(bean.getVideoId());
        if (videoInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        bean.setVideoUserId(videoInfo.getUserId());
        UserActionTypeEnum actionTypeEnum = UserActionTypeEnum.getByType(bean.getActionType());
        if (actionTypeEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        // 根据当前信息查找数据库里是否已经有对应的信息了
        UserAction dbAction = userActionMapper.selectByVideoIdAndCommentIdAndActionTypeAndUserId(bean.getVideoId(), bean.getCommentId(), bean.getActionType(), bean.getUserId());
        bean.setActionTime(new Date());

        switch (actionTypeEnum) {
            case VIDEO_LIKE:
            case VIDEO_COLLECT:
                if (dbAction == null) {
                    userActionMapper.insert(bean);
                } else {
                    userActionMapper.deleteByActionId(dbAction.getActionId());
                }
                Integer changeCount = dbAction==null? Constants.ONE : -Constants.ONE;
                videoInfoMapper.updateCountInfo(bean.getVideoId(), actionTypeEnum.getField(), changeCount);
                if(actionTypeEnum == UserActionTypeEnum.VIDEO_COLLECT){
                    esSearchComponent.updateDocCount(videoInfo.getVideoId(), SearchOrderTypeEnum.VIDEO_COLLECT.getField(), changeCount);
                }
                break;
            case VIDEO_COIN:
                if (videoInfo.getUserId().equals(bean.getUserId())) {
                    throw new BusinessException("UP主不能给自己投币");
                }
                if (dbAction!= null) {
                    throw new BusinessException("对本稿件的投币枚数已用完");
                }
                // 减少自己的币
                Integer updateCount = userInfoMapper.updateCoinCountInfo(bean.getUserId(), -bean.getActionCount());
                if (updateCount == 0) {
                    throw new BusinessException("硬币数量不足");
                }
                // 给up主加币
                updateCount = userInfoMapper.updateCoinCountInfo(videoInfo.getUserId(), bean.getActionCount());
                if (updateCount == 0) {
                    throw new BusinessException("投币失败");
                }
                userActionMapper.insert(bean);
                videoInfoMapper.updateCountInfo(bean.getVideoId(), actionTypeEnum.getField(), bean.getActionCount());
                break;
            case COMMENT_LIKE:
            case COMMENT_HATE:
                UserActionTypeEnum opposeTypeEnum = UserActionTypeEnum.COMMENT_LIKE==actionTypeEnum?UserActionTypeEnum.COMMENT_HATE:UserActionTypeEnum.COMMENT_LIKE;
                UserAction opposeAction = userActionMapper.selectByVideoIdAndCommentIdAndActionTypeAndUserId(bean.getVideoId(),
                        bean.getCommentId(), opposeTypeEnum.getType(), bean.getUserId());
                if (opposeAction != null) {
                    userActionMapper.deleteByActionId(opposeAction.getActionId());
                }

                if (dbAction != null) {
                    userActionMapper.deleteByActionId(dbAction.getActionId());
                } else {
                    userActionMapper.insert(bean);
                }
                changeCount = dbAction==null? Constants.ONE : -Constants.ONE;
                Integer opposeChangeCount = opposeAction==null? Constants.ZERO: -Constants.ONE;
                videoCommentMapper.updateCountInfo(bean.getCommentId(), actionTypeEnum.getField(), changeCount,
                                                        opposeTypeEnum==null?null:opposeTypeEnum.getField(),
                                                        opposeChangeCount);
                break;
        }
    }
}