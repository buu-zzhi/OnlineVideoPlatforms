package com.easylive.service;


import java.util.List;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.VideoInfoFile;
import com.easylive.entity.query.VideoInfoFileQuery;
/**
 * @Description: 视频文件信息 Service
 * @Author: false
 * @Date: 2026/02/05 21:42:17
 */
public interface VideoInfoFileService{

	/**
 	 * 根据条件查询列表
 	 */
	List<VideoInfoFile> findListByParam(VideoInfoFileQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(VideoInfoFileQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<VideoInfoFile> findListByPage(VideoInfoFileQuery query);

	/**
 	 * 新增
 	 */
	Integer add(VideoInfoFile bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<VideoInfoFile> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<VideoInfoFile> listBean);

	/**
 	 * 根据 FileId 查询
 	 */
	VideoInfoFile getVideoInfoFileByFileId(String fileId);

	/**
 	 * 根据 FileId 更新
 	 */
	Integer updateVideoInfoFileByFileId(VideoInfoFile bean, String fileId); 

	/**
 	 * 根据 FileId 删除
 	 */
	Integer deleteVideoInfoFileByFileId(String fileId);
}