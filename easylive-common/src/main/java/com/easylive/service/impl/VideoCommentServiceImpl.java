package com.easylive.service.impl;


import java.util.List;
import com.easylive.entity.query.SimplePage;
import com.easylive.entity.enums.PageSize;
import com.easylive.mapper.VideoCommentMapper;
import com.easylive.service.VideoCommentService;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.VideoComment;
import com.easylive.entity.query.VideoCommentQuery;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
 * @Description: 评论 业务接口实现
 * @Author: false
 * @Date: 2026/02/10 21:56:04
 */
@Service("VideoCommentMapper")
public class VideoCommentServiceImpl implements VideoCommentService{

	@Resource
	private VideoCommentMapper<VideoComment, VideoCommentQuery> videoCommentMapper;

	/**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<VideoComment> findListByParam(VideoCommentQuery query) {
		return this.videoCommentMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(VideoCommentQuery query) {
		return this.videoCommentMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<VideoComment> findListByPage(VideoCommentQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<VideoComment> list = this.findListByParam(query);
		PaginationResultVO<VideoComment> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(VideoComment bean) {
		return this.videoCommentMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<VideoComment> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.videoCommentMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<VideoComment> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.videoCommentMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 CommentId 查询
 	 */
	@Override
	public VideoComment getVideoCommentByCommentId(Integer commentId) {
		return this.videoCommentMapper.selectByCommentId(commentId);}

	/**
 	 * 根据 CommentId 更新
 	 */
	@Override
	public Integer updateVideoCommentByCommentId(VideoComment bean, Integer commentId) {
		return this.videoCommentMapper.updateByCommentId(bean, commentId);}

	/**
 	 * 根据 CommentId 删除
 	 */
	@Override
	public Integer deleteVideoCommentByCommentId(Integer commentId) {
		return this.videoCommentMapper.deleteByCommentId(commentId);}
}