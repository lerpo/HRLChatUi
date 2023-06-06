package com.hrl.chaui.util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GsonUtils {
    private static Gson gson = null;
    static {
        if (gson == null) {
            gson = new Gson();
        }
    }
    /**
     * 字符串转化为map
     *
     * @param string 字符串
     * @return map
     */
    public static Map<String, Object> stringToMapObject(String string) {
        Map map = new HashMap();
        if (gson != null) {
            map = gson.fromJson(string, new TypeToken<Map>() {
            }.getType());
        }
        return map;
    }
    /**
     * 字符串转化为map
     *
     * @param string 字符串
     * @return map
     */
    public static Map<String, String> stringToMap(String string) {
        Map<String, String> map = new HashMap<>();
        if (gson != null) {
            map = gson.fromJson(string, new TypeToken<Map>() {
            }.getType());
        }
        return map;
    }
    /**
     * 字符串转类
     *
     * @param string 字符串
     * @param clazz  类
     * @param <T>    t
     * @return t
     */
    public static <T> T stringToObject(String string, Class clazz) {
        return (T) gson.fromJson(string, clazz);
    }
    /**
     * 转成list
     *
     * @param string json字符串
     * @return list
     */
    public static <T> List<T> stringToList(String string) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(string, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }
    /**
     * 字符串转化为map
     *
     * @param string json字符串
     * @return list
     */
    public static List<Map> stringToListMap(String string) {
        List<Map> list = null;
        if (gson != null) {
            list = gson.fromJson(string, new TypeToken<List<Map>>() {
            }.getType());
        }
        return list;
    }
    /**
     * 转成json
     *
     * @param object
     * @return
     */
    public static String objectToJsonString(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }
}
