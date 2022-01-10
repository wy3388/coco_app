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
public class Classify {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;
    private String url;
    private String imageUrl;
    private Long createTime;
}
