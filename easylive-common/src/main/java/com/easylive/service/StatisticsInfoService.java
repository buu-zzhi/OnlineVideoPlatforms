package com.easylive.service;


import java.util.List;
import java.util.Map;

import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.StatisticsInfo;
import com.easylive.entity.query.StatisticsInfoQuery;
/**
 * @Description: 数据统计 Service
 * @Author: false
 * @Date: 2026/02/22 15:37:18
 */
public interface StatisticsInfoService{

	/**
 	 * 根据条件查询列表
 	 */
	List<StatisticsInfo> findListByParam(StatisticsInfoQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(StatisticsInfoQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<StatisticsInfo> findListByPage(StatisticsInfoQuery query);

	/**
 	 * 新增
 	 */
	Integer add(StatisticsInfo bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<StatisticsInfo> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<StatisticsInfo> listBean);

	/**
 	 * 根据 StatisticsDateAndUserIdAndDataType 查询
 	 */
	StatisticsInfo getStatisticsInfoByStatisticsDateAndUserIdAndDataType(String statisticsDate, String userId, Integer dataType);

	/**
 	 * 根据 StatisticsDateAndUserIdAndDataType 更新
 	 */
	Integer updateStatisticsInfoByStatisticsDateAndUserIdAndDataType(StatisticsInfo bean, String statisticsDate, String userId, Integer dataType); 

	/**
 	 * 根据 StatisticsDateAndUserIdAndDataType 删除
 	 */
	Integer deleteStatisticsInfoByStatisticsDateAndUserIdAndDataType(String statisticsDate, String userId, Integer dataType);

    void statisticsData();

    Map<String, Integer> getStatisticsInfoActualTime(String userId);
}