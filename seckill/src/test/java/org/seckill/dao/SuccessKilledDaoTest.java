package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by GuoFei on 2017/1/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    @Resource
    private  SuccessKilledDao successKilledDao;
    @Test
    public void insertSuccessKilled() throws Exception {
        /**
         * 第一次插入：insertCount=1
         * 第二次插入：insertCount=0
         */
        long seckillId = 1000L;
        long userPhone = 13545679876L;
        int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
        System.out.println("insertCount:" + insertCount);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        long seckillId = 1000L;
        long userPhone = 13545679876L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
        /**
         SuccessKilled{seckillId=1000, userPhone=13545679876, state=0, createTime=Thu Jan 26 10:10:44 CST 2017}二月 06, 2017 3:44:17 下午 org.springframework.context.support.GenericApplicationContext doClose
         Seckill{seckillId=1000, name='1000元秒杀iphone6', number=100, startTime=Wed Nov 23 00:00:00 CST 2016, endTime=Thu Nov 24 00:00:00 CST 2016, createTime=Mon Jan 23 15:19:18 CST 2017}
         */
    }

}