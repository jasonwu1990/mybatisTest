/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50525
Source Host           : 127.0.0.1:3306
Source Database       : imybatis

Target Server Type    : MYSQL
Target Server Version : 50525
File Encoding         : 65001

Date: 2016-12-05 15:38:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(22) DEFAULT NULL,
  `password` int(30) DEFAULT NULL,
  `age` int(2) NOT NULL,
  `deleteFlag` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'final', '123', '25', '0');
INSERT INTO `user` VALUES ('2', 'jason', '123', '100', '0');
INSERT INTO `user` VALUES ('3', 'steve', '123', '18', '0');
INSERT INTO `user` VALUES ('5', '秋刀鱼', '213', '12', '1');
INSERT INTO `user` VALUES ('8', '麦卡伦', '3212', '110', '1');
INSERT INTO `user` VALUES ('10', '类正字', '3512', '320', '0');
INSERT INTO `user` VALUES ('12', '类正字', '3512', '320', '0');
INSERT INTO `user` VALUES ('13', '类正字', '3512', '320', '0');
INSERT INTO `user` VALUES ('14', '类正字', '3512', '320', '0');
INSERT INTO `user` VALUES ('15', '牛逼呀', '1990', '154', '0');
