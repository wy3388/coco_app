package com.github.coco.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.github.coco.App;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class SharedPreferencesUtil {
    private static final SharedPreferences SHARED_PREFERENCES = App.getContext().getSharedPreferences("coco", Context.MODE_PRIVATE);

    public static void putString(String key, String value) {
        SharedPreferences.Editor edit = SHARED_PREFERENCES.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static void putInt(String key, Integer value) {
        SharedPreferences.Editor edit = SHARED_PREFERENCES.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static String getString(String key) {
        return SHARED_PREFERENCES.getString(key, "");
    }

    @NonNull
    public static Integer getInt(String key) {
        return SHARED_PREFERENCES.getInt(key, 0);
    }

    public static void clear() {
        SharedPreferences.Editor edit = SHARED_PREFERENCES.edit();
        edit.clear();
        edit.apply();
    }
}
