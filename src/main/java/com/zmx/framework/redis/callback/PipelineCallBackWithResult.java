package com.zmx.framework.redis.callback;

import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.Map;

/** * 
author  zhongjie
date 创建时间：2017年11月11日 上午10:37:11
version 1.0
parameter 
since 
return  
*/
public  interface PipelineCallBackWithResult<K,V> {
	public  void doExecute(Pipeline pipeline,Map<K,Response<V>> map);
}
