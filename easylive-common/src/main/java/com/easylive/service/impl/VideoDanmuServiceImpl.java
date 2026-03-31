package com.easylive.service.impl;


import java.util.List;

import com.easylive.component.EsSearchComponent;
import com.easylive.entity.constants.Constants;
import com.easylive.entity.enums.ResponseCodeEnum;
import com.easylive.entity.enums.SearchOrderTypeEnum;
import com.easylive.entity.enums.UserActionTypeEnum;
import com.easylive.entity.po.VideoInfo;
import com.easylive.entity.query.SimplePage;
import com.easylive.entity.enums.PageSize;
import com.easylive.entity.query.VideoInfoQuery;
import com.easylive.exception.BusinessException;
import com.easylive.mapper.VideoDanmuMapper;
import com.easylive.mapper.VideoInfoMapper;
import com.easylive.service.VideoDanmuService;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.VideoDanmu;
import com.easylive.entity.query.VideoDanmuQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
/**
 * @Description: 视频弹幕 业务接口实现
 * @Author: false
 * @Date: 2026/02/10 21:56:04
 */
@Service("VideoDanmuMapper")
public class VideoDanmuServiceImpl implements VideoDanmuService{

	@Resource
	private VideoDanmuMapper<VideoDanmu, VideoDanmuQuery> videoDanmuMapper;

    @Resource
    private VideoInfoMapper<VideoInfo, VideoInfoQuery> videoInfoMapper;
    @Autowired
    private EsSearchComponent esSearchComponent;

    /**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<VideoDanmu> findListByParam(VideoDanmuQuery query) {
		return this.videoDanmuMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(VideoDanmuQuery query) {
		return this.videoDanmuMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<VideoDanmu> findListByPage(VideoDanmuQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<VideoDanmu> list = this.findListByParam(query);
		PaginationResultVO<VideoDanmu> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(VideoDanmu bean) {
		return this.videoDanmuMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<VideoDanmu> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.videoDanmuMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<VideoDanmu> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.videoDanmuMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 DanmuId 查询
 	 */
	@Override
	public VideoDanmu getVideoDanmuByDanmuId(Integer danmuId) {
		return this.videoDanmuMapper.selectByDanmuId(danmuId);}

	/**
 	 * 根据 DanmuId 更新
 	 */
	@Override
	public Integer updateVideoDanmuByDanmuId(VideoDanmu bean, Integer danmuId) {
		return this.videoDanmuMapper.updateByDanmuId(bean, danmuId);}

	/**
 	 * 根据 DanmuId 删除
 	 */
	@Override
	public Integer deleteVideoDanmuByDanmuId(Integer danmuId) {
		return this.videoDanmuMapper.deleteByDanmuId(danmuId);}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveVideoDanmu(VideoDanmu videoDanmu) {
        VideoInfo videoInfo = videoInfoMapper.selectByVideoId(videoDanmu.getVideoId());
        if (videoInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (videoInfo.getInteraction() != null && videoInfo.getInteraction().contains(Constants.ONE.toString())) {
            throw new BusinessException("UP主已经关闭弹幕");
        }
        videoDanmuMapper.insert(videoDanmu);

        // 更新弹幕数量
        videoInfoMapper.updateCountInfo(videoDanmu.getVideoId(), UserActionTypeEnum.VIDEO_DANMU.getField(), 1);

        //更新es 弹幕数量
        esSearchComponent.updateDocCount(videoDanmu.getVideoId(), SearchOrderTypeEnum.VIDEO_DANMU.getField(), 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDanmu(String userId, Integer danmuId) {
        VideoDanmu videoDanmu = videoDanmuMapper.selectByDanmuId(danmuId);
        if (videoDanmu == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        VideoInfo videoInfo = videoInfoMapper.selectByVideoId(videoDanmu.getVideoId());
        if (videoInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (userId != null && !videoInfo.getUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        videoDanmuMapper.deleteByDanmuId(danmuId);
        videoInfoMapper.updateCountInfo(videoDanmu.getVideoId(), UserActionTypeEnum.VIDEO_DANMU.getField(), -Constants.ONE);
    }
}