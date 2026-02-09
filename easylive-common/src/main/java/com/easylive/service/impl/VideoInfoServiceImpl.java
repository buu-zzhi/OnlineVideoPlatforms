package com.easylive.service.impl;


import java.util.List;
import com.easylive.entity.query.SimplePage;
import com.easylive.entity.enums.PageSize;
import com.easylive.mapper.VideoInfoMapper;
import com.easylive.service.VideoInfoService;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.VideoInfo;
import com.easylive.entity.query.VideoInfoQuery;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
 * @Description: 视频信息 业务接口实现
 * @Author: false
 * @Date: 2026/02/05 21:42:17
 */
@Service("VideoInfoMapper")
public class VideoInfoServiceImpl implements VideoInfoService{

	@Resource
	private VideoInfoMapper<VideoInfo, VideoInfoQuery> videoInfoMapper;

	/**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<VideoInfo> findListByParam(VideoInfoQuery query) {
		return this.videoInfoMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(VideoInfoQuery query) {
		return this.videoInfoMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<VideoInfo> findListByPage(VideoInfoQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<VideoInfo> list = this.findListByParam(query);
		PaginationResultVO<VideoInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(VideoInfo bean) {
		return this.videoInfoMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<VideoInfo> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.videoInfoMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<VideoInfo> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.videoInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 VideoId 查询
 	 */
	@Override
	public VideoInfo getVideoInfoByVideoId(String videoId) {
		return this.videoInfoMapper.selectByVideoId(videoId);}

	/**
 	 * 根据 VideoId 更新
 	 */
	@Override
	public Integer updateVideoInfoByVideoId(VideoInfo bean, String videoId) {
		return this.videoInfoMapper.updateByVideoId(bean, videoId);}

	/**
 	 * 根据 VideoId 删除
 	 */
	@Override
	public Integer deleteVideoInfoByVideoId(String videoId) {
		return this.videoInfoMapper.deleteByVideoId(videoId);}
}