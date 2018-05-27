package me.jfenn.feedage.lib.utils;

public interface CacheInterface {

    void putCache(String key, String data);

    String getCache(String key);

}
