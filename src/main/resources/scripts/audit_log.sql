CREATE TABLE audit_log (
  `id` BIGINT(20) unsigned NOT NULL AUTO_INCREMENT,
  `modified_by` VARCHAR(255) DEFAULT NULL,
  `modified_by_type` VARCHAR(255) DEFAULT NULL,
  `table_name` VARCHAR(255) NOT NULL,
  `record_id` BIGINT(20) unsigned NOT NULL,
  `action` VARCHAR(64) DEFAULT NULL,
  `old_state` JSON DEFAULT NULL,
  `new_state` JSON DEFAULT NULL,
  `source_service` VARCHAR(128) DEFAULT NULL,
  `service_version` VARCHAR(128) DEFAULT NULL,
  `request_metadata` JSON DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `modified_by_and_modified_by_type_key` (`modified_by`, `modified_by_type`),
  KEY `modified_by_type_key` (`modified_by_type`),
  KEY `table_name_and_record_id_key` (`table_name`, `record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;