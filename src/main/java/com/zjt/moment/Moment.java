package com.zjt.moment;

import java.net.*;
import com.zjt.handler.*;
import com.zjt.threadpool.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Moment implements Runnable {
    private ServerSocket mServerSocket;
    private boolean running = false;
    private IHandler mHandler = new DiscardHandler();
    private IThreadPool mThreadPool = null;
    private int[] ports;

    public Moment(int[] ports) throws IOException{
        if(ports == null)throw new NullPointerException();
        this.ports = ports;
    }

    public Moment handler(IHandler handler){
        if(handler != null)
            mHandler = handler;
        return this;
    }

    public void start(){
        if(!running){
            running = true;
            // 提交到线程池
            execute(this);
        }
    }

    private void execute(Runnable runnable){
        // 提交到线程池
        if(mThreadPool == null)
            mThreadPool = DefaultThreadPool.getInstance();
        mThreadPool.submit(runnable);
    }

    public void run(){
        try{
            for(int p = 0 ; p < ports.length ; p++){
                try{
                    mServerSocket = new ServerSocket(ports[0]);
                    break;
                }catch(BindException e){
                    e.printStackTrace();
                }
            }
            while(running){
                final Socket socket = mServerSocket.accept();
                execute(new Runnable(){
                    public void run(){
                        try{
                            mHandler.read(socket.getInputStream(), socket.getOutputStream());
                        }catch(IOException e){
                            e.printStackTrace();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}