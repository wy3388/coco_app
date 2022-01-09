package com.github.coco.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.github.coco.entity.Info;

/**
 * Created on 2022/1/9.
 *
 * @author wy
 */
@Dao
public interface InfoDao {

    @Insert
    Long insert(Info info);

    @Insert
    void insert(Info... infoArr);

    @Query("select * from info where url = :url limit 1")
    Info findOneByUrl(String url);
}
