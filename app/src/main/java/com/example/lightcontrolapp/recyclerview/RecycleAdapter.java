package com.example.lightcontrolapp.recyclerview;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.lightcontrolapp.R;


public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyHolder> {

    private Context context;   // 方便视图的操作

    public RecycleAdapter(Context context)//, List<SceneTimeInfo> list)
    {
        this.context = context;

    }


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
        return 2;
    }

    // 绑定类
    public class MyHolder extends RecyclerView.ViewHolder {
        TextView recycler_content,recycler_time;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            recycler_content = itemView.findViewById(R.id.recycler_content);
            recycler_time = itemView.findViewById(R.id.recycler_time);
        }
    }
    //   实际绑定 item 操作
    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int position) {
        /*if (position<list.size()){
            SceneTimeInfo sceneTimeInfo = list.get(position);
            myHolder.recycler_content.setText("场景："+ CommonTools.regGetNum(sceneTimeInfo.getScenenum()));
            myHolder.recycler_time.setText(String.format("%02d:%02d",sceneTimeInfo.getMinute(),sceneTimeInfo.getSecond()) );

        }*/


        /*myHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "点击了:"+list.get(position), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

}


