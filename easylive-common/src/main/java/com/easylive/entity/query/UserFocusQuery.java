package com.easylive.entity.query;

import java.util.Date;


/**
 * @Description: 
 * @Author: false
 * @Date: 2026/02/12 20:52:11
 */
public class UserFocusQuery extends BaseQuery {
	/**
 	 *  查询对象
 	 */
	private String userId;

	private String userIdFuzzy;

	/**
 	 *  查询对象
 	 */
	private String focusUserId;

	private String focusUserIdFuzzy;

	/**
 	 *  查询对象
 	 */
	private Date focusTime;

	private String focusTimeStart;
	private String focusTimeEnd;

    /**
     * 0代表查关注 1代表查粉丝
     */
    private Integer queryType;

    public Integer getQueryType() {
        return queryType;
    }

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
    }

    public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setFocusUserId(String focusUserId) {
		this.focusUserId = focusUserId;
	}

	public String getFocusUserId() {
		return focusUserId;
	}

	public void setFocusTime(Date focusTime) {
		this.focusTime = focusTime;
	}

	public Date getFocusTime() {
		return focusTime;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return userIdFuzzy;
	}

	public void setFocusUserIdFuzzy(String focusUserIdFuzzy) {
		this.focusUserIdFuzzy = focusUserIdFuzzy;
	}

	public String getFocusUserIdFuzzy() {
		return focusUserIdFuzzy;
	}

	public void setFocusTimeStart(String focusTimeStart) {
		this.focusTimeStart = focusTimeStart;
	}

	public String getFocusTimeStart() {
		return focusTimeStart;
	}

	public void setFocusTimeEnd(String focusTimeEnd) {
		this.focusTimeEnd = focusTimeEnd;
	}

	public String getFocusTimeEnd() {
		return focusTimeEnd;
	}
}