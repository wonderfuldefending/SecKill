-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE seckill;
use seckill;

CREATE TABLE seckill(
  seckill_id  bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
  name VARCHAR(120) NOT NULL COMMENT '商品名称',
  number int NOT NULL COMMENT '库存数量',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  start_time TIMESTAMP NOT NULL COMMENT '秒杀开启时间',
  end_time TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
  PRIMARY KEY (seckill_id),
  KEY idx_create_time(create_time),
  KEY idx_start_time(start_time),
  KEY idx_end_time(end_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀数据库';
--mysql引擎配置为innordb，以便支持事务

-- 初始化数据库
INSERT INTO
  seckill(name,number,start_time,end_time)
 VALUES
  ('1000元秒杀iphone6',100,'2016-11-23 00:00:00','2016-11-24 00:00:00'),
  ('500元秒杀ipad2',200,'2016-11-23 00:00:00','2016-11-24 00:00:00'),
  ('300元秒杀小米4',300,'2016-11-23 00:00:00','2016-11-24 00:00:00'),
  ('2000元秒杀iphone6s',400,'2016-11-23 00:00:00','2016-11-24 00:00:00');

-- 秒杀成功明细表
-- 用户登录认证相关的信息
CREATE TABLE success_killed(
  seckill_id bigint NOT NULL COMMENT '秒杀商品ID',
  user_phone bigint NOT NULL COMMENT '用户手机号',
  state tinyint NOT NULL DEFAULT -1 COMMENT '状态标示:-1:无效 0：成功 1：已付款',
  create_time TIMESTAMP NOT NULL COMMENT '创建时间',
  PRIMARY KEY(seckill_id,user_phone),
  KEY idx_create_time(create_time)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';
--联合主键 可以防止一个手机号有重复秒杀的情况
