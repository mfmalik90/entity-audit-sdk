package com.hibernatesql.entityauditsdk.publisher;

import com.hibernatesql.entityauditsdk.dto.EntityAuditEventData;
import com.hibernatesql.entityauditsdk.dto.RawAuditEventData;
import com.hibernatesql.entityauditsdk.dto.RequestDto;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.persister.walking.spi.AttributeDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import static com.hibernatesql.entityauditsdk.util.Constants.USER_ID;
import static com.hibernatesql.entityauditsdk.util.Constants.USER_TYPE;
import static com.hibernatesql.entityauditsdk.util.UtilityMethods.*;

/**
 * @author faizanmalik
 * creation date 2019-05-05
 * This is an helper class to prepare and publish AuditLog Event using ApplicationEventPublisher which will
 * be consumed by the EntityAuditLogEventConsumer asynchronously to finally persist the logs in the database.
 * Once this class publishes the Event there is no check, weather the log should be persisted or not.
 * Any event published by this class will be directly persisted in the database along with source_service and service_version.
 * .....................................................................................................................
 * .....................................................................................................................
 * This class can be used for any database action log which is not getting triggered as hibernate event.
 */
@Slf4j
@Component
public class EntityAuditLogEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishRawAuditLogEvent(
            String tableName, Long recordId, String action, Object[] oldState,
            Object[] newState, Iterable<AttributeDefinition> attributeDefinitions
    ) {
        RequestDto requestDto = getRequestDto();
        String modifiedBy = getRequestUserInfo(requestDto, USER_ID);
        String modifiedByType = getRequestUserInfo(requestDto, USER_TYPE);
        RawAuditEventData rawAuditEventData = RawAuditEventData.builder()
                .modifiedBy(modifiedBy)
                .modifiedByType(modifiedByType)
                .tableName(tableName)
                .recordId(recordId)
                .action(action)
                .oldState(oldState)
                .newState(newState)
                .attributeDefinitionIterable(attributeDefinitions)
                .requestDto(requestDto)
                .build();
        log.info("Publishing custom audit event with data : {} from thread #{} with threadId #{}",
                rawAuditEventData, Thread.currentThread(), Thread.currentThread().getId());
        applicationEventPublisher.publishEvent(rawAuditEventData);
    }

    public void publishEntityAuditLogEvent(
            String tableName, Long recordId, String action, Object oldState, Object newState
    ) {
        RequestDto requestDto = getRequestDto();
        String modifiedBy = getRequestUserInfo(requestDto, USER_ID);
        String modifiedByType = getRequestUserInfo(requestDto, USER_TYPE);
        EntityAuditEventData entityAuditEventData = EntityAuditEventData.builder()
                .modifiedBy(modifiedBy)
                .modifiedByType(modifiedByType)
                .tableName(tableName)
                .recordId(recordId)
                .action(action)
                .oldState(oldState)
                .newState(newState)
                .requestDto(requestDto)
                .build();
        log.info("Publishing custom audit event with data : {} from thread #{} with threadId #{}",
                entityAuditEventData, Thread.currentThread(), Thread.currentThread().getId());
        applicationEventPublisher.publishEvent(entityAuditEventData);
    }
}
