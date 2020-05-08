package com.example.lightcontrolapp.settingSet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lightcontrolapp.R;
import com.example.lightcontrolapp.dto.ArtNetInfo;
import com.example.lightcontrolapp.dto.EventBusMessage;
import com.example.lightcontrolapp.tools.EditTextClearTools;

import org.greenrobot.eventbus.EventBus;

public class LightIniActivity extends AppCompatActivity {

    private Context mContent;
    private EditText etMinVal ;
    private EditText etMaxVal ;
    private Button btnBakc;
    private ImageView imgMin, imgMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_ini);

        mContent = this;
        etMinVal =findViewById(R.id.val_light_min);
        etMaxVal =findViewById(R.id.val_light_max);
        imgMin = findViewById(R.id.del_light_min);
        imgMax = findViewById(R.id.del_light_max);
        btnBakc = findViewById(R.id.light_back);
        // 添加清楚监听器大气
        EditTextClearTools.addclerListener(etMinVal, imgMin);
        EditTextClearTools.addclerListener(etMaxVal, imgMax);
        etMinVal.setText( ArtNetInfo.dataTotal.getLmMin().toString());
        etMaxVal.setText(ArtNetInfo.dataTotal.getLmMax().toString());
        etMaxVal.clearFocus();
        btnBakc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog
                        .Builder(mContent)
                        .setTitle("确定保存信息?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{
                                    int min = Integer.valueOf(etMinVal.getText().toString());
                                    int max = Integer.valueOf(etMaxVal.getText().toString());
                                    if (min >max ){
                                        Toast.makeText(mContent, "最小值大于最大值，保存失败", Toast.LENGTH_SHORT).show();

                                    }else if ( max>101){
                                        Toast.makeText(mContent, "最大值不能大于100%，保存失败", Toast.LENGTH_SHORT).show();
                                    }else {
                                        ArtNetInfo.dataTotal.setLmMin(min);
                                        ArtNetInfo.dataTotal.setLmMax(max);
                                        EventBus.getDefault().post(new EventBusMessage(3, null){});
                                        finish();
                                    }

                                }catch (Exception e){
                                    Toast.makeText(mContent, "格式错误，保存失败", Toast.LENGTH_SHORT).show();
                                }

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
            }
        });

    }
}
