package com.github.lib.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 2021/12/19.
 *
 * @author wy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Classify {
    private String name;
    private String url;
    private String imageUrl;
}
