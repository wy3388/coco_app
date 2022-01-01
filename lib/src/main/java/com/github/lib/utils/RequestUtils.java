package com.github.lib.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.http.HttpRequest;

/**
 * Created on 2021/12/12.
 *
 * @author wy
 */
public final class RequestUtils {
    public static String get(String url) {
        Map<String, String> headers = new HashMap<>();
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.55 Safari/537.36 Edg/96.0.1054.43");
        return HttpRequest.get(url)
                .headerMap(headers, true)
                .execute()
                .body();
    }

    public static Document document(String url) {
        return Jsoup.parse(get(url));
    }
}
