/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80300 (8.3.0)
 Source Host           : 127.0.0.1:3306
 Source Schema         : iot-single

 Target Server Type    : MySQL
 Target Server Version : 80300 (8.3.0)
 File Encoding         : 65001

 Date: 30/08/2024 14:46:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for device
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device`  (
  `id` bigint UNSIGNED NOT NULL COMMENT 'id',
  `gateway_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '网关id 外键gateway.id 唯一gateway_id和sn',
  `sn` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '设备序号 唯一gateway_id和sn',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '设备名称',
  `type` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '设备类型 0网关 1温度计',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `gateway_id__sn`(`gateway_id` ASC, `sn` ASC) USING BTREE,
  INDEX `sn`(`sn` ASC) USING BTREE,
  INDEX `gateway_id`(`gateway_id` ASC) USING BTREE,
  CONSTRAINT `device__gateway_id` FOREIGN KEY (`gateway_id`) REFERENCES `gateway` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device
-- ----------------------------
INSERT INTO `device` VALUES (12300, 123, 0, '网关', 0, '');
INSERT INTO `device` VALUES (12301, 123, 1, '温度计', 1, '');
INSERT INTO `device` VALUES (12302, 123, 2, '温湿度计', 2, '');
INSERT INTO `device` VALUES (12400, 124, 0, '网关', 0, '');
INSERT INTO `device` VALUES (12401, 124, 1, '温度计', 1, '');
INSERT INTO `device` VALUES (12500, 125, 0, '网关', 0, '');

-- ----------------------------
-- Table structure for gateway
-- ----------------------------
DROP TABLE IF EXISTS `gateway`;
CREATE TABLE `gateway`  (
  `id` bigint UNSIGNED NOT NULL COMMENT 'id',
  `sn` int NOT NULL DEFAULT 0 COMMENT '网关序号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '网关名称',
  `last_online_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '上一次在线时间',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `sn`(`sn` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '网关' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gateway
-- ----------------------------
INSERT INTO `gateway` VALUES (123, 123, '网关123号', '1970-01-01 00:00:00', '');
INSERT INTO `gateway` VALUES (124, 124, '网关124号', '1970-01-01 00:00:00', '');
INSERT INTO `gateway` VALUES (125, 125, '网关125号', '1970-01-01 00:00:00', '');

SET FOREIGN_KEY_CHECKS = 1;
