package com.example.lightcontrolapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.lightcontrolapp.gridview.ControlGV;
import com.example.lightcontrolapp.udp.SendUtils;



public class ControlFragment extends Fragment {

    private GridView mGV;
    private Button btnOn,btnOff,btnSingleCtrl,btnGroudCtrl,btnAllselect,btnClear;
    private TextView tvTotal,tvColor;
    private SeekBar sbTotal,sbColor;


    private Boolean isGroud = false;
    //总亮度
    private double totalLight = 0;
    //色温
    private double colorLight = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_control,container,false);
        tvTotal = view.findViewById(R.id.tv_total);
        tvColor = view.findViewById(R.id.tv_color);
        sbTotal = view.findViewById(R.id.sb_total);
        sbColor = view.findViewById(R.id.sb_color);
        //初始化图片
        mGV = view.findViewById(R.id.gv_control);
        //适配器
        mGV.setAdapter(new ControlGV(view.getContext()));

        //20盏灯点击事件
        mGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                ImageView imageView = view.findViewById(R.id.iv_grid);

                if (!isGroud){
                    AllOffIni();
                    sbTotal.setOnSeekBarChangeListener(null);
                    sbColor.setOnSeekBarChangeListener(null);
                    sbTotal.setProgress(ArtNetInfo.getBodyPipe1(i));
                    tvTotal.setText("亮度: "+sbTotal.getProgress() + "%");
                    sbColor.setProgress(ArtNetInfo.getBodyPipe2(i));
                    tvColor.setText("色温:"+sbColor.getProgress() + "k");
                    seebarIni();
                }
                if (imageView.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.drawable.on).getConstantState())){
                    imageView.setImageResource(R.drawable.off);

                }else {
                    imageView.setImageResource(R.drawable.on);

                }
            }
        });
        //滑动条初始化
        seebarIni();
        //按钮初始化
        btnIni(view);
        return  view;
    }

    private void btnIni(View view){
        btnOn  = view.findViewById(R.id.btn_on);
        btnOff = view.findViewById(R.id.off);
        btnSingleCtrl = view.findViewById(R.id.btn_singelctrl);
        btnGroudCtrl = view.findViewById(R.id.btn_groudctrl);
        btnAllselect = view.findViewById(R.id.btn_allselect);
        btnClear = view.findViewById(R.id.btn_clear);

        //单控和群控
        if (isGroud){
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
                if (isGroud){
                    isGroud=false;
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
                if (!isGroud){
                    isGroud=true;
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
                if (isGroud){
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
                ImageView imageView= null;
                int tmp = -1;
                for (int i =0;i<20;i++){
                    imageView = (ImageView) mGV.getChildAt(i).findViewById(R.id.iv_grid);
                    if (imageView.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.drawable.on).getConstantState())){
                        ArtNetInfo.BodyUpdatePipe1(255,i);
                        tmp = i;
                    }else {
                        if(isGroud){
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
                ImageView imageView= null;
                int tmp = -1;
                for (int i =0;i<20;i++){
                    imageView = (ImageView) mGV.getChildAt(i).findViewById(R.id.iv_grid);
                    if (imageView.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.drawable.on).getConstantState())){
                        ArtNetInfo.BodyUpdatePipe1(0,i);
                        tmp = i;
                    }else {
                        if (isGroud){
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
                    ImageView imageView = null;
                    pipe1 = (int)Math.ceil(totalLight/100*255);
                    for (int i =0;i<20;i++){
                        imageView = mGV.getChildAt(i).findViewById(R.id.iv_grid);
                        if (imageView.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.drawable.on).getConstantState())){
                            isSelect = true;
                            ArtNetInfo.BodyUpdatePipe1(pipe1,i);
                        }else {
                            if(isGroud){
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
                ImageView imageView = null;
                pipe2 = (int)Math.ceil((colorLight-3000)/3000*255);
                for (int i =0;i<20;i++){
                    imageView = mGV.getChildAt(i).findViewById(R.id.iv_grid);
                    if (imageView.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.drawable.on).getConstantState())){
                        ArtNetInfo.BodyUpdatePipe2(pipe2,i);
                        isSelect = true;
                    }else {
                        if(isGroud){
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
        for (int i = 0;i<mGV.getChildCount();i++){
            imageView = (ImageView) mGV.getChildAt(i).findViewById(R.id.iv_grid);
            imageView.setImageResource(R.drawable.off);
        }

    }

    //把mgv的灯全部开
    private void AllOnIni(){
        ImageView imageView = null;
        //所有灯关
        for (int i = 0;i<mGV.getChildCount();i++){
            imageView = mGV.getChildAt(i).findViewById(R.id.iv_grid);
            imageView.setImageResource(R.drawable.on);
        }

    }



}
