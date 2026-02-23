package com.easylive.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: 数据统计 Mapper
 * @Author: false
 * @Date: 2026/02/22 15:37:18
 */
public interface StatisticsInfoMapper<T, P> extends BaseMapper {

	/**
 	 * 根据 StatisticsDateAndUserIdAndDataType 查询
 	 */
	T selectByStatisticsDateAndUserIdAndDataType(@Param("statisticsDate")String statisticsDate, @Param("userId")String userId, @Param("dataType")Integer dataType);

	/**
 	 * 根据 StatisticsDateAndUserIdAndDataType 更新
 	 */
	Integer updateByStatisticsDateAndUserIdAndDataType(@Param("bean") T t, @Param("statisticsDate")String statisticsDate, @Param("userId")String userId, @Param("dataType")Integer dataType); 

	/**
 	 * 根据 StatisticsDateAndUserIdAndDataType 删除
 	 */
	Integer deleteByStatisticsDateAndUserIdAndDataType(@Param("statisticsDate")String statisticsDate, @Param("userId")String userId, @Param("dataType")Integer dataType);

    List<T> selectStatisticsFans(@Param("statisticsDate") String statisticsDate);

    List<T> selectStatisticsComment(@Param("statisticsDate") String statisticsDate);

    List<T> selectStatisticsInfo(@Param("statisticsDate") String statisticsDate, @Param("actionTypeArray") Integer[] actionType);

    Map<String, Integer> selectTotalCountInfo(@Param("userId") String userId);
}