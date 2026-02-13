package com.easylive.entity.po;

import java.io.Serializable;

import com.easylive.entity.enums.DateTimePatternEnum;
import com.easylive.utils.DateUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 
 * @Author: false
 * @Date: 2026/02/12 20:52:11
 */
public class UserFocus implements Serializable {
	/**
 	 * 
 	 */
	private String userId;

	/**
 	 * 
 	 */
	private String focusUserId;

	/**
 	 * 
 	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date focusTime;

    private String otherNickName;
    private String otherUserId;
    private String otherPersonIntroduction;
    private String otherAvatar;
    private Integer focusType;

    public String getOtherNickName() {
        return otherNickName;
    }

    public void setOtherNickName(String otherNickName) {
        this.otherNickName = otherNickName;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getOtherPersonIntroduction() {
        return otherPersonIntroduction;
    }

    public void setOtherPersonIntroduction(String otherPersonIntroduction) {
        this.otherPersonIntroduction = otherPersonIntroduction;
    }

    public String getOtherAvatar() {
        return otherAvatar;
    }

    public void setOtherAvatar(String otherAvatar) {
        this.otherAvatar = otherAvatar;
    }

    public Integer getFocusType() {
        return focusType;
    }

    public void setFocusType(Integer focusType) {
        this.focusType = focusType;
    }

    public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setFocusUserId(String focusUserId) {
		this.focusUserId = focusUserId;
	}

	public String getFocusUserId() {
		return focusUserId;
	}

	public void setFocusTime(Date focusTime) {
		this.focusTime = focusTime;
	}

	public Date getFocusTime() {
		return focusTime;
	}
	@Override
	public String toString() {
		return ":" + (userId == null ? "空" : userId) + "," + 
				":" + (focusUserId == null ? "空" : focusUserId) + "," + 
				":" + (focusTime == null ? "空" : DateUtils.format(focusTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
		}
}