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
public class Info {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;
    private String alias;
    private String year;
    private String type;
    private String info;
    private String url;
    private String image;
    private Long createTime;
}
