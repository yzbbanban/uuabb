package com.uuabb.common;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * token
 */
public class TokenCache {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
    public static final String TOKEN_PREFIX = "token_";
    //token 缓存
    public static LoadingCache<String, String> localCache = CacheBuilder.newBuilder()
            .initialCapacity(1000)//初始容量大小
            .maximumSize(10000)//最大长度
            .expireAfterAccess(12, TimeUnit.HOURS)//存活时间
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    //设置 token
    public static void setKey(String key, String value) {
        localCache.put(key, value);
    }

    //获取 token
    public static String getKey(String key) {
        String value = null;
        try {
            //获取的 token
            value = localCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
