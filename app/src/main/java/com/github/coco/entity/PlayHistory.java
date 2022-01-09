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
public class PlayHistory {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String url;
    private Integer sourceIndex;
    private String title;
    private String playUrl;
    private Long createTime;
}
