package com.easylive.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @Description: 视频信息 Mapper
 * @Author: false
 * @Date: 2026/02/05 21:42:17
 */
public interface VideoInfoMapper<T, P> extends BaseMapper {

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