package com.github.coco.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class ActivityUtil {

    public static void start(@NonNull Context context, @NonNull Class<? extends AppCompatActivity> clazz, Bundle bundle) {
        Intent intent = new Intent(context, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void start(@NonNull Context context, @NonNull Class<? extends AppCompatActivity> clazz) {
        start(context, clazz, null);
    }

    public static void start(@NonNull Fragment fragment, @NonNull Class<? extends AppCompatActivity> clazz, Bundle bundle) {
        start(fragment.requireContext(), clazz, bundle);
    }

    public static void start(@NonNull Fragment fragment, @NonNull Class<? extends AppCompatActivity> clazz) {
        start(fragment, clazz, null);
    }
}
