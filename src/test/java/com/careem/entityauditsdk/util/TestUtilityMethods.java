package com.careem.entityauditsdk.util;

import com.careem.entityauditsdk.dto.EntityAuditEventData;
import com.careem.entityauditsdk.dto.RawAuditEventData;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

import static com.careem.entityauditsdk.util.TestConstants.TEST_ID;
import static com.careem.entityauditsdk.util.TestConstants.TEST_STRING;

/**
 * @author faizanmalik
 * creation date 5/9/19
 */
public final class TestUtilityMethods {

    private TestUtilityMethods(){}

    public static TestEntity mockTestEntity(){
        Timestamp currentTime = Timestamp.from(Instant.now());
        return TestEntity.builder()
                .id(TEST_ID)
                .status(1)
                .createdAt(currentTime)
                .updatedAt(currentTime)
                .build();
    }

    public static RawAuditEventData mockRawAuditEventData(){
        String[] oldState = {"test1"};
        String[] newState = {"test2"};
        return RawAuditEventData.builder()
                .modifiedBy(TEST_STRING)
                .modifiedByType(TEST_STRING)
                .tableName("test_entity")
                .recordId(TEST_ID)
                .action("UPDATE")
                .oldState(oldState)
                .newState(newState)
                .attributeDefinitionIterable(new ArrayList<>())
                .build();
    }

    public static EntityAuditEventData mockEntityAuditEventData(Object oldState, Object newState){
        return EntityAuditEventData.builder()
                .modifiedBy(TEST_STRING)
                .modifiedByType(TEST_STRING)
                .tableName("test_entity")
                .recordId(TEST_ID)
                .action("UPDATE")
                .oldState(oldState)
                .newState(newState)
                .build();
    }
}
