# Entity Audit SDK

The SDK provides the capability for out of the box audit logs for hibernate entity.

SDK works on the **HibernateEventsListeners** namely **PostInsertEvent , PostUpdateEvent and PostDeleteEvent.**
and since only the JpaRepository/CrudRepository managed methods will publish the event so the audit logs will work only for the default methods exposed by JpaRepository/CrudRepository.

CICD pipeline :https://cicd-pipelines.careem-internal.com/job/generated-pipelines/job/captain-payments/job/entity-audit-sdk/

Bitbucket Repo: ssh://git@sshbitbucketdc.careem-internal.com:7999/sdk/entity-audit-sdk.git 

Example Pull Request :https://bitbucketdc.careem-internal.com/projects/CPAY/repos/payout-info-service/pull-requests/60/overview

Confluence Wiki: https://confluence.careempartner.com/display/CAPE/Entity+Audit+SDK

> Steps to add and configure the SDK
1. Add the dependency to the pom

    ```
    <dependency>
        <groupId>com.careem.commons</groupId>
        <artifactId>entity-audit-sdk</artifactId>
        <version>19.18.05</version>
    </dependency>
   ```
   
 2. Run the expansion script to create the `audit_log` table in the database you want to persist the audit logs.
    scripts path :  `/coms/entity-audit-sdk/src/main/resources/scripts/audit_log.sql`
 3. Add the database configuration for the database you want to persist the logs.
    ```# payout-info-db master
       audit-log-db.spring.master.datasource.url=${datasource.database.auditlog.endpoint}/${datasource.database.auditlog.schema}?autoReconnect=true&verifyServerCertificate=false&useSSL=false&requireSSL=false&characterEncoding=UTF-8&useUnicode=true
       audit-log-db.spring.master.datasource.username=${datasource.database.auditlog.username}
       audit-log-db.spring.master.datasource.password=${datasource.database.auditlog.password}
       audit-log-db.spring.master.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
       audit-log-db.spring.master.datasource.max-active=${datasource.database.config.pool.size}
       audit-log-db.spring.master.datasource.initial-size=5
       audit-log-db.spring.master.datasource.max-idle=5
       audit-log-db.spring.master.datasource.min-idle=1
       audit-log-db.spring.master.datasource.test-while-idle=true
       audit-log-db.spring.master.datasource.test-on-borrow=true
       audit-log-db.spring.master.datasource.validation-query=SELECT 1```
update the properties as per your service configurations.       


>How to use?

To enable the audit log for any entity just add the annotation **@EnableEntityAuditing** , you can control which type of database action you want to be audited y default it will start persisting the **INSERT,UPDATE,DELETE** actions.
To specify only specific actions to be audited use the annotation as **@EnableEntityAuditing(auditType = {EntityAuditType.INSERT, EntityAuditType.DELETE})**

>Notes

SDK will work for the below default methods exposed by JpaRepository/CrudRepository.
```JpaRepository:
       
       <S extends T> List<S> save(Iterable<S> var1);
   
       void flush();
   
       <S extends T> S saveAndFlush(S var1);
   
       void deleteInBatch(Iterable<T> var1);
   
       void deleteAllInBatch();
   
   CrudRepository:
   
       <S extends T> S save(S var1);
   
       <S extends T> Iterable<S> save(Iterable<S> var1);
   
       void delete(ID var1);
   
       void delete(T var1);
   
       void delete(Iterable<? extends T> var1);
   
       void deleteAll();```