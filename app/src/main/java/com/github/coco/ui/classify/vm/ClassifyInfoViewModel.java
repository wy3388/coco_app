package com.github.coco.ui.classify.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.coco.base.BaseViewModel;
import com.github.coco.ui.classify.adapter.ClassifyInfoAdapter;
import com.github.lib.VideoHelper;
import com.github.lib.bean.Video;
import com.github.lib.bean.VideoClassify;

import java.util.List;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class ClassifyInfoViewModel extends BaseViewModel {
    public ClassifyInfoViewModel(@NonNull Application application) {
        super(application);
    }

    private final MutableLiveData<List<Video>> videos = new MutableLiveData<>();

    public LiveData<List<Video>> getVideos() {
        return videos;
    }

    private final ClassifyInfoAdapter adapter = new ClassifyInfoAdapter();

    public ClassifyInfoAdapter getAdapter() {
        return adapter;
    }

    private int page = 1;

    private String type = "";

    public void loadData(String type) {
        if ("".equals(this.type)) {
            this.type = type;
        }
        async((PagerCallback<VideoClassify>) () -> {
            VideoClassify videoClassify = VideoHelper.classify(this.type, page);
            return new Pager<>(page, videoClassify.getTotalPage(), videoClassify);
        }, videoClassify -> videos.postValue(videoClassify.getVideos()));
    }

    public void loadMore() {
        page++;
        loadData(type);
    }

    public void refresh() {
        adapter.setNewInstance(null);
        page = 1;
        loadData(type);
    }
}
