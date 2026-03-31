package com.easylive.service.impl;


import java.util.List;
import com.easylive.entity.query.SimplePage;
import com.easylive.entity.enums.PageSize;
import com.easylive.mapper.UserVideoSeriesVideoMapper;
import com.easylive.service.UserVideoSeriesVideoService;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.UserVideoSeriesVideo;
import com.easylive.entity.query.UserVideoSeriesVideoQuery;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
/**
 * @Description:  业务接口实现
 * @Author: false
 * @Date: 2026/02/12 20:52:11
 */
@Service("UserVideoSeriesVideoMapper")
public class UserVideoSeriesVideoServiceImpl implements UserVideoSeriesVideoService{

	@Resource
	private UserVideoSeriesVideoMapper<UserVideoSeriesVideo, UserVideoSeriesVideoQuery> userVideoSeriesVideoMapper;

	/**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<UserVideoSeriesVideo> findListByParam(UserVideoSeriesVideoQuery query) {
		return this.userVideoSeriesVideoMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(UserVideoSeriesVideoQuery query) {
		return this.userVideoSeriesVideoMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<UserVideoSeriesVideo> findListByPage(UserVideoSeriesVideoQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserVideoSeriesVideo> list = this.findListByParam(query);
		PaginationResultVO<UserVideoSeriesVideo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(UserVideoSeriesVideo bean) {
		return this.userVideoSeriesVideoMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<UserVideoSeriesVideo> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.userVideoSeriesVideoMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<UserVideoSeriesVideo> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.userVideoSeriesVideoMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 SeriesIdAndVideoId 查询
 	 */
	@Override
	public UserVideoSeriesVideo getUserVideoSeriesVideoBySeriesIdAndVideoId(Integer seriesId, String videoId) {
		return this.userVideoSeriesVideoMapper.selectBySeriesIdAndVideoId(seriesId, videoId);}

	/**
 	 * 根据 SeriesIdAndVideoId 更新
 	 */
	@Override
	public Integer updateUserVideoSeriesVideoBySeriesIdAndVideoId(UserVideoSeriesVideo bean, Integer seriesId, String videoId) {
		return this.userVideoSeriesVideoMapper.updateBySeriesIdAndVideoId(bean, seriesId, videoId);}

	/**
 	 * 根据 SeriesIdAndVideoId 删除
 	 */
	@Override
	public Integer deleteUserVideoSeriesVideoBySeriesIdAndVideoId(Integer seriesId, String videoId) {
		return this.userVideoSeriesVideoMapper.deleteBySeriesIdAndVideoId(seriesId, videoId);}
}