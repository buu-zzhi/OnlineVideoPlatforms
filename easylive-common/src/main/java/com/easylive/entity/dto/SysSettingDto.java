package com.easylive.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysSettingDto implements Serializable {
    /**
     * 注册赠送硬币数量
     */
    private Integer registerCoinCount = 10;

    /**
     * 发布视频送硬币数量
     */
    private Integer postVideoCoinCount = 5;

    /**
     * 单个视频大小
     */
    private Integer videoSize = 100;

    /**
     * 稿件最大分P数量
     */
    private Integer videoPCount = 10;

    /**
     * 每天发布视频数
     */
    private Integer videoCount = 10;

    /**
     * 每天允许评论数
     */
    private Integer commentCount = 20;

    /**
     * 每天允许弹幕数
     */
    private Integer danmuCount = 20;

}
