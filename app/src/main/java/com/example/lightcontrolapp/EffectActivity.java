package com.example.lightcontrolapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.lightcontrolapp.dto.EventBusMessage;
import com.example.lightcontrolapp.recyclerview.RecycleAdapter;
import com.example.lightcontrolapp.tools.FastJsonUtils;
import com.example.lightcontrolapp.tools.SPUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class EffectActivity extends AppCompatActivity {

    private Context mContext;
    private Button pop_back,pop_add;
    private SwipeMenuRecyclerView swipeRecyclerView;//侧滑列表
    private RecycleAdapter adapter;
    private LinearLayoutManager layoutManager;
    private OptionsPickerView pvOptions;//自定义场景时间选择器

    private int effectIndex;//操作效果号effect_effectIndex
    //private List<SceneTimeInfo> sceneTimeInfos;
    private int adapterPosition;//侧滑列表的item行号
    private boolean isChangeData = false;//是否修改数据

    //场景时间选择器
    private ArrayList<Integer> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<Integer>>>options3Items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect);
        mContext = this;
        getEffectIndex();
        btnIni();
        sceneTimeItemIni();
        getOptionData();

    }

    //获取当前编辑的效果号 从0开始
    private void getEffectIndex(){
        /*Intent intent = getIntent();
        Bundle data = intent.getBundleExtra("data");
        effectIndex= data.getInt("effectIndex");
        sceneTimeInfos = FastJsonUtils.jsonToArray(SPUtils.get(mContext,"effect_"+effectIndex,"").toString(),SceneTimeInfo.class) ;
        if (sceneTimeInfos == null){
            sceneTimeInfos = new ArrayList<SceneTimeInfo>();
        }*/
    }

    private void  btnIni(){
        /*pop_back = (Button) findViewById(R.id.pop_back);
        pop_add = (Button) findViewById(R.id.pop_add);
        pop_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isChangeData){
                    new AlertDialog
                            .Builder(view.getContext())
                            .setTitle("是否保存数据更改？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SPUtils.put(mContext,"effect_"+effectIndex, FastJsonUtils.objectToJson(sceneTimeInfos));
                                    Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                                    //推送到分控窗体 更新事件
                                    EventBus.getDefault().post(new EventBusMessage(2, String.valueOf(effectIndex) ){});
                                    //退出Activity
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //退出Activity
                                    finish();
                                }
                            })
                            .show();
                }else
                {
                    finish();
                }

            }
        });

        pop_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //场景时间选择器
                pickerAdd();
                isChangeData = true;
            }
        });*/
    }

    //列表初始化
    private void sceneTimeItemIni() {

        /*swipeRecyclerView = findViewById(R.id.pop_recycler);
        //// 向recycleList 里添加数据

        adapter = new RecycleAdapter(mContext, sceneTimeInfos);
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
                adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                switch (menuPosition){
                    case 0:
                        //编辑
                        isChangeData = true;
                        pickerEditor();
                        break;
                    case 1:
                        isChangeData = true;
                        //删除数据
                        sceneTimeInfos.remove(adapterPosition);
                        //删除界面页面
                        adapter.notifyItemRemoved(adapterPosition);
                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();

                        break;
                        default:
                            break;
                }


            }
        };

        // 菜单点击监听。
        swipeRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);
        // 添加适配器
        swipeRecyclerView.setAdapter(adapter);*/
    }



    //添加场景时间选择器
    private void pickerAdd(){
       /* pvOptions = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                SceneTimeInfo sceneTimeInfo = new SceneTimeInfo();
                if (sceneTimeInfos.size() == 0){
                    sceneTimeInfo.setId(1);

                }else {
                    sceneTimeInfo.setId(sceneTimeInfos.get(sceneTimeInfos.size()-1).getId()+1);

                }
                //返回的分别是三个级别的选中位置
                sceneTimeInfo.setScenenum("scene_"+options1Items.get(options1));
                sceneTimeInfo.setMinute(options2Items.get(options1).get(option2));
                sceneTimeInfo.setSecond(options3Items.get(options1).get(option2).get(options3));
                sceneTimeInfos.add(sceneTimeInfo);
                adapter.notifyDataSetChanged();
                Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("添加")//标题
                .setSubCalSize(16)//确定和取消文字大小
                .setTitleSize(16)//标题文字大小
                .setContentTextSize(18)//滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setTitleBgColor(Color.DKGRAY)
                .setTitleColor(Color.WHITE)
                .setTextColorCenter(Color.WHITE)
                .setCancelColor(Color.parseColor("#FF9900"))
                .setSubmitColor(Color.parseColor("#FF9900"))
                .setBgColor(0xFF000000)//滚轮背景颜色 Night mode
                .setLinkage(false)//设置是否联动，默认true
                .setLabels("场景", "分", "秒")//设置选择的三级单位
                .setCyclic(true, true, true)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .build();
        pvOptions.setPicker(options1Items, options2Items,options3Items);//添加数据源
        pvOptions.show();*/
    }

    //编辑场景时间选择器
    private void pickerEditor(){
       /* pvOptions = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                SceneTimeInfo sceneTimeInfo = sceneTimeInfos.get(adapterPosition);

                //返回的分别是三个级别的选中位置
                sceneTimeInfo.setScenenum("scene_"+options1Items.get(options1));
                sceneTimeInfo.setMinute(options2Items.get(options1).get(option2));
                sceneTimeInfo.setSecond(options3Items.get(options1).get(option2).get(options3));
                sceneTimeInfos.set(adapterPosition,sceneTimeInfo);
                adapter.notifyDataSetChanged();
                Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("编辑")//标题
                .setSubCalSize(16)//确定和取消文字大小
                .setTitleSize(16)//标题文字大小
                .setContentTextSize(18)//滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setTitleBgColor(Color.DKGRAY)
                .setTitleColor(Color.WHITE)
                .setTextColorCenter(Color.WHITE)
                .setCancelColor(Color.parseColor("#FF9900"))
                .setSubmitColor(Color.parseColor("#FF9900"))
                .setBgColor(0xFF000000)//滚轮背景颜色 Night mode
                .setLinkage(false)//设置是否联动，默认true
                .setLabels("场景", "分", "秒")//设置选择的三级单位
                .setCyclic(true, true, true)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .build();
        pvOptions.setPicker(options1Items, options2Items,options3Items);//添加数据源
        pvOptions.show();*/
    }

    //时间选择器数据初始化
    private void getOptionData() {
        /*
         * 注意：如果是添加JavaBean实体数据，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        for (int i = 1;i<26;i++){
            options1Items.add(i);
        }

        ArrayList<Integer> options2Items_01 = new ArrayList<>();
        for (int i = 0;i<60;i++){
            options2Items_01.add(i);
        }

        for (int i = 0;i<60;i++){
            options2Items.add(options2Items_01);
        }

        for (int i = 0;i<60;i++){
            options3Items.add(options2Items);
        }

    }



}
