package com.zjt.http;

import java.nio.charset.Charset;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class HTTPResponse {
    private Status status = Status.OK;
    private String contentType = "text/plain";
    private String contentLength = "0";
    private String content = "";
    private OutputStream outputStream;
    private GZIPOutputStream gzipOutputStream;
    private Charset charset;
    private boolean isGzip = false, isKeepAlive = false;

    HTTPResponse(OutputStream outputStream)throws IOException{
        this.outputStream = outputStream;
        charset = Charset.forName("UTF-8");
    }

    HTTPResponse gzip(boolean isGzip)throws IOException{
        this.isGzip = isGzip;
        if(isGzip)
            this.gzipOutputStream = new GZIPOutputStream(outputStream);
        return this;
    }

    HTTPResponse keepAlive(boolean isKeepAlive){
        this.isKeepAlive = isKeepAlive;
        return this;
    }

    public HTTPResponse contentLength(String contentLength){
        this.contentLength = contentLength;
        return this;
    }

    public HTTPResponse contentType(String contentType){
        this.contentType = contentType;
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
        outputStream.write("Accept-Ranges: bytes\r\n".getBytes(charset));
        outputStream.write("Access-Control-Allow-Origin: *\r\n".getBytes(charset));
        if(isKeepAlive){
            outputStream.write("Connection: keep-alive\r\n".getBytes(charset));
            outputStream.write("Keep-Alive: timeout=10\r\n".getBytes(charset));
        }
        if(isGzip)
            outputStream.write("Content-Encoding: gzip\r\n".getBytes(charset));
        else {
            outputStream.write(("Content-Length: " + contentLength + "\r\n\r\n").getBytes(charset));
        }
    }

    public void flush()throws IOException{
        outputStream.flush();
        if(isGzip)gzipOutputStream.flush();
    }

    public void write()throws IOException{
        responseHeader();
        if(isGzip){
            gzipOutputStream.write(content.getBytes(charset));
        }else outputStream.write(content.getBytes(charset));
    }

    public void writeAndFlush()throws IOException{
        write();
        flush();
    }

    public void writeAndFlush(byte[] array)throws IOException{
        writeAndFlush(array, 0, array.length);
        flush();
    }

    public void writeAndFlush(byte[] array, int off, int len)throws IOException{
        responseHeader();
        if(isGzip){
            gzipOutputStream.write(array, off, len);
        }else outputStream.write(array, off, len);
        flush();
    }

    public void writeAndFlush(InputStream inputStream)throws IOException {
        responseHeader();
        byte[] in = new byte[1024];
        int len = 0;
        while((len = inputStream.read(in)) > 0){
            if(isGzip)gzipOutputStream.write(in, 0, len);
            else outputStream.write(in, 0, len);
        }
        flush();
    }
}