package com.careem.entityauditsdk.repository;

import com.careem.entityauditsdk.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author faizanmalik
 * creation date 2019-05-02
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}

