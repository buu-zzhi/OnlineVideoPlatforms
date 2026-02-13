package com.easylive.service;


import java.util.List;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.UserVideoSeriesVideo;
import com.easylive.entity.query.UserVideoSeriesVideoQuery;
/**
 * @Description:  Service
 * @Author: false
 * @Date: 2026/02/12 20:52:11
 */
public interface UserVideoSeriesVideoService{

	/**
 	 * 根据条件查询列表
 	 */
	List<UserVideoSeriesVideo> findListByParam(UserVideoSeriesVideoQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(UserVideoSeriesVideoQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<UserVideoSeriesVideo> findListByPage(UserVideoSeriesVideoQuery query);

	/**
 	 * 新增
 	 */
	Integer add(UserVideoSeriesVideo bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<UserVideoSeriesVideo> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<UserVideoSeriesVideo> listBean);

	/**
 	 * 根据 SeriesIdAndVideoId 查询
 	 */
	UserVideoSeriesVideo getUserVideoSeriesVideoBySeriesIdAndVideoId(Integer seriesId, String videoId);

	/**
 	 * 根据 SeriesIdAndVideoId 更新
 	 */
	Integer updateUserVideoSeriesVideoBySeriesIdAndVideoId(UserVideoSeriesVideo bean, Integer seriesId, String videoId); 

	/**
 	 * 根据 SeriesIdAndVideoId 删除
 	 */
	Integer deleteUserVideoSeriesVideoBySeriesIdAndVideoId(Integer seriesId, String videoId);
}