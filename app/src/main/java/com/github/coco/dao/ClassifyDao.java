package com.github.coco.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.github.coco.entity.Classify;

import java.util.List;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
@Dao
public interface ClassifyDao {
    @Insert
    void insert(Classify... classifies);

    @Query("select * from classify")
    LiveData<List<Classify>> findAll();
}
