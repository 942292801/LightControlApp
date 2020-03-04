package com.example.lightcontrolapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.example.lightcontrolapp.recyclerview.RecycleDemoAdapter;
import com.example.lightcontrolapp.tools.SPUtils;
import com.example.lightcontrolapp.udp.SendUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EffectActivity extends AppCompatActivity {

    private Context mContext;
    private Button pop_back,pop_add;
    private SwipeMenuRecyclerView swipeRecyclerView;
    private List<String> list = new ArrayList<>();
    private RecycleDemoAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect);
        mContext = this;

        btnIni();
        loadCH();

    }

    private void  btnIni(){
        pop_back = (Button) findViewById(R.id.pop_back);
        pop_add = (Button) findViewById(R.id.pop_add);
        pop_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pop_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerView pvTime;

                //时间选择器
                pvTime = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        //tvTime.setText(getTime(date));
                    }
                })
                        .build();
                pvTime.show();
            }
        });
    }


    //效果场景列表初始化
    private void loadCH() {

        swipeRecyclerView = findViewById(R.id.pop_recycler);
        //// 向list 里添加数据
        addData();
        adapter = new RecycleDemoAdapter(mContext, list);

        //        设置 recyclerView 的 布局管理器
        //        就是 recyclerview 的显示效果
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        swipeRecyclerView.setLayoutManager(layoutManager);
        // 创建菜单：
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {

                int width = mContext.getResources().getDimensionPixelOffset(R.dimen.w_120);
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                // 注意：哪边不想要菜单，那么不要添加即可。
                SwipeMenuItem addItem1 = new SwipeMenuItem(mContext)
                        .setBackground(R.color.SmokeWhite)
                        //.setImage(R.mipmap.ic_action_delete)
                        .setText("编辑")
                        .setTextSize(16)
                        .setTextColor(Color.BLACK)
                        .setWidth(width)
                        .setHeight(height);
                rightMenu.addMenuItem(addItem1); // 添加菜单到右侧。

                SwipeMenuItem addItem2 = new SwipeMenuItem(mContext)
                        .setBackground(R.color.Red)
                        //.setImage(R.mipmap.ic_action_delete)
                        .setText("删除")
                        .setTextSize(16)
                        .setTextColor(Color.BLACK)
                        .setWidth(width)
                        .setHeight(height);
                rightMenu.addMenuItem(addItem2); // 添加菜单到右侧。
            }
        };
// 设置监听器。
        swipeRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);

        SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();
                int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                //删除数据
                list.remove(adapterPosition);
                //删除界面页面
                adapter.notifyItemRemoved(adapterPosition);
                Toast.makeText(mContext, direction + " " + adapterPosition + " " + menuPosition, Toast.LENGTH_SHORT).show();
            }
        };

        // 菜单点击监听。
        swipeRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);

        // 添加适配器
        swipeRecyclerView.setAdapter(adapter);
    }

    private void addData() {
        Intent intent = getIntent();
        Bundle data = intent.getBundleExtra("data");
        String info = data.getString("myinfo");
        list.add(info);
        for (int i = 0; i < 100; i++) {
            list.add("场景：" + i+"00:00");
        }

    }

}
