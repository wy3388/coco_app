package com.github.lib.utils;

import androidx.annotation.NonNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created on 2021/12/12.
 *
 * @author wy
 */
public final class JsoupUtils {

    @NonNull
    public static String text(@NonNull Element element, String cssQuery) {
        Element el = element.selectFirst(cssQuery);
        if (el == null) {
            return "";
        }
        return el.text();
    }

    @NonNull
    public static String attr(@NonNull Element element, String attributeKey, String cssQuery) {
        Element el = element.selectFirst(cssQuery);
        if (el == null) {
            return "";
        }
        return el.attr(attributeKey);
    }

    @NonNull
    public static String text(@NonNull Document document, String cssQuery) {
        Element el = document.selectFirst(cssQuery);
        if (el == null) {
            return "";
        }
        return el.text();
    }

    @NonNull
    public static String attr(@NonNull Document document, String attributeKey, String cssQuery) {
        Element el = document.selectFirst(cssQuery);
        if (el == null) {
            return "";
        }
        return el.attr(attributeKey);
    }
}
