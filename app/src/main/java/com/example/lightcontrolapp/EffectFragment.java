package com.example.lightcontrolapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lightcontrolapp.dto.DataTotal;
import com.example.lightcontrolapp.dto.EventBusMessage;
import com.example.lightcontrolapp.gridview.EffectGV;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class EffectFragment extends Fragment {

    private Context mContext;
    private GridView mGV;
    private EffectGV mGridAdapter;

    private LinearLayout effect_layout;
    private int selectLastItem;

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View  view = inflater.inflate(R.layout.fragment_effect,container,false);
       mContext = view.getContext();


       //广播事件初始化
        EventBus.getDefault().register(this);

        mGV = view.findViewById(R.id.effect_gv);
        effect_layout = view.findViewById(R.id.effect_layout);

        gridViewIni();
        return  view;
    }

    private void gridViewIni(){
        //适配器
        mGridAdapter = new EffectGV(mContext);
        mGV.setAdapter(mGridAdapter);

        //效果Item:长按事件
        mGV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                //integralPopupWindow.showAtLocation(effect_layout,Gravity.CENTER,0,0);

                jumpEffectActivity(i);
                return true;
            }
        });
        //效果Item:点击事件
        mGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                //清除上一个选中
                closeSelectLastItem(i);
                ImageView imageView = view.findViewById(R.id.scene_img);
                imageView.setImageResource(R.drawable.effect_on);

                if (selectLastItem == i){

                }

            }
        });

    }


    //EvenBus 广播事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventBusMessage message) {
        if (message.getType()==2){
            mGridAdapter.notifyDataSetChanged();
        }
    }


    public void jumpEffectActivity(int index){
        Intent intent = new Intent(getActivity(), EffectActivity.class);
        Bundle data = new Bundle();
        //String info = editText.getText().toString();
        data.putInt("effectIndex",index);
        intent.putExtra("data",data);
        startActivity(intent);
    }

    //关闭列表上一次选中的项 index为-1时默认记录第一项
    private void closeSelectLastItem(int index){

        ImageView imageView=null;
        imageView = (ImageView) mGV.getChildAt(selectLastItem).findViewById(R.id.scene_img);
        imageView.setImageResource(R.drawable.effect_off);
        if (index <0){
            selectLastItem = 0;
        }else if (selectLastItem == index){
            selectLastItem = index;

        }else {

        }
    }




}
