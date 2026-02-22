package com.easylive.entity.query;



/**
 * @Description: 数据统计
 * @Author: false
 * @Date: 2026/02/22 15:37:18
 */
public class StatisticsInfoQuery extends BaseQuery {
	/**
 	 * 统计日期 查询对象
 	 */
	private String statisticsDate;

	private String statisticsDateFuzzy;

	/**
 	 * 用户ID 查询对象
 	 */
	private String userId;

	private String userIdFuzzy;

	/**
 	 * 数据统计类型 查询对象
 	 */
	private Integer dataType;

	/**
 	 * 统计数量 查询对象
 	 */
	private Integer statisticsCount;


	public void setStatisticsDate(String statisticsDate) {
		this.statisticsDate = statisticsDate;
	}

	public String getStatisticsDate() {
		return statisticsDate;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setStatisticsCount(Integer statisticsCount) {
		this.statisticsCount = statisticsCount;
	}

	public Integer getStatisticsCount() {
		return statisticsCount;
	}

	public void setStatisticsDateFuzzy(String statisticsDateFuzzy) {
		this.statisticsDateFuzzy = statisticsDateFuzzy;
	}

	public String getStatisticsDateFuzzy() {
		return statisticsDateFuzzy;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return userIdFuzzy;
	}
}