package com.easylive.service;


import java.util.List;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.UserVideoSeries;
import com.easylive.entity.query.UserVideoSeriesQuery;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * @Description: 用户视频序列归档 Service
 * @Author: false
 * @Date: 2026/02/12 20:52:11
 */
public interface UserVideoSeriesService{

	/**
 	 * 根据条件查询列表
 	 */
	List<UserVideoSeries> findListByParam(UserVideoSeriesQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(UserVideoSeriesQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<UserVideoSeries> findListByPage(UserVideoSeriesQuery query);

	/**
 	 * 新增
 	 */
	Integer add(UserVideoSeries bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<UserVideoSeries> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<UserVideoSeries> listBean);

	/**
 	 * 根据 SeriesId 查询
 	 */
	UserVideoSeries getUserVideoSeriesBySeriesId(Integer seriesId);

	/**
 	 * 根据 SeriesId 更新
 	 */
	Integer updateUserVideoSeriesBySeriesId(UserVideoSeries bean, Integer seriesId); 

	/**
 	 * 根据 SeriesId 删除
 	 */
	Integer deleteUserVideoSeriesBySeriesId(Integer seriesId);

    List<UserVideoSeries> getUserAllSeries(@NotEmpty String userId);

    void saveUserVideoSeries(UserVideoSeries bean, String videoIds);

    void saveSeriesVideo(String userId, Integer seriesId, String videoIds);

    void delSeriesVideo(String userId, @NotNull Integer seriesId, @NotEmpty String videoId);

    void delVideoSeries(String userId, @NotNull Integer seriesId);

    void changeVideoSeriesSort(String userId, String seriesIds);

    List<UserVideoSeries> findListWithVideoList(UserVideoSeriesQuery seriesQuery);
}