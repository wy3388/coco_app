package com.github.coco.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.coco.base.BaseViewModel;
import com.github.coco.common.AppDatabase;
import com.github.coco.dao.ClassifyDao;
import com.github.coco.entity.Classify;
import com.github.coco.utils.SharedPreferencesUtil;
import com.github.lib.VideoHelper;
import com.github.lib.bean.Video;
import com.github.lib.bean.VideoClassify;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.functions.Consumer;

/**
 * Created on 2022/1/1.
 *
 * @author wy
 */
public class HomeViewModel extends BaseViewModel {

    private final  int processors = Runtime.getRuntime().availableProcessors();

    private ThreadPoolExecutor executor = null;

    private final MutableLiveData<List<Video>> videos = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Video>> getVideos() {
        return videos;
    }

    private final HomeAdapter adapter = new HomeAdapter();

    public HomeAdapter getAdapter() {
        return adapter;
    }

    private final ClassifyDao classifyDao = AppDatabase.getInstance().classifyDao();

    private int page = 1;

    public void loadData() {
        async(() -> {
            VideoClassify videoClassify = VideoHelper.classify("", page);
            return new Pager<>(page, videoClassify.getTotalPage(), videoClassify);
        }, (Consumer<VideoClassify>) videoClassify -> {
            videos.postValue(videoClassify.getVideos());
            // 判断是否是第一次打开
            Integer isFirst = SharedPreferencesUtil.getInt("isFirst");
            if (isFirst == 0) {
                executor = new ThreadPoolExecutor(processors+ 1,
                        processors * 2 + 1,
                        300,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>(processors * 5));
                // 获取封面图片
                VideoHelper.getCoverImage(videoClassify.getClassifies(), classifies -> {
                    Classify[] arr = new Classify[videoClassify.getClassifies().size() - 1];
                    for (int i = 0; i < classifies.size(); i++) {
                        if (i > 0) {
                            Classify classify = new Classify();
                            classify.setName(classifies.get(i).getName());
                            classify.setUrl(classifies.get(i).getUrl());
                            classify.setImageUrl(classifies.get(i).getImageUrl());
                            classify.setCreateTime(System.currentTimeMillis());
                            arr[i - 1] = classify;
                        }
                    }
                    async(() -> classifyDao.insert(arr), () -> SharedPreferencesUtil.putInt("isFirst", 1));
                }, executor);
            }
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

    @Override
    protected void onCleared() {
        super.onCleared();
        if (executor != null) {
            executor.shutdown();
        }
    }
}
