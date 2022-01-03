package com.github.coco.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.github.coco.entity.Star;

import java.util.List;

/**
 * Created on 2022/1/3.
 *
 * @author wy
 */
@Dao
public interface StarDao {
    @Insert
    void insert(Star... stars);

    @Query("select * from star order by createTime desc")
    LiveData<List<Star>> findAll();

    @Query("select * from star where url = :url")
    Star findOneByUrl(String url);

    @Delete
    void delete(Star... stars);

    @Query("delete from star where url = :url")
    void deleteByUrl(String url);
}
