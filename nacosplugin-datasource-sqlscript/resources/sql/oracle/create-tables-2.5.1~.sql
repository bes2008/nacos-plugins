SET ECHO ON;
SET FEEDBACK ON;

WHENEVER SQLERROR EXIT;

ALTER SESSION SET CURRENT_SCHEMA=C##NACOS;



DROP TABLE IF EXISTS config_info;
CREATE TABLE config_info (
                             id int NOT NULL,
                             data_id varchar2(255)  NOT NULL,
                             group_id varchar2(255) ,
                             content CLOB  NOT NULL,
                             md5 varchar2(32) ,
                             gmt_create timestamp(6) NOT NULL,
                             gmt_modified timestamp(6) NOT NULL,
                             src_user CLOB ,
                             src_ip varchar2(20) ,
                             app_name varchar2(128) ,
                             tenant_id varchar2(128) DEFAULT 'public',
                             c_desc varchar2(256) ,
                             c_use varchar2(64) ,
                             effect varchar2(64) ,
                             type varchar2(64) ,
                             c_schema CLOB ,
                             encrypted_data_key CLOB DEFAULT ''
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_configinfo_datagrouptenant ON config_info (data_id,group_id,tenant_id);
ALTER TABLE config_info ADD CONSTRAINT config_info_pkey PRIMARY KEY (id);

COMMENT ON COLUMN config_info.id IS 'id';
COMMENT ON COLUMN config_info.data_id IS 'data_id';
COMMENT ON COLUMN config_info.content IS 'content';
COMMENT ON COLUMN config_info.md5 IS 'md5';
COMMENT ON COLUMN config_info.gmt_create IS '创建时间';
COMMENT ON COLUMN config_info.gmt_modified IS '修改时间';
COMMENT ON COLUMN config_info.src_user IS 'source user';
COMMENT ON COLUMN config_info.src_ip IS 'source ip';
COMMENT ON COLUMN config_info.tenant_id IS '租户字段';
COMMENT ON COLUMN config_info.encrypted_data_key IS '秘钥';
COMMENT ON TABLE config_info IS 'config_info';



DROP SEQUENCE IF EXISTS config_info_id_seq;
create sequence config_info_id_seq
    minvalue 1
    increment by 1
    start with 1;

create or replace trigger config_info_id_inc
before insert on config_info for each row
begin
select config_info_id_seq.nextval into:new.id from dual;
end;
/



DROP TABLE IF EXISTS config_info_gray;
CREATE TABLE config_info_gray (
  id int NOT NULL,
  data_id varchar(255) NOT NULL,
  group_id varchar(128) NOT NULL,
  content CLOB NOT NULL,
  md5 varchar(32),
  src_user CLOB,
  src_ip varchar(100),
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  app_name varchar(128) ,
  tenant_id varchar(128),
  gray_name varchar(128) NOT NULL,
  gray_rule CLOB NOT NULL,
  encrypted_data_key varchar(1024) NOT NULL
);
COMMENT ON COLUMN config_info_gray.id IS 'id';
COMMENT ON COLUMN config_info_gray.data_id IS 'data_id';
COMMENT ON COLUMN config_info_gray.group_id IS 'content';
COMMENT ON COLUMN config_info_gray.content IS 'md5';
COMMENT ON COLUMN config_info_gray.md5 IS '创建时间';
COMMENT ON COLUMN config_info_gray.src_user IS 'source user';
COMMENT ON COLUMN config_info_gray.src_ip IS 'source ip';
COMMENT ON COLUMN config_info_gray.gmt_create IS '创建时间';
COMMENT ON COLUMN config_info_gray.gmt_modified IS '修改时间';
COMMENT ON COLUMN config_info_gray.app_name IS 'app_name';
COMMENT ON COLUMN config_info_gray.tenant_id IS '租户字段';
COMMENT ON COLUMN config_info_gray.gray_name IS '灰度发布名称';
COMMENT ON COLUMN config_info_gray.gray_rule IS '灰度发布规则';
COMMENT ON COLUMN config_info_gray.encrypted_data_key IS '密钥';
COMMENT ON TABLE config_info_gray IS 'config_info_gray';

ALTER TABLE config_info_gray ADD CONSTRAINT config_info_gray_pkey PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_configinfogray_datagrouptenantgray ON config_info_gray (data_id,group_id,tenant_id,gray_name);
CREATE INDEX IF NOT EXISTS idx_dataid_gmt_modified ON config_info_gray (data_id, gmt_modified);
CREATE INDEX IF NOT EXISTS idx_gmt_modified ON config_info_gray  ( gmt_modified );



DROP SEQUENCE IF EXISTS config_info_gray_id_seq;
create sequence config_info_gray_id_seq
    minvalue 1
    increment by 1
    start with 1;

create or replace trigger config_info_gray_id_inc
before insert on config_info_gray for each row
begin
select config_info_gray_id_seq.nextval into:new.id from dual;
end;
/



DROP TABLE IF EXISTS config_tags_relation;
CREATE TABLE config_tags_relation (
                                      id int NOT NULL,
                                      tag_name varchar2(128)  NOT NULL,
                                      tag_type varchar2(64) ,
                                      data_id varchar2(255)  NOT NULL,
                                      group_id varchar2(128)  NOT NULL,
                                      tenant_id varchar2(128) DEFAULT 'public',
                                      nid int NOT NULL
);
COMMENT ON COLUMN config_tags_relation.id IS 'id';
COMMENT ON COLUMN config_tags_relation.tag_name IS 'tag_name';
COMMENT ON COLUMN config_tags_relation.tag_type IS 'tag_type';
COMMENT ON COLUMN config_tags_relation.data_id IS 'data_id';
COMMENT ON COLUMN config_tags_relation.group_id IS 'group_id';
COMMENT ON COLUMN config_tags_relation.tenant_id IS 'tenant_id';
COMMENT ON TABLE config_tags_relation IS 'config_tag_relation';

CREATE INDEX IF NOT EXISTS idx_tenant_id ON config_tags_relation (tenant_id);
CREATE UNIQUE INDEX IF NOT EXISTS uk_configtagrelation_configidtag ON config_tags_relation (id,tag_name,tag_type);
ALTER TABLE config_tags_relation ADD CONSTRAINT config_tags_relation_pkey PRIMARY KEY (nid);



DROP SEQUENCE IF EXISTS config_tags_relation_id_seq;
create sequence config_tags_relation_id_seq
    minvalue 1
    increment by 1
    start with 1;

create or replace trigger config_tags_relation_id_inc
before insert on config_tags_relation for each row
begin
select config_tags_relation_id_seq.nextval into:new.id from dual;
end;
/



DROP TABLE IF EXISTS group_capacity;
CREATE TABLE group_capacity (
                                id int NOT NULL,
                                group_id varchar2(128)  NOT NULL,
                                quota int NOT NULL,
                                usage int NOT NULL,
                                max_size int NOT NULL,
                                max_aggr_count int NOT NULL,
                                max_aggr_size int NOT NULL,
                                max_history_count int NOT NULL,
                                gmt_create timestamp(6) NOT NULL,
                                gmt_modified timestamp(6) NOT NULL
);
COMMENT ON COLUMN group_capacity.id IS '主键ID';
COMMENT ON COLUMN group_capacity.group_id IS 'Group ID，空字符表示整个集群';
COMMENT ON COLUMN group_capacity.quota IS '配额，0表示使用默认值';
COMMENT ON COLUMN group_capacity.usage IS '使用量';
COMMENT ON COLUMN group_capacity.max_size IS '单个配置大小上限，单位为字节，0表示使用默认值';
COMMENT ON COLUMN group_capacity.max_aggr_count IS '聚合子配置最大个数，，0表示使用默认值';
COMMENT ON COLUMN group_capacity.max_aggr_size IS '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值';
COMMENT ON COLUMN group_capacity.max_history_count IS '最大变更历史数量';
COMMENT ON COLUMN group_capacity.gmt_create IS '创建时间';
COMMENT ON COLUMN group_capacity.gmt_modified IS '修改时间';
COMMENT ON TABLE group_capacity IS '集群、各Group容量信息表';

CREATE UNIQUE INDEX IF NOT EXISTS uk_group_id ON group_capacity (group_id);
ALTER TABLE group_capacity ADD CONSTRAINT group_capacity_pkey PRIMARY KEY (id);



DROP SEQUENCE IF EXISTS group_capacity_id_seq;
create sequence group_capacity_id_seq
    minvalue 1
    increment by 1
    start with 1;

create or replace trigger group_capacity_id_inc
before insert on group_capacity for each row
begin
select group_capacity_id_seq.nextval into:new.id from dual;
end;
/


ALTER TABLE group_capacity MODIFY quota DEFAULT 0;
ALTER TABLE group_capacity MODIFY usage DEFAULT 0;
ALTER TABLE group_capacity MODIFY max_size DEFAULT 0;
ALTER TABLE group_capacity MODIFY max_aggr_count DEFAULT 0;
ALTER TABLE group_capacity MODIFY max_aggr_size DEFAULT 0;
ALTER TABLE group_capacity MODIFY max_history_count DEFAULT 0;


DROP TABLE IF EXISTS his_config_info;
CREATE TABLE his_config_info (
                                 id int NOT NULL,
                                 nid int NOT NULL,
                                 data_id varchar2(255)  NOT NULL,
                                 group_id varchar2(128)  NOT NULL,
                                 app_name varchar2(128) ,
                                 content CLOB  NOT NULL,
                                 md5 varchar2(32) ,
                                 gmt_create timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                 gmt_modified timestamp(6) NOT NULL,
                                 src_user CLOB ,
                                 src_ip varchar2(20) ,
                                 op_type char(10) ,
                                 tenant_id varchar2(128) DEFAULT 'public',
                                 encrypted_data_key CLOB  DEFAULT '',
                                 publish_type varchar(50) DEFAULT 'formal',
                                 gray_name varchar(50),
                                 ext_info  CLOB
);
COMMENT ON COLUMN his_config_info.app_name IS 'app_name';
COMMENT ON COLUMN his_config_info.tenant_id IS '租户字段';
COMMENT ON COLUMN his_config_info.encrypted_data_key IS '秘钥';
COMMENT ON TABLE his_config_info IS '多租户改造';

CREATE INDEX IF NOT EXISTS idx_did ON his_config_info (data_id);
CREATE INDEX IF NOT EXISTS idx_gmt_create ON his_config_info (gmt_create);
CREATE INDEX IF NOT EXISTS idx_gmt_modified ON his_config_info (gmt_modified);
ALTER TABLE his_config_info ADD CONSTRAINT his_config_info_pkey PRIMARY KEY (nid);



DROP SEQUENCE IF EXISTS his_config_info_nid_seq;
create sequence his_config_info_nid_seq
    minvalue 1
    increment by 1
    start with 1;

create or replace trigger his_config_info_nid_inc
before insert on his_config_info for each row
begin
select his_config_info_nid_seq.nextval into:new.nid from dual;
end;
/



DROP TABLE IF EXISTS permissions;
CREATE TABLE permissions (
                             "ROLE" varchar2(50)  NOT NULL,
                             "RESOURCE" varchar2(512)  NOT NULL,
                             "ACTION" varchar2(8)  NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_role_permission ON permissions ("ROLE","RESOURCE","ACTION");



DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
                            USERNAME varchar2(50)  NOT NULL,
                            ROLE varchar2(50)  NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_username_role ON roles ("USERNAME", "ROLE");



DROP TABLE IF EXISTS tenant_capacity;
CREATE TABLE tenant_capacity (
                                 id int NOT NULL,
                                 tenant_id varchar2(128)  NOT NULL,
                                 quota int NOT NULL,
                                 usage int NOT NULL,
                                 max_size int NOT NULL,
                                 max_aggr_count int NOT NULL,
                                 max_aggr_size int NOT NULL,
                                 max_history_count int NOT NULL,
                                 gmt_create timestamp(6) NOT NULL,
                                 gmt_modified timestamp(6) NOT NULL
);
COMMENT ON COLUMN tenant_capacity.id IS '主键ID';
COMMENT ON COLUMN tenant_capacity.tenant_id IS 'Tenant ID';
COMMENT ON COLUMN tenant_capacity.quota IS '配额，0表示使用默认值';
COMMENT ON COLUMN tenant_capacity.usage IS '使用量';
COMMENT ON COLUMN tenant_capacity.max_size IS '单个配置大小上限，单位为字节，0表示使用默认值';
COMMENT ON COLUMN tenant_capacity.max_aggr_count IS '聚合子配置最大个数';
COMMENT ON COLUMN tenant_capacity.max_aggr_size IS '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值';
COMMENT ON COLUMN tenant_capacity.max_history_count IS '最大变更历史数量';
COMMENT ON COLUMN tenant_capacity.gmt_create IS '创建时间';
COMMENT ON COLUMN tenant_capacity.gmt_modified IS '修改时间';
COMMENT ON TABLE tenant_capacity IS '租户容量信息表';

CREATE UNIQUE INDEX IF NOT EXISTS uk_tenant_id ON tenant_capacity (tenant_id);
ALTER TABLE tenant_capacity ADD CONSTRAINT tenant_capacity_pkey PRIMARY KEY (id);



DROP SEQUENCE IF EXISTS tenant_capacity_id_seq;
create sequence tenant_capacity_id_seq
    minvalue 1
    increment by 1
    start with 1;


create or replace trigger tenant_capacity_id_inc
before insert on tenant_capacity for each row
begin
select tenant_capacity_id_seq.nextval into:new.id from dual;
end;
/

ALTER TABLE tenant_capacity MODIFY quota DEFAULT 0;
ALTER TABLE tenant_capacity MODIFY usage DEFAULT 0;
ALTER TABLE tenant_capacity MODIFY max_size DEFAULT 0;
ALTER TABLE tenant_capacity MODIFY max_aggr_count DEFAULT 0;
ALTER TABLE tenant_capacity MODIFY max_aggr_size DEFAULT 0;
ALTER TABLE tenant_capacity MODIFY max_history_count DEFAULT 0;

DROP TABLE IF EXISTS tenant_info;
CREATE TABLE tenant_info (
                             id int NOT NULL,
                             kp varchar2(128)  NOT NULL,
                             tenant_id varchar2(128) DEFAULT 'public',
                             tenant_name varchar2(128) ,
                             tenant_desc varchar2(256) ,
                             create_source varchar2(32) ,
                             gmt_create int NOT NULL,
                             gmt_modified int NOT NULL
);
COMMENT ON COLUMN tenant_info.id IS 'id';
COMMENT ON COLUMN tenant_info.kp IS 'kp';
COMMENT ON COLUMN tenant_info.tenant_id IS 'tenant_id';
COMMENT ON COLUMN tenant_info.tenant_name IS 'tenant_name';
COMMENT ON COLUMN tenant_info.tenant_desc IS 'tenant_desc';
COMMENT ON COLUMN tenant_info.create_source IS 'create_source';
COMMENT ON COLUMN tenant_info.gmt_create IS '创建时间';
COMMENT ON COLUMN tenant_info.gmt_modified IS '修改时间';
COMMENT ON TABLE tenant_info IS 'tenant_info';



DROP SEQUENCE IF EXISTS tenant_info_id_seq;
create sequence tenant_info_id_seq
    minvalue 1
    increment by 1
    start with 1;

create or replace trigger tenant_info_id_inc
before insert on tenant_info for each row
begin
select tenant_info_id_seq.nextval into:new.id from dual;
end;
/

CREATE UNIQUE INDEX IF NOT EXISTS uk_tenant_info_kptenantid ON tenant_info (kp, tenant_id);



DROP TABLE IF EXISTS users;
CREATE TABLE users (
                       username varchar2(50)  NOT NULL,
                       password varchar2(500)  NOT NULL,
                       enabled NUMBER(1) NOT NULL
);




INSERT INTO roles VALUES ('nacos', 'ROLE_ADMIN');
INSERT INTO users VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);
COMMIT;

exit
