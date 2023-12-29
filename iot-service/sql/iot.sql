/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1_3306
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : 127.0.0.1:3306
 Source Schema         : iot

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 29/12/2023 14:35:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for device
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device`  (
  `id` bigint NOT NULL COMMENT 'id',
  `gateway_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '网关id：外键gateway.id，唯一gateway_id和sn',
  `sn` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '设备序号，唯一gateway_id和sn',
  `type` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '设备类型：0网关 1温湿度计',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `gateway_id__sn`(`gateway_id` ASC, `sn` ASC) USING BTREE,
  INDEX `sn`(`sn` ASC) USING BTREE,
  CONSTRAINT `device__gateway_id` FOREIGN KEY (`gateway_id`) REFERENCES `gateway` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device
-- ----------------------------
INSERT INTO `device` VALUES (0, 0, 0, 0);
INSERT INTO `device` VALUES (1, 0, 1, 1);

-- ----------------------------
-- Table structure for gateway
-- ----------------------------
DROP TABLE IF EXISTS `gateway`;
CREATE TABLE `gateway`  (
  `id` bigint UNSIGNED NOT NULL COMMENT 'id',
  `sn` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '网关序号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `sn`(`sn` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '网关' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gateway
-- ----------------------------
INSERT INTO `gateway` VALUES (0, 123);

SET FOREIGN_KEY_CHECKS = 1;
