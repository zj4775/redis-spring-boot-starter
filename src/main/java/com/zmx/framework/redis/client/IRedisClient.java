package com.zmx.framework.redis.client;

import com.alibaba.fastjson.TypeReference;
import com.zmx.framework.redis.callback.GetDataCallBack;
import com.zmx.framework.redis.callback.PipelineCallBackWithResult;
import com.zmx.framework.redis.callback.PipelineCallBackWithoutResult;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

/** * 
author  zhongjie
date 创建时间：2017年9月7日 上午10:52:51
version 1.0
parameter 
since
return 
*/
public interface IRedisClient {


    public String set(final String bizkey, final String nameSpace, final String value, final int expire,final int dbIndex);


    public String set(final String bizkey, final String nameSpace, final String value, final String nxxx,
            final String expx, final long expire,final int dbIndex);


    public String get(final String bizkey, final String nameSpace, final GetDataCallBack<String> gbs, final int dbIndex);


    public Boolean exists(final String bizkey, final String nameSpace,final int dbIndex);


    public String type(final String bizkey, final String nameSpace,final int dbIndex);


    public Long expire(final String bizkey, final String nameSpace, final int seconds,final int dbIndex);


    public Long expireAt(final String bizkey, final String nameSpace, final long unixTime,final int dbIndex);


    public Long ttl(final String bizkey, final String nameSpace,final int dbIndex);


    public Boolean setbit(final String bizkey, final String nameSpace, final long offset, final boolean value,final int dbIndex);


    public Boolean getbit(final String bizkey, final String nameSpace, final long offset,final int dbIndex);


    public Long setrange(final String bizkey, final String nameSpace, final long offset, final String value,final int dbIndex);


    public String getrange(final String bizkey, final String nameSpace, final long startOffset, final long endOffset,final int dbIndex);


    public String getSet(final String bizkey, final String nameSpace, final String value,final int dbIndex);


    public Long setnx(final String bizkey, final String nameSpace, final String value,final int dbIndex);


    public <T> String setex(final String bizkey, final String nameSpace, final int seconds, final T value,final int dbIndex);


    public String setex(final String bizkey, final String nameSpace, final int seconds, final String value,final int dbIndex);


    public Long decrBy(final String bizkey, final String nameSpace, final long integer,final int dbIndex);


    public Long decr(final String bizkey, final String nameSpace,final int dbIndex);


    public Long incrBy(final String bizkey, final String nameSpace, final long integer,final int dbIndex);


    public Long incr(final String bizkey, final String nameSpace,final int dbIndex);


    public String substr(final String bizkey, final String nameSpace, final int start, final int end,final int dbIndex);


    public Long hset(final String bizkey, final String nameSpace, final String field, final String value,final int dbIndex);


    public String hget(final String bizkey, final String nameSpace, final String field,
            final GetDataCallBack<String> gbs,final int dbIndex);


    public Long hsetnx(final String bizkey, final String nameSpace, final String field, final String value,final int dbIndex);


    public String hmset(final String bizkey, final String nameSpace, final Map<String, String> hash,final int dbIndex);


    public List<String> hmget(final String bizkey, final String nameSpace, final int dbIndex,final String... fields);


    public Long hincrBy(final String bizkey, final String nameSpace, final String field, final long value,final int dbIndex);


    public Boolean hexists(final String bizkey, final String nameSpace, final String field,final int dbIndex);


    public Long hdel(final String bizkey, final String nameSpace,final int dbIndexfinal, String... field);


    public Long hlen(final String bizkey, final String nameSpace,final int dbIndex);


    public Set<String> hkeys(final String bizkey, final String nameSpace,final int dbIndex);


    public List<String> hvals(final String bizkey, final String nameSpace,final int dbIndex);


    public <T> List<T> hvalsObject(final String bizkey, final String nameSpace, TypeReference<T> type,final int dbIndex);


    public <T> List<T> hvalsObject(final String bizkey, final String nameSpace, Class<T> type,final int dbIndex);


