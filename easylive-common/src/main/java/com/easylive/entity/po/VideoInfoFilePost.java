package com.easylive.entity.po;

import java.io.Serializable;


/**
 * @Description: 视频文件信息
 * @Author: false
 * @Date: 2026/02/05 21:42:17
 */
public class VideoInfoFilePost implements Serializable {
	/**
 	 * 唯一ID
 	 */
	private String fileId;

	/**
 	 * 上传ID
 	 */
	private String uploadId;

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

	/**
 	 * 文件名
 	 */
	private String fileName;

	/**
 	 * 文件大小
 	 */
	private Long fileSize;

	/**
 	 * 文件路径
 	 */
	private String filePath;

	/**
 	 * 0:无更新 1:有更新
 	 */
	private Integer updateType;

	/**
 	 * 0:转码中 1:转码成功 2:转码失败
 	 */
	private Integer transferResult;

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

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getUploadId() {
		return uploadId;
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

	public void setFileIndex(Integer fileIndex) {
		this.fileIndex = fileIndex;
	}

	public Integer getFileIndex() {
		return fileIndex;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
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

	public void setUpdateType(Integer updateType) {
		this.updateType = updateType;
	}

	public Integer getUpdateType() {
		return updateType;
	}

	public void setTransferResult(Integer transferResult) {
		this.transferResult = transferResult;
	}

	public Integer getTransferResult() {
		return transferResult;
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
				"上传ID:" + (uploadId == null ? "空" : uploadId) + "," + 
				"用户ID:" + (userId == null ? "空" : userId) + "," + 
				"视频ID:" + (videoId == null ? "空" : videoId) + "," + 
				"文件索引:" + (fileIndex == null ? "空" : fileIndex) + "," + 
				"文件名:" + (fileName == null ? "空" : fileName) + "," + 
				"文件大小:" + (fileSize == null ? "空" : fileSize) + "," + 
				"文件路径:" + (filePath == null ? "空" : filePath) + "," + 
				"0:无更新 1:有更新:" + (updateType == null ? "空" : updateType) + "," + 
				"0:转码中 1:转码成功 2:转码失败:" + (transferResult == null ? "空" : transferResult) + "," + 
				"持续时间（秒）:" + (duration == null ? "空" : duration);
		}
}