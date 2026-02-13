package com.easylive.entity.po;

import java.io.Serializable;

import com.easylive.entity.enums.DateTimePatternEnum;
import com.easylive.utils.DateUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 用户行为 点赞、评论
 * @Author: false
 * @Date: 2026/02/10 21:56:04
 */
public class UserAction implements Serializable {
	/**
 	 * 自增ID
 	 */
	private Integer actionId;

	/**
 	 * 视频ID
 	 */
	private String videoId;

	/**
 	 * 视频用户ID
 	 */
	private String videoUserId;

	/**
 	 * 评论ID
 	 */
	private Integer commentId;

	/**
 	 * 0:评论喜欢点赞 1:讨厌评论 2:视频点赞 3:视频收藏 4:视频投币
 	 */
	private Integer actionType;

	/**
 	 * 数量
 	 */
	private Integer actionCount;

	/**
 	 * 用户id
 	 */
	private String userId;

    /**
     * 视频的信息
     */
    private String videoCover;
    private String videoName;

	/**
 	 * 操作时间
 	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date actionTime;

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
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
	@Override
	public String toString() {
		return "自增ID:" + (actionId == null ? "空" : actionId) + "," + 
				"视频ID:" + (videoId == null ? "空" : videoId) + "," + 
				"视频用户ID:" + (videoUserId == null ? "空" : videoUserId) + "," + 
				"评论ID:" + (commentId == null ? "空" : commentId) + "," + 
				"0:评论喜欢点赞 1:讨厌评论 2:视频点赞 3:视频收藏 4:视频投币:" + (actionType == null ? "空" : actionType) + "," + 
				"数量:" + (actionCount == null ? "空" : actionCount) + "," + 
				"用户id:" + (userId == null ? "空" : userId) + "," + 
				"操作时间:" + (actionTime == null ? "空" : DateUtils.format(actionTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
		}


}