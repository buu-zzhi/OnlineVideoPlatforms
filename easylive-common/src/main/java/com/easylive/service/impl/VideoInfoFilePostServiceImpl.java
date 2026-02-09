package com.easylive.service.impl;


import java.util.List;

import com.easylive.entity.enums.PageSize;
import com.easylive.entity.query.SimplePage;
import com.easylive.mapper.VideoInfoFilePostMapper;
import com.easylive.service.VideoInfoFilePostService;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.VideoInfoFilePost;
import com.easylive.entity.query.VideoInfoFilePostQuery;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
 * @Description: 视频文件信息 业务接口实现
 * @Author: false
 * @Date: 2026/02/05 21:42:17
 */
@Service("VideoInfoFilePostMapper")
public class VideoInfoFilePostServiceImpl implements VideoInfoFilePostService{

	@Resource
	private VideoInfoFilePostMapper<VideoInfoFilePost, VideoInfoFilePostQuery> videoInfoFilePostMapper;

	/**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<VideoInfoFilePost> findListByParam(VideoInfoFilePostQuery query) {
		return this.videoInfoFilePostMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(VideoInfoFilePostQuery query) {
		return this.videoInfoFilePostMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<VideoInfoFilePost> findListByPage(VideoInfoFilePostQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<VideoInfoFilePost> list = this.findListByParam(query);
		PaginationResultVO<VideoInfoFilePost> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(VideoInfoFilePost bean) {
		return this.videoInfoFilePostMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<VideoInfoFilePost> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.videoInfoFilePostMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<VideoInfoFilePost> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.videoInfoFilePostMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 FileId 查询
 	 */
	@Override
	public VideoInfoFilePost getVideoInfoFilePostByFileId(String fileId) {
		return this.videoInfoFilePostMapper.selectByFileId(fileId);}

	/**
 	 * 根据 FileId 更新
 	 */
	@Override
	public Integer updateVideoInfoFilePostByFileId(VideoInfoFilePost bean, String fileId) {
		return this.videoInfoFilePostMapper.updateByFileId(bean, fileId);}

	/**
 	 * 根据 FileId 删除
 	 */
	@Override
	public Integer deleteVideoInfoFilePostByFileId(String fileId) {
		return this.videoInfoFilePostMapper.deleteByFileId(fileId);}

	/**
 	 * 根据 UploadIdAndUserId 查询
 	 */
	@Override
	public VideoInfoFilePost getVideoInfoFilePostByUploadIdAndUserId(String uploadId, String userId) {
		return this.videoInfoFilePostMapper.selectByUploadIdAndUserId(uploadId, userId);}

	/**
 	 * 根据 UploadIdAndUserId 更新
 	 */
	@Override
	public Integer updateVideoInfoFilePostByUploadIdAndUserId(VideoInfoFilePost bean, String uploadId, String userId) {
		return this.videoInfoFilePostMapper.updateByUploadIdAndUserId(bean, uploadId, userId);}

	/**
 	 * 根据 UploadIdAndUserId 删除
 	 */
	@Override
	public Integer deleteVideoInfoFilePostByUploadIdAndUserId(String uploadId, String userId) {
		return this.videoInfoFilePostMapper.deleteByUploadIdAndUserId(uploadId, userId);}
}