package com.github.coco.ui.info;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.coco.base.BaseViewModel;
import com.github.coco.common.AppDatabase;
import com.github.coco.dao.HistoryDao;
import com.github.coco.entity.History;
import com.github.lib.VideoHelper;
import com.github.lib.bean.VideoInfo;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class InfoViewModel extends BaseViewModel {
    public InfoViewModel(@NonNull Application application) {
        super(application);
    }

    private final MutableLiveData<VideoInfo> videoInfo = new MutableLiveData<>();

    public LiveData<VideoInfo> getVideoInfo() {
        return videoInfo;
    }

    private final InfoAdapter adapter = new InfoAdapter();

    public InfoAdapter getAdapter() {
        return adapter;
    }

    public void loadData(String url) {
        async(() -> VideoHelper.info(url), this.videoInfo::postValue);
    }

    private final HistoryDao historyDao = AppDatabase.getInstance().historyDao();

    public void insertHistory(String url, History history) {
        async(() -> {
            History history1 = historyDao.findOneByUrl(url);
            if (history1 == null) {
                async(() -> historyDao.insert(history));
            } else {
                history1.setEpisodesName(history.getEpisodesName());
                history1.setEpisodesUrl(history.getEpisodesUrl());
                async(() -> historyDao.update(history1));
            }
        });
    }
}
