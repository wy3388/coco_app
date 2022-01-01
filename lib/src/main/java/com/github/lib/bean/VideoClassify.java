package com.github.lib.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 2021/12/18.
 *
 * @author wy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoClassify {
    private Long page;
    private Long totalPage;
    private List<Video> videos;
    private List<Classify> classifies;
}
