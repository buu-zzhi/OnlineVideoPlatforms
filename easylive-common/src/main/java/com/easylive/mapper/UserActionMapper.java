package com.easylive.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @Description: 用户行为 点赞、评论 Mapper
 * @Author: false
 * @Date: 2026/02/10 21:56:04
 */
public interface UserActionMapper<T, P> extends BaseMapper {

	/**
 	 * 根据 ActionId 查询
 	 */
	T selectByActionId(@Param("actionId")Integer actionId);

	/**
 	 * 根据 ActionId 更新
 	 */
	Integer updateByActionId(@Param("bean") T t, @Param("actionId")Integer actionId); 

	/**
 	 * 根据 ActionId 删除
 	 */
	Integer deleteByActionId(@Param("actionId")Integer actionId);

	/**
 	 * 根据 VideoIdAndCommentIdAndActionTypeAndUserId 查询
 	 */
	T selectByVideoIdAndCommentIdAndActionTypeAndUserId(@Param("videoId")String videoId, @Param("commentId")Integer commentId, @Param("actionType")Integer actionType, @Param("userId")String userId);

	/**
 	 * 根据 VideoIdAndCommentIdAndActionTypeAndUserId 更新
 	 */
	Integer updateByVideoIdAndCommentIdAndActionTypeAndUserId(@Param("bean") T t, @Param("videoId")String videoId, @Param("commentId")Integer commentId, @Param("actionType")Integer actionType, @Param("userId")String userId); 

	/**
 	 * 根据 VideoIdAndCommentIdAndActionTypeAndUserId 删除
 	 */
	Integer deleteByVideoIdAndCommentIdAndActionTypeAndUserId(@Param("videoId")String videoId, @Param("commentId")Integer commentId, @Param("actionType")Integer actionType, @Param("userId")String userId);

}