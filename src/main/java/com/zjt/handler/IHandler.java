package com.zjt.handler;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public interface IHandler {
    void read(InputStream inputStream, OutputStream outputStream) throws IOException;
}