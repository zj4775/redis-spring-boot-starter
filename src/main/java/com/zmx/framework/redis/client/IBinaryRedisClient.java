package com.zmx.framework.redis.client;

import java.util.Map;

/** * 
author  zhongjie
date 创建时间：2017年9月7日 上午10:53:51
version 1.0
parameter 
since
return 
*/
public interface IBinaryRedisClient extends IRedisClient {

    public String setObject(final String bizkey, final String nameSpace, final Object value, final int expire);

    Object getObject(final String bizkey, final String nameSpace);

    Long hsetObject(final String bizkey, final String nameSpace, final String field, final Object value);

    Object hgetObject(final String bizkey, final String nameSpace, final String field);

    void hdelObject(final String bizkey, final String nameSpace, final String... field);

    Map<String, Object> hgetAllObjects(final String bizkey, final String nameSpace);
}