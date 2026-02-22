package com.easylive.entity.po;

import java.io.Serializable;

import com.easylive.entity.enums.DateTimePatternEnum;
import com.easylive.utils.DateUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 视频播放历史
 * @Author: false
 * @Date: 2026/02/22 15:37:18
 */
public class VideoPlayHistory implements Serializable {
	/**
 	 * 用户ID
 	 */
	private String userId;

	/**
 	 * 视频ID
 	 */
	private String videoId;

	/**
 	 * 文件索引
 	 */
	private Integer fileIndex;

    private String videoName;

    private String videoCover;

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

    /**
 	 * 最后更新时间
 	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastUpdateTime;


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

	public void setFileIndex(Integer fileIndex) {
		this.fileIndex = fileIndex;
	}

	public Integer getFileIndex() {
		return fileIndex;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	@Override
	public String toString() {
		return "用户ID:" + (userId == null ? "空" : userId) + "," + 
				"视频ID:" + (videoId == null ? "空" : videoId) + "," + 
				"文件索引:" + (fileIndex == null ? "空" : fileIndex) + "," + 
				"最后更新时间:" + (lastUpdateTime == null ? "空" : DateUtils.format(lastUpdateTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
		}
}