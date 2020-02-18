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

public class ControlGV extends BaseAdapter {

    private  Context mcontext;
    private LayoutInflater mLayoutInflater;

    public ControlGV(Context context){
        this.mcontext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        //产生多少个item
        return 20;
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
        holder.imageView.setImageDrawable(ContextCompat.getDrawable(mcontext,R.drawable.off));
        return view;
    }
}
