package com.example.lightcontrolapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lightcontrolapp.dto.ArtNetInfo;
import com.example.lightcontrolapp.dto.DataTotal;
import com.example.lightcontrolapp.dto.EffecDto;
import com.example.lightcontrolapp.dto.SceneDto;
import com.example.lightcontrolapp.tools.FastJsonUtils;
import com.example.lightcontrolapp.tools.FileUtils;
import com.example.lightcontrolapp.udp.UDPUtils;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


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

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //释放UDP资源
        UDPUtils.Destroy();
        //保存json数据 到文件中
        String data = FastJsonUtils.objectToJson(ArtNetInfo.dataTotal);
        Log.e( "保存资源DataTotal：",  data);
        FileUtils.write(data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //配置文件读写权限
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        dataTotalIni();
        IPSettingIni();
        initFragment();

    }

    /**
     * 总体数据初始化
     */
    private  void dataTotalIni(){
        //配置文件读写权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        //dataTotal = (DataTotal) context.getApplicationContext();
        ArtNetInfo.dataTotal = new DataTotal();
        //读取文件的总数据
        String data = FileUtils.read();
        Log.e( "读取资源DataTotal：",  data);
        if (!TextUtils.isEmpty(data)){
            ArtNetInfo.dataTotal = FastJsonUtils.jsonToObject(data,DataTotal.class);
        }

        if (ArtNetInfo.dataTotal.getSceneDtoList() == null){
            ArtNetInfo.dataTotal.setSceneDtoList(new ArrayList<SceneDto>());
        }
        if (ArtNetInfo.dataTotal.getEffecDtoList() == null){
            ArtNetInfo.dataTotal.setEffecDtoList(new ArrayList<EffecDto>());
        }
        if (ArtNetInfo.dataTotal.getIpGroup() == null)
        {
            ArtNetInfo.dataTotal.setIpGroup(new ArrayList<String>());
        }
        if(ArtNetInfo.dataTotal.getLmMin() == null){
            ArtNetInfo.dataTotal.setLmMin(0);
        }
        if (ArtNetInfo.dataTotal.getLmMax() == null){
            ArtNetInfo.dataTotal.setLmMax(100);
        }
        if(ArtNetInfo.dataTotal.getTpMin() == null){
            ArtNetInfo.dataTotal.setTpMin(3000);
        }
        if(ArtNetInfo.dataTotal.getTpMax() == null){
            ArtNetInfo.dataTotal.setTpMax(6000);
        }

    }

    /**
    * 初始化IP地址栏
    * */
    private void IPSettingIni(){
        btnIpAdd = findViewById(R.id.btn_ipadd);
        btnIpDel = findViewById(R.id.btn_ipdel);
        spinner = findViewById(R.id.spinner_sp);
        final List<String> ipGroup =  ArtNetInfo.dataTotal.getIpGroup();
        if (ipGroup.size() == 0){
            ipGroup.add("255.255.255.255");
            ipGroup.add("192.168.1.1");
        }
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,ipGroup);
        //设置下拉列表的风格
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        //设置默认值
        spinner.setVisibility(View.VISIBLE);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!UDPUtils.isIni()){
                    //创建UDP资源
                    UDPUtils.SocketIni((String)spinner.getItemAtPosition(i));
                }else {
                    //保存选中的网关IP地址
                    UDPUtils.changeAddress((String)spinner.getItemAtPosition(i));
                }
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
                new AlertDialog
                        .Builder(view.getContext())
                        .setTitle("请输入IP地址")
                        .setView(viewCategory)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{

                                    InetAddress inetAddress = InetAddress.getByName(etIP.getText().toString()) ;
                                    if(ipGroup.contains(etIP.getText().toString())){
                                        return;
                                    }
                                    ipGroup.add(etIP.getText().toString());
                                    spinner.setAdapter(adapter);
                                    int k= adapter.getCount();
                                    if (k>0){
                                        spinner.setSelection(k-1);
                                    }else {
                                        spinner.setSelection(0);
                                    }
                                }catch (Exception e){
                                    Toast.makeText(context, "IP:格式错误，添加失败", Toast.LENGTH_SHORT).show();
                                }


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
                if(ipGroup.size()==0){
                    return;
                }
                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                new AlertDialog
                        .Builder(view.getContext())
                        .setTitle("确定删除IP:"+ spinner.getSelectedItem().toString()+"?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ipGroup.remove(spinner.getSelectedItem().toString());
                                spinner.setAdapter(adapter);
                                spinner.setSelection(View.VISIBLE);

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
        wiringFragment = new EffectFragment();
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
                    menuItem.setIcon(R.drawable.img_fg_controlb);
                    return true;
                case R.id.navigation_scene:
                    if (lastFragment != 1) {
                        switchFragment(lastFragment, 1);
                        lastFragment = 1;
                    }
                    menuItem.setIcon(R.drawable.img_fg_sceneb);
                    return true;
                case R.id.navigation_wiring:
                    if (lastFragment != 2) {
                        switchFragment(lastFragment, 2);
                        lastFragment = 2;
                    }
                    menuItem.setIcon(R.drawable.img_fg_effectb);
                    return true;
                case R.id.navigation_setting:
                    if (lastFragment != 3) {
                        switchFragment(lastFragment, 3);
                        lastFragment = 3;
                    }
                    menuItem.setIcon(R.drawable.img_fg_settingb);
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
        navigationView.getMenu().findItem(R.id.navigation_control).setIcon(R.drawable.img_fg_controla);
        navigationView.getMenu().findItem(R.id.navigation_scene).setIcon(R.drawable.img_fg_scenea);
        navigationView.getMenu().findItem(R.id.navigation_wiring).setIcon(R.drawable.img_fg_effecta);
        navigationView.getMenu().findItem(R.id.navigation_setting).setIcon(R.drawable.img_fg_settinga);
    }




}
