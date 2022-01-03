drop table if exists users;
CREATE TABLE users
(
   id VARCHAR(40) NOT NULL COMMENT '主键ID',
  `name` varchar(64) NOT NULL COMMENT '用户名',
  `sex` tinyint(4) DEFAULT '0' COMMENT '性别',
  `avatar` varchar(128) DEFAULT NULL COMMENT '头像',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(16) NOT NULL DEFAULT '' COMMENT '手机',
  `password` varchar(128)  NOT NULL DEFAULT '' COMMENT '密码',
  `salt` varchar(128)  NOT NULL DEFAULT '' COMMENT '盐',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态,1正常，2禁用',
  `source` varchar(32)  NOT NULL DEFAULT '' COMMENT '来源',
  `create_id` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime,
  `update_id` int(11) NOT NULL DEFAULT '0',
  `update_time` datetime,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  `role` tinyint(4) DEFAULT '0',
  `open_id` varchar(128)  NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
);


drop table if exists file_system;
CREATE TABLE file_system
(
  id VARCHAR(40) NOT NULL COMMENT '主键ID',
  name VARCHAR(50) NULL DEFAULT NULL COMMENT '名称',
  user_id VARCHAR(40) NULL DEFAULT NULL COMMENT '用户ID',
  parent_id VARCHAR(40) NULL DEFAULT NULL COMMENT '父及ID',
  path VARCHAR(200) NULL DEFAULT NULL COMMENT '路径',
  create_date datetime NULL DEFAULT NULL COMMENT '创建时间',
  type tinyint(1) NULL DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (id)
);

drop table if exists analysis_task;
CREATE TABLE analysis_task
(
  id VARCHAR(40) NOT NULL COMMENT '主键ID',
  name VARCHAR(50) NULL DEFAULT NULL COMMENT '名称',
  user_id VARCHAR(40) NULL DEFAULT NULL COMMENT '用户ID',
  file_id VARCHAR(40) NULL DEFAULT NULL COMMENT '分析的文件ID',
  param VARCHAR(500) NULL DEFAULT NULL COMMENT '参数',
  output_path VARCHAR(200) NULL DEFAULT NULL COMMENT '路径',
  create_date datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (id)
);
