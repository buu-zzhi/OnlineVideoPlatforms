package com.easylive.entity.query;

import java.util.Date;


/**
 * @Description: 评论
 * @Author: false
 * @Date: 2026/02/10 21:56:04
 */
public class VideoCommentQuery extends BaseQuery {
	/**
 	 * 评论ID 查询对象
 	 */
	private Integer commentId;

	/**
 	 * 父级评论ID 查询对象
 	 */
	private Integer pCommentId;

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
 	 * 回复内容 查询对象
 	 */
	private String content;

	private String contentFuzzy;

	/**
 	 * 图片 查询对象
 	 */
	private String imgPath;

	private String imgPathFuzzy;

	/**
 	 * 用户ID 查询对象
 	 */
	private String userId;

	private String userIdFuzzy;

	/**
 	 * 回复人ID 查询对象
 	 */
	private String replyUserId;

	private String replyUserIdFuzzy;

	/**
 	 * 8:未置顶 1:置顶 查询对象
 	 */
	private Integer topType;

	/**
 	 * 发布时间 查询对象
 	 */
	private Date postTime;

	private String postTimeStart;
	private String postTimeEnd;
	/**
 	 * 喜欢数量 查询对象
 	 */
	private Integer likeCount;

    private Boolean queryVideoInfo;

    private String videoNameFuzzy;

    public String getVideoNameFuzzy() {
        return videoNameFuzzy;
    }

    public void setVideoNameFuzzy(String videoNameFuzzy) {
        this.videoNameFuzzy = videoNameFuzzy;
    }

    public Boolean getQueryVideoInfo() {
        return queryVideoInfo;
    }

    public void setQueryVideoInfo(Boolean queryVideoInfo) {
        this.queryVideoInfo = queryVideoInfo;
    }

    /**
 	 * 讨厌数量 查询对象
 	 */
	private Integer hateCount;

    private Boolean loadChildren;

    public Boolean getLoadChildren() {
        return loadChildren;
    }

    public void setLoadChildren(Boolean loadChildren) {
        this.loadChildren = loadChildren;
    }

    public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setPCommentId(Integer pCommentId) {
		this.pCommentId = pCommentId;
	}

	public Integer getPCommentId() {
		return pCommentId;
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

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setReplyUserId(String replyUserId) {
		this.replyUserId = replyUserId;
	}

	public String getReplyUserId() {
		return replyUserId;
	}

	public void setTopType(Integer topType) {
		this.topType = topType;
	}

	public Integer getTopType() {
		return topType;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}

	public Integer getLikeCount() {
		return likeCount;
	}

	public void setHateCount(Integer hateCount) {
		this.hateCount = hateCount;
	}

	public Integer getHateCount() {
		return hateCount;
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

	public void setContentFuzzy(String contentFuzzy) {
		this.contentFuzzy = contentFuzzy;
	}

	public String getContentFuzzy() {
		return contentFuzzy;
	}

	public void setImgPathFuzzy(String imgPathFuzzy) {
		this.imgPathFuzzy = imgPathFuzzy;
	}

	public String getImgPathFuzzy() {
		return imgPathFuzzy;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return userIdFuzzy;
	}

	public void setReplyUserIdFuzzy(String replyUserIdFuzzy) {
		this.replyUserIdFuzzy = replyUserIdFuzzy;
	}

	public String getReplyUserIdFuzzy() {
		return replyUserIdFuzzy;
	}

	public void setPostTimeStart(String postTimeStart) {
		this.postTimeStart = postTimeStart;
	}

	public String getPostTimeStart() {
		return postTimeStart;
	}

	public void setPostTimeEnd(String postTimeEnd) {
		this.postTimeEnd = postTimeEnd;
	}

	public String getPostTimeEnd() {
		return postTimeEnd;
	}
}