package com.github.coco.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.github.coco.entity.History;

import java.util.List;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
@Dao
public interface HistoryDao {

    @Insert
    void insert(History... histories);

    @Query("select * from history where url = :url")
    History findOneByUrl(String url);

    @Update
    void update(History... histories);

    @Query("delete from history")
    void deleteAll();

    @Query("select * from history order by createTime desc")
    LiveData<List<History>> findAll();
}
