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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.lightcontrolapp.View.MenuItemLayout;
import com.example.lightcontrolapp.dto.ArtNetInfo;
import com.example.lightcontrolapp.dto.EventBusMessage;
import com.example.lightcontrolapp.settingSet.LightIniActivity;
import com.example.lightcontrolapp.settingSet.SeWenIniActivity;
import com.example.lightcontrolapp.settingSet.ZhanghuIniActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class SettingFragment extends Fragment {

    private Context mContext;
    private  View view;
    private ImageView blurImageView;
    private ImageView avatarImageView;
    private MenuItemLayout zhanghuItem,liangduItem,sewenItem,versionItem,aboutItem;

    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放注册事件
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting,container,false);
        mContext = view.getContext();
        onActivityCreated(savedInstanceState);
        ItemCreated();
        setHint(new EventBusMessage(3, null));
        EventBus.getDefault().register(this);
        return  view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //实现个人中心头部磨砂布局
        blurImageView= view.findViewById(R.id.iv_blur);
        avatarImageView = view.findViewById(R.id.iv_avatar);
        Glide.with(this).load(R.drawable.img_setting_shangbiao).bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity())).into(blurImageView);
        Glide.with(this).load(R.drawable.img_setting_shangbiao).bitmapTransform(new CropCircleTransformation(getActivity())).into(avatarImageView);
    }

    public void ItemCreated(){
        zhanghuItem = view.findViewById(R.id.zhanghu);
        liangduItem = view.findViewById(R.id.liangdushezhi);
        sewenItem = view.findViewById(R.id.sewenshezhi);
        versionItem= view.findViewById(R.id.version);
        aboutItem = view.findViewById(R.id.about);


        zhanghuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpActivity(ZhanghuIniActivity.class);
            }
        });

        liangduItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpActivity(LightIniActivity.class);
            }
        });

        sewenItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpActivity(SeWenIniActivity.class);
            }
        });

        versionItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "当前版本：v1.0.0", Toast.LENGTH_SHORT).show();
            }
        });

        aboutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Email联系:942292801@qq.com", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void jumpActivity(Class clazz){
        Intent intent = new Intent(getActivity(), clazz);
       /* Bundle data = new Bundle();
        data.putInt("effectIndex",index);
        intent.putExtra("data",data);*/
        startActivity(intent);
    }

    //EvenBus 广播事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setHint(EventBusMessage message){
        if (message.getType()==3 ){
            liangduItem.setHintText(ArtNetInfo.dataTotal.getLmMin() +"%-"+ArtNetInfo.dataTotal.getLmMax()+"%");
            sewenItem.setHintText(ArtNetInfo.dataTotal.getTpMin() + "-" +ArtNetInfo.dataTotal.getTpMax());
        }

    }

}
