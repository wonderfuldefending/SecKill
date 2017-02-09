package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by GuoFei on 2017/1/24.
 * 配置spring和junit整合，junit启动时加载springIOC容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    //注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void queryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
        /**
         * 1000元秒杀iphone6
         Seckill{seckillId=1000,
         name='1000元秒杀iphone6',number=100,
         startTime=Wed Nov 23 00:00:00 CST 2016, endTime=Thu Nov 24 00:00:00 CST 2016,
         createTime=Mon Jan 23 15:19:18 CST 2017}
         */
    }

    @Test
    public void queryAll() throws Exception {
        /**Java运行期，没有保存形参信息，Parameter 'offset' not found. Available parameters are [0, 1, param1, param2]
         * queryAll(int offset, int limit) -> queryAll(arg0, arg1)
         * 所以有一个以上参数时就匹配不上了，要告诉MyBatis哪个位置是什么，用@Param注解（用名字）
         * List<Seckill> queryAll(@Param("offset")int offset, @Param("limit")int limit);
         */

        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        for (Seckill seckill : seckills) {
            System.out.println(seckill);
        }
    }

    @Test
    public void reduceNumber() throws Exception {
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L, killTime);
        System.out.println("updateCount:" + updateCount);
    }


}