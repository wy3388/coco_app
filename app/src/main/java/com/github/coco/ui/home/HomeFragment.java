package com.github.coco.ui.home;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.coco.R;
import com.github.coco.base.BaseVMFragment;
import com.github.coco.common.BundleBuilder;
import com.github.coco.databinding.FragmentHomeBinding;
import com.github.coco.ui.info.InfoActivity;
import com.github.coco.utils.ActivityUtil;
import com.github.lib.bean.Video;

/**
 * Created on 2022/1/1.
 *
 * @author wy
 */
public class HomeFragment extends BaseVMFragment<FragmentHomeBinding, HomeViewModel> {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected Class<HomeViewModel> mClass() {
        return HomeViewModel.class;
    }

    @Override
    protected void init() {
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(model);
        binding.homeRv.setAdapter(model.getAdapter());
        binding.homeRv.setLayoutManager(new LinearLayoutManager(getContext()));
        model.getAdapter().setEmptyView(R.layout.view_empty);
        model.loadData();
        model.getAdapter().setOnItemClickListener((baseQuickAdapter, view, position) -> {
            Video video = model.getAdapter().getData().get(position);
            Bundle bundle = BundleBuilder.builder()
                    .putString("url", video.getUrl())
                    .build();
            ActivityUtil.start(this, InfoActivity.class, bundle);
        });
    }

    @Override
    protected void observer() {
        model.getVideos().observe(this, videos -> model.getAdapter().addData(videos));
    }

}
