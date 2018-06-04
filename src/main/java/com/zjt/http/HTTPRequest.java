package com.zjt.http;

import java.util.HashMap;
import java.io.InputStream;
import java.io.IOException;

public class HTTPRequest {
    private HashMap<String, String> mHashMap;
    private String method = "", uri = "";
    private byte[] data;
    private boolean isGzip, isKeepAlive;

    public HTTPRequest(InputStream inputStream){
        mHashMap = new HashMap<>();
        byte[] read = new byte[2048];
        
    }

    public boolean gzip(){
        return isGzip;
    }

    public boolean keepAlive(){
        return isKeepAlive;
    }

    public String get(String key){
        return mHashMap.get(key);
    }

    public String uri(){
        return uri;
    }

    public String method(){
        return method;
    }
}