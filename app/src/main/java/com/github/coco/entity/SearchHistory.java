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
public class SearchHistory {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String key;
    private Long createTime;
}
