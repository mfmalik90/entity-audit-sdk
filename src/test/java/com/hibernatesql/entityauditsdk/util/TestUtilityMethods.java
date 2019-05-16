package com.hibernatesql.entityauditsdk.util;

import com.hibernatesql.entityauditsdk.dto.EntityAuditEventData;
import com.hibernatesql.entityauditsdk.dto.RawAuditEventData;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

/**
 * @author faizanmalik
 * creation date 5/9/19
 */
public final class TestUtilityMethods {

    private TestUtilityMethods(){}

    public static TestEntity mockTestEntity(){
        Timestamp currentTime = Timestamp.from(Instant.now());
        return TestEntity.builder()
                .id(TestConstants.TEST_ID)
                .status(1)
                .createdAt(currentTime)
                .updatedAt(currentTime)
                .build();
    }

    public static RawAuditEventData mockRawAuditEventData(){
        String[] oldState = {"test1"};
        String[] newState = {"test2"};
        return RawAuditEventData.builder()
                .modifiedBy(TestConstants.TEST_STRING)
                .modifiedByType(TestConstants.TEST_STRING)
                .tableName("test_entity")
                .recordId(TestConstants.TEST_ID)
                .action("UPDATE")
                .oldState(oldState)
                .newState(newState)
                .attributeDefinitionIterable(new ArrayList<>())
                .build();
    }

    public static EntityAuditEventData mockEntityAuditEventData(Object oldState, Object newState){
        return EntityAuditEventData.builder()
                .modifiedBy(TestConstants.TEST_STRING)
                .modifiedByType(TestConstants.TEST_STRING)
                .tableName("test_entity")
                .recordId(TestConstants.TEST_ID)
                .action("UPDATE")
                .oldState(oldState)
                .newState(newState)
                .build();
    }
}
