package org.seckill.dao;

import org.seckill.entity.SuccessKilled;

/**
 * Created by GuoFei on 2017/1/23.
 */
public interface SuccessKilledDao {

    /**
     * 插入购买明细，可过滤重复：
     * @param seckillId
     * @param userPhone
     * @return 插入的记录行数
     */
    int insertSuccessKilled(long seckillId, long userPhone);

    /**
     * 根据Id查询SuccessKilled并携带秒杀产品对象实体
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(long seckillId);
}
