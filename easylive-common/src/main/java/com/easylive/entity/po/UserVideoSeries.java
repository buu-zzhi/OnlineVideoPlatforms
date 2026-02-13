package com.easylive.entity.po;

import java.io.Serializable;

import com.easylive.entity.enums.DateTimePatternEnum;
import com.easylive.utils.DateUtils;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 用户视频序列归档
 * @Author: false
 * @Date: 2026/02/12 20:52:11
 */
public class UserVideoSeries implements Serializable {
	/**
 	 * 列表ID
 	 */
	private Integer seriesId;

	/**
 	 * 列表名称
 	 */
	private String seriesName;

	/**
 	 * 描述
 	 */
	private String seriesDescription;

	/**
 	 * 用户ID
 	 */
	private String userId;

	/**
 	 * 排序
 	 */
	private Integer sort;

	/**
 	 * 更新时间
 	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

    private String videoCover;

    private List<VideoInfo> videoInfoList;

    public List<VideoInfo> getVideoInfoList() {
        return videoInfoList;
    }

    public void setVideoInfoList(List<VideoInfo> videoInfoList) {
        this.videoInfoList = videoInfoList;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public void setSeriesId(Integer seriesId) {
		this.seriesId = seriesId;
	}

	public Integer getSeriesId() {
		return seriesId;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesDescription(String seriesDescription) {
		this.seriesDescription = seriesDescription;
	}

	public String getSeriesDescription() {
		return seriesDescription;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getSort() {
		return sort;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}
	@Override
	public String toString() {
		return "列表ID:" + (seriesId == null ? "空" : seriesId) + "," + 
				"列表名称:" + (seriesName == null ? "空" : seriesName) + "," + 
				"描述:" + (seriesDescription == null ? "空" : seriesDescription) + "," + 
				"用户ID:" + (userId == null ? "空" : userId) + "," + 
				"排序:" + (sort == null ? "空" : sort) + "," + 
				"更新时间:" + (updateTime == null ? "空" : DateUtils.format(updateTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
		}
}