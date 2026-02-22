package com.easylive.entity.po;

import java.io.Serializable;

import com.easylive.entity.dto.UserMessageExtendDto;
import com.easylive.entity.enums.DateTimePatternEnum;
import com.easylive.entity.query.UserMessageQuery;
import com.easylive.utils.DateUtils;
import java.util.Date;

import com.easylive.utils.JsonUtils;
import com.easylive.utils.StringTools;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 用户消息表
 * @Author: false
 * @Date: 2026/02/22 15:37:18
 */
public class UserMessage implements Serializable {
	/**
 	 * 消息ID
 	 */
	private Integer messageId;

	/**
 	 * 用户ID
 	 */
	private String userId;

	/**
 	 * 视频ID
 	 */
	private String videoId;

	/**
 	 * 消息类型
 	 */
	private Integer messageType;

	/**
 	 * 发送人ID
 	 */
	private String sendUserId;

	/**
 	 * 0:未读 1:已读
 	 */
	private Integer readType;

	/**
 	 * 创建时间
 	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
 	 * 扩展信息
 	 */
	private String extendJson;

    private String sendUserAvatar;
    private String sendUserName;
    private String videoName;
    private String videoCover;

    private UserMessageExtendDto  userMessageExtendDto;

    public UserMessageExtendDto getUserMessageExtendDto() {
        return StringTools.isEmpty(extendJson)?new UserMessageExtendDto(): JsonUtils.convertJson2Obj(extendJson, UserMessageExtendDto.class);
    }

    public String getSendUserAvatar() {
        return sendUserAvatar;
    }

    public void setSendUserAvatar(String sendUserAvatar) {
        this.sendUserAvatar = sendUserAvatar;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

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
	@Override
	public String toString() {
		return "消息ID:" + (messageId == null ? "空" : messageId) + "," + 
				"用户ID:" + (userId == null ? "空" : userId) + "," + 
				"视频ID:" + (videoId == null ? "空" : videoId) + "," + 
				"消息类型:" + (messageType == null ? "空" : messageType) + "," + 
				"发送人ID:" + (sendUserId == null ? "空" : sendUserId) + "," + 
				"0:未读 1:已读:" + (readType == null ? "空" : readType) + "," + 
				"创建时间:" + (createTime == null ? "空" : DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "," + 
				"扩展信息:" + (extendJson == null ? "空" : extendJson);
		}
}