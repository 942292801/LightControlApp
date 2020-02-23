package com.example.lightcontrolapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.lightcontrolapp.dto.EventBusMessage;
import com.example.lightcontrolapp.gridview.ControlGV;
import com.example.lightcontrolapp.tools.SPUtils;
import com.example.lightcontrolapp.udp.SendUtils;

import org.greenrobot.eventbus.EventBus;


public class MainActivity extends AppCompatActivity {

    private Context context;
    private BottomNavigationView navigationView;
    private Button btnIpAdd,btnIpDel;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private Fragment controlFragment,sceneFragment,wiringFragment,settingFragment;
    public Fragment[] fragmentlist;
    //用于标识上一个fragment
    private int lastFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        for(int i =0 ;i<64;i++){
            ControlGV.lightList.add(0);
        }
        initFragment();
        iniIPTitle();
    }

    /**
    * 初始化IP地址栏
    * */
    private void iniIPTitle(){
        btnIpAdd = findViewById(R.id.btn_ipadd);
        btnIpDel = findViewById(R.id.btn_ipdel);
        spinner = findViewById(R.id.spinner_sp);
        if(SendUtils.IPLIST.size()==0){
            String tmp = SPUtils.get(context,"ip","").toString();
            if (TextUtils.isEmpty(tmp)){
                SendUtils.IPLIST.add("255.255.255.255");
            }else {
                for (String s : tmp .split(",")){
                    SendUtils.IPLIST.add(s);
                }
            }

        }

        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,SendUtils.IPLIST);
        //设置下拉列表的风格
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        //设置默认值
        spinner.setVisibility(View.VISIBLE);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SendUtils.setIp((String)spinner.getItemAtPosition(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //添加按钮
        btnIpAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                final View viewCategory = inflater.inflate(R.layout.ip_edit, null);
                final EditText etIP = viewCategory.findViewById(R.id.input_IP);
                //final EditText etSort = viewCategory.findViewById(R.id.input_sort);
                new AlertDialog
                        .Builder(view.getContext())
                        .setTitle("请输入IP地址")
                        .setView(viewCategory)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(SendUtils.IPLIST.contains(etIP.getText().toString())){
                                    return;
                                }
                                SendUtils.IPLIST.add(etIP.getText().toString());
                                spinner.setAdapter(adapter);
                                int k= adapter.getCount();
                                if (k>0){
                                    spinner.setSelection(k-1);
                                }else {
                                    spinner.setSelection(0);
                                }
                                SPUtils.put(context, "ip", TextUtils.join(",",SendUtils.IPLIST));

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

            }
        });

        //删除按钮
        btnIpDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SendUtils.IPLIST.size()==0){
                    return;
                }
                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                new AlertDialog
                        .Builder(view.getContext())
                        .setTitle("确定删除IP:"+SendUtils.getIp()+"?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SendUtils.IPLIST.remove(SendUtils.getIp());
                                spinner.setAdapter(adapter);
                                spinner.setSelection(View.VISIBLE);
                                SPUtils.put(context, "ip", TextUtils.join(",",SendUtils.IPLIST));

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });


    }





    /**
     * 初始化fragment，并将headFragment显示出来
     */
    private void initFragment() {
        navigationView = (BottomNavigationView) findViewById(R.id.bnv_main);
        //配置菜单按钮显示图标
        navigationView.setItemIconTintList(null);
        //将三个fragment先放在数组里
        controlFragment = new ControlFragment();
        sceneFragment = new SceneFragment();
        wiringFragment = new WiringFragment();
        settingFragment = new SettingFragment();
        fragmentlist = new Fragment[]{controlFragment,sceneFragment,wiringFragment,settingFragment};
        //此时标识标识首页
        //0表示首页，1表示orderFragment，2表示userFragment
        lastFragment = 0;
        //为navigationView设置点击事件
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //设置默认页面为headFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, controlFragment)
                .show(controlFragment).commit();
        navigationView.setSelectedItemId(R.id.navigation_control);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            //每次点击后都将所有图标重置到默认不选中图片
            resetToDefaultIcon();

            switch (menuItem.getItemId()) {
                case R.id.navigation_control:
                    //判断要跳转的页面是否是当前页面，若是则不做动作
                    if (lastFragment != 0) {
                        switchFragment(lastFragment, 0);
                        lastFragment = 0;
                    }
                    //设置按钮的
                    menuItem.setIcon(R.drawable.controlb);
                    return true;
                case R.id.navigation_scene:
                    if (lastFragment != 1) {
                        switchFragment(lastFragment, 1);
                        lastFragment = 1;
                    }
                    menuItem.setIcon(R.drawable.sceneb);
                    return true;
                case R.id.navigation_wiring:
                    if (lastFragment != 2) {
                        switchFragment(lastFragment, 2);
                        lastFragment = 2;
                    }
                    menuItem.setIcon(R.drawable.wiringb);
                    return true;
                case R.id.navigation_setting:
                    if (lastFragment != 3) {
                        switchFragment(lastFragment, 3);
                        lastFragment = 3;
                    }
                    menuItem.setIcon(R.drawable.settingb);
                    return true;

            }
            return false;
        }
    };



    /**
     *
     * @param lastFragment 表示点击按钮前的页面
     * @param index 表示点击按钮对应的页面
     */
    private void switchFragment(int lastFragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //隐藏上个Fragment
        transaction.hide(fragmentlist[lastFragment]);

        //判断transaction中是否加载过index对应的页面，若没加载过则加载
        if (fragmentlist[index].isAdded() == false) {
            transaction.add(R.id.fl_main, fragmentlist[index]);
        }
        //根据角标将fragment显示出来
        transaction.show(fragmentlist[index]).commitAllowingStateLoss();
    }


    /**
     * 重新配置每个按钮的图标
     */
    private void resetToDefaultIcon() {
        navigationView.getMenu().findItem(R.id.navigation_control).setIcon(R.drawable.controla2);
        navigationView.getMenu().findItem(R.id.navigation_scene).setIcon(R.drawable.scenea2);
        navigationView.getMenu().findItem(R.id.navigation_wiring).setIcon(R.drawable.wiringa2);
        navigationView.getMenu().findItem(R.id.navigation_setting).setIcon(R.drawable.settinga2);
    }




}
