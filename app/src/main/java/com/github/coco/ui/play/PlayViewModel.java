package com.github.coco.ui.play;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.coco.base.BaseViewModel;
import com.github.coco.common.AppDatabase;
import com.github.coco.dao.HistoryDao;
import com.github.coco.entity.History;
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

    private final HistoryDao historyDao = AppDatabase.getInstance().historyDao();

    private final MutableLiveData<History> history = new MutableLiveData<>();

    public LiveData<History> getHistory() {
        return history;
    }

    public void playInfo(String url) {
        async(() -> VideoHelper.playInfo(url), this.videoPlay::postValue);
    }

    public void playUrl(String url) {
        async(() -> VideoHelper.playUrl(url), this.url::postValue);
    }

    public void updatePlayUrl(String url, String playUrl, int currentPosition) {
        async(() -> {
            History history = historyDao.findOneByUrl(url);
            if (history != null) {
                history.setSourceIndex(currentPosition);
                history.setPlayUrl(playUrl);
                async(() -> historyDao.update(history));
            }
        });
    }

    public void findOneByUrl(String url) {
        async(() -> historyDao.findOneByUrl(url), this.history::postValue);
    }
}
