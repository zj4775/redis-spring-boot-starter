package com.zmx.framework.redis.client.impl;

import com.alibaba.fastjson.TypeReference;
import com.zmx.framework.redis.callback.CallBack;
import com.zmx.framework.redis.callback.GetDataCallBack;
import com.zmx.framework.redis.callback.PipelineCallBackWithResult;
import com.zmx.framework.redis.callback.PipelineCallBackWithoutResult;
import com.zmx.framework.redis.client.IRedisClient;
import com.zmx.framework.redis.util.CacheUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;
import java.util.Map.Entry;

/** * 
author  zhongjie
date 创建时间：2017年9月7日 上午10:50:27
version 1.0
parameter 
since
return 
*/
public class RedisClientImpl extends DefaultClientImpl implements IRedisClient {

    /**
     * 日志记录
     */
    private static Logger logger = LoggerFactory.getLogger(RedisClientImpl.class);

    public RedisClientImpl() {
    }

    protected <R> R performFunction(String entrykey, CallBack<R> callBack) {
        return execute(callBack);
    }

    @Override
    public String set(final String bizkey, final String nameSpace, final String value, final int time,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        // 避免setex和setnx问题
        String res = this.performFunction(key, new CallBack<String>() {
            // set
            public String invoke(Jedis jedis) {
                // 代码合并，避免多次调用，走不同代理有时间差的问题
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                String res = jedis.set(key, value);
                // 如果没有设置时间或者为负数，则不设置超时时间
                if (time > 0) {
                    Long resLong = jedis.expire(key, time);
                    if (resLong != 1) {
                        logger.error("key:" + key + "expire exception!!!");
                    }
                }
                return res;
            }
        });
        return res;
    }

