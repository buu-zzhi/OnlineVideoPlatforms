package com.easylive.service;


import java.util.List;

import com.easylive.entity.constants.Constants;
import com.easylive.entity.dto.TokenUserInfoDto;
import com.easylive.entity.dto.UserCountInfoDto;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.UserInfo;
import com.easylive.entity.query.UserInfoQuery;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @Description: 用户信息 Service
 * @Author: false
 * @Date: 2026/02/01 19:48:05
 */
public interface UserInfoService{

	/**
 	 * 根据条件查询列表
 	 */
	List<UserInfo> findListByParam(UserInfoQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(UserInfoQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<UserInfo> findListByPage(UserInfoQuery query);

	/**
 	 * 新增
 	 */
	Integer add(UserInfo bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<UserInfo> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<UserInfo> listBean);

	/**
 	 * 根据 UserId 查询
 	 */
	UserInfo getUserInfoByUserId(String userId);

	/**
 	 * 根据 UserId 更新
 	 */
	Integer updateUserInfoByUserId(UserInfo bean, String userId); 

	/**
 	 * 根据 UserId 删除
 	 */
	Integer deleteUserInfoByUserId(String userId);

	/**
 	 * 根据 Email 查询
 	 */
	UserInfo getUserInfoByEmail(String email);

	/**
 	 * 根据 Email 更新
 	 */
	Integer updateUserInfoByEmail(UserInfo bean, String email); 

	/**
 	 * 根据 Email 删除
 	 */
	Integer deleteUserInfoByEmail(String email);

	/**
 	 * 根据 NickName 查询
 	 */
	UserInfo getUserInfoByNickName(String nickName);

	/**
 	 * 根据 NickName 更新
 	 */
	Integer updateUserInfoByNickName(UserInfo bean, String nickName); 

	/**
 	 * 根据 NickName 删除
 	 */
	Integer deleteUserInfoByNickName(String nickName);

    void register(@NotEmpty @Email @Size(max = 150) String email, @NotEmpty @Size(max = 20) String nickName, @NotEmpty @Pattern(regexp = Constants.REGEX_PASSWORD) String registerPassword);

    TokenUserInfoDto login(String email, String password, String ip);

    UserInfo getUserDetailInfo(String currentUserId, String userId);

    void updateUserInfo(UserInfo userInfo, TokenUserInfoDto tokenUserInfoDto);

    UserCountInfoDto getUserCountInfo(String userId);

    void changeUserStatus(String userId, Integer status);
}