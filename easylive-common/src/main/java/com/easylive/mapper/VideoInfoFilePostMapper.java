package com.easylive.mapper;

import com.easylive.entity.query.VideoInfoFilePostQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 视频文件信息 Mapper
 * @Author: false
 * @Date: 2026/02/05 21:42:17
 */
public interface VideoInfoFilePostMapper<T, P> extends BaseMapper {

	/**
	 * 根据条件更新，返回成功修改的数据条数
	 */
	Integer updateByParam(@Param("bean") T t, @Param("query") P p);

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

	/**
 	 * 根据 UploadIdAndUserId 查询
 	 */
	T selectByUploadIdAndUserId(@Param("uploadId")String uploadId, @Param("userId")String userId);

	/**
 	 * 根据 UploadIdAndUserId 更新
 	 */
	Integer updateByUploadIdAndUserId(@Param("bean") T t, @Param("uploadId")String uploadId, @Param("userId")String userId); 

	/**
 	 * 根据 UploadIdAndUserId 删除
 	 */
	Integer deleteByUploadIdAndUserId(@Param("uploadId")String uploadId, @Param("userId")String userId);

    void deleteBatchByFileId(@Param("fileIdList") List<String> fileIdList, @Param("userId") String userId);

    Integer sumDuration(@Param("videoId") String videoId);

    void deleteByParam(@Param("query") P p);
}