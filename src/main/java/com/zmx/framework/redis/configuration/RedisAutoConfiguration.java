package com.zmx.framework.redis.configuration;

import com.zmx.framework.redis.client.IRedisClient;
import com.zmx.framework.redis.client.impl.RedisClientImpl;
import com.zmx.framework.redis.exception.RedisClientException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author: zhongjie
 * date: 2018/10/30 0030
 * time: 14:04
 * description:
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfiguration {

    @Autowired
    private RedisProperties properties;

    @Bean
    public IRedisClient redisClient(){
        RedisClientImpl redisClient=new RedisClientImpl();
        RedisProperties.Sentinel sentinel= properties.getSentinel();
        if(null==sentinel||!sentinel.isUseSentinel()){
            redisClient.setPool(jedisPool());
        }else{
            JedisSentinelPool jedisSentinelPool=jedisSentinelPool();
            if(null==jedisSentinelPool){
                throw new RedisClientException("redis host and sentinel set is empty");
            }
            redisClient.setPool(jedisSentinelPool);
        }
        return redisClient;
    }

    @Bean
    public JedisPool jedisPool(){
        JedisPool jedisPool=new JedisPool(
                jedisPoolConfig()
                ,properties.getHost()
                ,properties.getPort()
                ,properties.getTimeout()
                ,properties.getPassword()
        );
        return jedisPool;
    }

    @Bean()
    @ConditionalOnProperty(prefix = "spring.redis.sentinel",name ="useSentinel",havingValue = "true")
    public JedisSentinelPool jedisSentinelPool(){
        String masterName=properties.getSentinel().getMaster();
        String nodes=properties.getSentinel().getNodes();
        if(StringUtils.isNotBlank(masterName)&&StringUtils.isNotBlank(nodes)){
            String[] sentinelSet= nodes.split(",");
            return new JedisSentinelPool(masterName,new HashSet<>(Arrays.asList(sentinelSet)),jedisPoolConfig(),properties.getTimeout(),properties.getPassword());
        }
        return null;
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        if(null!=properties.getPool()){
            jedisPoolConfig.setMaxTotal(properties.getPool().getMaxActive());
            jedisPoolConfig.setMaxIdle(properties.getPool().getMaxIdle());
            jedisPoolConfig.setMinIdle(properties.getPool().getMinIdle());
        }
        return jedisPoolConfig;

    }





}
