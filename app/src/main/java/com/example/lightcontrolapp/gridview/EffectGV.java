package com.example.lightcontrolapp.gridview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lightcontrolapp.R;
import com.example.lightcontrolapp.tools.SPUtils;

public class EffectGV extends BaseAdapter {
    private Context mContext;
    private TextView name_tv;
    private ImageView img;
    private ImageView markImg;
    private boolean isShowDelete;//根据这个变量来判断是否显示删除图标，true是显示，false是不显示

    public EffectGV(Context mContext){//,String names[], int icons[]) {
        this.mContext = mContext;
        //this.names=names;
        //this.icons=icons;
    }
    public void setIsShowDelete(boolean isShowDelete){
        //是否显示删除键
        this.isShowDelete=isShowDelete;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 15;
        //return icons.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
        //return icons[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(
                R.layout.griditem_scene, null);
        img = (ImageView) convertView.findViewById(R.id.scene_img);
        name_tv = (TextView) convertView.findViewById(R.id.scene_name);
        markImg =(ImageView)  convertView.findViewById(R.id.scene_markView);
        markImg.setVisibility(View.VISIBLE);
        String tmp = SPUtils.get(parent.getContext(),"effect_"+String.valueOf(position),"").toString();
        if ( tmp.length()>5){
            markImg.setImageResource(R.drawable.img_scene_mark_true);
        }else {
            markImg.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.img_scene_mark_false));

        }
        img.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.effect_off));
        name_tv.setText(String.valueOf(position+1));

        //deleteView.setVisibility(isShowDelete?View.VISIBLE:View.GONE);//设置删除按钮是否显示
        //img.setBackgroundResource(icons[position]);
        //name_tv.setText(names[position]);
        return convertView;
    }

}
