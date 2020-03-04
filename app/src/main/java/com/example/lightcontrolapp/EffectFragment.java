package com.example.lightcontrolapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lightcontrolapp.dto.EventBusMessage;
import com.example.lightcontrolapp.gridview.EffectGV;
import com.example.lightcontrolapp.popupwindow.IntegralPopupWindow;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EffectFragment extends Fragment {

    private Context context;
    private GridView mGV;
    private EffectGV mGridAdapter;

    private IntegralPopupWindow integralPopupWindow;
    private LinearLayout effect_layout;

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View  view = inflater.inflate(R.layout.fragment_effect,container,false);
        context = view.getContext();

        integralPopupWindow = new IntegralPopupWindow(getActivity());

        //广播事件初始化
        EventBus.getDefault().register(this);
        mGV = view.findViewById(R.id.effect_gv);
        effect_layout = view.findViewById(R.id.effect_layout);

        //适配器
        mGridAdapter = new EffectGV(view.getContext());
        gridViewIni();
        return  view;
    }

    private void gridViewIni(){

        mGV.setAdapter(mGridAdapter);
        /*String key ;
        ImageView imageView = null;
        for (int i = 0;i<mGV.getChildCount();i++){
            key = "scene_"+String.valueOf(i);
            Log.e("恢复数据",key + SPUtils.get(context, key, "").toString());
            if (SPUtils.contains(context,key)){
                imageView = mGV.getChildAt(i).findViewById(R.id.scene_markView);
                imageView.setImageResource(R.drawable.scene_info);
            }


        }*/


        //长按事件
        mGV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                //integralPopupWindow.showAtLocation(effect_layout,Gravity.CENTER,0,0);

                jumpEffectActivity();
                return true;
            }
        });
        //25场景点击事件
        mGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                ImageView imageView = view.findViewById(R.id.scene_img);
                if (imageView.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.drawable.effect_on).getConstantState())){
                    imageView.setImageResource(R.drawable.effect_off);
                }else {
                    imageView.setImageResource(R.drawable.effect_on);
                }
            }
        });

    }


    //EvenBus 广播事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventBusMessage message) {
        if (message.getType()>5){
            //textView.setText(message.toString());

        }
    }


    public void jumpEffectActivity(){
        Intent intent = new Intent(getActivity(), EffectActivity.class);
        Bundle data = new Bundle();
        //String info = editText.getText().toString();

        data.putString("myinfo","123");
        intent.putExtra("data",data);
        startActivity(intent);
    }





}
