package com.github.coco.utils;

import android.content.Context;
import android.widget.Toast;

import com.github.coco.App;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class ToastUtil {
    public static void show(String msg) {
        Toast.makeText(App.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
