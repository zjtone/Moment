package com.zjt.handler;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

public class DiscardHandler implements IHandler {

    public void read(Socket socket) throws IOException{
        InputStream inputStream = socket.getInputStream();
        byte[] b = new byte[1];
        while(inputStream.read(b) > 0);
    }
}