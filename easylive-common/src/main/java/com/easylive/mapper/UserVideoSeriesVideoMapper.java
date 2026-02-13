package com.easylive.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @Description:  Mapper
 * @Author: false
 * @Date: 2026/02/12 20:52:11
 */
public interface UserVideoSeriesVideoMapper<T, P> extends BaseMapper {

	/**
 	 * 根据 SeriesIdAndVideoId 查询
 	 */
	T selectBySeriesIdAndVideoId(@Param("seriesId")Integer seriesId, @Param("videoId")String videoId);

	/**
 	 * 根据 SeriesIdAndVideoId 更新
 	 */
	Integer updateBySeriesIdAndVideoId(@Param("bean") T t, @Param("seriesId")Integer seriesId, @Param("videoId")String videoId); 

	/**
 	 * 根据 SeriesIdAndVideoId 删除
 	 */
	Integer deleteBySeriesIdAndVideoId(@Param("seriesId")Integer seriesId, @Param("videoId")String videoId);

    Integer selectMaxSort(@Param("seriesId") Integer seriesId);

    void deleteByParam(@Param("query") P p);
}