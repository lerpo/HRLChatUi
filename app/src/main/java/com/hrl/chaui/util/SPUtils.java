package com.hrl.chaui.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    /**
     * 初始化SharedPreferences
     * @param context 上下文
     * @param name SharedPreferences名称
     */
    public static void init(Context context, String name) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * 存储String类型的数据
     * @param key 键
     * @param value 值
     */
    public static void putString(String key, String value) {
        editor.putString(key, value).apply();
    }

    /**
     * 获取String类型的数据
     * @param key 键
     * @param defaultValue 默认值
     * @return 存储的值
     */
    public static String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    /**
     * 存储int类型的数据
     * @param key 键
     * @param value 值
     */
    public static void putInt(String key, int value) {
        editor.putInt(key, value).apply();
    }

    /**
     * 获取int类型的数据
     * @param key 键
     * @param defaultValue 默认值
     * @return 存储的值
     */
    public static int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    /**
     * 存储boolean类型的数据
     * @param key 键
     * @param value 值
     */
    public static void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    /**
     * 获取boolean类型的数据
     * @param key 键
     * @param defaultValue 默认值
     * @return 存储的值
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * 删除指定键的数据
     * @param key 键
     */
    public static void remove(String key) {
        editor.remove(key).apply();
    }

    /**
     * 清空所有数据
     */
    public static void clear() {
        editor.clear().apply();
    }
}
