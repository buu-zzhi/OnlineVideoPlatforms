package com.easylive.service;


import java.util.List;

import com.easylive.entity.po.VideoInfoFilePost;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.VideoInfoPost;
import com.easylive.entity.query.VideoInfoPostQuery;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Description: 视频信息 Service
 * @Author: false
 * @Date: 2026/02/05 21:42:17
 */
public interface VideoInfoPostService{

	/**
 	 * 根据条件查询列表
 	 */
	List<VideoInfoPost> findListByParam(VideoInfoPostQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(VideoInfoPostQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<VideoInfoPost> findListByPage(VideoInfoPostQuery query);

	/**
 	 * 新增
 	 */
	Integer add(VideoInfoPost bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<VideoInfoPost> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<VideoInfoPost> listBean);

	/**
 	 * 根据 VideoId 查询
 	 */
	VideoInfoPost getVideoInfoPostByVideoId(String videoId);

	/**
 	 * 根据 VideoId 更新
 	 */
	Integer updateVideoInfoPostByVideoId(VideoInfoPost bean, String videoId); 

	/**
 	 * 根据 VideoId 删除
 	 */
	Integer deleteVideoInfoPostByVideoId(String videoId);

    void saveVideoInfo(VideoInfoPost videoInfoPost, List<VideoInfoFilePost> filePostList);

    void transferVideoFile(VideoInfoFilePost videoInfoFilePost);

    void auditVideo(@NotEmpty String videoId, @NotNull Integer status, String reason);
}