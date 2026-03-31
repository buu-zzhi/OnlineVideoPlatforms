package com.easylive.service.impl;


import java.util.Date;
import java.util.List;

import com.easylive.entity.dto.UserMessageCountDto;
import com.easylive.entity.dto.UserMessageExtendDto;
import com.easylive.entity.enums.MessageReadTypeEnum;
import com.easylive.entity.enums.MessageTypeEnum;
import com.easylive.entity.po.VideoComment;
import com.easylive.entity.po.VideoInfo;
import com.easylive.entity.po.VideoInfoPost;
import com.easylive.entity.query.*;
import com.easylive.entity.enums.PageSize;
import com.easylive.mapper.UserMessageMapper;
import com.easylive.mapper.VideoCommentMapper;
import com.easylive.mapper.VideoInfoMapper;
import com.easylive.mapper.VideoInfoPostMapper;
import com.easylive.service.UserMessageService;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.UserMessage;
import com.easylive.utils.JsonUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.parsson.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
/**
 * @Description: 用户消息表 业务接口实现
 * @Author: false
 * @Date: 2026/02/22 15:37:18
 */
@Service("UserMessageMapper")
public class UserMessageServiceImpl implements UserMessageService{

	@Resource
	private UserMessageMapper<UserMessage, UserMessageQuery> userMessageMapper;

    @Resource
    private VideoInfoMapper<VideoInfo, VideoInfoQuery> videoInfoMapper;
    @Autowired
    private VideoCommentMapper<VideoComment, VideoCommentQuery> videoCommentMapper;
    @Autowired
    private VideoInfoPostMapper<VideoInfoPost, VideoInfoPostQuery> videoInfoPostMapper;

    /**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<UserMessage> findListByParam(UserMessageQuery query) {
		return this.userMessageMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(UserMessageQuery query) {
		return this.userMessageMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<UserMessage> findListByPage(UserMessageQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserMessage> list = this.findListByParam(query);
		PaginationResultVO<UserMessage> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(UserMessage bean) {
		return this.userMessageMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<UserMessage> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.userMessageMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<UserMessage> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.userMessageMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 MessageId 查询
 	 */
	@Override
	public UserMessage getUserMessageByMessageId(Integer messageId) {
		return this.userMessageMapper.selectByMessageId(messageId);}

	/**
 	 * 根据 MessageId 更新
 	 */
	@Override
	public Integer updateUserMessageByMessageId(UserMessage bean, Integer messageId) {
		return this.userMessageMapper.updateByMessageId(bean, messageId);}

	/**
 	 * 根据 MessageId 删除
 	 */
	@Override
	public Integer deleteUserMessageByMessageId(Integer messageId) {
		return this.userMessageMapper.deleteByMessageId(messageId);}

    @Override
    @Async
    public void saveUserMessage(String videoId, String sendUserId, MessageTypeEnum messageTypeEnum, String content, Integer replyCommentId) {
        VideoInfo videoInfo = videoInfoMapper.selectByVideoId(videoId);
        if (videoInfo==null){
            return;
        }
        UserMessageExtendDto extendDto = new UserMessageExtendDto();
        extendDto.setMessageContent(content);

        String userId = videoInfo.getUserId();  // 收到信息的人的id

        // 收藏 点赞 已经记录的不再记录
        if (ArrayUtils.contains(new Integer[]{MessageTypeEnum.LIKE.getType(), MessageTypeEnum.COLLECTION.getType()}, messageTypeEnum.getType())) {
            UserMessageQuery userMessageQuery = new UserMessageQuery();
            userMessageQuery.setUserId(userId);
            userMessageQuery.setVideoId(videoId);
            userMessageQuery.setMessageType(messageTypeEnum.getType());
            Integer count = userMessageMapper.selectCount(userMessageQuery);
            if (count > 0) {
                return;
            }
        }

        UserMessage userMessage = new UserMessage();
        userMessage.setVideoId(videoId);
        userMessage.setReadType(MessageReadTypeEnum.NO_READ.getType());
        userMessage.setCreateTime(new Date());
        userMessage.setMessageType(messageTypeEnum.getType());
        userMessage.setSendUserId(sendUserId);
        // 评论特殊处理
        if (replyCommentId != null) {
            VideoComment comment = videoCommentMapper.selectByCommentId(replyCommentId);
            if (comment != null) {
                // 给回复的评论的人发送信息
                userId = comment.getUserId();
                extendDto.setMessageContentReply(comment.getContent());
            }
        }
        if (userId.equals(sendUserId)) {
            return;
        }
        // 系统信息
        if (MessageTypeEnum.SYS == messageTypeEnum) {
            VideoInfoPost videoInfoPost = videoInfoPostMapper.selectByVideoId(videoId);
            extendDto.setAuditStatus(videoInfoPost.getStatus());
        }

        userMessage.setUserId(userId);
        userMessage.setExtendJson(JsonUtils.convertObj2Json(extendDto));
        userMessageMapper.insert(userMessage);
    }

    @Override
    public List<UserMessageCountDto> getMessageTypeNoReadCount(String userId) {
        return userMessageMapper.getMessageTypeNoReadCount(userId);
    }

    @Override
    public void updateByParam(UserMessage userMessage, UserMessageQuery messageQuery) {
        userMessageMapper.updateByParam(userMessage, messageQuery);
    }

    @Override
    public void deleteByParam(UserMessageQuery messageQuery) {
        userMessageMapper.deleteByParam(messageQuery);
    }
}