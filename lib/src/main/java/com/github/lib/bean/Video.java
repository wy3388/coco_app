package com.github.lib.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 2021/12/12.
 *
 * @author wy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video {
    private String name;
    private String alias;
    private String year;
    private String type;
    private String info;
    private String url;
    private String image;
}
