package com.github.coco.common;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class BundleBuilder {
    private final Bundle bundle;

    private BundleBuilder() {
        bundle = new Bundle();
    }

    @NonNull
    public static BundleBuilder builder() {
        return new BundleBuilder();
    }

    public BundleBuilder putString(String key, String value) {
        bundle.putString(key, value);
        return this;
    }

    public BundleBuilder putLong(String key, Long value) {
        bundle.putLong(key, value);
        return this;
    }

    public BundleBuilder putInt(String key, Integer value) {
        bundle.putInt(key, value);
        return this;
    }

    public BundleBuilder putParcelable(String key, Parcelable value) {
        bundle.putParcelable(key, value);
        return this;
    }

    public BundleBuilder putBoolean(String key, boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }

    public BundleBuilder putParcelableArrayList(String key, ArrayList<? extends Parcelable> list) {
        bundle.putParcelableArrayList(key, list);
        return this;
    }

    public Bundle build() {
        return bundle;
    }
}
