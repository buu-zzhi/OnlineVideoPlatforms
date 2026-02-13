package com.easylive.service;


import java.util.List;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.UserFocus;
import com.easylive.entity.query.UserFocusQuery;
/**
 * @Description:  Service
 * @Author: false
 * @Date: 2026/02/12 20:52:11
 */
public interface UserFocusService{

	/**
 	 * 根据条件查询列表
 	 */
	List<UserFocus> findListByParam(UserFocusQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(UserFocusQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<UserFocus> findListByPage(UserFocusQuery query);

	/**
 	 * 新增
 	 */
	Integer add(UserFocus bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<UserFocus> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<UserFocus> listBean);

	/**
 	 * 根据 UserIdAndFocusUserId 查询
 	 */
	UserFocus getUserFocusByUserIdAndFocusUserId(String userId, String focusUserId);

	/**
 	 * 根据 UserIdAndFocusUserId 更新
 	 */
	Integer updateUserFocusByUserIdAndFocusUserId(UserFocus bean, String userId, String focusUserId); 

	/**
 	 * 根据 UserIdAndFocusUserId 删除
 	 */
	Integer deleteUserFocusByUserIdAndFocusUserId(String userId, String focusUserId);

    /**
     *
     * @param userId 当前登录的用户id
     * @param focusUserId  当前用户关注的用户的id
     */
    void focusUser(String userId, String focusUserId);

    void cancelFocusUser(String userId, String focusUserId);
}