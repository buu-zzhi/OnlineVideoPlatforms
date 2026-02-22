package com.easylive.mapper;

import com.easylive.entity.dto.UserMessageCountDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 用户消息表 Mapper
 * @Author: false
 * @Date: 2026/02/22 15:37:18
 */
public interface UserMessageMapper<T, P> extends BaseMapper {

	/**
 	 * 根据 MessageId 查询
 	 */
	T selectByMessageId(@Param("messageId")Integer messageId);

	/**
 	 * 根据 MessageId 更新
 	 */
	Integer updateByMessageId(@Param("bean") T t, @Param("messageId")Integer messageId); 

	/**
 	 * 根据 MessageId 删除
 	 */
	Integer deleteByMessageId(@Param("messageId")Integer messageId);

    List<UserMessageCountDto> getMessageTypeNoReadCount(@Param("userId") String userId);

    void updateByParam(@Param("bean") T t, @Param("query") P p);

    void deleteByParam(@Param("query") P p);
}