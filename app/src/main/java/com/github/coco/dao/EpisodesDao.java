package com.github.coco.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.github.coco.entity.Episodes;

import java.util.List;

/**
 * Created on 2022/1/9.
 *
 * @author wy
 */
@Dao
public interface EpisodesDao {
    @Insert
    Long insert(Episodes episodes);

    @Insert
    void insert(Episodes... episodes);

    @Query("select epi.* from episodes epi inner join info i on i.id = epi.infoId where i.url = :url")
    LiveData<List<Episodes>> findAllByUrl(String url);

    @Query("select * from episodes where url = :url")
    Episodes findOneByUrl(String url);

    @Query("select * from episodes where infoId = :id")
    List<Episodes> findAllByInfoId(long id);
}
