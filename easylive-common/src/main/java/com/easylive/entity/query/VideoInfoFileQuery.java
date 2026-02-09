package com.easylive.entity.query;



/**
 * @Description: 视频文件信息
 * @Author: false
 * @Date: 2026/02/05 21:42:17
 */
public class VideoInfoFileQuery extends BaseQuery {
	/**
 	 * 唯一ID 查询对象
 	 */
	private String fileId;

	private String fileIdFuzzy;

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
 	 * 文件名 查询对象
 	 */
	private String fileName;

	private String fileNameFuzzy;

	/**
 	 * 文件索引 查询对象
 	 */
	private Integer fileIndex;

	/**
 	 * 文件大小 查询对象
 	 */
	private Long fileSize;

	/**
 	 * 文件路径 查询对象
 	 */
	private String filePath;

	private String filePathFuzzy;

	/**
 	 * 持续时间（秒） 查询对象
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

	public void setFileIdFuzzy(String fileIdFuzzy) {
		this.fileIdFuzzy = fileIdFuzzy;
	}

	public String getFileIdFuzzy() {
		return fileIdFuzzy;
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

	public void setFileNameFuzzy(String fileNameFuzzy) {
		this.fileNameFuzzy = fileNameFuzzy;
	}

	public String getFileNameFuzzy() {
		return fileNameFuzzy;
	}

	public void setFilePathFuzzy(String filePathFuzzy) {
		this.filePathFuzzy = filePathFuzzy;
	}

	public String getFilePathFuzzy() {
		return filePathFuzzy;
	}
}