package com.easylive.service;


import java.util.List;

import com.easylive.entity.dto.UserMessageCountDto;
import com.easylive.entity.enums.MessageTypeEnum;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.UserMessage;
import com.easylive.entity.query.UserMessageQuery;
/**
 * @Description: 用户消息表 Service
 * @Author: false
 * @Date: 2026/02/22 15:37:18
 */
public interface UserMessageService{

	/**
 	 * 根据条件查询列表
 	 */
	List<UserMessage> findListByParam(UserMessageQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(UserMessageQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<UserMessage> findListByPage(UserMessageQuery query);

	/**
 	 * 新增
 	 */
	Integer add(UserMessage bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<UserMessage> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<UserMessage> listBean);

	/**
 	 * 根据 MessageId 查询
 	 */
	UserMessage getUserMessageByMessageId(Integer messageId);

	/**
 	 * 根据 MessageId 更新
 	 */
	Integer updateUserMessageByMessageId(UserMessage bean, Integer messageId); 

	/**
 	 * 根据 MessageId 删除
 	 */
	Integer deleteUserMessageByMessageId(Integer messageId);

    void saveUserMessage(String videoId, String sendUserId, MessageTypeEnum messageTypeEnum, String content, Integer replyCommentId);

    List<UserMessageCountDto> getMessageTypeNoReadCount(String userId);

    void updateByParam(UserMessage userMessage, UserMessageQuery messageQuery);

    void deleteByParam(UserMessageQuery messageQuery);
}