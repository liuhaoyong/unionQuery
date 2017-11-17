CREATE DATABASE `union-query`;

use union-query;


CREATE TABLE `kf_busniess` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `busniess_name` varchar(200) NOT NULL,
  `created_time` timestamp(3) NOT NULL,
  `updated_time` timestamp(3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `kf_database_source` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dbs_name` varchar(200) NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `pwd` varchar(100) NOT NULL,
  `jdbc_url` varchar(300) NOT NULL,
  `driver_type` varchar(100) DEFAULT NULL,
  `created_time` timestamp(3) NULL DEFAULT NULL,
  `updated_time` timestamp(3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `kf_param` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `param_name` varchar(200) DEFAULT NULL,
  `field_name` varchar(200) DEFAULT NULL,
  `memo` varchar(200) DEFAULT NULL,
  `created_time` timestamp(3) NULL DEFAULT NULL,
  `updated_time` timestamp(3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `kf_sql` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sql_name` varchar(100) NOT NULL,
  `busniess_id` int(11) NOT NULL,
  `priority` int(11) DEFAULT NULL,
  `data_source_id` int(11) DEFAULT NULL,
  `sql_status` int(11) DEFAULT NULL,
  `sql_statement` varchar(4000) DEFAULT NULL,
  `sql_desc` varchar(400) DEFAULT NULL,
  `created_time` timestamp(3) NULL DEFAULT NULL,
  `updated_time` timestamp(3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `kf_sql_param` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sql_id` int(11) DEFAULT NULL,
  `param_id` int(11) DEFAULT NULL,
  `sql_field` varchar(200) DEFAULT NULL,
  `param_desc` varchar(200) DEFAULT NULL,
  `created_time` timestamp(3) NULL DEFAULT NULL,
  `updated_time` timestamp(3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;
