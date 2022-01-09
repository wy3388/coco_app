package com.github.coco.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.github.coco.entity.Play;

import java.util.List;

/**
 * Created on 2022/1/9.
 *
 * @author wy
 */
@Dao
public interface PlayDao {
    @Insert
    void insert(Play... plays);

    @Query("select * from play where episodesId = :episodesId")
    List<Play> findAllByEpisodesId(long episodesId);
}
