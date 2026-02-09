package com.easylive.service;


import java.util.List;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.VideoInfoFilePost;
import com.easylive.entity.query.VideoInfoFilePostQuery;
/**
 * @Description: 视频文件信息 Service
 * @Author: false
 * @Date: 2026/02/05 21:42:17
 */
public interface VideoInfoFilePostService{

	/**
 	 * 根据条件查询列表
 	 */
	List<VideoInfoFilePost> findListByParam(VideoInfoFilePostQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(VideoInfoFilePostQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<VideoInfoFilePost> findListByPage(VideoInfoFilePostQuery query);

	/**
 	 * 新增
 	 */
	Integer add(VideoInfoFilePost bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<VideoInfoFilePost> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<VideoInfoFilePost> listBean);

	/**
 	 * 根据 FileId 查询
 	 */
	VideoInfoFilePost getVideoInfoFilePostByFileId(String fileId);

	/**
 	 * 根据 FileId 更新
 	 */
	Integer updateVideoInfoFilePostByFileId(VideoInfoFilePost bean, String fileId); 

	/**
 	 * 根据 FileId 删除
 	 */
	Integer deleteVideoInfoFilePostByFileId(String fileId);

	/**
 	 * 根据 UploadIdAndUserId 查询
 	 */
	VideoInfoFilePost getVideoInfoFilePostByUploadIdAndUserId(String uploadId, String userId);

	/**
 	 * 根据 UploadIdAndUserId 更新
 	 */
	Integer updateVideoInfoFilePostByUploadIdAndUserId(VideoInfoFilePost bean, String uploadId, String userId); 

	/**
 	 * 根据 UploadIdAndUserId 删除
 	 */
	Integer deleteVideoInfoFilePostByUploadIdAndUserId(String uploadId, String userId);
}