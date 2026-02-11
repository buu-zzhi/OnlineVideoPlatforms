package com.easylive.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @Description: 视频弹幕 Mapper
 * @Author: false
 * @Date: 2026/02/10 21:56:04
 */
public interface VideoDanmuMapper<T, P> extends BaseMapper {

	/**
 	 * 根据 DanmuId 查询
 	 */
	T selectByDanmuId(@Param("danmuId")Integer danmuId);

	/**
 	 * 根据 DanmuId 更新
 	 */
	Integer updateByDanmuId(@Param("bean") T t, @Param("danmuId")Integer danmuId); 

	/**
 	 * 根据 DanmuId 删除
 	 */
	Integer deleteByDanmuId(@Param("danmuId")Integer danmuId);

}