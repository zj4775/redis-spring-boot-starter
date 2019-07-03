package com.zmx.framework.redis.callback;
/** * 
author  zhongjie
date 创建时间：2017年9月7日 上午10:47:48
version 1.0
parameter 
since
return 
*/
public interface GetDataCallBack<R> {

    int getExpiredTime();

    R invoke();
}
