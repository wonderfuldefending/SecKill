package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by GuoFei on 2017/1/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
                "classpath:spring/spring-dao.xml",
                "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckills = seckillService.getSeckillList();
        logger.info("seckills={}", seckills);
    }

    @Test
    public void getById() throws Exception {
        long id = 1000L;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}", seckill);
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long id = 1004;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}", exposer);
        //exposer=Exposer{exposed=true, md5='38542340b1b1d58414cce301affac0fd', seckillId=1004, now=0, start=0, end=0}
        //Exposer{exposed=false,
        // md5='null', seckillId=1000, now=1485335675202, start=1479830400000, end=1479916800000}
    }

    //集成测试代码完整逻辑，注意可重复执行
    @Test
    public void testSeckillLogic() throws Exception{
        long id = 1004;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()){
            logger.info("exposer={}", exposer);
            long userPhone = 13312345678L;
            String md5 = "38542340b1b1d58414cce301affac0fd";
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(id, userPhone, md5);
                logger.info("seckillExecution={}", seckillExecution);
            }catch (RepeatKillException e){
                logger.error(e.getMessage());
            }catch (SeckillCloseException e){
                logger.error(e.getMessage());
            }
        }else {
            //秒杀未开启
            logger.warn("exposer={}", exposer);
        }
    }

    @Test
    public void executeSeckillprocedure(){
        long seckillId = 1004;
        long phone = 15920111023L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecution seckillExecution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            logger.info(seckillExecution.getStateInfo());
        }
    }

//    @Test
//    public void executeSeckill() throws Exception {
//        long id = 1004;
//        long userPhone = 13812345678L;
//        String md5 = "38542340b1b1d58414cce301affac0fd";
//        try {
//            SeckillExecution seckillExecution = seckillService.executeSeckill(id, userPhone, md5);
//            logger.info("seckillExecution={}", seckillExecution);
//        }catch (RepeatKillException e){
//            logger.error(e.getMessage());
//        }catch (SeckillCloseException e){
//            logger.error(e.getMessage());
//        }
//    }


}