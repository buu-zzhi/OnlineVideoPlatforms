package com.easylive.entity.query;

import java.util.Date;
import java.util.List;


/**
 * @Description: 用户信息
 * @Author: false
 * @Date: 2026/02/09 20:14:09
 */
public class UserInfoQuery extends BaseQuery {
	/**
 	 * 用户id 查询对象
 	 */
	private String userId;

	private String userIdFuzzy;

	/**
 	 *  查询对象
 	 */
	private String nickName;

	private String nickNameFuzzy;

	/**
 	 *  查询对象
 	 */
	private String email;

	private String emailFuzzy;

	/**
 	 *  查询对象
 	 */
	private String password;

	private String passwordFuzzy;

	/**
 	 * 0:女 1:男 2:未知 查询对象
 	 */
	private Integer sex;

	/**
 	 * 出生日期 查询对象
 	 */
	private String birthday;

	private String birthdayFuzzy;

	/**
 	 * 学校 查询对象
 	 */
	private String school;

	private String schoolFuzzy;

	/**
 	 * 个人介绍 查询对象
 	 */
	private String personIntroduction;

	private String personIntroductionFuzzy;

	/**
 	 *  查询对象
 	 */
	private Date joinTime;

	private String joinTimeStart;
	private String joinTimeEnd;
	/**
 	 * 最后登录时间 查询对象
 	 */
	private Date lastLoginTime;

	private String lastLoginTimeStart;
	private String lastLoginTimeEnd;
	/**
 	 * 最后登录ip 查询对象
 	 */
	private String lastLoginIp;

	private String lastLoginIpFuzzy;

	/**
 	 *  查询对象
 	 */
	private Integer status;

	/**
 	 * 空间公告 查询对象
 	 */
	private String noticeInfo;

	private String noticeInfoFuzzy;

	/**
 	 *  查询对象
 	 */
	private Integer totalCoinCount;

	/**
 	 *  查询对象
 	 */
	private Integer currentCoinCount;

	/**
 	 *  查询对象
 	 */
	private Integer theme;

	/**
 	 * 头像 查询对象
 	 */
	private String avatar;

	private String avatarFuzzy;

    private List<String> userIdList;

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getSex() {
		return sex;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getSchool() {
		return school;
	}

	public void setPersonIntroduction(String personIntroduction) {
		this.personIntroduction = personIntroduction;
	}

	public String getPersonIntroduction() {
		return personIntroduction;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}

	public Date getJoinTime() {
		return joinTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setNoticeInfo(String noticeInfo) {
		this.noticeInfo = noticeInfo;
	}

	public String getNoticeInfo() {
		return noticeInfo;
	}

	public void setTotalCoinCount(Integer totalCoinCount) {
		this.totalCoinCount = totalCoinCount;
	}

	public Integer getTotalCoinCount() {
		return totalCoinCount;
	}

	public void setCurrentCoinCount(Integer currentCoinCount) {
		this.currentCoinCount = currentCoinCount;
	}

	public Integer getCurrentCoinCount() {
		return currentCoinCount;
	}

	public void setTheme(Integer theme) {
		this.theme = theme;
	}

	public Integer getTheme() {
		return theme;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return userIdFuzzy;
	}

	public void setNickNameFuzzy(String nickNameFuzzy) {
		this.nickNameFuzzy = nickNameFuzzy;
	}

	public String getNickNameFuzzy() {
		return nickNameFuzzy;
	}

	public void setEmailFuzzy(String emailFuzzy) {
		this.emailFuzzy = emailFuzzy;
	}

	public String getEmailFuzzy() {
		return emailFuzzy;
	}

	public void setPasswordFuzzy(String passwordFuzzy) {
		this.passwordFuzzy = passwordFuzzy;
	}

	public String getPasswordFuzzy() {
		return passwordFuzzy;
	}

	public void setBirthdayFuzzy(String birthdayFuzzy) {
		this.birthdayFuzzy = birthdayFuzzy;
	}

	public String getBirthdayFuzzy() {
		return birthdayFuzzy;
	}

	public void setSchoolFuzzy(String schoolFuzzy) {
		this.schoolFuzzy = schoolFuzzy;
	}

	public String getSchoolFuzzy() {
		return schoolFuzzy;
	}

	public void setPersonIntroductionFuzzy(String personIntroductionFuzzy) {
		this.personIntroductionFuzzy = personIntroductionFuzzy;
	}

	public String getPersonIntroductionFuzzy() {
		return personIntroductionFuzzy;
	}

	public void setJoinTimeStart(String joinTimeStart) {
		this.joinTimeStart = joinTimeStart;
	}

	public String getJoinTimeStart() {
		return joinTimeStart;
	}

	public void setJoinTimeEnd(String joinTimeEnd) {
		this.joinTimeEnd = joinTimeEnd;
	}

	public String getJoinTimeEnd() {
		return joinTimeEnd;
	}

	public void setLastLoginTimeStart(String lastLoginTimeStart) {
		this.lastLoginTimeStart = lastLoginTimeStart;
	}

	public String getLastLoginTimeStart() {
		return lastLoginTimeStart;
	}

	public void setLastLoginTimeEnd(String lastLoginTimeEnd) {
		this.lastLoginTimeEnd = lastLoginTimeEnd;
	}

	public String getLastLoginTimeEnd() {
		return lastLoginTimeEnd;
	}

	public void setLastLoginIpFuzzy(String lastLoginIpFuzzy) {
		this.lastLoginIpFuzzy = lastLoginIpFuzzy;
	}

	public String getLastLoginIpFuzzy() {
		return lastLoginIpFuzzy;
	}

	public void setNoticeInfoFuzzy(String noticeInfoFuzzy) {
		this.noticeInfoFuzzy = noticeInfoFuzzy;
	}

	public String getNoticeInfoFuzzy() {
		return noticeInfoFuzzy;
	}

	public void setAvatarFuzzy(String avatarFuzzy) {
		this.avatarFuzzy = avatarFuzzy;
	}

	public String getAvatarFuzzy() {
		return avatarFuzzy;
	}
}