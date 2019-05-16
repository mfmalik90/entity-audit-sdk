package com.hibernatesql.entityauditsdk.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * @author faizanmalik
 * creation date 2019-05-05
 * database configuration for the audit-log database
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityAuditLogEntityManagerFactory",
        basePackages = {"com.careem.entityauditsdk.repository"}
)
public class EntityAuditLogDbConfiguration {

    @Bean(name = "auditLogDataSource")
    @ConfigurationProperties(prefix = "audit-log-db.spring.master.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "entityAuditLogEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("auditLogDataSource") DataSource dataSource
    ) {
        return builder.dataSource(dataSource)
                .packages("com.careem.entityauditsdk.model")
                .persistenceUnit("auditLogPersistenceUnit")
                .build();
    }

    @Bean(name = "auditLogTransactionManager")
    public PlatformTransactionManager auditLogTransactionManager(
            @Qualifier("entityAuditLogEntityManagerFactory") EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

