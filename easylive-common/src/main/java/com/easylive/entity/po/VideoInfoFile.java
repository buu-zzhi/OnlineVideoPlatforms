package com.easylive.entity.po;

import java.io.Serializable;


/**
 * @Description: 视频文件信息
 * @Author: false
 * @Date: 2026/02/05 21:42:17
 */
public class VideoInfoFile implements Serializable {
	/**
 	 * 唯一ID
 	 */
	private String fileId;

	/**
 	 * 用户ID
 	 */
	private String userId;

	/**
 	 * 视频ID
 	 */
	private String videoId;

	/**
 	 * 文件名
 	 */
	private String fileName;

	/**
 	 * 文件索引
 	 */
	private Integer fileIndex;

	/**
 	 * 文件大小
 	 */
	private Long fileSize;

	/**
 	 * 文件路径
 	 */
	private String filePath;

	/**
 	 * 持续时间（秒）
 	 */
	private Integer duration;


	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileId() {
		return fileId;
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

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileIndex(Integer fileIndex) {
		this.fileIndex = fileIndex;
	}

	public Integer getFileIndex() {
		return fileIndex;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getDuration() {
		return duration;
	}
	@Override
	public String toString() {
		return "唯一ID:" + (fileId == null ? "空" : fileId) + "," + 
				"用户ID:" + (userId == null ? "空" : userId) + "," + 
				"视频ID:" + (videoId == null ? "空" : videoId) + "," + 
				"文件名:" + (fileName == null ? "空" : fileName) + "," + 
				"文件索引:" + (fileIndex == null ? "空" : fileIndex) + "," + 
				"文件大小:" + (fileSize == null ? "空" : fileSize) + "," + 
				"文件路径:" + (filePath == null ? "空" : filePath) + "," + 
				"持续时间（秒）:" + (duration == null ? "空" : duration);
		}
}