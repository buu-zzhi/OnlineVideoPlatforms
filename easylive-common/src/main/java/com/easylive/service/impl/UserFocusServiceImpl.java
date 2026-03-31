package com.easylive.service.impl;


import java.util.Date;
import java.util.List;

import com.easylive.entity.enums.ResponseCodeEnum;
import com.easylive.entity.po.UserInfo;
import com.easylive.entity.query.SimplePage;
import com.easylive.entity.enums.PageSize;
import com.easylive.entity.query.UserInfoQuery;
import com.easylive.exception.BusinessException;
import com.easylive.mapper.UserFocusMapper;
import com.easylive.mapper.UserInfoMapper;
import com.easylive.service.UserFocusService;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.UserFocus;
import com.easylive.entity.query.UserFocusQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
/**
 * @Description:  业务接口实现
 * @Author: false
 * @Date: 2026/02/12 20:52:11
 */
@Service("UserFocusMapper")
public class UserFocusServiceImpl implements UserFocusService{

	@Resource
	private UserFocusMapper<UserFocus, UserFocusQuery> userFocusMapper;
    @Autowired
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    /**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<UserFocus> findListByParam(UserFocusQuery query) {
		return this.userFocusMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(UserFocusQuery query) {
		return this.userFocusMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<UserFocus> findListByPage(UserFocusQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserFocus> list = this.findListByParam(query);
		PaginationResultVO<UserFocus> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(UserFocus bean) {
		return this.userFocusMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<UserFocus> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.userFocusMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<UserFocus> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.userFocusMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 UserIdAndFocusUserId 查询
 	 */
	@Override
	public UserFocus getUserFocusByUserIdAndFocusUserId(String userId, String focusUserId) {
		return this.userFocusMapper.selectByUserIdAndFocusUserId(userId, focusUserId);}

	/**
 	 * 根据 UserIdAndFocusUserId 更新
 	 */
	@Override
	public Integer updateUserFocusByUserIdAndFocusUserId(UserFocus bean, String userId, String focusUserId) {
		return this.userFocusMapper.updateByUserIdAndFocusUserId(bean, userId, focusUserId);}

	/**
 	 * 根据 UserIdAndFocusUserId 删除
 	 */
	@Override
	public Integer deleteUserFocusByUserIdAndFocusUserId(String userId, String focusUserId) {
		return this.userFocusMapper.deleteByUserIdAndFocusUserId(userId, focusUserId);}

    @Override
    public void focusUser(String userId, String focusUserId) {
        if (userId.equals(focusUserId)) {
            throw new BusinessException("不能关注自己");
        }
        UserFocus dbInfo = userFocusMapper.selectByUserIdAndFocusUserId(userId, focusUserId);
        if (dbInfo != null) {
            return;
        }

        UserInfo userInfo = userInfoMapper.selectByUserId(focusUserId);
        if (userInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        UserFocus userFocus = new UserFocus();
        userFocus.setUserId(userId);
        userFocus.setFocusUserId(focusUserId);
        userFocus.setFocusTime(new Date());
        userFocusMapper.insert(userFocus);
    }

    @Override
    public void cancelFocusUser(String userId, String focusUserId) {
        userFocusMapper.deleteByUserIdAndFocusUserId(userId, focusUserId);
    }
}