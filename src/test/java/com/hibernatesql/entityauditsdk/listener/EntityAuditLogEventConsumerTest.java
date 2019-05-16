package com.hibernatesql.entityauditsdk.listener;

import com.hibernatesql.entityauditsdk.dto.EntityAuditEventData;
import com.hibernatesql.entityauditsdk.dto.RawAuditEventData;
import com.hibernatesql.entityauditsdk.model.AuditLog;
import com.hibernatesql.entityauditsdk.repository.AuditLogRepository;
import com.hibernatesql.entityauditsdk.util.TestEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.ResourceAccessException;

import static com.hibernatesql.entityauditsdk.util.TestUtilityMethods.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author faizanmalik
 * creation date 5/9/19
 */
@RunWith(MockitoJUnitRunner.class)
public class EntityAuditLogEventConsumerTest {

    @InjectMocks
    private EntityAuditLogEventConsumer auditEventService;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    public void processAuditEventTestRawEventNotNullStates(){

        RawAuditEventData rawAuditEventData = mockRawAuditEventData();
        auditEventService.processAuditEvent(rawAuditEventData);
        verify(auditLogRepository, times(1))
                .save(any(AuditLog.class));
    }

    @Test
    public void processAuditEventTestRawEventNullStates(){
        RawAuditEventData rawAuditEventData = mockRawAuditEventData();
        rawAuditEventData.setOldState(null);
        rawAuditEventData.setNewState(null);
        auditEventService.processAuditEvent(rawAuditEventData);
        verify(auditLogRepository, times(1))
                .save(any(AuditLog.class));
    }

    @Test
    public void processAuditEventTestRawEventNotNullStatesExceptionInMainMethod(){
        RawAuditEventData rawAuditEventData = mockRawAuditEventData();
        rawAuditEventData.setAttributeDefinitionIterable(null);
        auditEventService.processAuditEvent(rawAuditEventData);
        verify(auditLogRepository, times(0))
                .save(any(AuditLog.class));
    }

    @Test
    public void processAuditEventTestRawEventNotNullStatesExceptionInInternalMethod(){
        RawAuditEventData rawAuditEventData = mockRawAuditEventData();
        when(auditLogRepository.save(any(AuditLog.class)))
                .thenThrow(new ResourceAccessException("Unable to save"));
        auditEventService.processAuditEvent(rawAuditEventData);
        verify(auditLogRepository, times(1))
                .save(any(AuditLog.class));
    }

    @Test
    public void processAuditEventEntityEventNullStates(){
        EntityAuditEventData entityAuditEventData = mockEntityAuditEventData(null, null);
        auditEventService.processAuditEvent(entityAuditEventData);
        verify(auditLogRepository, times(1))
                .save(any(AuditLog.class));
    }

    @Test
    public void processAuditEventEntityEventNotNullStates(){
        TestEntity oldState = mockTestEntity();
        oldState.setStatus(1);
        TestEntity newState = mockTestEntity();
        newState.setStatus(1);
        EntityAuditEventData entityAuditEventData = mockEntityAuditEventData(oldState, newState);
        when(objectMapper.valueToTree(oldState))
                .thenReturn((new ObjectMapper()).valueToTree(oldState));
        when(objectMapper.valueToTree(newState))
                .thenReturn((new ObjectMapper()).valueToTree(newState));
        auditEventService.processAuditEvent(entityAuditEventData);
        verify(auditLogRepository, times(1))
                .save(any(AuditLog.class));
    }
}
