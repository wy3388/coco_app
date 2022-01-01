package com.github.lib;

import com.github.lib.bean.Classify;
import com.github.lib.bean.Video;
import com.github.lib.bean.VideoClassify;
import com.github.lib.bean.VideoInfo;
import com.github.lib.bean.VideoPlay;
import com.github.lib.constant.Constant;
import com.github.lib.utils.JsoupUtils;
import com.github.lib.utils.RequestUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2021/12/12.
 *
 * @author wy
 */
public final class VideoHelper {

    /**
     * 搜索
     *
     * @param key 关键字
     * @return {@link List<Video>}
     */
    public static List<Video> search(String key) {
        List<Video> videos = new ArrayList<>();
        String searchUrl = String.format(Constant.SEARCH_URL, key);
        Elements elements = RequestUtils.document(searchUrl).select("#search_list > ul > li");
        for (Element element : elements) {
            String name = JsoupUtils.text(element, "li > h6 > a");
            String alias = JsoupUtils.text(element, "li > div:nth-child(3)");
            String year = JsoupUtils.text(element, "li > div:nth-child(4)");
            String type = element.select("li > span > a").text();
            String info = JsoupUtils.text(element, "li > p");
            String image = Constant.URL + JsoupUtils.attr(element, "src", "li > a > img");
            String url = Constant.URL + JsoupUtils.attr(element, "href", "li > a");
            videos.add(new Video(name, alias, year, type, info, url, image));
        }
        return videos;
    }

    /**
     * 详情
     *
     * @param url url
     * @return {@link VideoInfo}
     */
    public static VideoInfo info(String url) {
        Document document = RequestUtils.document(url);
        String name = JsoupUtils.text(document, "body > div > div > div.col-md-8.detail-left.mb-3 > section.clearfix.mb-3 > div.small > div:nth-child(1)");
        String alias = JsoupUtils.text(document, "body > div > div > div.col-md-8.detail-left.mb-3 > section.clearfix.mb-3 > div.small > div:nth-child(2)");
        String year = JsoupUtils.text(document, "body > div > div > div.col-md-8.detail-left.mb-3 > section.clearfix.mb-3 > div.small > div.row.no-gutters > span:nth-child(2)");
        String type = document.select("body > div > div > div.col-md-8.detail-left.mb-3 > section.clearfix.mb-3 > div.small > div.row.no-gutters > span:nth-child(3)").text();
        String image = Constant.URL + JsoupUtils.attr(document, "src", "body > div > div > div.col-md-8.detail-left.mb-3 > section.clearfix.mb-3 > div.detail-poster.float-left > img");
        String info = JsoupUtils.text(document, "body > div > div > div.col-md-8.detail-left.mb-3 > div.small");
        List<VideoInfo.Episodes> episodes = new ArrayList<>();
        Elements elements = document.select("body > div > div > div.col-md-8.detail-left.mb-3 > section:nth-child(2) > div.ep-panel.mb-3 > ul > li");
        for (Element element : elements) {
            String eName = JsoupUtils.text(element, "li > a");
            String eUrl = Constant.URL + JsoupUtils.attr(element, "href", "li > a");
            episodes.add(new VideoInfo.Episodes(eName, eUrl));
        }
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setName(name);
        videoInfo.setAlias(alias);
        videoInfo.setYear(year);
        videoInfo.setImage(image);
        videoInfo.setInfo(info);
        videoInfo.setType(type);
        videoInfo.setUrl(url);
        videoInfo.setEpisodes(episodes);
        return videoInfo;
    }

    public static VideoPlay play(String url) {
        Document document = RequestUtils.document(url);
        Elements elements = document.select("body > div.container > div:nth-child(2) > ul > li");
        List<VideoPlay.Source> sources = new ArrayList<>();
        for (Element element : elements) {
            String name = JsoupUtils.text(element, "li > a");
            String u = Constant.URL + JsoupUtils.attr(element, "href", "li > a");
            sources.add(new VideoPlay.Source(name, u));
        }
        String playUrl = "";
        if (sources.size() > 0) {
            // 获取播放地址
            Document doc = RequestUtils.document(sources.get(0).getUrl());
            playUrl = JsoupUtils.attr(doc, "src", "#x-video > source");
        }
        return new VideoPlay(playUrl, sources);
    }

    /**
     * 获取播放地址
     *
     * @param url url
     * @return {@code String}
     */
    public static String playUrl(String url) {
        Document document = RequestUtils.document(url);
        return JsoupUtils.attr(document, "src", "#x-video > source");
    }

    /**
     * 获取分类分页数据
     *
     * @param type type
     * @param page page
     * @return {@link VideoClassify}
     */
    public static VideoClassify classify(String type, Long page) {
        String url = String.format(Constant.CLASSIFY_URL, type, page);
        Document doc = RequestUtils.document(url);
        VideoClassify videoClassify = new VideoClassify();
        // 获取分类
        Elements elements1 = doc.select("#contrainer > div.small.font-2.catalog-tags.mb-3 > div:nth-child(3) > ul > li");
        List<Classify> classifies = new ArrayList<>();
        for (Element element : elements1) {
            String name = JsoupUtils.text(element, "li > a");
            String u = Constant.URL + JsoupUtils.attr(element, "href", "li > a");
            classifies.add(new Classify(name, u));
        }
        videoClassify.setClassifies(classifies);
        Elements elements = doc.select("#contrainer > div:nth-child(3) > ul > li");
        videoClassify.setPage(Long.parseLong(page.toString()));
        // 获取totalPage
        String text = JsoupUtils.text(doc, "#contrainer > div.pages.small.mb-3");
        String regex = "/\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            videoClassify.setTotalPage(Long.valueOf(s.substring(1)));
        } else {
            videoClassify.setTotalPage(0L);
        }
        List<Video> videos = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            String name = JsoupUtils.text(element, "li > a:nth-child(2) > h6");
            String u = Constant.URL + JsoupUtils.attr(element, "href", "li > a:nth-child(1)");
            String t = JsoupUtils.text(element, "li > div:nth-child(3)");
            String info = JsoupUtils.text(element, "li > p");
            String image = JsoupUtils.attr(element, "src", "li > a:nth-child(1) > img");
            if (image.equals("")) {
                image = JsoupUtils.attr(element, "data-original", "li > a:nth-child(1) > img");
            }
            image = Constant.URL + image;
            Video video = new Video();
            video.setUrl(u);
            video.setImage(image);
            video.setType(t);
            video.setName(name);
            video.setInfo(info);
            videos.add(video);
        }
        videoClassify.setVideos(videos);
        return videoClassify;
    }
}
