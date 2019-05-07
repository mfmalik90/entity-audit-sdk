package com.careem.entityauditsdk.model;

import com.careem.entityauditsdk.util.jsontype.JsonStringType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author faizanmalik
 * creation date 2019-05-02
 */
@Getter
@Setter
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class)
})
@Entity
@Table(name = "audit_log")
@DynamicUpdate
@Builder
@AllArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    // modifiedBy is derived from the attribute (User) first from the headers of the request if it exist,
    // else fallback to RequestContextHolder attribute (User)
    @Column(name = "modified_by")
    private String modifiedBy;
    // modifiedByType is derived from the attribute (UserType) first from the headers of the request if it exist,
    // else fallback to RequestContextHolder attribute (UserType)
    @Column(name = "modified_by_type")
    private String modifiedByType;
    @Column(name = "table_name")
    private String tableName;
    @Column(name = "record_id")
    private Long recordId;
    @Column(name = "action")
    private String action;
    @Type(type = "json")
    @Column(columnDefinition = "json", name="old_state")
    private JsonNode oldState;
    @Type(type = "json")
    @Column(columnDefinition = "json", name="new_state")
    private JsonNode newState;
    @Column(name = "source_service")
    private String sourceService;
    @Column(name = "service_version")
    private String serviceVersion;
    @Type(type = "json")
    @Column(columnDefinition = "json", name="request_metadata")
    private JsonNode requestMetadata;
    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
