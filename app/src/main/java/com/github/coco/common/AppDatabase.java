package com.github.coco.common;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.github.coco.App;
import com.github.coco.dao.ClassifyDao;
import com.github.coco.dao.EpisodesDao;
import com.github.coco.dao.HistoryDao;
import com.github.coco.dao.InfoDao;
import com.github.coco.dao.PlayDao;
import com.github.coco.dao.PlayHistoryDao;
import com.github.coco.dao.SearchHistoryDao;
import com.github.coco.dao.StarDao;
import com.github.coco.entity.Classify;
import com.github.coco.entity.Episodes;
import com.github.coco.entity.History;
import com.github.coco.entity.Info;
import com.github.coco.entity.Play;
import com.github.coco.entity.PlayHistory;
import com.github.coco.entity.SearchHistory;
import com.github.coco.entity.Star;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
@Database(entities =
        {Classify.class, History.class, Star.class, SearchHistory.class, Info.class, Episodes.class, Play.class, PlayHistory.class},
        version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE = null;

    public abstract ClassifyDao classifyDao();

    public abstract HistoryDao historyDao();

    public abstract StarDao starDao();

    public abstract InfoDao infoDao();

    public abstract EpisodesDao episodesDao();

    public abstract PlayDao playDao();

    public abstract SearchHistoryDao searchHistoryDao();

    public abstract PlayHistoryDao playHistoryDao();

    public static AppDatabase getInstance() {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(App.getContext(), AppDatabase.class, "coco.db").build();
                }
            }
        }
        return INSTANCE;
    }
}
