package com.easylive.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @Description: 视频播放历史 Mapper
 * @Author: false
 * @Date: 2026/02/22 15:37:18
 */
public interface VideoPlayHistoryMapper<T, P> extends BaseMapper {

	/**
 	 * 根据 UserIdAndVideoId 查询
 	 */
	T selectByUserIdAndVideoId(@Param("userId")String userId, @Param("videoId")String videoId);

	/**
 	 * 根据 UserIdAndVideoId 更新
 	 */
	Integer updateByUserIdAndVideoId(@Param("bean") T t, @Param("userId")String userId, @Param("videoId")String videoId); 

	/**
 	 * 根据 UserIdAndVideoId 删除
 	 */
	Integer deleteByUserIdAndVideoId(@Param("userId")String userId, @Param("videoId")String videoId);

    void deleteByParam(@Param("query") P p);
}