package com.jxlg.app.store.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouboxi
 * @create 2017-12-02 10:21
 **/
public class TokenCache {

    public static final String TOKEN_PREFIX="token_";

    //LRU最近最少使用算法  通过谷歌创建创建一个12小时的指令

    private static LoadingCache<String, String> localCache= CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
                //默认的数据加载实现,当调用get取值的时候,如果key没有对应的值,就调用这个方法进行加载.
            });
    public static void setKey(String key,String value){
        localCache.put(key,value);
    }

    public static String getKey(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        }catch (Exception e){

        }
        return null;
    }
}

