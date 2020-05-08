package com.example.lightcontrolapp.udp;

import android.util.Log;

import com.example.lightcontrolapp.dto.ArtNetInfo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * UDP发送信息
 */
public class UDPUtils {
    private static DatagramSocket socket = null;
    private static InetAddress serverAddress;
    private static final int serverPort = 6454;  //发送方和接收方需要端口一致
    //创建基本线程池
    private static ThreadPoolExecutor threadPoolExecutor ;
    public static boolean isIni(){
        if (socket == null){
            return false;
        }
        return true;
    }

    public static void SocketIni(String ip){
        //初始化socker资源和线程池
        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(ip);
            //初始化线程池
            threadPoolExecutor = new ThreadPoolExecutor(6,10,1, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(100));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     */
    public static void Destroy(){
        try {
            if (socket != null){
                socket.close();
            }
            if(threadPoolExecutor != null){
                threadPoolExecutor.shutdown();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 改变UDP发送IP地址
     * @param ip
     */
    public static void changeAddress(String ip){
        try {

            serverAddress = InetAddress.getByName(ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * UDP发送信息
     * @param sendBuf
     */
    public static void Send ( final byte[] sendBuf) {
        //用线程发送信息
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramPacket recvPacket1 = new DatagramPacket(sendBuf, sendBuf.length, serverAddress, serverPort);
                    socket.send(recvPacket1);
                    Log.e(Thread.currentThread().getName()+ "当前UDP线程发送：",  ArtNetInfo.SendOrder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        threadPoolExecutor.execute(runnable);




    }
}



