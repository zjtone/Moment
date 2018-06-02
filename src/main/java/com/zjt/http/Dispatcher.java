package com.zjt.http;

import java.util.HashMap;

public class Dispatcher {
    private static Dispatcher instance = null;
    private HashMap<String, Api> mGetApiMap, mPostApiMap;

    private Dispatcher(){
        mGetApiMap = new HashMap<>();
        mPostApiMap = new HashMap<>();
    }

    public static Dispatcher getInstance() {
        if(instance == null)
            instance = new Dispatcher();
        return instance;
    }

    public Dispatcher get(String key, Api value){
        if(key == null || value == null)throw new NullPointerException("api: key or value cannot be null");
        mGetApiMap.put(key, value);
        return this;
    }

    public Dispatcher post(String key, Api value){
        if(key == null || value == null)throw new NullPointerException("api: key or value cannot be null");
        mPostApiMap.put(key, value);
        return this;
    }

    public String toString(){
        return "GET: " + mGetApiMap.toString() + "\nPOST: " + mPostApiMap.toString();
    }

    public Api get(String key){
        return mGetApiMap.get(key);
    }

    public Api post(String key){
        return mPostApiMap.get(key);
    }
}