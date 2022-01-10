package com.github.lib;

import com.github.lib.bean.Classify;
import com.github.lib.bean.Video;
import com.github.lib.bean.VideoClassify;
import com.github.lib.bean.VideoInfo;
import com.github.lib.bean.VideoPlay;
import com.github.lib.constant.Constant;
import com.github.lib.utils.JsoupUtil;
import com.github.lib.utils.RequestUtil;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        Elements elements = RequestUtil.document(searchUrl).select("#search_list > ul > li");
        for (Element element : elements) {
            String name = JsoupUtil.text(element, "li > h6 > a");
            String alias = JsoupUtil.text(element, "li > div:nth-child(3)");
            String year = JsoupUtil.text(element, "li > div:nth-child(4)");
            String type = element.select("li > span > a").text();
            String info = JsoupUtil.text(element, "li > p");
            String image = Constant.URL + JsoupUtil.attr(element, "src", "li > a > img");
            String url = Constant.URL + JsoupUtil.attr(element, "href", "li > a");
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
        Document document = RequestUtil.document(url);
        String name = JsoupUtil.text(document, "body > div > div > div.col-md-8.detail-left.mb-3 > section.clearfix.mb-3 > div.small > div:nth-child(1)");
        String alias = JsoupUtil.text(document, "body > div > div > div.col-md-8.detail-left.mb-3 > section.clearfix.mb-3 > div.small > div:nth-child(2)");
        String year = JsoupUtil.text(document, "body > div > div > div.col-md-8.detail-left.mb-3 > section.clearfix.mb-3 > div.small > div.row.no-gutters > span:nth-child(2)");
        String type = document.select("body > div > div > div.col-md-8.detail-left.mb-3 > section.clearfix.mb-3 > div.small > div.row.no-gutters > span:nth-child(3)").text();
        String image = Constant.URL + JsoupUtil.attr(document, "src", "body > div > div > div.col-md-8.detail-left.mb-3 > section.clearfix.mb-3 > div.detail-poster.float-left > img");
        String info = JsoupUtil.text(document, "body > div > div > div.col-md-8.detail-left.mb-3 > div.small");
        List<VideoInfo.Episodes> episodes = new ArrayList<>();
        Elements elements = document.select("body > div > div > div.col-md-8.detail-left.mb-3 > section:nth-child(2) > div.ep-panel.mb-3 > ul > li");
        for (Element element : elements) {
            String eName = JsoupUtil.text(element, "li > a");
            String eUrl = Constant.URL + JsoupUtil.attr(element, "href", "li > a");
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

    /**
     * 获取播放列表
     *
     * @param url url
     * @return {@link List}
     */
    public static List<VideoInfo.Episodes> episodesList(String url) {
        Document document = RequestUtil.document(url);
        Elements elements = document.select("body > div.container > section > div.ep-panel.mb-3 > ul > li");
        List<VideoInfo.Episodes> list = new ArrayList<>();
        for (Element element : elements) {
            String name = JsoupUtil.text(element, "li > a");
            String u = Constant.URL + JsoupUtil.attr(element, "href", "li > a");
            list.add(new VideoInfo.Episodes(name, u));
        }
        return list;
    }

    /**
     * 获取播放源列表
     *
     * @param url url
     */
    @SuppressWarnings("unchecked")
    public static void playList(String url, Consumer<List<VideoPlay.Play>> consumer, Function<Throwable, Void> function, ThreadPoolExecutor executor) {
        CompletableFuture.supplyAsync(() -> RequestUtil.document(url), executor).thenApply(document -> {
            Elements elements = document.select("body > div.container > div:nth-child(2) > ul > li");
            List<CompletableFuture<VideoPlay.Play>> futures = new ArrayList<>();
            for (Element element : elements) {
                String name = JsoupUtil.text(element, "li > a");
                String u = Constant.URL + JsoupUtil.attr(element, "href", "li > a");
                VideoPlay.Play play = new VideoPlay.Play();
                play.setName(name);
                play.setUrl(u);
                CompletableFuture<VideoPlay.Play> future = CompletableFuture.supplyAsync(() -> {
                    // 获取播放地址
                    Document doc = RequestUtil.document(play.getUrl());
                    String playUrl = JsoupUtil.attr(doc, "src", "#x-video > source");
                    play.setPlayUrl(playUrl);
                    return play;
                }, executor);
                futures.add(future);
            }
            return futures;
        }).thenApply((Function<List<CompletableFuture<VideoPlay.Play>>, Void>) completableFutures -> {
            CompletableFuture<VideoPlay.Play>[] array = new CompletableFuture[completableFutures.size()];
            for (int i = 0; i < completableFutures.size(); i++) {
                array[i] = completableFutures.get(i);
            }
            CompletableFuture.allOf(array).thenApply(unused -> completableFutures.stream().map(playCompletableFuture -> {
                try {
                    return playCompletableFuture.get(3, TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }).filter(play -> {
                if (play == null) {
                    return false;
                }
                return play.getPlayUrl() != null && !"".equals(play.getPlayUrl());
            }).collect(Collectors.toList())).thenAccept(consumer).exceptionally(function);
            return null;
        });
    }

    @SuppressWarnings("unchecked")
    public static List<Classify> getCoverImage(List<Classify> classifies, Consumer<List<Classify>> consumer, ThreadPoolExecutor executor) {
        List<CompletableFuture<Classify>> futures = new ArrayList<>();
        for (Classify classify : classifies) {
            CompletableFuture<Classify> future = CompletableFuture.supplyAsync(() -> {
                // 获取封面图片
                Document document = RequestUtil.document(classify.getUrl());
                String imageUrl = Constant.URL +
                        JsoupUtil.attr(document, "src", "#contrainer > div:nth-child(3) > ul > li:nth-child(1) > a > img");
                classify.setImageUrl(imageUrl);
                return classify;
            }, executor);
            futures.add(future);
        }
        CompletableFuture<Classify>[] arr = new CompletableFuture[futures.size()];
        for (int i = 0; i < futures.size(); i++) {
            arr[i] = futures.get(i);
        }
        CompletableFuture.allOf(futures.toArray(arr)).thenApply(unused -> futures.stream().map(classifyCompletableFuture -> {
            try {
                return classifyCompletableFuture.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList())).thenAccept(consumer).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
        return null;
    }

    /**
     * 获取分类分页数据
     *
     * @param type type
     * @param page page
     * @return {@link VideoClassify}
     */
    public static VideoClassify classify(String type, Integer page) {
        String url = String.format(Constant.CLASSIFY_URL, type, page);
        Document doc = RequestUtil.document(url);
        VideoClassify videoClassify = new VideoClassify();
        // 获取分类
        Elements elements1 = doc.select("#contrainer > div.small.font-2.catalog-tags.mb-3 > div:nth-child(3) > ul > li");
        List<Classify> classifies = new ArrayList<>();
        for (Element element : elements1) {
            String name = JsoupUtil.text(element, "li > a");
            String u = Constant.URL + JsoupUtil.attr(element, "href", "li > a");
            classifies.add(new Classify(name, u, null));
        }
        videoClassify.setClassifies(classifies);
        Elements elements = doc.select("#contrainer > div:nth-child(3) > ul > li");
        videoClassify.setPage(Integer.parseInt(page.toString()));
        // 获取totalPage
        String text = JsoupUtil.text(doc, "#contrainer > div.pages.small.mb-3");
        String regex = "/\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            videoClassify.setTotalPage(Integer.parseInt(s.substring(1)));
        } else {
            videoClassify.setTotalPage(0);
        }
        List<Video> videos = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            String name = JsoupUtil.text(element, "li > a:nth-child(2) > h6");
            String u = Constant.URL + JsoupUtil.attr(element, "href", "li > a:nth-child(1)");
            String t = JsoupUtil.text(element, "li > div:nth-child(3)");
            String info = JsoupUtil.text(element, "li > p");
            String image = JsoupUtil.attr(element, "src", "li > a:nth-child(1) > img");
            if (image.equals("")) {
                image = JsoupUtil.attr(element, "data-original", "li > a:nth-child(1) > img");
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
