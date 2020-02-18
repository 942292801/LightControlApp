package com.example.lightcontrolapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.lightcontrolapp.gridview.GridViewAdapter;



public class SceneFragment extends Fragment {

    private GridView mGV;
    private GridViewAdapter mGridAdapter;
    private Button btnSave,btnDel,btnCancel;
    private ImageView editImageView;
    private boolean isEditScene = false;
    private boolean isNullScene = false;
    private AlphaAnimation alphaAnimation1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scene,container,false);
        //初始化图片
        mGV = view.findViewById(R.id.scene_gv);
        btnSave = view.findViewById(R.id.btn_scene_save);
        btnDel = view.findViewById(R.id.btn_scene_del);
        btnCancel = view.findViewById(R.id.btn_scene_cancel);
        //适配器
        mGridAdapter = new GridViewAdapter(view.getContext());
        alphaAnimation1Ini();
        gridViewIni();
        btnIni();

        return  view;
    }


    private void gridViewIni(){

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

                if (editImageView.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.drawable.scene_null).getConstantState())){
                    isNullScene = true;
                }else {
                    isNullScene = false;
                }
                editImageView.setImageResource(R.drawable.scene_on);
                editImageView.setAnimation(alphaAnimation1);
                alphaAnimation1.start();

                return true;
            }
        });
        //25场景点击事件
        mGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                if(isEditScene){
                    return;
                }
                ImageView imageView = view.findViewById(R.id.scene_img);
                if (imageView.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.drawable.scene_on).getConstantState())){
                    //当image1的src为R.drawable.A时，设置image1的src为R.drawable.B
                    imageView.setImageResource(R.drawable.scene_off);
                }else{
                    //否则设置image1的src为R.drawable.A
                    imageView.setImageResource(R.drawable.scene_on);

                }

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
                editImageView.clearAnimation();
                editImageView.setImageResource(R.drawable.scene_select);
                sceneEditorStatus(false);

            }
        });

        //删除按钮
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editImageView.clearAnimation();
                editImageView.setImageResource(R.drawable.scene_null);
                sceneEditorStatus(false);

            }
        });

        //取消按钮
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sceneEditorStatus(false);
                editImageView.clearAnimation();
                if (isNullScene){
                    editImageView.setImageResource(R.drawable.scene_null);
                }else {
                    editImageView.setImageResource(R.drawable.scene_select);
                }


            }
        });

    }

    //显示是否处于编辑场景状态
    private void sceneEditorStatus(boolean status){
        isEditScene = status;
        //编辑状态
        btnSave.setVisibility(status?View.VISIBLE:View.GONE);
        btnDel.setVisibility(status?View.VISIBLE:View.GONE);
        btnCancel.setVisibility(status?View.VISIBLE:View.GONE);
    }



}
