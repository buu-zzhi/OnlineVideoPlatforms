package com.easylive.entity.query;

import java.util.Date;


/**
 * @Description: 用户消息表
 * @Author: false
 * @Date: 2026/02/22 15:37:18
 */
public class UserMessageQuery extends BaseQuery {
	/**
 	 * 消息ID 查询对象
 	 */
	private Integer messageId;

	/**
 	 * 用户ID 查询对象
 	 */
	private String userId;

	private String userIdFuzzy;

	/**
 	 * 视频ID 查询对象
 	 */
	private String videoId;

	private String videoIdFuzzy;

	/**
 	 * 消息类型 查询对象
 	 */
	private Integer messageType;

	/**
 	 * 发送人ID 查询对象
 	 */
	private String sendUserId;

	private String sendUserIdFuzzy;

	/**
 	 * 0:未读 1:已读 查询对象
 	 */
	private Integer readType;

	/**
 	 * 创建时间 查询对象
 	 */
	private Date createTime;

	private String createTimeStart;
	private String createTimeEnd;
	/**
 	 * 扩展信息 查询对象
 	 */
	private String extendJson;

	private String extendJsonFuzzy;


	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	public Integer getMessageType() {
		return messageType;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setReadType(Integer readType) {
		this.readType = readType;
	}

	public Integer getReadType() {
		return readType;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setExtendJson(String extendJson) {
		this.extendJson = extendJson;
	}

	public String getExtendJson() {
		return extendJson;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return userIdFuzzy;
	}

	public void setVideoIdFuzzy(String videoIdFuzzy) {
		this.videoIdFuzzy = videoIdFuzzy;
	}

	public String getVideoIdFuzzy() {
		return videoIdFuzzy;
	}

	public void setSendUserIdFuzzy(String sendUserIdFuzzy) {
		this.sendUserIdFuzzy = sendUserIdFuzzy;
	}

	public String getSendUserIdFuzzy() {
		return sendUserIdFuzzy;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setExtendJsonFuzzy(String extendJsonFuzzy) {
		this.extendJsonFuzzy = extendJsonFuzzy;
	}

	public String getExtendJsonFuzzy() {
		return extendJsonFuzzy;
	}
}