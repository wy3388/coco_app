package com.github.coco.ui.search.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.coco.base.BaseViewModel;
import com.github.coco.ui.search.adapter.SearchInfoAdapter;
import com.github.lib.VideoHelper;
import com.github.lib.bean.Video;

import java.util.List;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class SearchInfoViewModel extends BaseViewModel {
    public SearchInfoViewModel(@NonNull Application application) {
        super(application);
    }

    private final SearchInfoAdapter adapter = new SearchInfoAdapter();

    public SearchInfoAdapter getAdapter() {
        return adapter;
    }

    private final MutableLiveData<List<Video>> videos = new MutableLiveData<>();

    public LiveData<List<Video>> getVideos() {
        return videos;
    }

    private String key = "";

    public void loadData(String key) {
        if ("".equals(this.key)) {
            this.key = key;
        }
        asyncStatus(() -> VideoHelper.search(this.key), this.videos::postValue);
    }

    public void refresh() {
        adapter.setNewInstance(null);
        loadData(key);
    }
}
