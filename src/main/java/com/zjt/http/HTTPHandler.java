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
                byte[] read = new byte[totalLength];
                while(length < totalLength && 
                    (read_len = inputStream.read(read, length, totalLength - length)) > 0) {
                    length += read_len;
                }
                if(length > 0){
                    System.out.println("" + new String(read));
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
                    break;
                }
            }catch(IOException e){
                //  break;
            }
        }
        inputStream.close();
        outputStream.close();
        socket.close();
    }

    private int headerDivider(byte[] b, int offset) {
        int location = -1;
        for(int i = offset ; i + 4 < b.length ; i++) {
            if(b[i] == 13 && b[i + 1] == 10 && b[i + 2] == 13 && b[i + 3] == 10) {
                location = i;
                break;
            }
        }
        return location;
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