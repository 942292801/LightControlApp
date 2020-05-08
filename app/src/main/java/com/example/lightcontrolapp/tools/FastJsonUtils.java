package com.example.lightcontrolapp.tools;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * desc: FastJson工具类
 * author：djj on 2017/8/25 15:19
 * 简书：http://www.jianshu.com/u/dfbde65a03fc
 */
public class FastJsonUtils {

    /**
     * 对象转化为json fastjson 使用方式
     *
     * @return
     */
    public static String objectToJson(Object object) {
        if (object == null) {
            return "";
        }
        try {
            return JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);


        } catch (Exception e) {
            Log.e("error",e.getMessage());
        }
        return "";
    }

    /**
     * json转化为对象  fastjson 使用方式
     *
     * @return
     */
    public static <T> T jsonToObject(String jsonData, Class<T> clazz) {
        T t = null;
        if (TextUtils.isEmpty(jsonData)) {
            return null;
        }
        try {
            t = JSON.parseObject(jsonData, clazz);
        } catch (Exception e) {
        }
        return t;
    }

    /**
     * json转化为List<Person>或List<String>数据  fastjson 使用方式
     *
     * @param jsonData
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToArray(String jsonData, Class<T> clazz) {
        List<T> list = null;
        try {
            list = JSON.parseArray(jsonData, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * json转化为List  fastjson 使用方式
     */
    public static List jsonToList(String jsonData) {
        if (TextUtils.isEmpty(jsonData)) {
            return null;
        }
        List arrayList = null;
        try {
            arrayList = JSON.parseObject(jsonData, new TypeReference<ArrayList>() {
            });
        } catch (Exception e) {
        }
        return arrayList;
    }


    /**
     * json转化为Map  fastjson 使用方式
     */
    public static Map jsonToMap(String jsonData) {
        if (TextUtils.isEmpty(jsonData)) {
            return null;
        }
        Map map = null;
        try {
            map = JSON.parseObject(jsonData, new TypeReference<Map>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
