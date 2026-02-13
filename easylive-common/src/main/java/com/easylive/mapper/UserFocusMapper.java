package com.easylive.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @Description:  Mapper
 * @Author: false
 * @Date: 2026/02/12 20:52:11
 */
public interface UserFocusMapper<T, P> extends BaseMapper {

	/**
 	 * 根据 UserIdAndFocusUserId 查询
 	 */
	T selectByUserIdAndFocusUserId(@Param("userId")String userId, @Param("focusUserId")String focusUserId);

	/**
 	 * 根据 UserIdAndFocusUserId 更新
 	 */
	Integer updateByUserIdAndFocusUserId(@Param("bean") T t, @Param("userId")String userId, @Param("focusUserId")String focusUserId); 

	/**
 	 * 根据 UserIdAndFocusUserId 删除
 	 */
	Integer deleteByUserIdAndFocusUserId(@Param("userId")String userId, @Param("focusUserId")String focusUserId);

    Integer selectFansCount(@Param("userId") String userId);

    Integer selectFocusCount(@Param("userId") String userId);

}