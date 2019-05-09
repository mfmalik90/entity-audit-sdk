package com.careem.entityauditsdk.listener;

import com.careem.entityauditsdk.dto.EntityAuditEventData;
import com.careem.entityauditsdk.dto.RawAuditEventData;
import com.careem.entityauditsdk.model.AuditLog;
import com.careem.entityauditsdk.repository.AuditLogRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.careem.entityauditsdk.util.Constants.ID;
import static com.careem.entityauditsdk.util.UtilityMethods.censorRequestDto;

/**
 * @author faizanmalik
 * creation date 2019-05-02
 */
@Slf4j
@Service
public class EntityAuditLogEventConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Value("${service.identifier:unidentified}")
    private String sourceService;

    @Value("${spring.application.version:unidentified}")
    private String serviceVersion;

    @Async
    @EventListener
    @Transactional(value = "auditLogTransactionManager")
    public void processAuditEvent(RawAuditEventData rawAuditEventData) {
        try {
            ObjectNode oldStateNode = rawAuditEventData.getOldState() != null
                    ? JsonNodeFactory.instance.objectNode() : null;
            ObjectNode newStateNode = rawAuditEventData.getNewState() != null
                    ? JsonNodeFactory.instance.objectNode() : null;
            final int[] count = {0};
            rawAuditEventData.getAttributeDefinitionIterable().forEach(attributeDefinition -> {
                String name = attributeDefinition.getName();
                if (rawAuditEventData.getOldState() != null) {
                    oldStateNode.set(name, objectMapper.valueToTree(rawAuditEventData.getOldState()[count[0]]));
                }
                if (rawAuditEventData.getNewState() != null) {
                    newStateNode.set(name, objectMapper.valueToTree(rawAuditEventData.getNewState()[count[0]]));
                }
                count[0]++;
            });
            if (oldStateNode != null) {
                oldStateNode.put(ID, rawAuditEventData.getRecordId());
            }
            if (newStateNode != null) {
                newStateNode.put(ID, rawAuditEventData.getRecordId());
            }
            ObjectNode requestNode = (rawAuditEventData.getRequestDto() == null) ? null :
                    objectMapper.valueToTree(rawAuditEventData.getRequestDto());
            JsonNode censoredRequestNode;
            if (requestNode != null) {
                String censoredRequestNodeString = censorRequestDto(requestNode.toString());
                censoredRequestNode = objectMapper.readTree(censoredRequestNodeString);
            } else {
                censoredRequestNode = null;
            }
            saveAuditLog(rawAuditEventData.getModifiedBy(), rawAuditEventData.getModifiedByType(),
                    rawAuditEventData.getTableName(), rawAuditEventData.getRecordId(),
                    rawAuditEventData.getAction(), oldStateNode, newStateNode, censoredRequestNode);
        } catch (Exception e) {
            log.warn("Error occurred while processing the rawAuditEventData : {}", rawAuditEventData);
        }
    }

    @Async
    @EventListener
    @Transactional(value = "auditLogTransactionManager")
    public void processAuditEvent(EntityAuditEventData entityAuditEventData) {
        try {
            ObjectNode oldStateNode = entityAuditEventData.getOldState() != null
                    ? objectMapper.valueToTree(entityAuditEventData.getOldState()) : null;
            ObjectNode newStateNode = entityAuditEventData.getNewState() != null
                    ? objectMapper.valueToTree(entityAuditEventData.getNewState()) : null;
            ObjectNode requestNode = (entityAuditEventData.getRequestDto() == null) ? null :
                    objectMapper.valueToTree(entityAuditEventData.getRequestDto());
            JsonNode censoredRequestNode;
            if (requestNode != null) {
                String censoredRequestNodeString = censorRequestDto(requestNode.toString());
                censoredRequestNode = objectMapper.readTree(censoredRequestNodeString);
            } else {
                censoredRequestNode = null;
            }
            saveAuditLog(entityAuditEventData.getModifiedBy(), entityAuditEventData.getModifiedByType(),
                    entityAuditEventData.getTableName(), entityAuditEventData.getRecordId(),
                    entityAuditEventData.getAction(), oldStateNode, newStateNode, censoredRequestNode);
        } catch (Exception e) {
            log.warn("Error occurred while processing the entityAuditEventData : {}", entityAuditEventData);
        }
    }

    private void saveAuditLog(
            String modifiedBy, String modifiedByType, String tableName, Long recordId,
            String action, ObjectNode oldStateNode, ObjectNode newStateNode, JsonNode requestNode
    ) {
        try {
            AuditLog auditLog = AuditLog.builder()
                    .modifiedBy(modifiedBy)
                    .modifiedByType(modifiedByType)
                    .tableName(tableName)
                    .recordId(recordId)
                    .action(action)
                    .oldState(oldStateNode)
                    .newState(newStateNode)
                    .sourceService(sourceService)
                    .serviceVersion(serviceVersion)
                    .requestMetadata(requestNode)
                    .build();
            log.info("Saving custom audit log with with content : {} on thread #{} with threadId #{}",
                    auditLog, Thread.currentThread(), Thread.currentThread().getId());
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.warn("Error occurred while saving audit logs by the modifiedBy : {} and modifiedByType : {} for the" +
                            " table : {} and recordId : {} and action : {} with oldState : {} and newState : {}",
                    modifiedBy, modifiedByType, tableName, recordId, action, oldStateNode, newStateNode);
        }
    }

}

