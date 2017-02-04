-- 秒杀执行存储过程
--存储过程
--1：存储过程优化：事务行级锁持有的时间
--2：不要过度依赖存储过程
--3：简单的逻辑，可以应用存储过程
--4：QPS：一个秒杀单6000/qps

DELIMITER $$ -- console ; 转化为 $$
--定义存储过程
CREATE PROCEDURE `SECKILL`.`execute_seckill`
(in v_seckillId bigint, in v_phone bigint,
 in v_kill_time TIMESTAMP, out r_result int)
BEGIN
  DECLARE insert_count int DEFAULT 0;
  start TRANSACTION ;
  INSERT ignore into success_killed(seckill_id, user_phone, create_time)
    VALUES(v_seckillId, v_phone, v_kill_time);

  SELECT ROW_COUNT() into insert_count;
  IF (insert_count = 0) THEN
    ROLLBACK ;
    set r_result = -1;
  ELSEIF(insert_count < 0) THEN
    ROLLBACK ;
    set r_result = -2;
  ELSE
    UPDATE seckill set number = number - 1
      WHERE seckill_id = v_seckillId
      AND start_time < v_kill_time
      AND end_time > v_kill_time
      AND number > 0;

    SELECT ROW_COUNT() into insert_count;
    IF (insert_count = 0) THEN
      ROLLBACK ;
      set r_result = 0;
    ELSEIF(insert_count < 0) THEN
      ROLLBACK ;
      set r_result = -2;
    ELSE
      COMMIT ;
      set r_result = 1;
    END IF;
  END IF;
END;
$$
--存储过程定义结束

DELIMITER ;
set @r_result = -3;
--执行存储过程
call execute_seckill(1004, 13102178891, now(), @r_result);

--获取结果
SELECT @r_result;
SELECT * FROM seckill;
SELECT * FROM success_killed;
