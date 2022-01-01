package com.github.coco.common;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.github.coco.views.IImageView;
import com.github.coco.views.IRecyclerView;

/**
 * Created on 2022/1/1.
 *
 * @author wy
 */
public class CustomBinding {

    @BindingAdapter("imageUrl")
    public static void loadImage(IImageView view, String url) {
        if (url != null && !"".equals(url)) {
            view.setImageUrl(view, url);
        }
    }

    @BindingAdapter("setRefreshListener")
    public static void setRefreshListener(IRecyclerView view, InverseBindingListener listener) {
        view.setOnRefreshListener(listener::onChange);
    }

    @BindingAdapter("setLoadMoreListener")
    public static void setLoadMoreListener(IRecyclerView view, InverseBindingListener listener) {
        view.getAdapter().getLoadMoreModule().setOnLoadMoreListener(listener::onChange);
    }

    @BindingAdapter("notifyAdapter")
    public static void notifyAdapter(IRecyclerView view, LoadStatus status) {
        if (status != null) {
            view.setRefreshing(false);
            switch (status) {
                case LOADING:
                    view.getAdapter().getLoadMoreModule().loadMoreComplete();
                    break;
                case LOADING_END:
                    view.getAdapter().getLoadMoreModule().loadMoreEnd();
                    break;
                default:
                    view.getAdapter().getLoadMoreModule().loadMoreFail();
                    break;
            }
        }
    }
}
