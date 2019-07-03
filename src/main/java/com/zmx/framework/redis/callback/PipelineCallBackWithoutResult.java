package com.zmx.framework.redis.callback;

import redis.clients.jedis.Pipeline;

/** * 
author  zhongjie
date 创建时间：2017年11月11日 上午10:37:11
version 1.0
parameter 
since 
return  
*/
public interface PipelineCallBackWithoutResult {
	
	public  void doExecute(Pipeline pipeline);
}
