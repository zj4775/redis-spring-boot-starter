package com.zmx.framework.redis.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zmx.framework.redis.exception.RedisClientException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.SafeEncoder;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/** * 
author  zhongjie
date 创建时间：2017年9月7日 上午10:54:21
version 1.0
parameter 
since
return 
*/
public class CacheUtils {

    /**
     * log
     */
    private static Logger logger = LoggerFactory.getLogger(CacheUtils.class);

    private static String DOT = ".";
    
    private static String COLON=":";

    /**
     * 5000 constant
     */
    private static final int FIVE = 5000;

    public static byte[][] blistToArray(String... serializables) {
        byte[][] paramByte = null;
        if (serializables != null && serializables.length > 0) {
            paramByte = new byte[serializables.length][0];
            for (int i = 0; i < serializables.length; i++) {
                paramByte[i] = encode(serializables[i]);
            }
        }
        return paramByte;
    }

    public static byte[][] listToArray(Serializable... serializables) {
        byte[][] paramByte = null;
        if (serializables != null && serializables.length > 0) {
            paramByte = new byte[serializables.length][0];
            for (int i = 0; i < serializables.length; i++) {
                paramByte[i] = encode(serializables[i]);
            }
        }
        return paramByte;
    }

    public static String[] slistToArray(Serializable... fields) {
        if (fields != null) {
            String[] returnAry = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                returnAry[i] = cast(fields[i]);
            }
            return returnAry;
        }
        return null;
    }

    public static String cast(Serializable value) {
        try {
            return (String) value;
        } catch (ClassCastException e) {
            throw new RedisClientException("Cast to String error.", e);
        }
    }

    public static String[] smapToArray(Map<String, String> map) {
        String[] paramByte = null;
        if (map != null && map.size() > 0) {
            paramByte = new String[map.size() * 2];
            Iterator<Entry<String, String>> it = map.entrySet().iterator();
            int index = 0;
            while (it.hasNext()) {
                Entry<String, String> entry = it.next();
                paramByte[index++] = entry.getKey();
                paramByte[index++] = entry.getValue();
            }
        }
        return paramByte;
    }

    public static byte[][] mapToArray(Map<byte[], byte[]> map) {
        byte[][] paramByte = null;
        if (map != null && map.size() > 0) {
            paramByte = new byte[map.size() * 2][0];
            Iterator<Entry<byte[], byte[]>> it = map.entrySet().iterator();
            int index = 0;
            while (it.hasNext()) {
                Entry<byte[], byte[]> entry = it.next();
                paramByte[index++] = entry.getKey();
                paramByte[index++] = entry.getValue();
            }
        }
        return paramByte;
    }

    public static Serializable decode(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        Serializable object = null;
        ObjectInputStream objectIS = null;
        ByteArrayInputStream byteIS = null;
        try {
            byteIS = new ByteArrayInputStream(bytes);
            objectIS = new ObjectInputStream(byteIS);
            object = (Serializable) objectIS.readObject();
        } catch (IOException e) {
            try {
                object = SafeEncoder.encode(bytes);
            } catch (JedisException e1) {
                throw new RedisClientException(e1);
            }
        } catch (ClassNotFoundException e) {
            try {
                object = SafeEncoder.encode(bytes);
            } catch (JedisException e1) {
                throw new RedisClientException(e1);
            }
        } finally {
            if (byteIS != null) {
                try {
                    byteIS.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            if (objectIS != null) {
                try {
                    objectIS.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return object;
    }

    public static byte[] encode(Serializable object) {
        ByteArrayOutputStream byteOS = null;
        ObjectOutputStream objectOS = null;

        byte[] bytes = null;
        if (object instanceof Integer) {
            bytes = SafeEncoder.encode(((Integer) object).toString());
        } else if (object instanceof Long) {
            bytes = SafeEncoder.encode(((Long) object).toString());
        }

        if (bytes != null) {
            return bytes;
        }
        try {
            byteOS = new ByteArrayOutputStream();
            objectOS = new ObjectOutputStream(byteOS);
            objectOS.writeObject(object);
            bytes = byteOS.toByteArray();
        } catch (IOException e) {
            throw new RedisClientException(e);
        } finally {
            if (objectOS != null) {
                try {
                    objectOS.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            if (byteOS != null) {
                try {
                    byteOS.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return bytes;
    }

    public static Map<String, String> toSMap(Map<String, String> map) {
        if (map != null) {
            Map<String, String> returnMap = new HashMap<String, String>();
            Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> entry = iterator.next();
                returnMap.put(entry.getKey(), cast(entry.getValue()));
            }
            return returnMap;
        }
        return null;
    }

    public static String toJSONString(String key, Object object) {
        String res = JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
        logger.debug("key:" + key + ",toJSONString length:" + res.length());
        return res;
    }

    public static <T> T parseObject(String key, String text, Class<T> clazz) {
        logger.debug("key:" + key + ",parseObject length:" + text.length());
        return JSON.parseObject(text, clazz);
    }

    public static <T> T parseObject(String key, String text, TypeReference<T> type) {
        logger.debug("key:" + key + ",parseObject length:" + text.length());
        return JSON.parseObject(text, type);
    }

    public static String getSizeType(Object object) {
        StringBuffer logSb = new StringBuffer();
        appendStr2log(object, logSb, " return");
        return logSb.toString();
    }

    public static String getSendCommondSizeType(Object[] args) {
        StringBuffer logSb = new StringBuffer();
        for (int i = 2; i < args.length; i++) {
            if (args[i] instanceof byte[]) {
                byte[] commandByte = (byte[]) args[i];
                logSb.append(" param type: byte[] length:").append(commandByte.length);
            } else {
                logSb.append(" param type: unknown ");
            }
        }
        return logSb.toString();
    }

    public static String getKeyByNamespace(String key, String nameSpace) {
        if (StringUtils.isNotBlank(nameSpace)) {
            return nameSpace + COLON + key;
        }
        return key;
    }

    public static void appendStr2log(Object object, StringBuffer logSb, String strPre) {
        logSb.append(strPre);
        if (object instanceof String) {
            String s = (String) object;
            logSb.append(" type: String length:").append(s.length());
        } else if (object instanceof byte[]) {
            byte[] b = (byte[]) object;
            logSb.append(" type: byte[] length:").append(b.length);
        } else if (object instanceof List) {
            List<?> l = (List<?>) object;
            logSb.append(" type: List size:").append(l.size());
        } else if (object instanceof Map) {
            Map m = (Map) object;
            logSb.append(" type: Map size:").append(m.size());
        } else if (object instanceof Set) {
            Set m = (Set) object;
            logSb.append(" type: Set size:").append(m.size());
        } else if (object instanceof Integer) {
            // int value = ((Integer) object).intValue();
            logSb.append(" type: Integer ");
        } else if (object instanceof Double) {
            // double d = ((Double) object).doubleValue();
            logSb.append(" type: Double ");
        } else if (object instanceof Float) {
            // float f = ((Float) object).floatValue();
            logSb.append(" type: Float ");
        } else if (object instanceof Long) {
            // long l = ((Long) object).longValue();
            logSb.append(" type: Long ");
        } else if (object instanceof Boolean) {
            // boolean b = ((Boolean) object).booleanValue();
            logSb.append(" type: Boolean ");
        } else {
            logSb.append(" type: other size unknown");
        }
    }

    public static String getObjectType(Object object) {
        String res = "";
        if (object instanceof String) {
            res = "String";
        } else if (object instanceof byte[]) {
            res = "byte[]";
        } else if (object instanceof List) {
            res = "List";
        } else if (object instanceof Map) {
            res = "Map";
        } else if (object instanceof Set) {
            res = "Set";
        } else if (object instanceof Integer) {
            res = "Integer";
        } else if (object instanceof Double) {
            res = "Double";
        } else if (object instanceof Float) {
            res = "Float";
        } else if (object instanceof Long) {
            res = "Long";
        } else if (object instanceof Boolean) {
            res = "Boolean";
        } else {
            res = "other";
        }
        return res;
    }

    public static String getObjectSize(Object object) {
        Integer res = 0;
        if (object instanceof String) {
            String s = (String) object;
            res = s.length();
        } else if (object instanceof byte[]) {
            byte[] b = (byte[]) object;
            res = b.length;
        } else if (object instanceof List) {
            List<?> l = (List<?>) object;
            res = l.size();
        } else if (object instanceof Map) {
            Map m = (Map) object;
            res = m.size();
        } else if (object instanceof Set) {
            Set m = (Set) object;
            res = m.size();
        } else if (object instanceof Integer) {
            res = Integer.SIZE;
        } else if (object instanceof Double) {
            res = Double.SIZE;
        } else if (object instanceof Float) {
            res = Float.SIZE;
        } else if (object instanceof Long) {
            res = Long.SIZE;
        } else if (object instanceof Boolean) {
            // 真的是1...
            res = 1;
        } else {
            // res = JSON.toJSONString(object).length();
            // 性能开销太大
            return "unknown";
        }
        return res + "";
    }

    public static String[] smapToArray(Map<String, String> map, String nameSpace) {
        String[] paramByte = null;
        if (map != null && map.size() > 0) {
            paramByte = new String[map.size() * 2];
            Iterator<Entry<String, String>> it = map.entrySet().iterator();
            int index = 0;
            while (it.hasNext()) {
                Entry<String, String> entry = it.next();
                paramByte[index++] = getKeyByNamespace(entry.getKey(), nameSpace);
                paramByte[index++] = entry.getValue();
            }
        }
        return paramByte;
    }

    public static String[] getKeyByNamespace(String[] bizkeys, String nameSpace) {
        String[] paramByte = null;
        if (bizkeys != null && bizkeys.length > 0) {
            paramByte = new String[bizkeys.length];
            for (int i = 0; i < bizkeys.length; i++) {
                paramByte[i] = getKeyByNamespace(bizkeys[i], nameSpace);
            }
        }
        return paramByte;
    }

    public static List<String> getKeyByNamespace(List<String> bizkeys, String nameSpace) {
        List<String> keys = new ArrayList<String>();
        String key = null;
        for (String bizkey : bizkeys) {
            key = getKeyByNamespace(bizkey, nameSpace);
            keys.add(key);
        }
        return keys;
    }
    public static String[] getKeyArrByNamespace(List<String> bizkeys, String nameSpace) {
    	if(CollectionUtils.isEmpty(bizkeys)) {
    		return null;
    	}
    	String[] keys = new String[bizkeys.size()];
    	String key = null;
    	for (int i=0;i<bizkeys.size();i++) {
    	    String bizkey=bizkeys.get(i);
    		key = getKeyByNamespace(bizkey, nameSpace);
    		keys[i]=key;
    	}
    	return keys;
    }

    public static String[] getKeyByNamespace(Map map, String nameSpace) {
        String[] paramByte = null;
        if (map != null && map.size() > 0) {
            paramByte = new String[map.size()];
            Iterator<Entry<String, String>> it = map.entrySet().iterator();
            int index = 0;
            while (it.hasNext()) {
                Entry<String, String> entry = it.next();
                paramByte[index++] = getKeyByNamespace(entry.getKey(), nameSpace);
            }
        }
        return paramByte;
    }

    public static String getBizKeyWithNS(String keySplit, String nameSpace) {
        if (!StringUtils.isBlank(nameSpace)) {
            if (keySplit.indexOf(nameSpace) != -1) {
                return keySplit.substring(nameSpace.length() + 1, keySplit.length());
            }
        }
        return keySplit;
    }

    public static byte[] objectToByteArray(Object obj) throws IOException {
        if (obj == null) {
            return null;
        }
        ObjectOutputStream os = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(FIVE);
        os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
        os.flush();
        os.writeObject(obj);
        os.flush();
        byte[] sendBuf = byteStream.toByteArray();
        os.close();
        return sendBuf;
    }

    public static Object byteArrayToObject(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(bis));
            obj = ois.readObject();
            bis.close();
            ois.close();
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
        return obj;
    }

    public static long nextDelayTime(int interval) {
        long now = System.currentTimeMillis() / 1000;
        long next = (now / interval + 1) * interval;
        long delay = next - now;
        if (delay <= 0) {
            return interval - delay;
        } else {
            return delay;
        }
    }

}
