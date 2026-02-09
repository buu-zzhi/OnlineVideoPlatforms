package com.easylive.entity.po;

import java.io.Serializable;


import com.easylive.entity.enums.DateTimePatternEnum;
import com.easylive.entity.enums.VideoStatusEnum;
import com.easylive.utils.DateUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * @Description: 视频信息
 * @Author: false
 * @Date: 2026/02/05 21:42:17
 */
public class VideoInfoPost extends VideoInfo implements Serializable {
	/**
 	 * 视频ID
 	 */
	private String videoId;

	/**
 	 * 视频封面
 	 */
	private String videoCover;

	/**
 	 * 视频名称
 	 */
	private String videoName;

	/**
 	 * 用户ID
 	 */
	private String userId;

	/**
 	 * 创建时间
 	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
 	 * 最后更新时间
 	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastUpdateTime;

	/**
 	 * 父级分类ID
 	 */
	private Integer pCategoryId;

	/**
 	 * 分类ID
 	 */
	private Integer categoryId;

	/**
 	 * 0:转码中 1:转码失败 2:待审核 3:审核成功 4:审核失败
 	 */
	private Integer status;

	/**
 	 * 0:自制作 1:转载
 	 */
	private Integer postType;

	/**
 	 * 原资源说明
 	 */
	private String originInfo;

	/**
 	 * 标签
 	 */
	private String tags;

	/**
 	 * 简介
 	 */
	private String introduction;

	/**
 	 * 互动设置
 	 */
	private String interaction;

	/**
 	 * 持续时间（秒）
 	 */
	private Integer duration;

    private String statusName;

    public String getStatusName() {
        VideoStatusEnum videoStatusEnum = VideoStatusEnum.getByStatus(status);
        return videoStatusEnum == null ? "" : videoStatusEnum.getDesc();
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoCover(String videoCover) {
		this.videoCover = videoCover;
	}

	public String getVideoCover() {
		return videoCover;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setPCategoryId(Integer pCategoryId) {
		this.pCategoryId = pCategoryId;
	}

	public Integer getPCategoryId() {
		return pCategoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setPostType(Integer postType) {
		this.postType = postType;
	}

	public Integer getPostType() {
		return postType;
	}

	public void setOriginInfo(String originInfo) {
		this.originInfo = originInfo;
	}

	public String getOriginInfo() {
		return originInfo;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getTags() {
		return tags;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setInteraction(String interaction) {
		this.interaction = interaction;
	}

	public String getInteraction() {
		return interaction;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getDuration() {
		return duration;
	}
	@Override
	public String toString() {
		return "视频ID:" + (videoId == null ? "空" : videoId) + "," + 
				"视频封面:" + (videoCover == null ? "空" : videoCover) + "," + 
				"视频名称:" + (videoName == null ? "空" : videoName) + "," + 
				"用户ID:" + (userId == null ? "空" : userId) + "," + 
				"创建时间:" + (createTime == null ? "空" : DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "," +
				"最后更新时间:" + (lastUpdateTime == null ? "空" : DateUtils.format(lastUpdateTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "," + 
				"父级分类ID:" + (pCategoryId == null ? "空" : pCategoryId) + "," + 
				"分类ID:" + (categoryId == null ? "空" : categoryId) + "," + 
				"0:转码中 1:转码失败 2:待审核 3:审核成功 4:审核失败:" + (status == null ? "空" : status) + "," + 
				"0:自制作 1:转载:" + (postType == null ? "空" : postType) + "," + 
				"原资源说明:" + (originInfo == null ? "空" : originInfo) + "," + 
				"标签:" + (tags == null ? "空" : tags) + "," + 
				"简介:" + (introduction == null ? "空" : introduction) + "," + 
				"互动设置:" + (interaction == null ? "空" : interaction) + "," + 
				"持续时间（秒）:" + (duration == null ? "空" : duration);
		}
}