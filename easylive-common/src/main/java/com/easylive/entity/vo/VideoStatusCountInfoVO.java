package com.easylive.entity.vo;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class VideoStatusCountInfoVO implements Serializable {
    private Integer auditPassCount;
    private Integer auditFailCount;
    private Integer inProgressCount;

    public Integer getAuditPassCount() {
        return auditPassCount;
    }

    public void setAuditPassCount(Integer auditPassCount) {
        this.auditPassCount = auditPassCount;
    }

    public Integer getAuditFailCount() {
        return auditFailCount;
    }

    public void setAuditFailCount(Integer auditFailCount) {
        this.auditFailCount = auditFailCount;
    }

    public Integer getInProgressCount() {
        return inProgressCount;
    }

    public void setInProgressCount(Integer inProgressCount) {
        this.inProgressCount = inProgressCount;
    }
}
