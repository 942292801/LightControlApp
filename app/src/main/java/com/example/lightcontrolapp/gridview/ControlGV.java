package com.example.lightcontrolapp.gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lightcontrolapp.R;

import java.util.ArrayList;
import java.util.List;

public class ControlGV extends BaseAdapter {

    private  Context mcontext;
    private LayoutInflater mLayoutInflater;
    //灯光选中 标记
    public static List<Integer> lightList = new ArrayList<Integer>();

    public ControlGV(Context context){
        this.mcontext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        //产生多少个item
        return 64;
    }


    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder{
        public ImageView imageView;
        public TextView textView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            view = mLayoutInflater.inflate(R.layout.grid_item,null);
            holder = new ViewHolder();
            holder.imageView = view.findViewById(R.id.iv_grid);
            holder.textView = view.findViewById(R.id.tv_title);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        holder.textView.setText(String.valueOf(i+1));
        if (lightList.get(i) == 0){
            holder.imageView.setImageResource(R.drawable.off);

        }else {
            holder.imageView.setImageResource(R.drawable.on);

        }
        return view;
    }
}
