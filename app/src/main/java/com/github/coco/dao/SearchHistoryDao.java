package com.github.coco.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.github.coco.entity.SearchHistory;

import java.util.List;

/**
 * Created on 2022/1/3.
 *
 * @author wy
 */
@Dao
public interface SearchHistoryDao {

    @Insert
    void insert(SearchHistory... searchHistories);

    @Query("select * from searchhistory")
    LiveData<List<SearchHistory>> findAll();

    @Query("delete from searchhistory")
    void deleteAll();
}
