package com.easylive.entity.query;

import java.util.Date;


/**
 * @Description: 用户行为 点赞、评论
 * @Author: false
 * @Date: 2026/02/10 21:56:04
 */
public class UserActionQuery extends BaseQuery {
	/**
 	 * 自增ID 查询对象
 	 */
	private Integer actionId;

	/**
 	 * 视频ID 查询对象
 	 */
	private String videoId;

	private String videoIdFuzzy;

	/**
 	 * 视频用户ID 查询对象
 	 */
	private String videoUserId;

	private String videoUserIdFuzzy;

	/**
 	 * 评论ID 查询对象
 	 */
	private Integer commentId;

	/**
 	 * 0:评论喜欢点赞 1:讨厌评论 2:视频点赞 3:视频收藏 4:视频投币 查询对象
 	 */
	private Integer actionType;

	/**
 	 * 数量 查询对象
 	 */
	private Integer actionCount;

	/**
 	 * 用户id 查询对象
 	 */
	private String userId;

	private String userIdFuzzy;

	/**
 	 * 操作时间 查询对象
 	 */
	private Date actionTime;

	private String actionTimeStart;
	private String actionTimeEnd;

    private Integer[] actionTypeArray;

    private Boolean queryVideoInfo;

    public Boolean getQueryVideoInfo() {
        return queryVideoInfo;
    }

    public void setQueryVideoInfo(Boolean queryVideoInfo) {
        this.queryVideoInfo = queryVideoInfo;
    }

    public Integer[] getActionTypeArray() {
        return actionTypeArray;
    }

    public void setActionTypeArray(Integer[] actionTypeArray) {
        this.actionTypeArray = actionTypeArray;
    }

    public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	public Integer getActionId() {
		return actionId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoUserId(String videoUserId) {
		this.videoUserId = videoUserId;
	}

	public String getVideoUserId() {
		return videoUserId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}

	public Integer getActionType() {
		return actionType;
	}

	public void setActionCount(Integer actionCount) {
		this.actionCount = actionCount;
	}

	public Integer getActionCount() {
		return actionCount;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}

	public Date getActionTime() {
		return actionTime;
	}

	public void setVideoIdFuzzy(String videoIdFuzzy) {
		this.videoIdFuzzy = videoIdFuzzy;
	}

	public String getVideoIdFuzzy() {
		return videoIdFuzzy;
	}

	public void setVideoUserIdFuzzy(String videoUserIdFuzzy) {
		this.videoUserIdFuzzy = videoUserIdFuzzy;
	}

	public String getVideoUserIdFuzzy() {
		return videoUserIdFuzzy;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return userIdFuzzy;
	}

	public void setActionTimeStart(String actionTimeStart) {
		this.actionTimeStart = actionTimeStart;
	}

	public String getActionTimeStart() {
		return actionTimeStart;
	}

	public void setActionTimeEnd(String actionTimeEnd) {
		this.actionTimeEnd = actionTimeEnd;
	}

	public String getActionTimeEnd() {
		return actionTimeEnd;
	}
}