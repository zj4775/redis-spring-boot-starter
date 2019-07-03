package com.zmx.framework.redis.exception;
/** * 
author  zhongjie
date 创建时间：2017年9月7日 上午10:54:58
version 1.0
parameter 
since
return 
*/
public class RedisClientException extends RuntimeException {

    /**
     */
    private static final long serialVersionUID = -4629579849260670181L;

    /**
     * 构造方法
     * 
     * @param msg 异常信息
     */
    public RedisClientException(String msg) {
        super(msg);
    }

    /**
     * 构造方法
     * 
     * @param exception 异常原因
     */
    public RedisClientException(Throwable exception) {
        super(exception);
    }

    /**
     * 构造方法
     * 
     * @param mag 异常信息
     * @param exception 异常原因
     */
    public RedisClientException(String mag, Exception exception) {
        super(mag, exception);
    }
}
