package com.hibernatesql.entityauditsdk.repository;

import com.hibernatesql.entityauditsdk.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author faizanmalik
 * creation date 2019-05-02
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}

