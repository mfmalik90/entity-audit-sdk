package com.hibernatesql.entityauditsdk.publisher;

import com.hibernatesql.entityauditsdk.dto.EntityAuditEventData;
import com.hibernatesql.entityauditsdk.dto.RawAuditEventData;
import com.hibernatesql.entityauditsdk.type.DatabaseActionType;
import com.hibernatesql.entityauditsdk.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author faizanmalik
 * creation date 5/9/19
 */
@RunWith(MockitoJUnitRunner.class)
public class EntityAuditLogEventPublisherTest {

    @InjectMocks
    private EntityAuditLogEventPublisher auditLogEventPublisher;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private RequestAttributes attrs;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        RequestContextHolder.setRequestAttributes(attrs);
    }

    private final String tableName = "test_entity";

    @Test
    public void publishRawAuditLogEventTest(){
        auditLogEventPublisher.publishRawAuditLogEvent(
                tableName, TestConstants.TEST_ID, DatabaseActionType.UPDATE.name(), null, null, null);
        verify(applicationEventPublisher, times(1))
                .publishEvent(any(RawAuditEventData.class));
    }

    @Test
    public void publishEntityAuditLogEventTest(){
        auditLogEventPublisher.publishEntityAuditLogEvent(
                tableName, TestConstants.TEST_ID, DatabaseActionType.UPDATE.name(), null, null);
        verify(applicationEventPublisher, times(1))
                .publishEvent(any(EntityAuditEventData.class));
    }
}


