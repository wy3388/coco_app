package com.github.coco.base;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.coco.common.LoadStatus;
import com.github.coco.common.RxHelper;
import com.github.coco.utils.ToastUtil;
import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.ScopeViewModel;

import java.util.concurrent.CancellationException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 2022/1/1.
 *
 * @author wy
 */
public class BaseViewModel extends ScopeViewModel {
    private final MutableLiveData<LoadStatus> status = new MutableLiveData<>();

    public BaseViewModel(@androidx.annotation.NonNull Application application) {
        super(application);
    }

    public LiveData<LoadStatus> getStatus() {
        return status;
    }

    protected <T> void async(AsyncCallback<T> asyncCallback, Consumer<T> consumer) {
        RxHelper.create((ObservableOnSubscribe<T>) emitter -> {
            T t = asyncCallback.accept();
            emitter.onNext(t);
            emitter.onComplete();
        }).to(RxLife.to(this)).subscribe(consumer, throwable -> {
            throwable.printStackTrace();
            if (!(throwable instanceof CancellationException)) {
                ToastUtil.show(throwable.getMessage());
            }
        });
    }

    protected <T> void asyncStatus(AsyncCallback<T> asyncCallback, Consumer<T> consumer) {
        RxHelper.create((ObservableOnSubscribe<T>) emitter -> {
            T t = asyncCallback.accept();
            emitter.onNext(t);
            emitter.onComplete();
        }).to(RxLife.to(this)).subscribe(t -> {
            consumer.accept(t);
            status.postValue(LoadStatus.LOADING_END);
        }, throwable -> {
            status.postValue(LoadStatus.FAILED);
            throwable.printStackTrace();
            if (!(throwable instanceof CancellationException)) {
                ToastUtil.show(throwable.getMessage());
            }
        });
    }

    protected <T> void async(AsyncCallback<T> asyncCallback, Consumer<T> consumer, Consumer<Throwable> throwableConsumer) {
        RxHelper.create((ObservableOnSubscribe<T>) emitter -> {
            T t = asyncCallback.accept();
            emitter.onNext(t);
            emitter.onComplete();
        }).to(RxLife.to(this))
                .subscribe(consumer, throwableConsumer);
    }

    protected void async(EmptyCallback emptyCallback) {
        RxHelper.create((ObservableOnSubscribe<Void>) emitter -> {
            emptyCallback.accept();
            emitter.onComplete();
        }).to(RxLife.to(this))
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Void t) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        if (!(e instanceof CancellationException)) {
                            ToastUtil.show(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    protected void async(EmptyCallback emptyCallback, EmptyCallback completeCallback) {
        RxHelper.create((ObservableOnSubscribe<Void>) emitter -> {
            emptyCallback.accept();
            emitter.onComplete();
        }).to(RxLife.to(this))
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Void t) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        if (!(e instanceof CancellationException)) {
                            ToastUtil.show(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        completeCallback.accept();
                    }
                });
    }

    protected <T> void async(PagerCallback<T> pagerCallback, Consumer<T> consumer) {
        RxHelper.create((ObservableOnSubscribe<Pager<T>>) emitter -> {
            Pager<T> pager = pagerCallback.accept();
            emitter.onNext(pager);
            emitter.onComplete();
        }).to(RxLife.to(this))
                .subscribe(tPager -> {
                    consumer.accept(tPager.getData());
                    if (tPager.getPage() < tPager.getTotalPage()) {
                        status.postValue(LoadStatus.LOADING);
                    } else {
                        status.postValue(LoadStatus.LOADING_END);
                    }
                }, throwable -> {
                    status.postValue(LoadStatus.FAILED);
                    throwable.printStackTrace();
                    if (!(throwable instanceof CancellationException)) {
                        ToastUtil.show(throwable.getMessage());
                    }
                });
    }

    protected interface AsyncCallback<T> {
        T accept();
    }

    protected interface EmptyCallback {
        void accept();
    }

    protected interface PagerCallback<T> {
        Pager<T> accept();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    protected static class Pager<T> {
        private Integer page;
        private Integer totalPage;
        private T data;
    }
}
