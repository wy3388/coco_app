package com.github.coco.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.coco.base.BaseViewModel;
import com.github.lib.VideoHelper;
import com.github.lib.bean.Video;
import com.github.lib.bean.VideoClassify;

import java.util.List;

import io.reactivex.rxjava3.functions.Consumer;

/**
 * Created on 2022/1/1.
 *
 * @author wy
 */
public class HomeViewModel extends BaseViewModel {
    private final MutableLiveData<List<Video>> videos = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Video>> getVideos() {
        return videos;
    }

    public final HomeAdapter adapter = new HomeAdapter();

    private int page = 1;

    public void loadData() {
        async(() -> {
            VideoClassify videoClassify = VideoHelper.classify("", page);
            return new Pager<>(page, videoClassify.getTotalPage(), videoClassify.getVideos());
        }, (Consumer<List<Video>>) videos::postValue);
    }

    public void loadMore() {
        page++;
        loadData();
    }

    public void refresh() {
        adapter.setNewInstance(null);
        page = 1;
        loadData();
    }
}
