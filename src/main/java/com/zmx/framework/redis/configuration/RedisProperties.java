package com.zmx.framework.redis.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author: zhongjie
 * date: 2018/10/30 0030
 * time: 14:39
 * description:
 */
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisProperties {

	/**
	 * Database index used by the connection factory.
	 */
	private int database = 0;

	/**
	 * Redis server host.
	 */
	private String host = "localhost";

	/**
	 * Login password of the redis server.
	 */
	private String password="";

	/**
	 * Redis server port.
	 */
	private int port = 6379;

	/**
	 * Connection timeout in milliseconds.
	 */
	private int timeout=120000;

	private Pool pool;

	private Sentinel sentinel;


	/**
	 * Pool properties.
	 */
	@Data
	public static class Pool {

		/**
		 * Max number of "idle" connections in the pool. Use a negative value to indicate
		 * an unlimited number of idle connections.
		 */
		private int maxIdle = 8;

		/**
		 * Target for the minimum number of idle connections to maintain in the pool. This
		 * setting only has an effect if it is positive.
		 */
		private int minIdle = 0;

		/**
		 * Max number of connections that can be a+++++++++++++++++++++llocated by the pool at a given time.
		 * Use a negative value for no limit.
		 */
		private int maxActive = 8;

		/**
		 * Maximum amount of time (in milliseconds) a connection allocation should block
		 * before throwing an exception when the pool is exhausted. Use a negative value
		 * to block indefinitely.
		 */
		private int maxWait = -1;

	

	}

	@Data
	public static class Sentinel {

	    private boolean useSentinel;

		/**
		 * Name of Redis server.
		 */
		private String master;

		/**
		 * Comma-separated list of host:port pairs.
		 */
		private String nodes;


	}

	

	

}
