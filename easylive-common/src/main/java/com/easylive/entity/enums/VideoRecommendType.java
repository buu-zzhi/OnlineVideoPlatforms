package com.easylive.entity.enums;

public enum VideoRecommendType {
    NO_RECOMMEND(0, "未推荐"),
    RECOMMEND(1, "已推荐");

    private Integer status;
    private String desc;

    VideoRecommendType(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static VideoRecommendType getByStatus(Integer status) {
        for (VideoRecommendType videoRecommendType : VideoRecommendType.values()) {
            if (videoRecommendType.getStatus() == status) {
                return videoRecommendType;
            }
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
