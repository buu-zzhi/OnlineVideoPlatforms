package com.easylive.entity.enums;

public enum MessageTypeEnum {
    SYS(1, "系统消息"),
    LIKE(2, "点赞"),
    COLLECTION(3, "收藏"),
    COMMENT(4, "评论");


    private Integer type;
    private String desc;

    MessageTypeEnum(Integer type, String desc) {
        this.desc = desc;
        this.type = type;
    }

    public static MessageTypeEnum getByType(Integer type) {
        for (MessageTypeEnum item : MessageTypeEnum.values()) {
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
