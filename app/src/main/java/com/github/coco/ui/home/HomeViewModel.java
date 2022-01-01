package com.github.coco.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.coco.base.BaseViewModel;
import com.github.lib.VideoHelper;
import com.github.lib.bean.Video;
import com.github.lib.bean.VideoClassify;

import java.util.List;

/**
 * Created on 2022/1/1.
 *
 * @author wy
 */
public class HomeViewModel extends BaseViewModel {
    private final MutableLiveData<List<Video>> videos = new MutableLiveData<>();

    public LiveData<List<Video>> getVideos() {
        return videos;
    }

    public final HomeAdapter adapter = new HomeAdapter();

    private int page = 1;

    public void loadData() {
        async(() -> {
            VideoClassify videoClassify = VideoHelper.classify("", page);
            videos.postValue(videoClassify.getVideos());
            return new Pager(page, videoClassify.getTotalPage());
        });
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
