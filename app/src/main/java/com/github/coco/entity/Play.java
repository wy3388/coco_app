package com.github.coco.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

/**
 * Created on 2022/1/9.
 *
 * @author wy
 */
@Data
@Entity
public class Play {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long episodesId;
    private String name;
    private String url;
    private String playUrl;
    private Long createTime;
}
