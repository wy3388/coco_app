package com.github.coco.ui.classify;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;

import com.github.coco.R;
import com.github.coco.base.BaseFragment;
import com.github.coco.common.AppDatabase;
import com.github.coco.common.BundleBuilder;
import com.github.coco.databinding.FragmentClassifyBinding;
import com.github.coco.entity.Classify;
import com.github.coco.ui.classify.adapter.ClassifyAdapter;
import com.github.coco.utils.ActivityUtil;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class ClassifyFragment extends BaseFragment<FragmentClassifyBinding> {

    private ClassifyAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classify;
    }

    @Override
    protected void init() {
        adapter = new ClassifyAdapter();
        binding.classifyRv.setAdapter(adapter);
        binding.classifyRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter.setOnItemClickListener((baseQuickAdapter, view, position) -> {
            Classify classify = adapter.getData().get(position);
            Bundle bundle = BundleBuilder.builder()
                    .putString("type", classify.getName())
                    .build();
            ActivityUtil.start(this, ClassifyInfoActivity.class, bundle);
        });
    }

    @Override
    protected void observer() {
        AppDatabase.getInstance().classifyDao().findAll().observe(this, classifies -> adapter.setNewInstance(classifies));
    }
}
