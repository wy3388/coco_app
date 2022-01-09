package com.github.coco.ui.info;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.coco.base.BaseViewModel;
import com.github.coco.common.AppDatabase;
import com.github.coco.dao.HistoryDao;
import com.github.coco.dao.StarDao;
import com.github.coco.entity.History;
import com.github.coco.entity.Star;
import com.github.coco.utils.ToastUtil;
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

    private final MutableLiveData<Boolean> starStatus = new MutableLiveData<>();

    public LiveData<Boolean> getStarStatus() {
        return starStatus;
    }

    public void loadData(String url) {
        async(() -> VideoHelper.info(url), this.videoInfo::postValue);
    }

    private final HistoryDao historyDao = AppDatabase.getInstance().historyDao();

    private final StarDao starDao = AppDatabase.getInstance().starDao();

    private boolean isClick = false;

    private final MutableLiveData<History> history = new MutableLiveData<>();

    public LiveData<History> getHistory() {
        return history;
    }

    public void insertHistory(String url, History history) {
        async(() -> {
            History history1 = historyDao.findOneByUrl(url);
            if (history1 == null) {
                async(() -> historyDao.insert(history));
            } else {
                history1.setEpisodesName(history.getEpisodesName());
                history1.setEpisodesUrl(history.getEpisodesUrl());
                history1.setEpisodesIndex(history.getEpisodesIndex());
                if (history.getSourceIndex() != null) {
                    history1.setSourceIndex(history.getSourceIndex());
                }
                async(() -> historyDao.update(history1));
            }
        });
    }

    public void loadHistory(String url) {
        async(() -> historyDao.findOneByUrl(url), this.history::postValue);
    }

    public void loadStarStatus(String url) {
        async(() -> {
            Star star = starDao.findOneByUrl(url);
            if (star != null) {
                starStatus.postValue(true);
            } else {
                starStatus.postValue(false);
            }
        }, () -> isClick = true);
    }

    public void starClick(VideoInfo videoInfo) {
        if (isClick) {
            Boolean value = starStatus.getValue();
            // 判断是否已经收藏
            if (value != null && getVideoInfo().getValue() != null) {
                // 已收藏
                if (!value) {
                    Star star = new Star();
                    star.setUrl(videoInfo.getUrl());
                    star.setName(videoInfo.getName());
                    star.setImage(videoInfo.getImage());
                    star.setType(videoInfo.getType());
                    star.setCreateTime(System.currentTimeMillis());
                    // 保存数据
                    async(() -> starDao.insert(star), () -> starStatus.postValue(true));
                } else { // 取消收藏
                    async(() -> starDao.deleteByUrl(videoInfo.getUrl()), () -> starStatus.postValue(false));
                }
            } else {
                ToastUtil.show("请等待数据加载完成");
            }
        }
    }
}
