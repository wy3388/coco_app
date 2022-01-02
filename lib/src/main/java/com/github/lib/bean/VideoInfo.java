package com.github.lib.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created on 2021/12/12.
 *
 * @author wy
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoInfo extends Video {

    private List<Episodes> episodes;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Episodes {
        private String name;
        private String url;
    }
}
