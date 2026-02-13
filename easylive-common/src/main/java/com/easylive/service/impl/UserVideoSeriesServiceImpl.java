package com.easylive.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.easylive.entity.enums.ResponseCodeEnum;
import com.easylive.entity.po.UserVideoSeriesVideo;
import com.easylive.entity.po.VideoInfo;
import com.easylive.entity.query.SimplePage;
import com.easylive.entity.enums.PageSize;
import com.easylive.entity.query.UserVideoSeriesVideoQuery;
import com.easylive.entity.query.VideoInfoQuery;
import com.easylive.exception.BusinessException;
import com.easylive.mapper.UserVideoSeriesMapper;
import com.easylive.mapper.UserVideoSeriesVideoMapper;
import com.easylive.mapper.VideoInfoMapper;
import com.easylive.service.UserVideoSeriesService;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.UserVideoSeries;
import com.easylive.entity.query.UserVideoSeriesQuery;
import com.easylive.service.UserVideoSeriesVideoService;
import com.easylive.utils.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
/**
 * @Description: 用户视频序列归档 业务接口实现
 * @Author: false
 * @Date: 2026/02/12 20:52:11
 */
@Service("UserVideoSeriesMapper")
public class UserVideoSeriesServiceImpl implements UserVideoSeriesService{

	@Resource
	private UserVideoSeriesMapper<UserVideoSeries, UserVideoSeriesQuery> userVideoSeriesMapper;
    @Autowired
    private VideoInfoMapper<VideoInfo, VideoInfoQuery> videoInfoMapper;
    @Resource
    private UserVideoSeriesVideoMapper<UserVideoSeriesVideo, UserVideoSeriesVideoQuery> userVideoSeriesVideoMapper;

    /**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<UserVideoSeries> findListByParam(UserVideoSeriesQuery query) {
		return this.userVideoSeriesMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(UserVideoSeriesQuery query) {
		return this.userVideoSeriesMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<UserVideoSeries> findListByPage(UserVideoSeriesQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserVideoSeries> list = this.findListByParam(query);
		PaginationResultVO<UserVideoSeries> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(UserVideoSeries bean) {
		return this.userVideoSeriesMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<UserVideoSeries> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.userVideoSeriesMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<UserVideoSeries> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.userVideoSeriesMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 SeriesId 查询
 	 */
	@Override
	public UserVideoSeries getUserVideoSeriesBySeriesId(Integer seriesId) {
		return this.userVideoSeriesMapper.selectBySeriesId(seriesId);}

	/**
 	 * 根据 SeriesId 更新
 	 */
	@Override
	public Integer updateUserVideoSeriesBySeriesId(UserVideoSeries bean, Integer seriesId) {
		return this.userVideoSeriesMapper.updateBySeriesId(bean, seriesId);}

	/**
 	 * 根据 SeriesId 删除
 	 */
	@Override
	public Integer deleteUserVideoSeriesBySeriesId(Integer seriesId) {
		return this.userVideoSeriesMapper.deleteBySeriesId(seriesId);}

    @Override
    public List<UserVideoSeries> getUserAllSeries(String userId) {
        return userVideoSeriesMapper.selectUserAllSeries(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserVideoSeries(UserVideoSeries bean, String videoIds) {
        // 新建集合中必须要有视频
        if (bean.getSeriesId() == null&& StringTools.isEmpty(videoIds)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if (bean.getSeriesId() == null) {// 新增
            checkVideoIds(bean.getUserId(), videoIds);
            bean.setUpdateTime(new Date());
            bean.setSort(userVideoSeriesMapper.selectMaxSort(bean.getUserId()) + 1);
            userVideoSeriesMapper.insert(bean);
            saveSeriesVideo(bean.getUserId(), bean.getSeriesId(), videoIds);
        } else { // 修改
            UserVideoSeriesQuery videoSeries = new UserVideoSeriesQuery();
            videoSeries.setUserId(bean.getUserId());
            videoSeries.setSeriesId(bean.getSeriesId());
            userVideoSeriesMapper.updateByParam(bean, videoSeries);
        }
    }

    private void checkVideoIds(String userId, String videoIds) {
        String[] videoIdArray = videoIds.split(",");
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setVideoIdArray(videoIdArray);
        videoInfoQuery.setUserId(userId);
        Integer count = videoInfoMapper.selectCount(videoInfoQuery);
        if (videoIdArray.length != count) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
    }

    @Override
    public void saveSeriesVideo(String userId, Integer seriesId, String videoIds) {
        UserVideoSeries userVideoSeries = getUserVideoSeriesBySeriesId(seriesId);
        if (userVideoSeries == null || !userVideoSeries.getUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        checkVideoIds(userId, videoIds);
        String[] videoIdArray = videoIds.split(",");
        Integer sort = userVideoSeriesVideoMapper.selectMaxSort(seriesId);
        List<UserVideoSeriesVideo> seriesVideoList = new ArrayList<>();
        for (String videoId : videoIdArray) {
            UserVideoSeriesVideo videoSeriesVideo = new UserVideoSeriesVideo();
            videoSeriesVideo.setVideoId(videoId);
            videoSeriesVideo.setSort(++sort);
            videoSeriesVideo.setSeriesId(seriesId);
            videoSeriesVideo.setUserId(userId);
            seriesVideoList.add(videoSeriesVideo);
        }
        userVideoSeriesVideoMapper.insertOrUpdateBatch(seriesVideoList);
    }

    @Override
    public void delSeriesVideo(String userId, Integer seriesId, String videoId) {
        UserVideoSeriesVideoQuery videoSeriesVideoQuery = new UserVideoSeriesVideoQuery();
        videoSeriesVideoQuery.setUserId(userId);
        videoSeriesVideoQuery.setSeriesId(seriesId);
        videoSeriesVideoQuery.setVideoId(videoId);
        userVideoSeriesVideoMapper.deleteByParam(videoSeriesVideoQuery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delVideoSeries(String userId, Integer seriesId) {
        UserVideoSeriesQuery userVideoSeriesQuery = new UserVideoSeriesQuery();
        userVideoSeriesQuery.setSeriesId(seriesId);
        userVideoSeriesQuery.setUserId(userId);
        Integer count = userVideoSeriesMapper.deleteByParam(userVideoSeriesQuery);
        if (count == 0) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        UserVideoSeriesVideoQuery videoSeriesVideoQuery = new UserVideoSeriesVideoQuery();
        videoSeriesVideoQuery.setSeriesId(seriesId);
        videoSeriesVideoQuery.setUserId(userId);
        userVideoSeriesVideoMapper.deleteByParam(videoSeriesVideoQuery);
    }

    @Override
    public void changeVideoSeriesSort(String userId, String seriesIds) {
        String[] seriesIdArray = seriesIds.split(",");
        List<UserVideoSeries> videoSeriesList = new ArrayList<>();
        Integer sort = 0;
        for (String seriesId : seriesIdArray) {
            UserVideoSeries userVideoSeries = new UserVideoSeries();
            userVideoSeries.setUserId(userId);
            userVideoSeries.setSeriesId(Integer.parseInt(seriesId));
            userVideoSeries.setSort(++sort);
            videoSeriesList.add(userVideoSeries);
        }
        userVideoSeriesMapper.changeSort(videoSeriesList);
    }

    @Override
    public List<UserVideoSeries> findListWithVideoList(UserVideoSeriesQuery seriesQuery) {
        return userVideoSeriesMapper.selectListWithVideo(seriesQuery);
    }
}