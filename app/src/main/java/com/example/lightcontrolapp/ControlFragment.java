package com.example.lightcontrolapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.lightcontrolapp.dto.ArtNetInfo;
import com.example.lightcontrolapp.dto.EventBusMessage;
import com.example.lightcontrolapp.dto.StatusDto;
import com.example.lightcontrolapp.gridview.ControlGVAdapter;
import com.example.lightcontrolapp.udp.UDPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class ControlFragment extends Fragment {

    private Context mContext;
    private View view;
    private GridView gv;
    private ControlGVAdapter controlGVAdapter;
    private TextView tvLightMin,tvLightVal,tvLightMax;

    private TextView tvSewenMin,tvSewenVal,tvSewenMax;
    private SeekBar sbLight,sbSewen;
    private Button btnOn,btnOff,btnAllselect,btnClear;
    private ImageView imageView;

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_control,container,false);
        mContext = view.getContext();

        tvLightMin = view.findViewById(R.id.control_light_min);
        tvLightVal= view.findViewById(R.id.control_light_val);
        tvLightMax = view.findViewById(R.id.control_light_max);

        sbLight = view.findViewById(R.id.control_light_sb);

        tvSewenMin = view.findViewById(R.id.control_sewen_min);
        tvSewenVal= view.findViewById(R.id.control_sewen_val);
        tvSewenMax = view.findViewById(R.id.control_sewen_max);
        sbSewen = view.findViewById(R.id.control_sewen_sb);

        btnOn  = view.findViewById(R.id.control_btn_on);
        btnOff = view.findViewById(R.id.control_btn_off);
        btnAllselect = view.findViewById(R.id.control_btn_allselect);
        btnClear = view.findViewById(R.id.control_btn_clear);

        gvIni();
        tvIni();
        //按钮初始化
        btnIni();
        //滑动条初始化
        seebarIni();
        //EventBus.getDefault().register(this);

        return  view;
    }

    private void gvIni(){
        //初始化图片
        gv = view.findViewById(R.id.gv_control);
        //适配器
        controlGVAdapter = new ControlGVAdapter(mContext);
        gv.setAdapter(controlGVAdapter);

        //20盏灯点击事件
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                try {
                    imageView = view.findViewById(R.id.control_img);
                    if (ControlGVAdapter.lightList.get(i).getSelect()) {
                        ControlGVAdapter.lightList.get(i).setSelect(false);
                        imageView.setImageResource(R.drawable.img_control_off);
                    } else {
                        ControlGVAdapter.lightList.get(i).setSelect(true);
                        imageView.setImageResource(R.drawable.img_control_on);
                    }
                }catch (Exception e){
                    Log.e("gvClick:",e.getMessage());
                }

            }
        });
    }

    private void tvIni(){
        tvLightMin.setText(ArtNetInfo.dataTotal.getLmMin()+"%");
        SetLightVal(ArtNetInfo.dataTotal.getLmMin());
        tvLightMax.setText(ArtNetInfo.dataTotal.getLmMax()+"%");

        tvSewenMin.setText(ArtNetInfo.dataTotal.getTpMin()+"K");
        SetSewenVal(ArtNetInfo.dataTotal.getTpMin());
        tvSewenMax.setText(ArtNetInfo.dataTotal.getTpMax()+"K");

    }


    //按钮初始化
    private void btnIni(){
        //全选
        btnAllselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectLight(true);
            }
        });

        //清除
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectLight(false);
            }
        });

        //开
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SetLightVal(ArtNetInfo.dataTotal.getLmMax() - ArtNetInfo.dataTotal.getLmMin());
                SetLightSb(ArtNetInfo.dataTotal.getLmMax());
                SetSelectLightVal(ArtNetInfo.dataTotal.getLmMax());
                //发送代码
                ArtNetInfo.UDPSendOrder(ControlGVAdapter.lightList);

            }
        });

        //关
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置文字的val值 和 seebar的值
                SetLightVal(ArtNetInfo.dataTotal.getLmMax() - ArtNetInfo.dataTotal.getLmMin());
                SetLightSb(ArtNetInfo.dataTotal.getLmMin());
                SetSelectLightVal(ArtNetInfo.dataTotal.getLmMin());
                //发送代码
                ArtNetInfo.UDPSendOrder(ControlGVAdapter.lightList);
            }
        });

    }

    //seebar初始化
    private void seebarIni(){
        //seebar的最小值只能为0， 所以设置最大值为当前 最大最小的差即可，运算的时候加上最小值运算
        sbLight.setMax(ArtNetInfo.dataTotal.getLmMax()-ArtNetInfo.dataTotal.getLmMin());
        sbSewen.setMax(ArtNetInfo.dataTotal.getTpMax()-ArtNetInfo.dataTotal.getTpMin());
        SetLightSb(ArtNetInfo.dataTotal.getLmMin());
        SetSewenSb(ArtNetInfo.dataTotal.getTpMin());
        //总亮度条
        sbLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                SetLightVal(progress+ArtNetInfo.dataTotal.getLmMin());
                SetSelectLightVal(progress+ArtNetInfo.dataTotal.getLmMin());
                //发送代码
                ArtNetInfo.UDPSendOrder(ControlGVAdapter.lightList);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //色温条
        sbSewen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                SetSewenVal(progress+ArtNetInfo.dataTotal.getTpMin());
                SetSelectSewenVal(progress+ArtNetInfo.dataTotal.getTpMin());
                //发送代码
                ArtNetInfo.UDPSendOrder(ControlGVAdapter.lightList);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //控制灯的选中状态
    private void SelectLight(Boolean isSelect){
        try{
            //改数据
            if (isSelect){
                //全部选中
                for (int i = 0;i<ControlGVAdapter.lightList.size();i++){
                    ControlGVAdapter.lightList.get(i).setSelect(isSelect);
                }

            }else {
                //全部取消
                for (int i = 0;i<ControlGVAdapter.lightList.size();i++){
                    ControlGVAdapter.lightList.get(i).setSelect(isSelect);
                }

            }
            controlGVAdapter.notifyDataSetChanged();

        }catch (Exception e){
            Log.e("error",e.getMessage());
        }


    }

    //选中灯亮度值
    private void SetSelectLightVal(Integer val){
        try {

            for (int i = 0; i < ControlGVAdapter.lightList.size(); i++) {
                if (ControlGVAdapter.lightList.get(i).getSelect()) {
                    //设置最大值
                    ControlGVAdapter.lightList.get(i).setLmVal(val);
                }
            }
            controlGVAdapter.notifyDataSetChanged();

        }catch (Exception e){
            Log.e("error",e.getMessage());
        }
    }

    //选择灯色温值
    private void SetSelectSewenVal(Integer val){
        try {

            for (int i = 0; i < ControlGVAdapter.lightList.size(); i++) {
                if (ControlGVAdapter.lightList.get(i).getSelect()) {
                    //设置最大值
                    ControlGVAdapter.lightList.get(i).setTpVal(val);
                }
            }
            controlGVAdapter.notifyDataSetChanged();

        }catch (Exception e){
            Log.e("error",e.getMessage());
        }
    }

    //设置亮度条的数值和显示txt值
    private void SetLightVal(Integer val){
        tvLightVal.setText("亮度: "+val+ "%");
    }

    private void SetLightSb(Integer val){
        sbLight.setProgress(val-ArtNetInfo.dataTotal.getLmMin());

    }


    //设置色温条的数值和显示txt值
    private void SetSewenVal(Integer val){
        tvSewenVal.setText("色温:"+ val + "k");

    }

    private void SetSewenSb(Integer val){
        sbSewen.setProgress(val-ArtNetInfo.dataTotal.getTpMin());

    }

    //EvenBus 广播事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventBusMessage message) {
        if (message.getType()==1){
            /*if (TextUtils.isEmpty(message.getMessage())){
                return;
            }
            ArtNetInfo.BODY_PACKET = message.getMessage();
            Log.e("恢复",message.getMessage());
            ArtNetInfo.isGroud=true;
            btnSingleCtrl.setBackgroundResource(R.drawable.btn_gray);
            btnGroudCtrl.setBackgroundResource(R.drawable.btn_yellow);
            ImageView imageView= null;
            TextView textView = null;
            for (int i = 0;i<ControlGVAdapter.lightList.size();i++){
                if (!ArtNetInfo.getBodyIsNull(i)){
                    //通话值不为空 切换选中图片
                    ControlGVAdapter.lightList.set(i,1);
                }
                else {
                    ControlGVAdapter.lightList.set(i,0);

                }
            }

            int flag = 0;
            for (int i =0;i<mGV.getChildCount();i++){
                imageView = (ImageView) mGV.getChildAt(i).findViewById(R.id.iv_grid);
                textView = mGV.getChildAt(i).findViewById(R.id.tv_title);
                flag = Integer.valueOf(textView.getText().toString()) -1;
                if(ControlGVAdapter.lightList.get(flag)==1){
                    imageView.setImageResource(R.drawable.on);
                }else {
                    imageView.setImageResource(R.drawable.off);
                }
            }*/

        }
    }



}
