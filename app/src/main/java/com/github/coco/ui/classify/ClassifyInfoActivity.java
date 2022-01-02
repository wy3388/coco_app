package com.github.coco.ui.classify;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.coco.R;
import com.github.coco.base.BaseVMActivity;
import com.github.coco.common.BundleBuilder;
import com.github.coco.databinding.ActivityClassifyInfoBinding;
import com.github.coco.ui.classify.vm.ClassifyInfoViewModel;
import com.github.coco.ui.info.InfoActivity;
import com.github.coco.utils.ActivityUtil;
import com.github.lib.bean.Video;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class ClassifyInfoActivity extends BaseVMActivity<ActivityClassifyInfoBinding, ClassifyInfoViewModel> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_classify_info;
    }

    @Override
    protected Class<ClassifyInfoViewModel> mClass() {
        return ClassifyInfoViewModel.class;
    }

    @Override
    protected void init() {
        binding.setLifecycleOwner(this);
        binding.setViewModel(model);
        binding.classifyInfoRv.setAdapter(model.getAdapter());
        binding.classifyInfoRv.setLayoutManager(new LinearLayoutManager(this));
        String type = getIntent().getExtras().getString("type");
        if (type != null && !"".equals(type)) {
            model.loadData(type);
        }
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
