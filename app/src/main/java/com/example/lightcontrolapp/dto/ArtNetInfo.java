package com.example.lightcontrolapp.dto;



import android.util.Log;

import com.example.lightcontrolapp.udp.UDPUtils;

import java.util.List;

import static com.example.lightcontrolapp.tools.CommonTools.hexToByteArray;

public class ArtNetInfo {

    public static DataTotal dataTotal;
    public final static String HEART_PACKET = "4172742d4e6574000050000ec50000000200";
    public static String SendOrder ;

    public static byte[] GetArtNetOrder(List<StatusDto> statusDtoList){
        String tmp = HEART_PACKET;
        Double light = 0.0;
        Double sewen = 0.0;
        Integer tpmanSubTpmin = dataTotal.getTpMax() - dataTotal.getTpMin();

        for (int i =0;i<statusDtoList.size();i++){
            light = Math.ceil((Double.valueOf(statusDtoList.get(i).getLmVal()))*2.55);
            sewen = Math.ceil(Double.valueOf(statusDtoList.get(i).getTpVal()-dataTotal.getTpMin())/tpmanSubTpmin*255.0);
           tmp = tmp + String.format("%02x%02x", light.intValue() ,sewen.intValue());
        }
        while (tmp.length()<530){
            tmp = tmp+"00";
        }
        SendOrder = tmp;
        return hexToByteArray(tmp);
    }

    public static void UDPSendOrder(List<StatusDto> statusDtoList){
        try {
            String tmp = HEART_PACKET;
            Double light = 0.0;
            Double sewen = 0.0;
            Integer tpmanSubTpmin = dataTotal.getTpMax() - dataTotal.getTpMin();

            for (int i =0;i<statusDtoList.size();i++){
                light = Math.ceil((Double.valueOf(statusDtoList.get(i).getLmVal()))*2.55);
                sewen = Math.ceil(Double.valueOf(statusDtoList.get(i).getTpVal()-dataTotal.getTpMin())/tpmanSubTpmin*255.0);
                tmp = tmp + String.format("%02x%02x", light.intValue() ,sewen.intValue());
            }
            while (tmp.length()<530){
                tmp = tmp+"00";
            }
            SendOrder = tmp;
            UDPUtils.Send(hexToByteArray(tmp));
        }catch (Exception e){
            Log.e("ARt-udpsend",e.getMessage());
        }

    }

    /*//获取主控制参数
    public static byte[] getBodyOrder()
    {
        //SET_PACKET=BODY_PACKET;
        return hexToByteArray(HEART_PACKET + BODY_PACKET);
    }

    public static byte[] getAllOffOrder()
    {
        //SET_PACKET=OFF_PACKET;
        return hexToByteArray(HEART_PACKET + OFF_PACKET);
    }
    public static byte[] getAllOnOrder()
    {
        //SET_PACKET=ON_PACKET;
        return hexToByteArray(HEART_PACKET + ON_PACKET);
    }

    //获取通道1的值 返回十进制数字
    public static int getBodyPipe1(int num){
        double tmp =  Integer.valueOf(BODY_PACKET.substring(num*4,num*4+2),16);
        return (int)Math.floor(tmp/255*100);
    }
    //获取通道2的值 返回十进制数字
    public static int getBodyPipe2(int num){
        double tmp =  Integer.valueOf(BODY_PACKET.substring(num*4+2,num*4+4),16);
        return (int)Math.floor(tmp/255*3000);
    }


    public static Boolean getBodyIsNull(int num){
        String tmp = BODY_PACKET.substring(num*4,num*4+4);
        if (tmp.equals("0000") ){
            return true;
        }else {
            return false;
        }
    }

    //控制通道1  pipel 十进制0-255   num：灯的位置
    public static void BodyUpdatePipe1(int pipe1 ,int num){
        String tmp = String.format("%02x",pipe1);
        StringBuilder sb = new StringBuilder(ArtNetInfo.BODY_PACKET);
        sb.replace(num*4,num*4+2,tmp);
        BODY_PACKET = sb.toString();
    }

    //控制 通道2  num：灯的位置
    public static void BodyUpdatePipe2(int pipe2 ,int num){
        String tmp = String.format("%02x",pipe2);
        StringBuilder sb = new StringBuilder(ArtNetInfo.BODY_PACKET);
        sb.replace(num*4+2,num*4+4,tmp);
        BODY_PACKET = sb.toString();
    }

    //控制 通道1 2  num：灯的位置
    public static void BodyUpdateAll(int pipe1 ,int pipe2,int num){
        String tmp = String.format("%02x",pipe1) + String.format("%02x",pipe2);
        StringBuilder sb = new StringBuilder(ArtNetInfo.BODY_PACKET);
        sb.replace(num*4,num*4+4,tmp);
        BODY_PACKET = sb.toString();

    }*/



}
