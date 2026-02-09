package com.easylive.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @Description: 视频文件信息 Mapper
 * @Author: false
 * @Date: 2026/02/05 21:42:17
 */
public interface VideoInfoFileMapper<T, P> extends BaseMapper {

	/**
 	 * 根据 FileId 查询
 	 */
	T selectByFileId(@Param("fileId")String fileId);

	/**
 	 * 根据 FileId 更新
 	 */
	Integer updateByFileId(@Param("bean") T t, @Param("fileId")String fileId); 

	/**
 	 * 根据 FileId 删除
 	 */
	Integer deleteByFileId(@Param("fileId")String fileId);

    void deleteByParam(@Param("query") P p);
}