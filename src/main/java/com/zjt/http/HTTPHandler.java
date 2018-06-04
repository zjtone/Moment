package com.zjt.http;

import java.io.*;
import java.net.Socket;
import com.zjt.handler.*;

public class HTTPHandler implements IHandler {
    private Api mDefaultApi;

    public void read(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        long startTime = System.currentTimeMillis();
        while(!socket.isClosed() && startTime + 10000 > System.currentTimeMillis()) {
            try {
                int length = 0, read_len = -1;
                int totalLength = inputStream.available();
                if(totalLength <= 0)continue;
                HTTPRequest request = new HTTPRequest(inputStream);
                HTTPResponse response = new HTTPResponse(outputStream);
                Api api;
                if(request.method().toLowerCase().equals("get"))
                    api = Dispatcher.getInstance().get(request.uri());
                else{
                    api = Dispatcher.getInstance().post(request.uri());
                }
                if(api == null && mDefaultApi != null){
                    mDefaultApi.response(request, response);
                }
                if(api != null)
                    api.response(request, response);
                break;
            }catch(IOException e) {
                //  break;
                e.printStackTrace();
            }
        }
        inputStream.close();
        outputStream.close();
        socket.close();
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