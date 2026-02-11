package com.easylive.service;


import java.util.List;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.VideoComment;
import com.easylive.entity.query.VideoCommentQuery;
/**
 * @Description: 评论 Service
 * @Author: false
 * @Date: 2026/02/10 21:56:04
 */
public interface VideoCommentService{

	/**
 	 * 根据条件查询列表
 	 */
	List<VideoComment> findListByParam(VideoCommentQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(VideoCommentQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<VideoComment> findListByPage(VideoCommentQuery query);

	/**
 	 * 新增
 	 */
	Integer add(VideoComment bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<VideoComment> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<VideoComment> listBean);

	/**
 	 * 根据 CommentId 查询
 	 */
	VideoComment getVideoCommentByCommentId(Integer commentId);

	/**
 	 * 根据 CommentId 更新
 	 */
	Integer updateVideoCommentByCommentId(VideoComment bean, Integer commentId); 

	/**
 	 * 根据 CommentId 删除
 	 */
	Integer deleteVideoCommentByCommentId(Integer commentId);
}