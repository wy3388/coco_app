package com.github.coco.ui.play;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.coco.base.BaseViewModel;
import com.github.coco.common.AppDatabase;
import com.github.coco.dao.HistoryDao;
import com.github.coco.entity.History;
import com.github.coco.utils.ToastUtil;
import com.github.lib.VideoHelper;
import com.github.lib.bean.VideoInfo;
import com.github.lib.bean.VideoPlay;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class PlayViewModel extends BaseViewModel {

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 15, 300, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100));

    public PlayViewModel(@NonNull Application application) {
        super(application);
    }

    private final MutableLiveData<VideoPlay> videoPlay = new MutableLiveData<>();

    public LiveData<VideoPlay> getVideoPlay() {
        return videoPlay;
    }

    private final HistoryDao historyDao = AppDatabase.getInstance().historyDao();

    private final MutableLiveData<History> history = new MutableLiveData<>();

    public LiveData<History> getHistory() {
        return history;
    }

    private final MutableLiveData<List<VideoInfo.Episodes>> episodes = new MutableLiveData<>();

    public LiveData<List<VideoInfo.Episodes>> getEpisodes() {
        return episodes;
    }

    public void playInfo(String url) {
        VideoHelper.playList(url, plays -> videoPlay.postValue(new VideoPlay(plays)), throwable -> {
            throwable.printStackTrace();
            ToastUtil.show(throwable.getMessage());
            return null;
        }, executor);
    }

    public void updatePlayUrl(String url, String playUrl, int currentPosition, int episodesPosition) {
        async(() -> {
            History history = historyDao.findOneByUrl(url);
            if (history != null) {
                history.setEpisodesIndex(episodesPosition);
                history.setSourceIndex(currentPosition);
                history.setPlayUrl(playUrl);
                async(() -> historyDao.update(history));
            }
        });
    }

    public void findOneByUrl(String url) {
        async(() -> historyDao.findOneByUrl(url), this.history::postValue);
    }

    public void episodesList(String url) {
        async(() -> VideoHelper.episodesList(url), this.episodes::postValue);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
