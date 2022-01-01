package com.github.coco;

import android.app.Application;
import android.content.Context;

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
    }
}
