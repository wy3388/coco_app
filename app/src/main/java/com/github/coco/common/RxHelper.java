package com.github.coco.common;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class RxHelper {
    @NonNull
    public static <T> Observable<T> create(@NonNull ObservableOnSubscribe<T> subscribe) {
        return Observable.create(subscribe)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
