package com.easylive.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 评论 Mapper
 * @Author: false
 * @Date: 2026/02/10 21:56:04
 */
public interface VideoCommentMapper<T, P> extends BaseMapper {

	/**
 	 * 根据 CommentId 查询
 	 */
	T selectByCommentId(@Param("commentId")Integer commentId);

	/**
 	 * 根据 CommentId 更新
 	 */
	Integer updateByCommentId(@Param("bean") T t, @Param("commentId")Integer commentId); 

	/**
 	 * 根据 CommentId 删除
 	 */
	Integer deleteByCommentId(@Param("commentId")Integer commentId);

    List<T> selectListWithChildren(@Param("query")P p);

    Integer updateCountInfo(@Param("commentId") Integer commentId, @Param("field") String field, @Param("changeCount")Integer changeCount,
    @Param("opposeField") String opposeField, @Param("opposeChangeCount") Integer opposeChangeCount);

    void updateByParam(@Param("bean") T t, @Param("query")P p);

    void deleteByParam(@Param("query")P p);
}