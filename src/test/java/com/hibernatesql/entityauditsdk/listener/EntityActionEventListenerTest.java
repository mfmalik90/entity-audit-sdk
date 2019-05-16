package com.hibernatesql.entityauditsdk.listener;

import com.hibernatesql.entityauditsdk.model.AuditLog;
import com.hibernatesql.entityauditsdk.publisher.EntityAuditLogEventPublisher;
import com.hibernatesql.entityauditsdk.util.TestEntity;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.hibernatesql.entityauditsdk.util.TestConstants.TEST_ID;
import static com.hibernatesql.entityauditsdk.util.TestUtilityMethods.mockTestEntity;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author faizanmalik
 * creation date 5/9/19
 */
@RunWith(MockitoJUnitRunner.class)
public class EntityActionEventListenerTest {

    @InjectMocks
    private EntityActionEventListener entityActionEventListener;

    @Mock
    private EntityAuditLogEventPublisher auditLogEventPublisher;

    @Test
    public void onPostDeleteTestAuditingDisabled(){
        AuditLog auditLog = AuditLog.builder().id(TEST_ID).build();
        String[] deletedState = {"1"};
        PostDeleteEvent postDeleteEvent =
                new PostDeleteEvent(auditLog, TEST_ID, deletedState,null, null);
        entityActionEventListener.onPostDelete(postDeleteEvent);
        verify(auditLogEventPublisher, times(0))
                .publishEntityAuditLogEvent(anyString(), anyLong(), anyString(), any(), any());
    }

    @Test
    public void onPostDeleteTestAuditingEnabled(){
        TestEntity testEntity = mockTestEntity();
        String[] deletedState = {"1"};
        PostDeleteEvent postDeleteEvent =
                new PostDeleteEvent(testEntity, TEST_ID, deletedState,null, null);
        entityActionEventListener.onPostDelete(postDeleteEvent);
        verify(auditLogEventPublisher, times(1))
                .publishEntityAuditLogEvent(anyString(), anyLong(), anyString(), any(), any());
    }

    @Test
    public void onPostInsertTestAuditingDisabled(){
        AuditLog auditLog = AuditLog.builder().id(TEST_ID).build();
        String[] state = {"1", TEST_ID.toString()};
        PostInsertEvent postInsertEvent =
                new PostInsertEvent(auditLog, TEST_ID, state,null, null);
        entityActionEventListener.onPostInsert(postInsertEvent);
        verify(auditLogEventPublisher, times(0))
                .publishEntityAuditLogEvent(anyString(), anyLong(), anyString(), any(), any());
    }

    @Test
    public void onPostInsertTestAuditingEnabled(){
        TestEntity testEntity = mockTestEntity();
        String[] state = {"1", "2L"};
        PostInsertEvent postInsertEvent =
                new PostInsertEvent(testEntity, TEST_ID, state,null, null);
        entityActionEventListener.onPostInsert(postInsertEvent);
        verify(auditLogEventPublisher, times(1))
                .publishEntityAuditLogEvent(anyString(), anyLong(), anyString(), any(), any());
    }

    @Test
    public void onPostUpdateTestAuditingDisabled(){
        AuditLog auditLog = AuditLog.builder().id(TEST_ID).build();
        String[] state = {"1", TEST_ID.toString()};
        int[] testProp = {1};
        PostUpdateEvent postUpdateEvent =
                new PostUpdateEvent(auditLog,  TEST_ID, state, state, testProp,null, null);
        entityActionEventListener.onPostUpdate(postUpdateEvent);
        verify(auditLogEventPublisher, times(0))
                .publishEntityAuditLogEvent(anyString(), anyLong(), anyString(), any(), any());
    }

    @Test
    public void onPostUpdateTestAuditingEnabled(){
        TestEntity testEntity = mockTestEntity();
        String[] state = {"1", "2L"};
        int[] testProp = {1, 2};
        PostUpdateEvent postUpdateEvent =
                new PostUpdateEvent(testEntity,  TEST_ID, state, state, testProp,null, null);
        entityActionEventListener.onPostUpdate(postUpdateEvent);
        verify(auditLogEventPublisher, times(1))
                .publishRawAuditLogEvent("test_entity", TEST_ID, "UPDATE", state, state, null);
    }
}
