package com.zjt.http;

import java.io.*;
import com.zjt.handler.*;

public class HTTPHandler implements IHandler {
    private Api mDefaultApi;

    public void read(InputStream inputStream, OutputStream outputStream) throws IOException{
        int totalLength = 1024 * 1024 * 2;
        byte[] read = new byte[totalLength];
        int length = 0, read_len = -1;
        while(length < totalLength && inputStream.available() > 0 && 
            (read_len = inputStream.read(read, length, totalLength - length)) > 0){
            length += read_len;
        }
        if(length <= 0)return;
        HTTPRequest request = new HTTPRequest(read);
        HTTPResponse response = new HTTPResponse(outputStream);
        Api api;
        if(request.method().toLowerCase().equals("get"))
            api = Dispatcher.getInstance().get(request.uri());
        else api = Dispatcher.getInstance().post(request.uri());
        if(api == null && mDefaultApi != null){
            mDefaultApi.response(request, response);
        }
        if(api != null)
            api.response(request, response);
    }

    public HTTPHandler defaultApi(Api api){
        this.mDefaultApi = api;
        return this;
    }
    
    public HTTPHandler get(String key, Api api){
        Dispatcher.getInstance().get(key, api);
        return this;
    }

    public HTTPHandler post(String key, Api api){
        Dispatcher.getInstance().post(key, api);
        return this;
    }
}