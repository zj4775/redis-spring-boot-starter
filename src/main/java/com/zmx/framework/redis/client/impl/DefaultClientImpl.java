package com.zmx.framework.redis.client.impl;

import com.zmx.framework.redis.callback.CallBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

/** * 
author  zhongjie
date 创建时间：2017年9月7日 上午10:49:56
version 1.0
parameter 
since
return 
*/
public class DefaultClientImpl {

    private final Logger logger = LoggerFactory.getLogger(DefaultClientImpl.class.getName());

    protected Pool pool;


    public void setPool(Pool pool) {
        this.pool = pool;
    }

    protected <T> T execute(CallBack<T> callback) {
        Jedis jedis = null;
        try {
            jedis = (Jedis)pool.getResource();
            return callback.invoke(jedis);
        } catch (JedisException e) {
            logger.error("jedis pool get resource error:{}", e);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return null;
    }
}
