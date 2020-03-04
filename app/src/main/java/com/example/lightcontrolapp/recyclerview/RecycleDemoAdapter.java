package com.example.lightcontrolapp.recyclerview;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.lightcontrolapp.R;

import java.util.List;

public class RecycleDemoAdapter extends RecyclerView.Adapter<RecycleDemoAdapter.MyHolder> {

    private Context context;   // 方便视图的操作
    private List<String> list;
    public  RecycleDemoAdapter(Context context,List<String> list)
    {
        this.context = context;
        this.list = list;
    }

    //
//    public void update(List<String> list)
//    {
//        this.list = list;
//        notifyDataSetChanged();
//    }
    // 获取 item 的布局
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item,viewGroup,false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    // 绑定类
    public class MyHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.recycler_content);
        }
    }
    //   实际绑定 item 操作
    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int position) {
        String s = list.get(position);
        myHolder.textView.setText(s);
        myHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "点击了:"+list.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

}