    @Override
    public String set(final String bizkey, final String nameSpace, final String value, final String nxxx,
            final String expx, final long time,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.set(key, value, nxxx, expx, time);
            }
        });
    }

    @Override
    public String get(final String bizkey, final String nameSpace, final GetDataCallBack<String> gbs, final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                String res = jedis.get(key);
                if (StringUtils.isEmpty(res)) {
                    if (null != gbs) {
                        res = gbs.invoke();
                        if (StringUtils.isNotEmpty(res)) {
                            set(bizkey, nameSpace, res, "NX", "EX", gbs.getExpiredTime(),dbIndex);
                        }
                    }
                }
                return res;
            }
        });
    }

    @Override
    public Boolean exists(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Boolean>() {
            public Boolean invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.exists(key);
            }
        });
    }

    @Override
    public String type(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.type(key);
            }
        });
    }

    @Override
    public Long expire(final String bizkey, final String nameSpace, final int seconds,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.expire(key, seconds);
            }
        });
    }

    @Override
    public Long expireAt(final String bizkey, final String nameSpace, final long unixTime,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.expireAt(key, unixTime);
            }
        });
    }

    @Override
    public Long ttl(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.ttl(key);
            }
        });
    }

    @Override
    public Boolean setbit(final String bizkey, final String nameSpace, final long offset, final boolean value,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Boolean>() {
            public Boolean invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.setbit(key, offset, value);
            }
        });
    }

    @Override
    public Boolean getbit(final String bizkey, final String nameSpace, final long offset,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Boolean>() {
            public Boolean invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.getbit(key, offset);
            }
        });
    }

    @Override
    public Long setrange(final String bizkey, final String nameSpace, final long offset, final String value,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.setrange(key, offset, value);
            }
        });
    }

    @Override
    public String getrange(final String bizkey, final String nameSpace, final long startOffset, final long endOffset,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.getrange(key, startOffset, endOffset);
            }
        });
    }

    @Override
    public String getSet(final String bizkey, final String nameSpace, final String value,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.getSet(key, value);
            }
        });
    }

    @Override
    public Long setnx(final String bizkey, final String nameSpace, final String value,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.setnx(key, value);
            }
        });
    }

    @Override
    public String setex(final String bizkey, final String nameSpace, final int seconds, final String value,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.setex(key, seconds, value);
            }
        });
    }

    @Override
    public Long decrBy(final String bizkey, final String nameSpace, final long integer,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.decrBy(key, integer);
            }
        });
    }

    @Override
    public Long decr(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.decr(key);
            }
        });
    }

    @Override
    public Long incrBy(final String bizkey, final String nameSpace, final long integer,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.incrBy(key, integer);
            }
        });
    }

    @Override
    public Long incr(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.incr(key);
            }
        });
    }

    @Override
    public String substr(final String bizkey, final String nameSpace, final int start, final int end,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.substr(key, start, end);
            }
        });
    }

    @Override
    public Long hset(final String bizkey, final String nameSpace, final String field, final String value,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.hset(key, field, value);
            }
        });
    }

    @Override
    public String hget(final String bizkey, final String nameSpace, final String field,
            final GetDataCallBack<String> gbs,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                String res = jedis.hget(key, field);
                if (StringUtils.isEmpty(res)) {
                    if (null != gbs) {
                        res = gbs.invoke();
                        if (StringUtils.isNotEmpty(res)) {
                            hset(bizkey, nameSpace, field, res,dbIndex);
                        }
                    }
                }
                return res;
            }
        });
    }

    @Override
    public Long hsetnx(final String bizkey, final String nameSpace, final String field, final String value,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.hsetnx(key, field, value);
            }
        });
    }

    @Override
    public String hmset(final String bizkey, final String nameSpace, final Map<String, String> hash,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.hmset(key, hash);
            }
        });
    }

    @Override
    public List<String> hmget(final String bizkey, final String nameSpace,final int dbIndex, final String... fields) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<List<String>>() {
            public List<String> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.hmget(key, fields);
            }
        });
    }

    @Override
    public Long hincrBy(final String bizkey, final String nameSpace, final String field, final long value,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.hincrBy(key, field, value);
            }
        });
    }

    @Override
    public Boolean hexists(final String bizkey, final String nameSpace, final String field,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Boolean>() {
            public Boolean invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.hexists(key, field);
            }
        });
    }

    @Override
    public Long hdel(final String bizkey, final String nameSpace,final int dbIndex, final String... field) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.hdel(key, field);
            }
        });
    }

    @Override
    public Long hlen(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.hlen(key);
            }
        });
    }

    @Override
    public Set<String> hkeys(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Set<String>>() {
            public Set<String> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.hkeys(key);
            }
        });
    }

    @Override
    public List<String> hvals(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<List<String>>() {
            public List<String> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.hvals(key);
            }
        });
    }

    @Override
    public Map<String, String> hgetAll(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Map<String, String>>() {
            public Map<String, String> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.hgetAll(key);
            }
        });
    }

    @Override
    public Long llen(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.llen(key);
            }
        });
    }

    @Override
    public List<String> lrange(final String bizkey, final String nameSpace, final long start, final long end,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<List<String>>() {
            public List<String> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.lrange(key, start, end);
            }
        });
    }

    @Override
    public String ltrim(final String bizkey, final String nameSpace, final long start, final long end,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.ltrim(key, start, end);
            }
        });
    }

    @Override
    public String lindex(final String bizkey, final String nameSpace, final long index,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.lindex(key, index);
            }
        });
    }

    @Override
    public String lset(final String bizkey, final String nameSpace, final long index, final String value,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.lset(key, index, value);
            }
        });
    }

    @Override
    public Long lrem(final String bizkey, final String nameSpace, final long count, final String value,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.lrem(key, count, value);
            }
        });
    }

    @Override
    public String lpop(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.lpop(key);
            }
        });
    }

    @Override
    public String rpop(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.rpop(key);
            }
        });
    }

    @Override
    public Long sadd(final String bizkey, final String nameSpace,final int dbIndex, final String... member) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.sadd(key, member);
            }
        });
    }

    @Override
    public Set<String> smembers(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Set<String>>() {
            public Set<String> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.smembers(key);
            }
        });
    }

    @Override
    public Long srem(final String bizkey, final String nameSpace,final int dbIndex, final String... member) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.srem(key, member);
            }
        });
    }

    @Override
    public String spop(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.spop(key);
            }
        });
    }

    @Override
    public Long scard(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.scard(key);
            }
        });
    }

    @Override
    public Boolean sismember(final String bizkey, final String nameSpace, final String member,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Boolean>() {
            public Boolean invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.sismember(key, member);
            }
        });
    }

    @Override
    public String srandmember(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.srandmember(key);
            }
        });
    }

    @Override
    public List<String> srandmember(final String bizkey, final String nameSpace, final int count,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<List<String>>() {
            public List<String> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.srandmember(key, count);
            }
        });
    }

    @Override
    public Long zadd(final String bizkey, final String nameSpace, final double score, final String member,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zadd(key, score, member);
            }
        });
    }

    @Override
    public Set<String> zrange(final String bizkey, final String nameSpace, final long start, final long end,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Set<String>>() {
            public Set<String> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zrange(key, start, end);
            }
        });
    }

    @Override
    public Long zrem(final String bizkey, final String nameSpace,final int dbIndex, final String... member) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zrem(key, member);
            }
        });
    }

    @Override
    public Double zincrby(final String bizkey, final String nameSpace, final double score, final String member,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Double>() {
            public Double invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zincrby(key, score, member);
            }
        });
    }

    @Override
    public Long zrank(final String bizkey, final String nameSpace, final String member,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zrank(key, member);
            }
        });
    }

    @Override
    public Long zrevrank(final String bizkey, final String nameSpace, final String member,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zrevrank(key, member);
            }
        });
    }

    @Override
    public Set<String> zrevrange(final String bizkey, final String nameSpace, final long start, final long end,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Set<String>>() {
            public Set<String> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zrevrange(key, start, end);
            }
        });
    }

    @Override
    public Set<Tuple> zrangeWithScores(final String bizkey, final String nameSpace, final long start, final long end,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Set<Tuple>>() {
            public Set<Tuple> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zrangeWithScores(key, start, end);
            }
        });
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(final String bizkey, final String nameSpace, final long start, final long end,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Set<Tuple>>() {
            public Set<Tuple> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zrevrangeWithScores(key, start, end);
            }
        });
    }

    @Override
    public Long zcard(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zcard(key);
            }
        });
    }

    @Override
    public Double zscore(final String bizkey, final String nameSpace, final String member,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Double>() {
            public Double invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zscore(key, member);
            }
        });
    }

    @Override
    public List<String> sort(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<List<String>>() {
            public List<String> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.sort(key);
            }
        });
    }

    @Override
    public List<String> sort(final String bizkey, final String nameSpace, final SortingParams sortingParameters,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<List<String>>() {
            public List<String> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.sort(key, sortingParameters);
            }
        });
    }

    @Override
    public Long zcount(final String bizkey, final String nameSpace, final double min, final double max,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zcount(key, min, max);
            }
        });
    }

    @Override
    public Set<String> zrangeByScore(final String bizkey, final String nameSpace, final double min, final double max,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Set<String>>() {
            public Set<String> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zrangeByScore(key, min, max);
            }
        });
    }

    @Override
    public Set<String> zrevrangeByScore(final String bizkey, final String nameSpace, final double max, final double min,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Set<String>>() {
            public Set<String> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zrevrangeByScore(key, max, min);
            }
        });
    }

    @Override
    public Set<String> zrangeByScore(final String bizkey, final String nameSpace, final double min, final double max,
            final int offset, final int count,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Set<String>>() {
            public Set<String> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zrangeByScore(key, min, max, offset, count);
            }
        });
    }


    @Override
    public Set<String> zrevrangeByScore(final String bizkey, final String nameSpace, final double max,
            final double min, final int offset, final int count,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Set<String>>() {
            public Set<String> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zrevrangeByScore(key, max, min, offset, count);
            }
        });
    }

    @Override
    public Long zremrangeByRank(final String bizkey, final String nameSpace, final long start, final long end,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zremrangeByRank(key, start, end);
            }
        });
    }

    @Override
    public Long zremrangeByScore(final String bizkey, final String nameSpace, final double start, final double end,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zremrangeByScore(key, start, end);
            }
        });
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(final String bizkey, final String nameSpace, final double min,
            final double max,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Set<Tuple>>() {
            public Set<Tuple> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zrangeByScoreWithScores(key, min, max);
            }
        });
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final String bizkey, final String nameSpace, final double max,
            final double min,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Set<Tuple>>() {
            public Set<Tuple> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zrevrangeByScoreWithScores(key, max, min);
            }
        });
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(final String bizkey, final String nameSpace, final double min,
            final double max, final int offset, final int count,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Set<Tuple>>() {
            public Set<Tuple> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
            }
        });
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final String bizkey, final String nameSpace, final double max,
            final double min, final int offset, final int count,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Set<Tuple>>() {
            public Set<Tuple> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
            }
        });
    }

    @Override
    public Long linsert(final String bizkey, final String nameSpace, final LIST_POSITION where, final String pivot,
            final String value,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.linsert(key, where, pivot, value);
            }
        });
    }

    @Override
    public Long del(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.del(key);
            }
        });
    }

    @Override
    public Long lpush(final String bizkey, final String nameSpace,final int dbIndex, final String... fields) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                long r = 0;
                for (String field : fields) {
                    r = r + jedis.lpush(key, field);
                }
                return r;
            }
        });
    }

    @Override
    public Long rpush(final String bizkey, final String nameSpace,final int dbIndex, final String... fields) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                long r = 0;
                for (String field : fields) {
                    r = r + jedis.rpush(key, field);
                }
                return r;
            }
        });
    }

    @Override
    public Boolean setbit(final String bizkey, final String nameSpace, final long offset, final String value,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Boolean>() {
            public Boolean invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.setbit(key, offset, value);
            }
        });
    }

    @Override
    public Long strlen(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.strlen(key);
            }
        });
    }

    @Override
    public String echo(final String string,final int dbIndex) {
        return this.performFunction("", new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.echo(string);
            }
        });
    }

    @Override
    public Long bitcount(final String bizkey, final String nameSpace,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.bitcount(key);
            }
        });
    }

    @Override
    public Long bitcount(final String bizkey, final String nameSpace, final long start, final long end,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.bitcount(key, start, end);
            }
        });
    }

    @Override
    public <T> String set(final String bizkey, final String nameSpace, final T value, final int time,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return set(bizkey, nameSpace, CacheUtils.toJSONString(key, value), time,dbIndex);
    }

    @Override
    public <T> String setex(final String bizkey, final String nameSpace, final int time, final T value,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return setex(bizkey, nameSpace, time, CacheUtils.toJSONString(key, value),dbIndex);
    }

    @Override
    public <T> T get(final String bizkey, final String nameSpace, Class<T> value, final GetDataCallBack<T> gbs,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        String res = get(bizkey, nameSpace, null,dbIndex);
        T rtn = null;
        if (StringUtils.isNotEmpty(res)) {
            rtn = CacheUtils.parseObject(key, res, value);
        } else {
            if (gbs != null) {
                rtn = gbs.invoke();
                // 取出的数据要set回去
                if (null != rtn) {
                    set(bizkey, nameSpace, rtn, gbs.getExpiredTime(),dbIndex);
                }
            }
        }
        return rtn;
    }

    @Override
    public <T> T get(final String bizkey, final String nameSpace, TypeReference<T> type, final GetDataCallBack<T> gbs,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        String res = get(bizkey, nameSpace, null,dbIndex);
        T rtn = null;
        if (StringUtils.isNotEmpty(res)) {
            rtn = CacheUtils.parseObject(key, res, type);
        } else {
            if (gbs != null) {
                rtn = gbs.invoke();
                // 取出的数据要set回去
                if (null != rtn) {
                    set(bizkey, nameSpace, rtn, gbs.getExpiredTime(),dbIndex);
                }
            }
        }
        return rtn;
    }

    @Override
    public String mset(final Map<String, String> bizkeyValues, final String nameSpace,final int dbIndex) {
        return this.performFunction("", new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.mset(CacheUtils.smapToArray(bizkeyValues, nameSpace));
            }
        });
    }

    @Override
    public List<String> mget(String[] bizkeys, String nameSpace,final int dbIndex) {
        final String[] key = CacheUtils.getKeyByNamespace(bizkeys, nameSpace);
        return this.performFunction("", new CallBack<List<String>>() {
            public List<String> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.mget(key);
            }
        });
    }

    @Override
    public <T> Long hsetObject(String bizkey, String nameSpace, String field, T value,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return hset(bizkey, nameSpace, field, CacheUtils.toJSONString(key, value),dbIndex);
    }

    @Override
    public <T> T hgetObject(final String bizkey, final String nameSpace, final String field, Class<T> value,
            final GetDataCallBack<T> gbs,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        String res = hget(bizkey, nameSpace, field, null,dbIndex);
        T rtn = null;
        if (StringUtils.isNotEmpty(res)) {
            rtn = CacheUtils.parseObject(key, res, value);
        } else {
            if (gbs != null) {
                rtn = gbs.invoke();
            }
            if (null != rtn) {
                hsetObject(bizkey, nameSpace, field, rtn,dbIndex);
            }
        }
        return rtn;
    }

    @Override
    public <T> T hgetObject(final String bizkey, final String nameSpace, final String field, TypeReference<T> type,
            final GetDataCallBack<T> gbs,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        String res = hget(bizkey, nameSpace, field, null,dbIndex);
        T rtn = null;
        if (StringUtils.isNotEmpty(res)) {
            rtn = CacheUtils.parseObject(key, res, type);
        } else {
            if (gbs != null) {
                rtn = gbs.invoke();
            }
            if (null != rtn) {
                hsetObject(bizkey, nameSpace, field, rtn,dbIndex);
            }
        }
        return rtn;
    }

    @Override
    public void hdelObject(String bizkey, String nameSpace,final int dbIndex, String... field) {
        hdel(bizkey, nameSpace,dbIndex, field);
    }

    @Override
    public <T> Map<String, T> hgetAllObjects(final String bizkey, final String nameSpace, final TypeReference<T> type,
            final GetDataCallBack<T> gbs,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Map<String, T>>() {
            public Map<String, T> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                try {
                    Map<String, String> all = jedis.hgetAll(key);
                    Map<String, T> allObjs = new HashMap<String, T>();
                    for (Entry<String, String> item : all.entrySet()) {
                        String _key = item.getKey();
                        T _value = CacheUtils.parseObject(key, item.getValue(), type);
                        allObjs.put(_key, _value);
                    }
                    return allObjs;
                } catch (Exception e) {
                    logger.error("key:" + key + "hgetAllObjects Exception：" + e.getMessage());
                }
                return null;
            }
        });
    }

    @Override
    public <T> Map<String, T> hgetAllObjects(final String bizkey, final String nameSpace, final Class<T> value,
            final GetDataCallBack<T> gbs,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<Map<String, T>>() {
            public Map<String, T> invoke(Jedis jedis) {
                try {
					if(0!=dbIndex){
						jedis.select(dbIndex);
					}
                    Map<String, String> all = jedis.hgetAll(key);
                    Map<String, T> allObjs = new HashMap<String, T>();
                    for (Entry<String, String> item : all.entrySet()) {
                        String _key = item.getKey();
                        T _value = CacheUtils.parseObject(key, item.getValue(), value);
                        allObjs.put(_key, _value);
                    }
                    return allObjs;
                } catch (Exception e) {
                    logger.error("key:" + key + "hgetAllObjects Exception：" + e.getMessage());
                }
                return null;
            }
        });
    }

    @Override
    public Long del(String[] bizkeys, String nameSpace,final int dbIndex) {
        final String[] keys = CacheUtils.getKeyByNamespace(bizkeys, nameSpace);
        return this.performFunction("", new CallBack<Long>() {
            public Long invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.del(keys);
            }
        });
    }

    @Override
    public <T> List<T> hvalsObject(final String bizkey, final String nameSpace, final TypeReference<T> type,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        List<String> res = hvals(bizkey, nameSpace,dbIndex);
        List<T> rtnList = null;
        T rtn = null;
        if (null != res && res.size() > 0) {
            rtnList = new ArrayList<T>();
            for (String tmp : res) {
                rtn = CacheUtils.parseObject(key, tmp, type);
                rtnList.add(rtn);
            }
        }
        return rtnList;
    }

    @Override
    public <T> List<T> hvalsObject(String bizkey, String nameSpace, Class<T> type,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        List<String> res = hvals(bizkey, nameSpace,dbIndex);
        List<T> rtnList = null;
        T rtn = null;
        if (null != res && res.size() > 0) {
            rtnList = new ArrayList<T>();
            for (String tmp : res) {
                rtn = CacheUtils.parseObject(key, tmp, type);
                rtnList.add(rtn);
            }
        }
        return rtnList;
    }

    @Override
    public List<Map.Entry<String, String>> hscan(final String bizkey, final String nameSpace, final String match,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        return this.performFunction(key, new CallBack<List<Map.Entry<String, String>>>() {
            public List<Map.Entry<String, String>> invoke(Jedis jedis) {
                try {
					if(0!=dbIndex){
						jedis.select(dbIndex);
					}
                    int cursor = 0;
                    ScanParams scanParams = new ScanParams();
                    scanParams.match(match);
                    scanParams.count(1000);
                    ScanResult<Map.Entry<String, String>> scanResult;
                    List<Map.Entry<String, String>> res = new ArrayList<Map.Entry<String, String>>();
                    do {
                        scanResult = jedis.hscan(key, String.valueOf(cursor), scanParams);
                        res.addAll(scanResult.getResult());
                        cursor = Integer.parseInt(scanResult.getStringCursor());
                    } while (cursor > 0);
                    return res;
                } catch (Exception ex) {
                    logger.error("hscan key:" + key + ",match:" + match + ",error:", ex);
                }
                return null;
            }
        });
    }

    @Override
    public Set<String> sscan(final String bizkey, final String nameSpace, final String match,final int dbIndex) {
        final String key = CacheUtils.getKeyByNamespace(bizkey, nameSpace);
        final String fmatch=CacheUtils.getKeyByNamespace(match, nameSpace);
        return this.performFunction(key, new CallBack<Set<String>>() {
            public Set<String> invoke(Jedis jedis) {
                try {
					if(0!=dbIndex){
						jedis.select(dbIndex);
					}
                    int cursor = 0;
                    ScanParams scanParams = new ScanParams();
                    scanParams.match(fmatch);
                    scanParams.count(1000);
                    ScanResult<String> scanResult;
                    Set<String> res = new HashSet<String>();
                    do {
                        scanResult = jedis.sscan(key, String.valueOf(cursor), scanParams);
                        res.addAll(scanResult.getResult());
                        cursor = Integer.parseInt(scanResult.getStringCursor());
                    } while (cursor > 0);
                    return res;
                } catch (Exception ex) {
                    logger.error("sscan key:" + key + ",match:" + fmatch + ",error:", ex);
                }
                return null;
            }
        });
    }
    
    @Override
    public Set<String> scan(final String nameSpace, final String match,final int dbIndex) {
    	final String fmatch=CacheUtils.getKeyByNamespace(match, nameSpace);
        return this.performFunction(null, new CallBack<Set<String>>() {
            public Set<String> invoke(Jedis jedis) {
                try {
					if(0!=dbIndex){
						jedis.select(dbIndex);
					}
                    int cursor = 0;
                    ScanParams scanParams = new ScanParams();
                    scanParams.match(fmatch);
                    scanParams.count(1000);
                    ScanResult<String> scanResult;
                    Set<String> res = new HashSet<String>();
                    do {
                        scanResult = jedis.scan(String.valueOf(cursor), scanParams);
                        res.addAll(scanResult.getResult());
                        cursor = Integer.parseInt(scanResult.getStringCursor());
                    } while (cursor > 0);
                    return res;
                } catch (Exception ex) {
                    logger.error("match:" + fmatch + ",error:", ex);
                }
                return null;
            }
        });
    }

    @Override
    public Object patternDel(final String pattern, final String nameSpace,final int dbIndex) {
        final String patternKey = CacheUtils.getKeyByNamespace(pattern, nameSpace);
        // final String script =
        // "return redis.call('del', unpack(redis.call('keys','"+patternKey+"')))";
        final String script = "local keys = redis.call('keys', '"
                + patternKey
                + "') \n for i=1,#keys,2000 do \n redis.call('del', unpack(keys, i, math.min(i+1999, #keys))) \n end \n return keys";
        return this.performFunction("", new CallBack<Object>() {
            public Object invoke(Jedis jedis) {
                try {
					if(0!=dbIndex){
						jedis.select(dbIndex);
					}
                    return jedis.eval(script);
                } catch (Exception e) {
                    logger.error("patternDel:" + patternKey + " eval Exception：" + e.getMessage());
                }
                return null;
            }
        });
    }

    @Override
    public Object eval(final String script, final List<String> bizkeys, final String nameSpace, final List<String> args,final int dbIndex) {
        final List<String> keys = CacheUtils.getKeyByNamespace(bizkeys, nameSpace);
        return this.performFunction("", new CallBack<Object>() {
            public Object invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.eval(script, keys, args);
            }
        });
    }
    
    @Override
    public Object eval(final String script, final int keyCount,final List<String> params, final String nameSpace, final int dbIndex) {
    	final String[] c_params = CacheUtils.getKeyArrByNamespace(params, nameSpace);
    	return this.performFunction("", new CallBack<Object>() {
            public Object invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
                return jedis.eval(script, keyCount, c_params);
            }
        });
    }


	@Override
	public Set<String> keys(final String pattern,final String nameSpace,final int dbIndex) {
		final String patternKey = CacheUtils.getKeyByNamespace(pattern, nameSpace);
        return this.performFunction("", new CallBack<Set<String>>() {
            public Set<String> invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
            	return jedis.keys(patternKey);
            }
        });
	}
	
	@Override
	public String rename(final String oldKey,final String newKey,final String nameSpace,final int dbIndex) {
		final String patternOldKey = CacheUtils.getKeyByNamespace(oldKey, nameSpace);
		final String patternNewKey = CacheUtils.getKeyByNamespace(newKey, nameSpace);
        return this.performFunction("", new CallBack<String>() {
            public String invoke(Jedis jedis) {
				if(0!=dbIndex){
					jedis.select(dbIndex);
				}
            	return jedis.rename(patternOldKey, patternNewKey);
            }
        });
	}

	@Override
	public <K, V> Map<K, V> pipelineWithResult(int dbIndex, PipelineCallBackWithResult<K,V> pipelineCallBack) {
		Map<K, V> result=new HashMap<>();
		Map<K, Response<V>> map=new HashMap<>();
		Jedis jedis = null;
        try {
            jedis = (Jedis) pool.getResource();
			if(0!=dbIndex){
				jedis.select(dbIndex);
			}
            Pipeline pipeline=jedis.pipelined();
            pipelineCallBack.doExecute(pipeline,map);
    		pipeline.sync();
    		for(Entry<K, Response<V>> entry:map.entrySet()) {
    			result.put(entry.getKey(), entry.getValue().get());
    		}
    		return result;
        } catch (JedisException e) {
            logger.error("jedis pool get resource error:{}", e);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
		return null;
	}
	
	@Override
	public void pipelineWithoutResult(int dbIndex, PipelineCallBackWithoutResult pipelineCallBack) {
		Jedis jedis = null;
        try {
            jedis = (Jedis) pool.getResource();
			if(0!=dbIndex){
				jedis.select(dbIndex);
			}
            Pipeline pipeline=jedis.pipelined();
            pipelineCallBack.doExecute(pipeline);
    		pipeline.sync();
        } catch (JedisException e) {
            logger.error("jedis pool get resource error:{}", e);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
	}

	@Override
	public Jedis getJedis() {
		return (Jedis) pool.getResource();
	}
	
	@Override
	public Long getTime() {
		Jedis jedis = null;
        try {
            jedis = (Jedis) pool.getResource();
            return Long.parseLong(jedis.time().get(0));
        } catch (JedisException e) {
            logger.error("jedis pool get resource error:{}", e);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return 0L;
	}
	
	public String scriptLoad(final String script) {
		return this.performFunction("", new CallBack<String>() {
            public String invoke(Jedis jedis) {
            	return jedis.scriptLoad(script);
            }
        });
	}
	
	public Boolean scriptExists(final String scriptSha) {
		return this.performFunction("", new CallBack<Boolean>() {
            public Boolean invoke(Jedis jedis) {
            	return jedis.scriptExists(scriptSha);
            }
        });
	}
	
	public Object evalsha(final String sha1, final List<String> keys, final List<String> args) {
		return this.performFunction("", new CallBack<Object>() {
            public Object invoke(Jedis jedis) {
            	return jedis.evalsha(sha1,keys,args);
            }
        });
	}
    
    public Object evalsha(final String sha1, final int keyCount, final String... params) {
    	return this.performFunction("", new CallBack<Object>() {
            public Object invoke(Jedis jedis) {
            	return jedis.evalsha(sha1,keyCount,params);
            }
        });
    }
	
}
