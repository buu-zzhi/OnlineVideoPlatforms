package com.easylive.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.easylive.component.RedisComponent;
import com.easylive.entity.constants.Constants;
import com.easylive.entity.enums.StatisticsTypeEnum;
import com.easylive.entity.enums.UserActionTypeEnum;
import com.easylive.entity.po.UserFocus;
import com.easylive.entity.po.UserInfo;
import com.easylive.entity.po.VideoInfo;
import com.easylive.entity.query.*;
import com.easylive.entity.enums.PageSize;
import com.easylive.mapper.StatisticsInfoMapper;
import com.easylive.mapper.UserFocusMapper;
import com.easylive.mapper.UserInfoMapper;
import com.easylive.mapper.VideoInfoMapper;
import com.easylive.service.StatisticsInfoService;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.StatisticsInfo;
import com.easylive.utils.DateUtils;
import com.easylive.utils.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
/**
 * @Description: 数据统计 业务接口实现
 * @Author: false
 * @Date: 2026/02/22 15:37:18
 */
@Service("StatisticsInfoMapper")
public class StatisticsInfoServiceImpl implements StatisticsInfoService{

	@Resource
	private StatisticsInfoMapper<StatisticsInfo, StatisticsInfoQuery> statisticsInfoMapper;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private VideoInfoMapper<VideoInfo, VideoInfoQuery> videoInfoMapper;
    @Autowired
    private UserFocusMapper<UserFocus, UserFocusQuery> userFocusMapper;
    @Autowired
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    /**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<StatisticsInfo> findListByParam(StatisticsInfoQuery query) {
		return this.statisticsInfoMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(StatisticsInfoQuery query) {
		return this.statisticsInfoMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<StatisticsInfo> findListByPage(StatisticsInfoQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<StatisticsInfo> list = this.findListByParam(query);
		PaginationResultVO<StatisticsInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(StatisticsInfo bean) {
		return this.statisticsInfoMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<StatisticsInfo> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.statisticsInfoMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<StatisticsInfo> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.statisticsInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 StatisticsDateAndUserIdAndDataType 查询
 	 */
	@Override
	public StatisticsInfo getStatisticsInfoByStatisticsDateAndUserIdAndDataType(String statisticsDate, String userId, Integer dataType) {
		return this.statisticsInfoMapper.selectByStatisticsDateAndUserIdAndDataType(statisticsDate, userId, dataType);}

	/**
 	 * 根据 StatisticsDateAndUserIdAndDataType 更新
 	 */
	@Override
	public Integer updateStatisticsInfoByStatisticsDateAndUserIdAndDataType(StatisticsInfo bean, String statisticsDate, String userId, Integer dataType) {
		return this.statisticsInfoMapper.updateByStatisticsDateAndUserIdAndDataType(bean, statisticsDate, userId, dataType);}

	/**
 	 * 根据 StatisticsDateAndUserIdAndDataType 删除
 	 */
	@Override
	public Integer deleteStatisticsInfoByStatisticsDateAndUserIdAndDataType(String statisticsDate, String userId, Integer dataType) {
		return this.statisticsInfoMapper.deleteByStatisticsDateAndUserIdAndDataType(statisticsDate, userId, dataType);}

    @Override
    public void statisticsData() {
        List<StatisticsInfo> statisticsInfoList = new ArrayList<>();

        // TODO 修改为今天方便测试
        final String statisticDate = DateUtils.getBeforeDate(0);

        // 统计播放数
        Map<String,Integer> videoPlayCountMap = redisComponent.getVideoPlayCount(statisticDate);
        List<String> playVideoKeys = new ArrayList<>(videoPlayCountMap.keySet());
        playVideoKeys = playVideoKeys.stream().map(item->item.substring(item.lastIndexOf(":")+1)).collect(Collectors.toList());

        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setVideoIdArray(playVideoKeys.toArray(new String[playVideoKeys.size()]));
        List<VideoInfo> videoInfoList = videoInfoMapper.selectList(videoInfoQuery);

        Map<String, Integer> videoCountMap = videoInfoList.stream().collect(Collectors.groupingBy(VideoInfo::getUserId,
                Collectors.summingInt(item->videoPlayCountMap.get(Constants.REDIS_KEY_VIDEO_PLAY_COUNT + statisticDate + ":" + item.getVideoId()))));

        videoCountMap.forEach((k,v)->{
            StatisticsInfo statisticsInfo = new StatisticsInfo();
            statisticsInfo.setStatisticsDate(statisticDate);
            statisticsInfo.setUserId(k);
            statisticsInfo.setDataType(StatisticsTypeEnum.PLAY.getType());
            statisticsInfo.setStatisticsCount(v);
            statisticsInfoList.add(statisticsInfo);
        });

        // 统计粉丝
        List<StatisticsInfo> fansDataList = statisticsInfoMapper.selectStatisticsFans(statisticDate);
        for (StatisticsInfo item : fansDataList) {
            item.setDataType(StatisticsTypeEnum.FANS.getType());
            item.setStatisticsDate(statisticDate);
        }
        statisticsInfoList.addAll(fansDataList);

        // 统计评论
        List<StatisticsInfo> commentDataList = statisticsInfoMapper.selectStatisticsComment(statisticDate);
        for (StatisticsInfo item : commentDataList) {
            item.setStatisticsDate(statisticDate);
            item.setDataType(StatisticsTypeEnum.COMMENT.getType());
        }
        statisticsInfoList.addAll(commentDataList);

        // 弹幕 点赞 收藏 投币
        List<StatisticsInfo> statisticsOthers = statisticsInfoMapper.selectStatisticsInfo(statisticDate,
                new Integer[]{UserActionTypeEnum.VIDEO_LIKE.getType(),
                            UserActionTypeEnum.VIDEO_COLLECT.getType(), UserActionTypeEnum.VIDEO_COIN.getType()});
        for (StatisticsInfo item : statisticsOthers) {
            item.setStatisticsDate(statisticDate);
            if (item.getDataType().equals(UserActionTypeEnum.VIDEO_LIKE.getType())) {
                item.setDataType(StatisticsTypeEnum.LIKE.getType());
            } else if (item.getDataType().equals(UserActionTypeEnum.VIDEO_COLLECT.getType())) {
                item.setDataType(StatisticsTypeEnum.COLLECTION.getType());
            } else if (item.getDataType().equals(UserActionTypeEnum.VIDEO_COIN.getType())) {
                item.setDataType(StatisticsTypeEnum.COIN.getType());
            }
        }
        statisticsInfoList.addAll(statisticsOthers);


        statisticsInfoMapper.insertOrUpdateBatch(statisticsInfoList);
    }

    @Override
    public Map<String, Integer> getStatisticsInfoActualTime(String userId) {
        Map<String, Integer> result = statisticsInfoMapper.selectTotalCountInfo(userId);
        if (!StringTools.isEmpty(userId)) {
            result.put("userCount", userFocusMapper.selectFansCount(userId));
        } else {
            result.put("userCount", userInfoMapper.selectCount(new UserInfoQuery()));
        }
        return result;
    }

    @Override
    public List<StatisticsInfo> findListTotalInfoByParam(StatisticsInfoQuery query) {
        return statisticsInfoMapper.selectListTotalInfoByParam(query);
    }

    @Override
    public List<StatisticsInfo> findUserCountTotalInfoByParam(StatisticsInfoQuery param) {
        return statisticsInfoMapper.selectUserCountTotalInfoByParam(param);
    }
}