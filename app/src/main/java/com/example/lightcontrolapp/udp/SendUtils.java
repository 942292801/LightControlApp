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

public class SendUtils {
    private static InetAddress mAddress;
    private static DatagramSocket socket = null;

    public static List<String> IPLIST =  new ArrayList<String>();

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        SendUtils.ip = ip;
    }

    private static String ip = null;//"255.255.255.255"; 发送给整个局域网
    private static final int SendPort = 6454;  //发送方和接收方需要端口一致

    public static void Send ( final byte[] sendBuf) {
        //初始化socket
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            mAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //创建线程发送信息
        new Thread() {

            public void run() {

                DatagramPacket recvPacket1 = new DatagramPacket(sendBuf, sendBuf.length, mAddress, SendPort);
                try {
                    socket.send(recvPacket1);
                    socket.close();
                    Log.e("zziafyc", "已发送内容为：" + ArtNetInfo.BODY_PACKET);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}



