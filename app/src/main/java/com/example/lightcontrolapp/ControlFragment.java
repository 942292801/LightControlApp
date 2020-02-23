package com.example.lightcontrolapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.lightcontrolapp.dto.ArtNetInfo;
import com.example.lightcontrolapp.dto.EventBusMessage;
import com.example.lightcontrolapp.gridview.ControlGV;
import com.example.lightcontrolapp.udp.SendUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class ControlFragment extends Fragment {

    private Context context;
    private GridView mGV;
    private Button btnOn,btnOff,btnSingleCtrl,btnGroudCtrl,btnAllselect,btnClear;
    private TextView tvTotal,tvColor;
    private SeekBar sbTotal,sbColor;

    //总亮度
    private double totalLight = 0;
    //色温
    private double colorLight = 0;



    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_control,container,false);
        context = view.getContext();
        tvTotal = view.findViewById(R.id.tv_total);
        tvColor = view.findViewById(R.id.tv_color);
        sbTotal = view.findViewById(R.id.sb_total);
        sbColor = view.findViewById(R.id.sb_color);

        btnOn  = view.findViewById(R.id.btn_on);
        btnOff = view.findViewById(R.id.off);
        btnSingleCtrl = view.findViewById(R.id.btn_singelctrl);
        btnGroudCtrl = view.findViewById(R.id.btn_groudctrl);
        btnAllselect = view.findViewById(R.id.btn_allselect);
        btnClear = view.findViewById(R.id.btn_clear);

        //初始化图片
        mGV = view.findViewById(R.id.gv_control);

        gvIni();
        //滑动条初始化
        seebarIni();
        //按钮初始化
        btnIni();

        EventBus.getDefault().register(this);

        return  view;
    }

    private void gvIni(){

        //适配器
        mGV.setAdapter(new ControlGV(context));

        //20盏灯点击事件
        mGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                ImageView imageView = view.findViewById(R.id.iv_grid);
                TextView textView =  view.findViewById(R.id.tv_title);
                if (!ArtNetInfo.isGroud){
                    AllOffIni();
                    sbTotal.setOnSeekBarChangeListener(null);
                    sbColor.setOnSeekBarChangeListener(null);
                    sbTotal.setProgress(ArtNetInfo.getBodyPipe1(i));
                    tvTotal.setText("亮度: "+sbTotal.getProgress() + "%");
                    sbColor.setProgress(ArtNetInfo.getBodyPipe2(i));
                    tvColor.setText("色温:"+sbColor.getProgress() + "k");
                    seebarIni();
                }
                int flag = Integer.valueOf(textView.getText().toString()) -1;
                if (ControlGV.lightList.get(flag)==0){
                    imageView.setImageResource(R.drawable.on);
                    ControlGV.lightList.set(flag,1);
                }else {
                    imageView.setImageResource(R.drawable.off);
                    ControlGV.lightList.set(flag,0);
                }
            }
        });
    }

    private void btnIni(){


        //单控和群控
        if (ArtNetInfo.isGroud){
            btnSingleCtrl.setBackgroundResource(R.drawable.btn_gray);
            btnGroudCtrl.setBackgroundResource(R.drawable.btn_yellow);
        }else {
            btnSingleCtrl.setBackgroundResource(R.drawable.btn_yellow);
            btnGroudCtrl.setBackgroundResource(R.drawable.btn_gray);
        }


        //单控
        btnSingleCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ArtNetInfo.isGroud){
                    ArtNetInfo.isGroud=false;
                    btnSingleCtrl.setBackgroundResource(R.drawable.btn_yellow);
                    btnGroudCtrl.setBackgroundResource(R.drawable.btn_gray);
                    AllOffIni();
                    //SendUtils.Send(ArtNetInfo.getBodyOrder());
                }

            }
        });


        //群控
        btnGroudCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ArtNetInfo.isGroud){
                    ArtNetInfo.isGroud=true;
                    btnSingleCtrl.setBackgroundResource(R.drawable.btn_gray);
                    btnGroudCtrl.setBackgroundResource(R.drawable.btn_yellow);
                    AllOffIni();
                    //SendUtils.Send(ArtNetInfo.getBodyOrder());

                }

            }
        });

        //全选
        btnAllselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ArtNetInfo.isGroud){
                    AllOnIni();
                    //SendUtils.Send(ArtNetInfo.getBodyOrder());
                }
            }
        });

        //清除
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllOffIni();
                //SendUtils.Send(ArtNetInfo.getBodyOrder());
            }
        });


        //开
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tmp = -1;
                for (int i =0;i<ControlGV.lightList.size();i++){
                    if (ControlGV.lightList.get(i)==1){
                        ArtNetInfo.BodyUpdatePipe1(255,i);
                        tmp = i;
                    }else {
                        if(ArtNetInfo.isGroud){
                            ArtNetInfo.BodyUpdatePipe1(0,i);
                        }
                    }
                }
                if (tmp != -1){
                    SendUtils.Send(ArtNetInfo.getBodyOrder());
                    sbTotal.setOnSeekBarChangeListener(null);
                    sbTotal.setProgress(ArtNetInfo.getBodyPipe1(tmp));
                    tvTotal.setText("亮度: "+sbTotal.getProgress() + "%");
                    seebarIni();
                }


            }
        });

        //关
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tmp = -1;
                for (int i =0;i<ControlGV.lightList.size();i++){
                    if (ControlGV.lightList.get(i)==1){
                        ArtNetInfo.BodyUpdatePipe1(0,i);
                        tmp = i;
                    }else {
                        if (ArtNetInfo.isGroud){
                            ArtNetInfo.BodyUpdatePipe1(0,i);
                        }
                    }
                }
                if (tmp != -1){
                    SendUtils.Send(ArtNetInfo.getBodyOrder());
                    sbTotal.setOnSeekBarChangeListener(null);
                    sbTotal.setProgress(ArtNetInfo.getBodyPipe1(tmp));
                    tvTotal.setText("亮度: "+sbTotal.getProgress() + "%");
                    seebarIni();
                }
            }
        });

    }

    private void seebarIni(){
        //总亮度条
        sbTotal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    totalLight = progress;
                    tvTotal.setText("亮度: "+progress + "%");
                    int pipe1 =0;
                    boolean isSelect = false;
                    pipe1 = (int)Math.ceil(totalLight/100*255);
                    for (int i =0;i<ControlGV.lightList.size();i++){
                        if (ControlGV.lightList.get(i)==1){
                            isSelect = true;
                            ArtNetInfo.BodyUpdatePipe1(pipe1,i);
                        }else {
                            if(ArtNetInfo.isGroud){
                                ArtNetInfo.BodyUpdatePipe1(0,i);
                            }
                        }

                    }
                    if (isSelect){
                        SendUtils.Send(ArtNetInfo.getBodyOrder());

                    }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //色温条
        sbColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                colorLight = progress+3000;
                tvColor.setText("色温:"+progress + "k");
                int pipe2 =0;
                boolean isSelect = false;
                pipe2 = (int)Math.ceil((colorLight-3000)/3000*255);
                for (int i =0;i<ControlGV.lightList.size();i++){
                    if (ControlGV.lightList.get(i)==1){
                        ArtNetInfo.BodyUpdatePipe2(pipe2,i);
                        isSelect = true;
                    }else {
                        if(ArtNetInfo.isGroud){
                            ArtNetInfo.BodyUpdatePipe2(0,i);
                        }
                    }

                }
                if (isSelect){
                    SendUtils.Send(ArtNetInfo.getBodyOrder());

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //把mgv的灯全部关
    private void AllOffIni(){
        ImageView imageView = null;
        //所有灯关
        for (int i = 0;i<ControlGV.lightList.size();i++){
            ControlGV.lightList.set(i,0);
        }
        for (int i = 0;i<mGV.getChildCount();i++)
        {
            imageView = (ImageView) mGV.getChildAt(i).findViewById(R.id.iv_grid);
            imageView.setImageResource(R.drawable.off);
        }

    }

    //把mgv的灯全部开
    private void AllOnIni(){
        ImageView imageView = null;
        //所有灯开
        for (int i = 0;i<ControlGV.lightList.size();i++){
            ControlGV.lightList.set(i,1);

        }
        for (int i = 0;i<mGV.getChildCount();i++)
        {
            imageView = (ImageView) mGV.getChildAt(i).findViewById(R.id.iv_grid);
            imageView.setImageResource(R.drawable.on);
        }

    }



    //EvenBus 广播事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventBusMessage message) {
        if (message.getType()==1){
            if (TextUtils.isEmpty(message.getMessage())){
                return;
            }
            ArtNetInfo.BODY_PACKET = message.getMessage();
            Log.e("恢复",message.getMessage());
            ArtNetInfo.isGroud=true;
            btnSingleCtrl.setBackgroundResource(R.drawable.btn_gray);
            btnGroudCtrl.setBackgroundResource(R.drawable.btn_yellow);
            ImageView imageView= null;
            TextView textView = null;
            for (int i = 0;i<ControlGV.lightList.size();i++){
                if (!ArtNetInfo.getBodyIsNull(i)){
                    //通话值不为空 切换选中图片
                    ControlGV.lightList.set(i,1);
                }
                else {
                    ControlGV.lightList.set(i,0);

                }
            }

            int flag = 0;
            for (int i =0;i<mGV.getChildCount();i++){
                imageView = (ImageView) mGV.getChildAt(i).findViewById(R.id.iv_grid);
                textView = mGV.getChildAt(i).findViewById(R.id.tv_title);
                flag = Integer.valueOf(textView.getText().toString()) -1;
                if(ControlGV.lightList.get(flag)==1){
                    imageView.setImageResource(R.drawable.on);
                }else {
                    imageView.setImageResource(R.drawable.off);
                }


            }

        }
    }



}
