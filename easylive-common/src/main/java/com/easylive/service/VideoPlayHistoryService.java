package com.easylive.service;


import java.util.List;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.VideoPlayHistory;
import com.easylive.entity.query.VideoPlayHistoryQuery;
/**
 * @Description: 视频播放历史 Service
 * @Author: false
 * @Date: 2026/02/22 15:37:18
 */
public interface VideoPlayHistoryService{

	/**
 	 * 根据条件查询列表
 	 */
	List<VideoPlayHistory> findListByParam(VideoPlayHistoryQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(VideoPlayHistoryQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<VideoPlayHistory> findListByPage(VideoPlayHistoryQuery query);

	/**
 	 * 新增
 	 */
	Integer add(VideoPlayHistory bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<VideoPlayHistory> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<VideoPlayHistory> listBean);

	/**
 	 * 根据 UserIdAndVideoId 查询
 	 */
	VideoPlayHistory getVideoPlayHistoryByUserIdAndVideoId(String userId, String videoId);

	/**
 	 * 根据 UserIdAndVideoId 更新
 	 */
	Integer updateVideoPlayHistoryByUserIdAndVideoId(VideoPlayHistory bean, String userId, String videoId); 

	/**
 	 * 根据 UserIdAndVideoId 删除
 	 */
	Integer deleteVideoPlayHistoryByUserIdAndVideoId(String userId, String videoId);

    void saveHistory(String userId, String videoId, Integer fileIndex);

    void deleteByParam(VideoPlayHistoryQuery videoPlayHistoryQuery);
}