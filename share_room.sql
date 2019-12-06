/*
 Navicat Premium Data Transfer

 Source Server         : 本地连接MySQL
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : share_room

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 27/11/2019 17:23:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for conf_record
-- ----------------------------
DROP TABLE IF EXISTS `conf_record`;
CREATE TABLE `conf_record`  (
  `conference_id` int(11) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) NULL DEFAULT NULL,
  `conference_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `conference_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '申请人id',
  `conference_start` timestamp(0) NULL DEFAULT NULL,
  `conference_end` timestamp(0) NULL DEFAULT NULL,
  `room_status` tinyint(4) NULL DEFAULT NULL COMMENT '会议室的申请进程/0.申请未分配/1.已分配未开始/2.会议已经结束/3.分配失败',
  `conference_nums` int(255) NULL DEFAULT NULL COMMENT '会议申请人数',
  PRIMARY KEY (`conference_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sr_room
-- ----------------------------
DROP TABLE IF EXISTS `sr_room`;
CREATE TABLE `sr_room`  (
  `room_id` int(11) NOT NULL AUTO_INCREMENT,
  `room_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `room_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `room_nums` int(11) NOT NULL COMMENT '会议室可容纳人数',
  `room_status` tinyint(4) NOT NULL COMMENT '会议室状态/0.授权可用/1.该会议室不可用',
  `user_id` int(11) NOT NULL COMMENT '上传者id',
  PRIMARY KEY (`room_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sr_staff
-- ----------------------------
DROP TABLE IF EXISTS `sr_staff`;
CREATE TABLE `sr_staff`  (
  `staff_id` int(11) NOT NULL,
  `conference_id` int(11) NOT NULL,
  `staff_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `staff_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `staff_status` tinyint(4) NULL DEFAULT NULL COMMENT '该与会人员是否签到/0.未签到/1.已签到',
  `staff_checkInTime` timestamp(0) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sr_user
-- ----------------------------
DROP TABLE IF EXISTS `sr_user`;
CREATE TABLE `sr_user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `user_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `user_role` tinyint(4) NOT NULL COMMENT '账户权限/0.root/2.会议室管理员/5.一级用户/10.二级用户/15.三级用户',
  `user_alias` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `user_passwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
