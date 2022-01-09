package com.github.coco.ui.info;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.coco.base.BaseViewModel;
import com.github.coco.common.AppDatabase;
import com.github.coco.dao.HistoryDao;
import com.github.coco.dao.InfoDao;
import com.github.coco.dao.StarDao;
import com.github.coco.entity.Episodes;
import com.github.coco.entity.History;
import com.github.coco.entity.Info;
import com.github.coco.entity.Star;
import com.github.coco.utils.ToastUtil;
import com.github.lib.VideoHelper;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class InfoViewModel extends BaseViewModel {
    public InfoViewModel(@NonNull Application application) {
        super(application);
    }

    private final MutableLiveData<Boolean> starStatus = new MutableLiveData<>();

    public LiveData<Boolean> getStarStatus() {
        return starStatus;
    }

    private final HistoryDao historyDao = AppDatabase.getInstance().historyDao();

    private final StarDao starDao = AppDatabase.getInstance().starDao();

    private boolean isClick = false;

    private final InfoAdapter adapter = new InfoAdapter();

    public InfoAdapter getAdapter() {
        return adapter;
    }

    private final MutableLiveData<Info> info = new MutableLiveData<>();

    public LiveData<Info> getInfo() {
        return info;
    }

    private final MutableLiveData<Boolean> infoStatus = new MutableLiveData<>();

    public LiveData<Boolean> getInfoStatus() {
        return infoStatus;
    }

    private final InfoDao infoDao = AppDatabase.getInstance().infoDao();

    private final MutableLiveData<History> history = new MutableLiveData<>();

    public LiveData<History> getHistory() {
        return history;
    }


    public void loadData(String url) {
        async(() -> {
            // 获取本地缓存数据
            Info info1 = infoDao.findOneByUrl(url);
            if (info1 == null) {
                // 从网络获取数据
                async(() -> VideoHelper.info(url), videoInfo -> {
                    Info info2 = new Info();
                    info2.setName(videoInfo.getName());
                    info2.setAlias(videoInfo.getAlias());
                    info2.setYear(videoInfo.getYear());
                    info2.setType(videoInfo.getType());
                    info2.setInfo(videoInfo.getInfo());
                    info2.setUrl(videoInfo.getUrl());
                    info2.setImage(videoInfo.getImage());
                    info2.setCreateTime(System.currentTimeMillis());
                    this.info.postValue(info2);
                    async(() -> {
                        Long id = infoDao.insert(info2);
                        Episodes[] epiArr = new Episodes[videoInfo.getEpisodes().size()];
                        for (int i = 0; i < videoInfo.getEpisodes().size(); i++) {
                            Episodes episodes = new Episodes();
                            episodes.setInfoId(id);
                            episodes.setName(videoInfo.getEpisodes().get(i).getName());
                            episodes.setUrl(videoInfo.getEpisodes().get(i).getUrl());
                            episodes.setCreateTime(System.currentTimeMillis());
                            epiArr[i] = episodes;
                        }
                        async(() -> AppDatabase.getInstance().episodesDao().insert(epiArr));
                    });
                });
            } else {
                this.info.postValue(info1);
            }
        }, () -> infoStatus.postValue(true));
    }

    public void starClick(Info info) {
        if (isClick) {
            Boolean value = starStatus.getValue();
            // 判断是否已经收藏
            if (value != null && getInfo().getValue() != null) {
                // 已收藏
                if (!value) {
                    Star star = new Star();
                    star.setUrl(info.getUrl());
                    star.setName(info.getName());
                    star.setImage(info.getImage());
                    star.setType(info.getType());
                    star.setCreateTime(System.currentTimeMillis());
                    // 保存数据
                    async(() -> starDao.insert(star), () -> starStatus.postValue(true));
                } else { // 取消收藏
                    async(() -> starDao.deleteByUrl(info.getUrl()), () -> starStatus.postValue(false));
                }
            } else {
                ToastUtil.show("请等待数据加载完成");
            }
        }
    }


    public void insertOrUpdateHistory(String url, History history) {
        async(() -> {
            History history1 = historyDao.findOneByUrl(url);
            if (history1 == null) {
                async(() -> historyDao.insert(history));
            } else {
                history1.setEpisodesName(history.getEpisodesName());
                history1.setEpisodesUrl(history.getEpisodesUrl());
                history1.setEpisodesIndex(history.getEpisodesIndex());
                async(() -> historyDao.update(history1));
            }
        });
    }

    public void loadHistory(String url) {
        async(() -> {
            History history = historyDao.findOneByUrl(url);
            if (history != null) {
                this.history.postValue(history);
            }
        });
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
}
