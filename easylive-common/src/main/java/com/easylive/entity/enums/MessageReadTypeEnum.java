package com.easylive.entity.enums;

public enum MessageReadTypeEnum {
    NO_READ(0, "未读"),
    READ(1, "已读");


    private Integer type;
    private String desc;

    MessageReadTypeEnum(Integer type, String desc) {
        this.desc = desc;
        this.type = type;
    }

    public static MessageReadTypeEnum getByType(Integer type) {
        for (MessageReadTypeEnum item : MessageReadTypeEnum.values()) {
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
