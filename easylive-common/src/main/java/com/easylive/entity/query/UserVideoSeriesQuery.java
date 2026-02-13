package com.easylive.entity.query;

import java.util.Date;


/**
 * @Description: 用户视频序列归档
 * @Author: false
 * @Date: 2026/02/12 20:52:11
 */
public class UserVideoSeriesQuery extends BaseQuery {
	/**
 	 * 列表ID 查询对象
 	 */
	private Integer seriesId;

	/**
 	 * 列表名称 查询对象
 	 */
	private String seriesName;

	private String seriesNameFuzzy;

	/**
 	 * 描述 查询对象
 	 */
	private String seriesDescription;

	private String seriesDescriptionFuzzy;

	/**
 	 * 用户ID 查询对象
 	 */
	private String userId;

	private String userIdFuzzy;

	/**
 	 * 排序 查询对象
 	 */
	private Integer sort;

	/**
 	 * 更新时间 查询对象
 	 */
	private Date updateTime;

	private String updateTimeStart;
	private String updateTimeEnd;

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

	public void setSeriesNameFuzzy(String seriesNameFuzzy) {
		this.seriesNameFuzzy = seriesNameFuzzy;
	}

	public String getSeriesNameFuzzy() {
		return seriesNameFuzzy;
	}

	public void setSeriesDescriptionFuzzy(String seriesDescriptionFuzzy) {
		this.seriesDescriptionFuzzy = seriesDescriptionFuzzy;
	}

	public String getSeriesDescriptionFuzzy() {
		return seriesDescriptionFuzzy;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return userIdFuzzy;
	}

	public void setUpdateTimeStart(String updateTimeStart) {
		this.updateTimeStart = updateTimeStart;
	}

	public String getUpdateTimeStart() {
		return updateTimeStart;
	}

	public void setUpdateTimeEnd(String updateTimeEnd) {
		this.updateTimeEnd = updateTimeEnd;
	}

	public String getUpdateTimeEnd() {
		return updateTimeEnd;
	}
}