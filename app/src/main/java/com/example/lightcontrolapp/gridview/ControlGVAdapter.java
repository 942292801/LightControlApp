package com.example.lightcontrolapp.gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lightcontrolapp.R;
import com.example.lightcontrolapp.dto.ArtNetInfo;
import com.example.lightcontrolapp.dto.StatusDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 控制界面的DGView  holder添加后 不卡顿
 */
public class ControlGVAdapter extends BaseAdapter {

    //灯光选中 标记
    public static List<StatusDto> lightList;


    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private StatusDto statusDto;

    public ControlGVAdapter(Context mContext){//,String names[], int icons[]) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        lightList = new ArrayList<StatusDto>();
        for(int i =0 ;i<64;i++){
            StatusDto statusDto = new StatusDto();
            statusDto.setId(i+1);
            statusDto.setLmVal(ArtNetInfo.dataTotal.getLmMin());
            statusDto.setTpVal(ArtNetInfo.dataTotal.getTpMin());
            statusDto.setSelect(false);
            lightList.add(statusDto);
        }
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

        public TextView name_tv,content_tv;
        public ImageView img;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            view = mLayoutInflater.inflate(R.layout.griditem_control,null);
            holder = new ViewHolder();
            holder.img = view.findViewById(R.id.control_img);
            holder.name_tv = view.findViewById(R.id.control_name);
            holder.content_tv = view.findViewById(R.id.control_content);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        statusDto = lightList.get(position);
        if (statusDto.getSelect()){
            holder.img.setImageResource(R.drawable.img_control_on);


        }else {
            holder.img.setImageResource(R.drawable.img_control_off);

        }
        holder.name_tv.setText(String.valueOf(position+1));
        holder.content_tv.setText(statusDto.getLmVal()+"%-"+statusDto.getTpVal());

        return view;
    }





}
