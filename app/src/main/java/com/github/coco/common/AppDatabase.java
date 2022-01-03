package com.github.coco.common;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.github.coco.App;
import com.github.coco.dao.ClassifyDao;
import com.github.coco.dao.HistoryDao;
import com.github.coco.dao.StarDao;
import com.github.coco.entity.Classify;
import com.github.coco.entity.History;
import com.github.coco.entity.Star;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
@Database(entities = {Classify.class, History.class, Star.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE = null;

    public abstract ClassifyDao classifyDao();

    public abstract HistoryDao historyDao();

    public abstract StarDao starDao();

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
