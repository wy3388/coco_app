package com.github.lib.bean;

import java.util.List;

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
public class VideoPlay {
    private String url;

    private List<Source> sources;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Source {
        private String name;
        private String url;
    }
}
