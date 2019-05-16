package com.hibernatesql.entityauditsdk.util;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @author faizanmalik
 * creation date 5/9/19
 */
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "test_entity")
@DynamicUpdate
@EnableEntityAuditing
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "status")
    Integer status;

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt = new Date();

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    Date updatedAt = new Date();
}