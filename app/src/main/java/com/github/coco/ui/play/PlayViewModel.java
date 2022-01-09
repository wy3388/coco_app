package com.github.coco.ui.play;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.coco.base.BaseViewModel;
import com.github.coco.common.AppDatabase;
import com.github.coco.dao.EpisodesDao;
import com.github.coco.dao.HistoryDao;
import com.github.coco.dao.PlayDao;
import com.github.coco.dao.PlayHistoryDao;
import com.github.coco.entity.Episodes;
import com.github.coco.entity.History;
import com.github.coco.entity.Play;
import com.github.coco.entity.PlayHistory;
import com.github.coco.utils.ToastUtil;
import com.github.lib.VideoHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.Data;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class PlayViewModel extends BaseViewModel {

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(10,
            15,
            300,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(100));

    public PlayViewModel(@NonNull Application application) {
        super(application);
    }

    private final MutableLiveData<PlayInfo> playInfo = new MutableLiveData<>();

    public LiveData<PlayInfo> getPlayInfo() {
        return playInfo;
    }

    private final PlayDao playDao = AppDatabase.getInstance().playDao();

    private final PlayHistoryDao playHistoryDao = AppDatabase.getInstance().playHistoryDao();

    private final EpisodesDao episodesDao = AppDatabase.getInstance().episodesDao();

    private final HistoryDao historyDao = AppDatabase.getInstance().historyDao();

    public void playInfo(String url, long episodesId, long infoId) {
        async(() -> {
            PlayInfo playInfo = new PlayInfo();
            // 获取剧集
            List<Episodes> episodesList = episodesDao.findAllByInfoId(infoId);
            Collections.reverse(episodesList);
            playInfo.setEpisodes(episodesList);
            // 设置title
            Episodes episodes = episodesDao.findOneByUrl(url);
            playInfo.setUrl(url);
            playInfo.setTitle(episodes.getName());
            // 判断是否有播放历史
            PlayHistory history = playHistoryDao.findOneByUrl(url);
            if (history == null) {
                playInfo.setSourceIndex(0);
            } else {
                playInfo.setPlayUrl(history.getPlayUrl());
                playInfo.setSourceIndex(history.getSourceIndex());
            }
            // 获取播放地址
            List<Play> playList = playDao.findAllByEpisodesId(episodesId);
            if (playList == null || playList.size() == 0) {
                // 没有缓存
                VideoHelper.playList(url, plays -> {
                    Play[] playArr = new Play[plays.size()];
                    for (int i = 0; i < plays.size(); i++) {
                        Play play = new Play();
                        play.setEpisodesId(episodesId);
                        play.setName(plays.get(i).getName());
                        play.setUrl(plays.get(i).getUrl());
                        play.setPlayUrl(plays.get(i).getPlayUrl());
                        play.setCreateTime(System.currentTimeMillis());
                        playArr[i] = play;
                    }
                    playInfo.setPlays(Arrays.asList(playArr));
                    if (playInfo.getPlayUrl() == null || "".equals(playInfo.getPlayUrl())) {
                        playInfo.setPlayUrl(playArr[0].getPlayUrl());
                    }
                    this.playInfo.postValue(playInfo);
                    async(() -> playDao.insert(playArr));
                    // 写入历史记录
                    PlayHistory playHistory = new PlayHistory();
                    playHistory.setUrl(url);
                    playHistory.setSourceIndex(0);
                    playHistory.setTitle(episodes.getName());
                    playHistory.setPlayUrl(plays.get(0).getPlayUrl());
                    playHistory.setCreateTime(System.currentTimeMillis());
                    async(() -> playHistoryDao.insert(playHistory));
                }, throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.show(throwable.getMessage());
                    return null;
                }, executor);
            } else {
                playInfo.setPlays(playList);
                if (playInfo.getPlayUrl() == null || "".equals(playInfo.getPlayUrl())) {
                    playInfo.setPlayUrl(playList.get(0).getPlayUrl());
                }
                this.playInfo.postValue(playInfo);
            }
        });
    }

    public void insertOrUpdatePlayHistory(String url, PlayHistory playHistory) {
        async(() -> {
            PlayHistory history = playHistoryDao.findOneByUrl(url);
            if (history == null) {
                playHistoryDao.insert(playHistory);
            } else {
                history.setPlayUrl(playHistory.getPlayUrl());
                history.setSourceIndex(playHistory.getSourceIndex());
                history.setTitle(playHistory.getTitle());
                playHistoryDao.update(history);
            }
        });
    }

    public void updateHistory(String baseUrl, String episodesUrl, String episodesName, Long episodesId, Integer episodesIndex) {
        async(() -> {
            History history = historyDao.findOneByUrl(baseUrl);
            if (history != null) {
                history.setEpisodesUrl(episodesUrl);
                history.setEpisodesName(episodesName);
                history.setEpisodesId(episodesId);
                history.setEpisodesIndex(episodesIndex);
                async(() -> historyDao.update(history));
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }

    @Data
    public static class PlayInfo {
        private String title;
        private String url;
        private String playUrl;
        private List<Play> plays;
        private List<Episodes> episodes;
        private Integer sourceIndex;
    }
}
