package com.zjt.http;

import java.nio.charset.Charset;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;

public class HTTPResponse {
    private Status status = Status.OK;
    private String contentType = "text/plain";
    private String contentLength = "0";
    private String content = "";
    private OutputStream outputStream;
    private Charset charset;
    private boolean isGzip = false;

    HTTPResponse(OutputStream outputStream){
        this.outputStream = outputStream;
        charset = Charset.forName("UTF-8");
    }

    public HTTPResponse contentLength(String contentLength){
        this.contentLength = contentLength;
        return this;
    }

    public HTTPResponse contentType(String contentType){
        this.contentType = contentType;
        return this;
    }

    public HTTPResponse gzip(boolean gzip){
        this.isGzip = gzip;
        return this;
    }

    public HTTPResponse content(String content){
        this.content = content;
        return this;
    }

    public HTTPResponse status(Status s){
        this.status = s;
        return this;
    }

    private void responseHeader()throws IOException{
        outputStream.write(("HTTP/1.1 " + status.getDescription() + "\r\n").getBytes(charset));
        outputStream.write(("Server: Moment\r\n").getBytes(charset));
        outputStream.write(("Charset: UTF-8\r\n").getBytes(charset));
        outputStream.write(("Content-Type: " + contentType + "\r\n").getBytes(charset));
        outputStream.write(("Cache-Control: no-cache\r\n").getBytes(charset));
        outputStream.write(("Content-Length: " + contentLength + "\r\n").getBytes(charset));
        if(isGzip)
            outputStream.write("Content-Encoding: gzip\r\n".getBytes(charset));
        outputStream.write("Accept-Ranges: bytes\r\n\r\n".getBytes(charset));
    }

    public void flush()throws IOException{
        outputStream.flush();
    }

    public void write()throws IOException{
        responseHeader();
        outputStream.write(content.getBytes(charset));
    }

    public void writeAndFlush()throws IOException{
        write();
        flush();
    }

    public void writeAndFlush(byte[] array)throws IOException{
        responseHeader();
        writeAndFlush(array, 0, array.length);
        flush();
    }

    public void writeAndFlush(byte[] array, int off, int len)throws IOException{
        responseHeader();
        outputStream.write(array, off, len);
        flush();
    }

    public void writeAndFlush(InputStream inputStream)throws IOException {
        responseHeader();
        byte[] in = new byte[1024 * 10];
        int len = 0;
        while((len = inputStream.read(in)) > 0){
            outputStream.write(in, 0, len);
        }
        flush();
    }
}