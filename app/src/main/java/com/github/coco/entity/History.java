package com.github.coco.entity;

import lombok.Data;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
@Data
//@Entity
public class History {
    private Long id;
    private String url;
    private String name;
    private String image;
    private String type;
    private Long createTime;
}
