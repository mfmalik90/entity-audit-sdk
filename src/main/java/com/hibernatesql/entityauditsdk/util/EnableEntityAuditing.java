package com.hibernatesql.entityauditsdk.util;

import com.hibernatesql.entityauditsdk.type.EntityAuditType;

import java.lang.annotation.*;

/**
 * @author faizanmalik
 * creation date 2019-05-05
 * This annotation will be checked against the object entity and if found the
 * automatic audit_log will be enabled for that entity
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableEntityAuditing {
    EntityAuditType[] auditType() default EntityAuditType.ALL;
}
