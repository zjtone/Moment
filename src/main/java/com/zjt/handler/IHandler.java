package com.zjt.handler;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

public interface IHandler {
    void read(Socket socket) throws IOException;
}