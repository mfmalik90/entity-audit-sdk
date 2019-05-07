package com.careem.entityauditsdk.configuration;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author faizanmalik
 * creation date 2019-05-02
 * This is EventListener Registerer for the Hibernate Entity life-cycle events.
 */
@Component
public class HibernateEventListenerRegistry {

        @Autowired
        private HibernateEntityManagerFactory entityManagerFactory;

        @Autowired(required = false)
        private List<PostInsertEventListener> postInsertEventListeners;

        @Autowired(required = false)
        private List<PostUpdateEventListener> postUpdateEventListeners;

        @Autowired(required = false)
        private List<PostDeleteEventListener> postDeleteEventListeners;

        @PostConstruct
        public void registerListeners() {
            EventListenerRegistry registry = entityManagerFactory
                    .getSessionFactory().getServiceRegistry().getService(EventListenerRegistry.class);
            if (postInsertEventListeners != null) {
                registry.appendListeners(
                        EventType.POST_INSERT,
                        postInsertEventListeners.toArray(new PostInsertEventListener[postInsertEventListeners.size()])
                );
            }
            if (postUpdateEventListeners != null) {
                registry.appendListeners(
                        EventType.POST_UPDATE,
                        postUpdateEventListeners.toArray(new PostUpdateEventListener[postUpdateEventListeners.size()])
                );
            }
            if (postDeleteEventListeners != null) {
                registry.appendListeners(
                        EventType.POST_DELETE,
                        postDeleteEventListeners.toArray(new PostDeleteEventListener[postDeleteEventListeners.size()])
                );
            }
        }
}
