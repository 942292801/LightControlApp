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

//场景使用的gridview
public class GridViewAdapter extends BaseAdapter {
    private String names[];

    private int icons[];
    private Context mContext;
    private TextView name_tv;
    private ImageView img;
    private ImageView markImg;
    private boolean isShowDelete;//根据这个变量来判断是否显示删除图标，true是显示，false是不显示

    public GridViewAdapter(Context mContext){//,String names[], int icons[]) {
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
        return 25;
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
                R.layout.fragmet_grid_item, null);
        img = (ImageView) convertView.findViewById(R.id.scene_img);
        name_tv = (TextView) convertView.findViewById(R.id.scene_name);
        markImg =(ImageView)  convertView.findViewById(R.id.scene_markView);

        img.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.scene_off));
        name_tv.setText(String.valueOf(position+1));
        markImg.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.scene_null));

        //deleteView.setVisibility(isShowDelete?View.VISIBLE:View.GONE);//设置删除按钮是否显示
        //img.setBackgroundResource(icons[position]);
        //name_tv.setText(names[position]);
        return convertView;
    }
}

