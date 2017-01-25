package org.seckill.exception;

/**
 * 秒杀业务相关异常
 * Created by GuoFei on 2017/1/25.
 */
public class SeckillException extends RuntimeException {

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
