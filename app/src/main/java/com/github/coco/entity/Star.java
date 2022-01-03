package com.github.coco.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

/**
 * Created on 2022/1/3.
 *
 * @author wy
 */
@Data
@Entity
public class Star {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String url;
    private String name;
    private String image;
    private String type;
    private Long createTime;
}
