package com.zjt.http;

import java.util.HashMap;

public class HTTPRequest {
    private HashMap<String, String> mHashMap;
    private String method = "", uri = "";
    private byte[] mContent;
    private boolean isGzip, isKeepAlive;

    public HTTPRequest(){
        mHashMap = new HashMap<>();
    }

    public HTTPRequest(byte[] b){
        this(new String(b));
    }

    public HTTPRequest(String header){
        mHashMap = new HashMap<>();
        int index = header.indexOf("\r\n\r\n");
        header = header.substring(0, index + 1);
        index = header.indexOf(" ");
        // 请求的方法
        method = header.substring(0, index);
        // 请求的资源
        int newIndex = header.indexOf(" ", index + 1);
        uri = header.substring(index + 1, newIndex);
        header = header.substring(header.indexOf("\r\n", newIndex + 1) + 2);
        // 头部其他字段
        String[] headers = header.split("\r\n");
        if(headers != null){
            for(String h: headers){
                String[] str = h.split(":");
                if(str != null){
                    mHashMap.put(str[0].trim(), str[1].trim());
                    if(str[0].equalsIgnoreCase("Accept-Encoding") && str[1].contains("gzip")){
                        isGzip = true;
                    }
                    if(str[0].equalsIgnoreCase("Connection") && str[1].equalsIgnoreCase("keep-alive")){
                        isKeepAlive = true;
                    }
                }
            }
        }
    }

    public boolean gzip(){
        return isGzip;
    }

    public boolean keepAlive(){
        return isKeepAlive;
    }

    public byte[] content(){
        return mContent;
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