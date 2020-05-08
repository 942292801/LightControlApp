package com.example.lightcontrolapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.lightcontrolapp.dto.ArtNetInfo;
import com.example.lightcontrolapp.dto.EventBusMessage;
import com.example.lightcontrolapp.gridview.GridViewAdapter;
import com.example.lightcontrolapp.tools.SPUtils;
import com.example.lightcontrolapp.udp.UDPUtils;

import org.greenrobot.eventbus.EventBus;


public class SceneFragment extends Fragment {

    private Context context;
    private GridView mGV;
    private GridViewAdapter mGridAdapter;
    private Button btnSave,btnDel,btnCancel;
    private ImageView editImageView;
    private boolean isEditScene = false;
    private int editSceneNum ;
    private boolean isNullScene = false;
    private AlphaAnimation alphaAnimation1;



    @Override
    public void onResume() {
        super.onResume();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scene,container,false);
        context = view.getContext(); //getActivity(); //view.getContext();
        //初始化图片
        btnSave = view.findViewById(R.id.btn_scene_save);
        btnDel = view.findViewById(R.id.btn_scene_del);
        btnCancel = view.findViewById(R.id.btn_scene_cancel);
        mGV = view.findViewById(R.id.scene_gv);


        alphaAnimation1Ini();
        gridViewIni();
        btnIni();
        return  view;
    }


    private void gridViewIni(){
        //适配器
        mGridAdapter = new GridViewAdapter(context);
        mGV.setAdapter(mGridAdapter);

        //长按事件
        mGV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(isEditScene){
                    return true;
                }
                sceneEditorStatus(true);
                editImageView = view.findViewById(R.id.scene_markView);
                editSceneNum = i;
                String key = "scene_"+String.valueOf(i);
                if (SPUtils.contains(context,key)){
                    //推送到分控窗体 更新事件
                    EventBus.getDefault().post(new EventBusMessage(1, SPUtils.get(context, key, "").toString()){});
                }

                if (editImageView.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.drawable.img_scene_mark_false).getConstantState())){
                    isNullScene = true;
                }else {
                    isNullScene = false;
                }
                editImageView.setImageResource(R.drawable.img_scene_on);
                editImageView.setAnimation(alphaAnimation1);
                alphaAnimation1.start();

                return true;
            }
        });
        //25场景点击事件
        mGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                /*if(isEditScene){
                    return;
                }

                String key = "scene_"+String.valueOf(i);
                ImageView imageView = view.findViewById(R.id.scene_img);
                if (imageView.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.drawable.img_scene_on).getConstantState())){
                    AllOffIni();
                    ArtNetInfo.BODY_PACKET = ArtNetInfo.OFF_PACKET;
                    imageView.setImageResource(R.drawable.img_scene_off);
                }else {
                    AllOffIni();
                    ArtNetInfo.BODY_PACKET = SPUtils.get(context, key, "").toString();
                    imageView.setImageResource(R.drawable.img_scene_on);
                }
                UDPUtils.Send(ArtNetInfo.getBodyOrder());*/
            }
        });

    }

    //闪烁动画
    private void alphaAnimation1Ini(){
        alphaAnimation1 = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation1.setDuration(1000);
        alphaAnimation1.setRepeatCount(Animation.INFINITE);
        alphaAnimation1.setRepeatMode(Animation.RESTART);

    }

    private void btnIni(){

        //保存按钮
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*editImageView.clearAnimation();
                editImageView.setImageResource(R.drawable.img_scene_mark_true);
                sceneEditorStatus(false);
                Log.e("保存","scene_"+String.valueOf(editSceneNum)+":" + ArtNetInfo.BODY_PACKET);
                SPUtils.put(context, "scene_"+String.valueOf(editSceneNum), ArtNetInfo.BODY_PACKET);*/
            }
        });

        //删除按钮
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editImageView.clearAnimation();
                editImageView.setImageResource(R.drawable.img_scene_mark_false);
                sceneEditorStatus(false);
                SPUtils.remove(context,"scene_"+String.valueOf(editSceneNum));
                Log.e("删除","scene_"+String.valueOf(editSceneNum));
            }
        });

        //取消按钮
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sceneEditorStatus(false);
                editImageView.clearAnimation();
                if (isNullScene){
                    editImageView.setImageResource(R.drawable.img_scene_mark_false);
                }else {
                    editImageView.setImageResource(R.drawable.img_scene_mark_true);
                }


            }
        });

    }

    //显示是否处于编辑场景状态  显示按钮
    private void sceneEditorStatus(boolean status){
        isEditScene = status;
        //编辑状态
        btnSave.setVisibility(status?View.VISIBLE:View.GONE);
        btnDel.setVisibility(status?View.VISIBLE:View.GONE);
        btnCancel.setVisibility(status?View.VISIBLE:View.GONE);
    }

    //把mgv的灯全部关
    private void AllOffIni(){
        ImageView imageView = null;
        //所有灯关
        for (int i = 0;i<mGV.getChildCount();i++){
            imageView = (ImageView) mGV.getChildAt(i).findViewById(R.id.scene_img);
            imageView.setImageResource(R.drawable.img_scene_off);
        }

    }



}
