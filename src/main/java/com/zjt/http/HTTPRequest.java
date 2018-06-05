package com.zjt.http;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.io.InputStream;
import java.io.IOException;

public class HTTPRequest {
    private HashMap<String, String> mHashMap;
    private String method = "", uri = "";
    private byte[] mContent;
    private boolean isGzip, isKeepAlive;

    public HTTPRequest(InputStream inputStream)throws IOException {
        mHashMap = new HashMap<>();
        int len = -1, totalLength = 2048, length = 0;
        byte[] read = new byte[totalLength];
        int divider = -1;
        for(; length < totalLength && (len = inputStream.read(read, length, totalLength - length)) > 0 ; ){
            divider = divider(read, length - 4);
            length += len;
            if(divider > 0)break;
        }
        // 解析http首部
        String header = new String(read);
        int index = header.indexOf(" ");
        method = header.substring(0, index);
        int newIndex = header.indexOf(" ", index + 1);
        uri = header.substring(index + 1, newIndex);
        index = header.indexOf("\r\n");
        header = header.substring(index + 2);
        String[] headers = header.split("\r\n");
        if(headers != null) {
            for(String h: headers) {
                String[] strs = h.split(":");
                if(strs != null && strs.length >= 2) {
                    mHashMap.put(strs[0].trim(), strs[1].trim());
                    if(strs[0].equals("Connection") && strs[1].trim().equals("keep-alive")) {
                        isKeepAlive = true;
                    }else if(strs[0].trim().equals("Content-Length")) {
                        int contentLength = Integer.parseInt(strs[1].trim());
                        mContent = new byte[contentLength];
                    }
                }
            }
        }
        if(mContent == null)return;
        // 解析数据
        length -= divider;
        System.arraycopy(read, divider, mContent, 0, length);
        for(; length < mContent.length && (len = inputStream.read(mContent, length, mContent.length - length)) > 0 ;) {
            length += len;
        }
    }

    private int divider(byte[] b, int off){
        int location = -1;
        if(off < 0)off = 0;
        for(int i = off ; i + 4 < b.length ; i++){
            if(b[i] == 13 && b[i + 1] == 10 && b[i + 2] == 13 && b[i + 3] == 10){
                location = i + 4;
                break;
            }
        }
        return location;
    }

    public byte[] content(){
        return this.mContent;
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