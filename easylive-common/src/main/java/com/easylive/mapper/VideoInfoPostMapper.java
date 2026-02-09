package com.easylive.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @Description: 视频信息 Mapper
 * @Author: false
 * @Date: 2026/02/05 21:42:17
 */
public interface VideoInfoPostMapper<T, P> extends BaseMapper {

	/**
	 * 根据条件更新，返回成功修改的数据条数
	 */
	Integer updateByParam(@Param("bean") T t, @Param("query") P p);

	/**
 	 * 根据 VideoId 查询
 	 */
	T selectByVideoId(@Param("videoId")String videoId);

	/**
 	 * 根据 VideoId 更新
 	 */
	Integer updateByVideoId(@Param("bean") T t, @Param("videoId")String videoId); 

	/**
 	 * 根据 VideoId 删除
 	 */
	Integer deleteByVideoId(@Param("videoId")String videoId);
}