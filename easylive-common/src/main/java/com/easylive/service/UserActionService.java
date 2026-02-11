package com.easylive.service;


import java.util.List;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.UserAction;
import com.easylive.entity.query.UserActionQuery;
/**
 * @Description: 用户行为 点赞、评论 Service
 * @Author: false
 * @Date: 2026/02/10 21:56:04
 */
public interface UserActionService{

	/**
 	 * 根据条件查询列表
 	 */
	List<UserAction> findListByParam(UserActionQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(UserActionQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<UserAction> findListByPage(UserActionQuery query);

	/**
 	 * 新增
 	 */
	Integer add(UserAction bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<UserAction> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<UserAction> listBean);

	/**
 	 * 根据 ActionId 查询
 	 */
	UserAction getUserActionByActionId(Integer actionId);

	/**
 	 * 根据 ActionId 更新
 	 */
	Integer updateUserActionByActionId(UserAction bean, Integer actionId); 

	/**
 	 * 根据 ActionId 删除
 	 */
	Integer deleteUserActionByActionId(Integer actionId);

	/**
 	 * 根据 VideoIdAndCommentIdAndActionTypeAndUserId 查询
 	 */
	UserAction getUserActionByVideoIdAndCommentIdAndActionTypeAndUserId(String videoId, Integer commentId, Integer actionType, String userId);

	/**
 	 * 根据 VideoIdAndCommentIdAndActionTypeAndUserId 更新
 	 */
	Integer updateUserActionByVideoIdAndCommentIdAndActionTypeAndUserId(UserAction bean, String videoId, Integer commentId, Integer actionType, String userId); 

	/**
 	 * 根据 VideoIdAndCommentIdAndActionTypeAndUserId 删除
 	 */
	Integer deleteUserActionByVideoIdAndCommentIdAndActionTypeAndUserId(String videoId, Integer commentId, Integer actionType, String userId);

    void saveAction(UserAction userAction);
}