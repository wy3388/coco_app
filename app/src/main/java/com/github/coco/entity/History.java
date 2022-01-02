package com.github.coco.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
@Data
@Entity
public class History {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String url;
    private String name;
    private String image;
    private String type;
    private Integer sourceIndex;
    private String playUrl;
    private Long createTime;
    private String episodesUrl;
    private String episodesName;
}
