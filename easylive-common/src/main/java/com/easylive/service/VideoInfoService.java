package com.easylive.service;


import java.util.List;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.VideoInfo;
import com.easylive.entity.query.VideoInfoQuery;
/**
 * @Description: 视频信息 Service
 * @Author: false
 * @Date: 2026/02/05 21:42:17
 */
public interface VideoInfoService{

	/**
 	 * 根据条件查询列表
 	 */
	List<VideoInfo> findListByParam(VideoInfoQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(VideoInfoQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<VideoInfo> findListByPage(VideoInfoQuery query);

	/**
 	 * 新增
 	 */
	Integer add(VideoInfo bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<VideoInfo> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<VideoInfo> listBean);

	/**
 	 * 根据 VideoId 查询
 	 */
	VideoInfo getVideoInfoByVideoId(String videoId);

	/**
 	 * 根据 VideoId 更新
 	 */
	Integer updateVideoInfoByVideoId(VideoInfo bean, String videoId); 

	/**
 	 * 根据 VideoId 删除
 	 */
	Integer deleteVideoInfoByVideoId(String videoId);
}