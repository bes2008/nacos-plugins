USE nacos;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS "config_info";
CREATE TABLE "config_info" (
                               "id" BIGINT NOT NULL IDENTITY(1,1),
                               "data_id" varchar(255)  NOT NULL,
                               "group_id" varchar(255) ,
                               "content" ntext  NOT NULL,
                               "md5" varchar(32) ,
                               "gmt_create" datetime NOT NULL,
                               "gmt_modified" datetime NOT NULL,
                               "src_user" ntext ,
                               "src_ip" varchar(20) ,
                               "app_name" varchar(128) ,
                               "tenant_id" varchar(128) ,
                               "c_desc" varchar(256) ,
                               "c_use" varchar(64) ,
                               "effect" varchar(64) ,
                               "type" varchar(64) ,
                               "c_schema" ntext ,
                               "encrypted_data_key" ntext
);


-- ----------------------------
-- Indexes structure for table config_info
-- ----------------------------
CREATE UNIQUE INDEX "uk_configinfo_datagrouptenant" ON "config_info" ("data_id","group_id","tenant_id");

-- ----------------------------
-- Primary Key structure for table config_info
-- ----------------------------
ALTER TABLE "config_info" ADD CONSTRAINT "config_info_pkey" PRIMARY KEY ("id");


/******************************************/
/*   表名称 = config_info_gray             */
/******************************************/
DROP TABLE IF EXISTS "config_info_gray";
CREATE TABLE "config_info_gray" (
                                    "id" bigint unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                    "data_id" varchar(255) NOT NULL,
                                    "group_id" varchar(128) NOT NULL,
                                    "content" longtext NOT NULL,
                                    "md5" varchar(32) DEFAULT NULL,
                                    "src_user" text,
                                    "src_ip" varchar(100) DEFAULT NULL,
                                    "gmt_create" datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
                                    "gmt_modified" datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
                                    "app_name" varchar(128) DEFAULT NULL,
                                    "tenant_id" varchar(128) DEFAULT '',
                                    "gray_name" varchar(128) NOT NULL,
                                    "gray_rule" text NOT NULL,
                                    "encrypted_data_key" varchar(256) NOT NULL DEFAULT ''

);


CREATE UNIQUE INDEX  "uk_configinfogray_datagrouptenantgray" ON "config_info_gray"  ("data_id","group_id","tenant_id","gray_name");
CREATE UNIQUE INDEX  "idx_dataid_gmt_modified" ON "config_info_gray"  ("data_id","gmt_modified");
CREATE UNIQUE INDEX  "idx_gmt_modified" ON "config_info_gray" ("gmt_modified");
-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS "config_tags_relation";
CREATE TABLE "config_tags_relation" (
                                        "id" BIGINT NOT NULL,
                                        "tag_name" varchar(128)  NOT NULL,
                                        "tag_type" varchar(64) ,
                                        "data_id" varchar(255)  NOT NULL,
                                        "group_id" varchar(128)  NOT NULL,
                                        "tenant_id" varchar(128) ,
                                        "nid" BIGINT NOT NULL IDENTITY(1,1)
);



-- ----------------------------
-- Indexes structure for table config_tags_relation
-- ----------------------------
CREATE INDEX  "idx_tenant_id" ON "config_tags_relation"  ("tenant_id");
CREATE UNIQUE INDEX  "uk_configtagrelation_configidtag" ON "config_tags_relation"  ("id","tag_name","tag_type");

-- ----------------------------
-- Primary Key structure for table config_tags_relation
-- ----------------------------
ALTER TABLE "config_tags_relation" ADD CONSTRAINT "config_tags_relation_pkey" PRIMARY KEY ("nid");




-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS "group_capacity";
CREATE TABLE "group_capacity" (
                                  "id" BIGINT NOT NULL IDENTITY(1,1),
                                  "group_id" varchar(128)  NOT NULL,
                                  "quota" INT NOT NULL DEFAULT 0,
                                  "usage" INT NOT NULL DEFAULT 0,
                                  "max_size" INT NOT NULL DEFAULT 0,
                                  "max_aggr_count" INT NOT NULL DEFAULT 0,
                                  "max_aggr_size" INT NOT NULL DEFAULT 0,
                                  "max_history_count" INT NOT NULL DEFAULT 0,
                                  "gmt_create" datetime NOT NULL,
                                  "gmt_modified" datetime NOT NULL
);

-- ----------------------------
-- Indexes structure for table group_capacity
-- ----------------------------
CREATE UNIQUE INDEX  "uk_group_id" ON "group_capacity"  ("group_id");

