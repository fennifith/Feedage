package me.jfenn.feedage.utils;

import android.content.SharedPreferences;

import me.jfenn.feedage.lib.utils.CacheInterface;

public class HackyCacheInterface implements CacheInterface {

    private SharedPreferences prefs;

    public HackyCacheInterface(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    @Override
    public void putCache(String key, String data) {
        prefs.edit().putString(key, data).apply();
    }

    @Override
    public String getCache(String key) {
        return prefs.getString(key, null);
    }
}
