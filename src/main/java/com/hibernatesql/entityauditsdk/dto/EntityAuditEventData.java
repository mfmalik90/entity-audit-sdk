package com.hibernatesql.entityauditsdk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author faizanmalik
 * creation date 2019-05-02
 * This is a DTO for the audit_log event which accept the instance
 * of the entity as states and other attributes needed for audit_log
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityAuditEventData {
    private String modifiedBy;
    private String modifiedByType;
    private String tableName;
    private Long recordId;
    private String action;
    private Object oldState;
    private Object newState;
    private RequestDto requestDto;
}
