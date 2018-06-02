package com.zjt.http;

import java.io.IOException;
import java.io.OutputStream;

public interface Api {
    void response(HTTPRequest request, HTTPResponse response) throws IOException;
}