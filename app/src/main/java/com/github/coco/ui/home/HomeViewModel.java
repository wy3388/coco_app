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
                List<com.github.lib.bean.Classify> classifyList = videoClassify.getClassifies();
                Classify[] classifies = new Classify[videoClassify.getClassifies().size() - 1];
                for (int i = 0; i < classifyList.size(); i++) {
                    if (i > 0) {
                        Classify classify = new Classify();
                        classify.setName(classifyList.get(i).getName());
                        classify.setUrl(classifyList.get(i).getUrl());
                        classify.setCreateTime(System.currentTimeMillis());
                        classifies[i - 1] = classify;
                    }
                }
                async(() -> classifyDao.insert(classifies), () -> SharedPreferencesUtil.putInt("isFirst", 1));
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
}
