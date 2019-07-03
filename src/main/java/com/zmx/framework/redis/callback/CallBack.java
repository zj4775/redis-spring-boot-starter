package com.zmx.framework.redis.callback;

import redis.clients.jedis.Jedis;

/** * 
author  zhongjie
date 创建时间：2017年9月7日 上午10:45:07
version 1.0
parameter 
since
return 
*/
public interface CallBack<T> {

	T invoke(Jedis jedis);
	
}
