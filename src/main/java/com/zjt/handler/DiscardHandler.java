package com.zjt.handler;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class DiscardHandler implements IHandler {

    public void read(InputStream inputStream, OutputStream outputStream) throws IOException{
        byte[] b = new byte[1];
        while(inputStream.read(b) > 0);
    }
}