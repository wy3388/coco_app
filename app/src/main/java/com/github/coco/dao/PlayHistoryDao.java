package com.github.coco.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.github.coco.entity.PlayHistory;

/**
 * Created on 2022/1/9.
 *
 * @author wy
 */
@Dao
public interface PlayHistoryDao {
    @Insert
    void insert(PlayHistory... playHistories);

    @Query("select * from playhistory where url = :url")
    PlayHistory findOneByUrl(String url);

    @Update
    void update(PlayHistory... playHistories);
}