    public Map<String, String> hgetAll(final String bizkey, final String nameSpace,final int dbIndex);


    public Long llen(final String bizkey, final String nameSpace,final int dbIndex);


    public List<String> lrange(final String bizkey, final String nameSpace, final long start, final long end,final int dbIndex);


    public String ltrim(final String bizkey, final String nameSpace, final long start, final long end,final int dbIndex);


    public String lindex(final String bizkey, final String nameSpace, final long index,final int dbIndex);


    public String lset(final String bizkey, final String nameSpace, final long index, final String value,final int dbIndex);


    public Long lrem(final String bizkey, final String nameSpace, final long count, final String value,final int dbIndex);


    public String lpop(final String bizkey, final String nameSpace,final int dbIndex);


    public String rpop(final String bizkey, final String nameSpace,final int dbIndex);


    public Long sadd(final String bizkey, final String nameSpace,final int dbIndex, final String... member);


    public Set<String> smembers(final String bizkey, final String nameSpace,final int dbIndex);


    public Long srem(final String bizkey, final String nameSpace,final int dbIndex, final String... member);


    public String spop(final String bizkey, final String nameSpace,final int dbIndex);


    public Long scard(final String bizkey, final String nameSpace,final int dbIndex);


    public Boolean sismember(final String bizkey, final String nameSpace, final String member,final int dbIndex);


    public String srandmember(final String bizkey, final String nameSpace,final int dbIndex);

    public List<String> srandmember(String bizkey, String nameSpace, int count,final int dbIndex);


    public Long zadd(final String bizkey, final String nameSpace, final double score, final String member,final int dbIndex);


    public Set<String> zrange(final String bizkey, final String nameSpace, final long start, final long end,final int dbIndex);


    public Long zrem(final String bizkey, final String nameSpace,final int dbIndex, final String... member);


    public Double zincrby(final String bizkey, final String nameSpace, final double score, final String member,final int dbIndex);


    public Long zrank(final String bizkey, final String nameSpace, final String member,final int dbIndex);


    public Long zrevrank(final String bizkey, final String nameSpace, final String member,final int dbIndex);


    public Set<String> zrevrange(final String bizkey, final String nameSpace, final long start, final long end,final int dbIndex);


    public Set<Tuple> zrangeWithScores(final String bizkey, final String nameSpace, final long start, final long end,final int dbIndex);


    public Set<Tuple> zrevrangeWithScores(final String bizkey, final String nameSpace, final long start, final long end,final int dbIndex);


    public Long zcard(final String bizkey, final String nameSpace,final int dbIndex);


    public Double zscore(final String bizkey, final String nameSpace, final String member,final int dbIndex);


    public List<String> sort(final String bizkey, final String nameSpace,final int dbIndex);


    public List<String> sort(final String bizkey, final String nameSpace, final SortingParams sortingParameters,final int dbIndex);


    public Long zcount(final String bizkey, final String nameSpace, final double min, final double max,final int dbIndex);


    public Set<String> zrangeByScore(final String bizkey, final String nameSpace, final double min, final double max,final int dbIndex);


    public Set<String> zrevrangeByScore(final String bizkey, final String nameSpace, final double max, final double min,final int dbIndex);


    public Set<String> zrangeByScore(final String bizkey, final String nameSpace, final double min, final double max,
            final int offset, final int count,final int dbIndex);


    public Set<String> zrevrangeByScore(final String bizkey, final String nameSpace, final double max,
            final double min, final int offset, final int count,final int dbIndex);


    public Long zremrangeByRank(final String bizkey, final String nameSpace, final long start, final long end,final int dbIndex);


    public Long zremrangeByScore(final String bizkey, final String nameSpace, final double start, final double end,final int dbIndex);


    public Set<Tuple> zrangeByScoreWithScores(final String bizkey, final String nameSpace, final double min,
            final double max,final int dbIndex);


    public Set<Tuple> zrevrangeByScoreWithScores(final String bizkey, final String nameSpace, final double max,
            final double min,final int dbIndex);


