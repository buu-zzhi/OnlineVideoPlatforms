package com.easylive.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO implements Serializable {
    /**
     * 用户id
     */
    private String userId;

    /**
     *
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 0:女 1:男 2:未知
     */
    private Integer sex;

    /**
     * 个人介绍
     */
    private String personIntroduction;

    /**
     * 空间公告
     */
    private String noticeInfo;

    /**
     * 等级
     */
    private Integer grade;

    /**
     * 出生日期
     */
    private String birthday;

    /**
     * 学校
     */
    private String school;

    private Integer fansCount;
    private Integer focusCount;
    private Integer likeCount;
    private Integer playCount;
    private Boolean haveFocus;

    /**
     * 主题
     */
    private Integer theme;
}
