package com.easylive.entity.enums;

public enum CommentTopTypeEnum {
    NO_TOP(0, "未置顶"),
    TOP(1, "已置顶");

    private Integer type;
    private String desc;

    CommentTopTypeEnum(Integer status, String desc) {
        this.type = status;
        this.desc = desc;
    }

    public static CommentTopTypeEnum getByType(Integer type) {
        for (CommentTopTypeEnum item : CommentTopTypeEnum.values()) {
            if (item.getType() == type) {
                return item;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
