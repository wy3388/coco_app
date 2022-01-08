package com.github.coco;

import android.app.Application;
import android.content.Context;

import com.chad.library.adapter.base.module.LoadMoreModuleConfig;
import com.github.coco.utils.ToastUtil;
import com.github.coco.views.LoadMoreView;

import java.util.concurrent.CancellationException;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;

/**
 * Created on 2022/1/1.
 *
 * @author wy
 */
public class App extends Application {
    private volatile static App app;

    public static Context getContext() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        LoadMoreModuleConfig.setDefLoadMoreView(new LoadMoreView());
        RxJavaPlugins.setErrorHandler(throwable -> {
            if (!(throwable instanceof CancellationException)) {
                ToastUtil.show(app, throwable.getMessage());
            }
            ToastUtil.show(app, throwable.getMessage());
        });
    }
}
