package com.hibernatesql.entityauditsdk.listener;

import com.hibernatesql.entityauditsdk.publisher.EntityAuditLogEventPublisher;
import com.hibernatesql.entityauditsdk.type.DatabaseActionType;
import com.hibernatesql.entityauditsdk.type.EntityAuditType;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.hibernatesql.entityauditsdk.util.UtilityMethods.entityAuditingEnabled;
import static com.hibernatesql.entityauditsdk.util.UtilityMethods.getTableName;

/**
 * @author faizanmalik
 * creation date 2019-05-05
 * This is a listener to the hibernate entity life-cycle events,
 * It will receive the event and get the relevant data from the entity
 * and finally publish the ApplicationEvent using EntityAuditLogEventPublisher to be consumed for logging
 */
@Slf4j
@Component
public class EntityActionEventListener implements
        PostUpdateEventListener,
        PostInsertEventListener,
        PostDeleteEventListener
{

    @Autowired
    private EntityAuditLogEventPublisher entityAuditLogEventPublisher;

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        try {
            if (entityAuditingEnabled(event.getEntity(), EntityAuditType.DELETE)) {
                String tableName = getTableName(event.getEntity());
                entityAuditLogEventPublisher.publishEntityAuditLogEvent(
                        tableName, (Long) event.getId(), DatabaseActionType.DELETE.name(), event.getEntity(), null
                );
            }
        } catch (Exception e) {
            log.warn("Unable to publish audit-log-event for onPostDelete with event : {}", event, e);
        }

    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        try {
            if (entityAuditingEnabled(event.getEntity(), EntityAuditType.INSERT)) {
                String tableName = getTableName(event.getEntity());
                entityAuditLogEventPublisher.publishEntityAuditLogEvent(
                        tableName, (Long) event.getId(), DatabaseActionType.INSERT.name(), null, event.getEntity()
                );
            }
        } catch (Exception e) {
            log.warn("Unable to publish audit-log-event for onPostInsert with event : {}", event, e);
        }
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        try {
            if (entityAuditingEnabled(event.getEntity(), EntityAuditType.UPDATE)) {
                String tableName = getTableName(event.getEntity());
                entityAuditLogEventPublisher.publishRawAuditLogEvent(
                        tableName, (Long) event.getId(), DatabaseActionType.UPDATE.name(), event.getOldState(),
                        event.getState(), event.getPersister() != null ? event.getPersister().getAttributes() : null
                );
            }
        } catch (Exception e) {
            log.warn("Unable to publish audit-log-event for onPostUpdate with event : {}", event, e);
        }
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return true;
    }

}