-- ----------------------------
-- Primary Key structure for table group_capacity
-- ----------------------------
ALTER TABLE "group_capacity" ADD CONSTRAINT "group_capacity_pkey" PRIMARY KEY ("id");



-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS "his_config_info";
CREATE TABLE "his_config_info" (
                                   "id" BIGINT NOT NULL,
                                   "nid" BIGINT NOT NULL IDENTITY(1,1),
                                   "data_id" varchar(255)  NOT NULL,
                                   "group_id" varchar(128)  NOT NULL,
                                   "app_name" varchar(128) ,
                                   "content" ntext  NOT NULL,
                                   "md5" varchar(32) ,
                                   "gmt_create" datetime NOT NULL,
                                   "gmt_modified" datetime NOT NULL,
                                   "src_user" ntext ,
                                   "src_ip" varchar(20) ,
                                   "op_type" char(10) ,
                                   "tenant_id" varchar(128) ,
                                   "encrypted_data_key" ntext,
                                   "publish_type" varchar(50)  DEFAULT 'formal',
                                   "gray_name" varchar(50)  DEFAULT NULL,
                                   "ext_info"  longtext DEFAULT NULL
);



-- ----------------------------
-- Indexes structure for table his_config_info
-- ----------------------------
CREATE INDEX  "idx_did" ON "his_config_info"  ("data_id");
CREATE INDEX  "idx_gmt_create" ON "his_config_info"  ("gmt_create");
CREATE INDEX  "idx_gmt_modified" ON "his_config_info"  ("gmt_modified");

-- ----------------------------
-- Primary Key structure for table his_config_info
-- ----------------------------
ALTER TABLE "his_config_info" ADD CONSTRAINT "his_config_info_pkey" PRIMARY KEY ("nid");


-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS "tenant_capacity";
CREATE TABLE "tenant_capacity" (
                                   "id" BIGINT NOT NULL IDENTITY(1,1),
                                   "tenant_id" varchar(128) NOT NULL,
                                   "quota" INT NOT NULL DEFAULT 0,
                                   "usage" INT NOT NULL DEFAULT 0,
                                   "max_size" INT NOT NULL DEFAULT 0,
                                   "max_aggr_count" INT NOT NULL DEFAULT 0,
                                   "max_aggr_size" INT NOT NULL DEFAULT 0,
                                   "max_history_count" INT NOT NULL DEFAULT 0,
                                   "gmt_create" datetime NOT NULL,
                                   "gmt_modified" datetime NOT NULL
);

-- ----------------------------
-- Indexes structure for table tenant_capacity
-- ----------------------------
CREATE UNIQUE INDEX  "uk_tenant_id" ON "tenant_capacity"  ("tenant_id");

-- ----------------------------
-- Primary Key structure for table tenant_capacity
-- ----------------------------
ALTER TABLE "tenant_capacity" ADD CONSTRAINT "tenant_capacity_pkey" PRIMARY KEY ("id");



-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS "tenant_info";
CREATE TABLE "tenant_info" (
                               "id" BIGINT NOT NULL IDENTITY(1,1),
                               "kp" varchar(128)  NOT NULL,
                               "tenant_id" varchar(128) ,
                               "tenant_name" varchar(128) ,
                               "tenant_desc" varchar(256) ,
                               "create_source" varchar(32) ,
                               "gmt_create" BIGINT NOT NULL,
                               "gmt_modified" BIGINT NOT NULL
);

-- ----------------------------
-- Indexes structure for table tenant_info
-- ----------------------------
CREATE UNIQUE INDEX  "uk_tenant_info_kptenantid" ON "tenant_info"  ("kp","tenant_id");



-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS "users";
CREATE TABLE "users" (
                         "username" varchar(50)  NOT NULL,
                         "password" varchar(500)  NOT NULL,
                         "enabled" BIT NOT NULL
);


-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS "roles";
CREATE TABLE "roles" (
                         "username" varchar(50)  NOT NULL,
                         "role" varchar(50)  NOT NULL
);


-- ----------------------------
-- Indexes structure for table roles
-- ----------------------------
CREATE UNIQUE INDEX  "uk_username_role" ON "roles"  ("username","role");

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS "permissions";
CREATE TABLE "permissions" (
                               "role" varchar(50)  NOT NULL,
                               "resource" varchar(512)  NOT NULL,
                               "action" varchar(8)  NOT NULL
);


-- ----------------------------
-- Indexes structure for table permissions
-- ----------------------------
CREATE UNIQUE INDEX  "uk_role_permission" ON "permissions"  ("role","resource","action");


-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO "users" VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);


-- ----------------------------
-- Records of roles
-- ----------------------------

INSERT INTO "roles" VALUES ('nacos', 'ROLE_ADMIN');

