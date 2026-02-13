package com.easylive.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 用户视频序列归档 Mapper
 * @Author: false
 * @Date: 2026/02/12 20:52:11
 */
public interface UserVideoSeriesMapper<T, P> extends BaseMapper {

	/**
 	 * 根据 SeriesId 查询
 	 */
	T selectBySeriesId(@Param("seriesId")Integer seriesId);

	/**
 	 * 根据 SeriesId 更新
 	 */
	Integer updateBySeriesId(@Param("bean") T t, @Param("seriesId")Integer seriesId); 

	/**
 	 * 根据 SeriesId 删除
 	 */
	Integer deleteBySeriesId(@Param("seriesId")Integer seriesId);

    List<T> selectUserAllSeries(@Param("userId") String userId);

    Integer selectMaxSort(@Param("userId") String userId);

    void updateByParam(@Param("bean") T t, @Param("query") P p);

    Integer deleteByParam(@Param("query") P p);

    void changeSort(@Param("videoSeriesList") List<T> videoSeriesList);

    List<T> selectListWithVideo(@Param("query") P p);
}