    public Set<Tuple> zrangeByScoreWithScores(final String bizkey, final String nameSpace, final double min,
            final double max, final int offset, final int count,final int dbIndex);

    public Set<Tuple> zrevrangeByScoreWithScores(final String bizkey, final String nameSpace, final double max,
            final double min, final int offset, final int count,final int dbIndex);


    public Long linsert(final String bizkey, final String nameSpace, final LIST_POSITION where, final String pivot,
            final String value,final int dbIndex);


    public Long del(final String bizkey, final String nameSpace,final int dbIndex);


    public Long del(final String[] bizkey, final String nameSpace,final int dbIndex);


    public Long lpush(final String bizkey, final String nameSpace,final int dbIndex, final String... fields);


    public Long rpush(final String bizkey, final String nameSpace,final int dbIndex, final String... fields);

    public String mset(Map<String, String> keyValues, String nameSpace,final int dbIndex);

    public List<String> mget(String[] keys, String nameSpace,final int dbIndex);


    public Boolean setbit(final String bizkey, final String nameSpace, final long offset, final String value,final int dbIndex);


    public Long strlen(final String bizkey, final String nameSpace,final int dbIndex);


    public String echo(final String string,final int dbIndex);

    public Long bitcount(final String bizkey, final String nameSpace,final int dbIndex);

    public Long bitcount(final String bizkey, final String nameSpace, final long start, final long end,final int dbIndex);

    public <T> String set(final String bizkey, final String nameSpace, final T value, final int time,final int dbIndex);

    public <T> T get(final String bizkey, final String nameSpace, Class<T> value, final GetDataCallBack<T> gbs,final int dbIndex);

    public <T> T get(final String bizkey, final String nameSpace, TypeReference<T> type, final GetDataCallBack<T> gbs,final int dbIndex);

    public <T> Long hsetObject(final String bizkey, final String nameSpace, final String field, final T value,final int dbIndex);

    public <T> T hgetObject(final String bizkey, final String nameSpace, final String field, Class<T> value,
            final GetDataCallBack<T> gbs,final int dbIndex);

    public <T> T hgetObject(final String bizkey, final String nameSpace, final String field, TypeReference<T> type,
            final GetDataCallBack<T> gbs,final int dbIndex);

    public void hdelObject(final String bizkey, final String nameSpace,final int dbIndex, final String... field);

    public <T> Map<String, T> hgetAllObjects(final String bizkey, final String nameSpace, Class<T> value,
            final GetDataCallBack<T> gbs,final int dbIndex);

    public <T> Map<String, T> hgetAllObjects(final String bizkey, final String nameSpace, TypeReference<T> type,
            final GetDataCallBack<T> gbs,final int dbIndex);

    public List<Map.Entry<String, String>> hscan(final String bizkey, final String nameSpace, final String match,final int dbIndex);

    public Set<String> sscan(final String bizkey, final String nameSpace, final String match,final int dbIndex);
    
    public Set<String> scan(final String nameSpace, final String match,final int dbIndex);

    public Object patternDel(final String pattern, final String nameSpace,final int dbIndex);

    public Object eval(final String script, final List<String> keys, final String nameSpace, final List<String> args,final int dbIndex);
    
    public Object eval(final String script, final int keyCount,final List<String> params, final String nameSpace, final int dbIndex);

    public <K,V> Map<K,V> pipelineWithResult(final int dbIndex,PipelineCallBackWithResult<K,V> pipelineCallBack);
    
    public void pipelineWithoutResult(final int dbIndex,PipelineCallBackWithoutResult pipelineCallBack);
    
    public Set<String> keys(final String pattern,final String nameSpace,final int dbIndex);
    
    public String rename(final String oldKey,final String newKey,final String nameSpace, int dbIndex);
    
    public Jedis getJedis();
    
    public Long getTime();
    
    public String scriptLoad(String script);
    
    public Boolean scriptExists(String scriptSha);
    
    public Object evalsha(String sha1, List<String> keys, List<String> args);
    
    public Object evalsha(String sha1, int keyCount, String... params);
    
}
