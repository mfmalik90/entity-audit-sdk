package com.careem.entityauditsdk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.persister.walking.spi.AttributeDefinition;

/**
 * @author faizanmalik
 * creation date 2019-05-02
 * This is a DTO for receiving the audit_log event and the content of state
 * are the array of the attribute values of the entity along with attributeDefinition
 * which will be used to create the state ObjectNode. It also has some other attributes needed for logging.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawAuditEventData {
    private String modifiedBy;
    private String modifiedByType;
    private String tableName;
    private Long recordId;
    private String action;
    private Object[] oldState;
    private Object[] newState;
    private Iterable<AttributeDefinition> attributeDefinitionIterable;
    private RequestDto requestDto;
}