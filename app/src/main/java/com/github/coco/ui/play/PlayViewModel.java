package com.github.coco.ui.play;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.coco.base.BaseViewModel;
import com.github.lib.VideoHelper;
import com.github.lib.bean.VideoPlay;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class PlayViewModel extends BaseViewModel {
    public PlayViewModel(@NonNull Application application) {
        super(application);
    }

    private final MutableLiveData<VideoPlay> videoPlay = new MutableLiveData<>();

    public LiveData<VideoPlay> getVideoPlay() {
        return videoPlay;
    }

    private final MutableLiveData<String> url = new MutableLiveData<>();

    public LiveData<String> getUrl() {
        return url;
    }

    private final PlaySourceAdapter adapter = new PlaySourceAdapter();

    public PlaySourceAdapter getAdapter() {
        return adapter;
    }

    public void playInfo(String url) {
        async(() -> VideoHelper.playInfo(url), this.videoPlay::postValue);
    }

    public void playUrl(String url) {
        async(() -> VideoHelper.playUrl(url), this.url::postValue);
    }
}
