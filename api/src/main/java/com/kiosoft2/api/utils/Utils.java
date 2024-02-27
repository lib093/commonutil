package com.kiosoft2.api.utils;

import android.text.TextUtils;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.List;

public final class Utils {

    private Utils(){}

    public static <T> T getOrDefault(final T object, final T defaultObject) {
        if (object == null) {
            return defaultObject;
        }
        return object;
    }

    public static String getStrDefault(final String value, final String defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        return value;
    }

    public static String toJson(Object src) {
        return JSON.toJSONString(src);
    }

    public static <T> T toObject(Object object, Class<T> clazz) {
        try {
            if (object instanceof String) {
                return JSON.parseObject(object.toString(), clazz);
            }
            return JSON.parseObject(toJson(object), clazz);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> toObjectList(Object object, Class<T> clazz) {
        try {
            if (object instanceof String) {
                return JSON.parseArray(object.toString(), clazz);
            }
            return JSON.parseArray(toJson(object), clazz);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static JSONObject toJSONObject(Object object) {
        try {
            if (object instanceof String) {
                return JSON.parseObject(object.toString());
            }
            return JSON.parseObject(toJson(object));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static JSONArray toJSONArray(Object jsonArray) {
        try {
            if (jsonArray instanceof String) {
                return JSONArray.parseArray(jsonArray.toString());
            } else {
                return JSONArray.parseArray(toJson(jsonArray));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
