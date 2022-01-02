package com.github.coco.ui.classify;

import androidx.recyclerview.widget.GridLayoutManager;

import com.github.coco.R;
import com.github.coco.base.BaseFragment;
import com.github.coco.common.AppDatabase;
import com.github.coco.databinding.FragmentClassifyBinding;

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
        binding.classifyRv.setSwipeRefreshLayoutEnabled(false);
    }

    @Override
    protected void observer() {
        AppDatabase.getInstance().classifyDao().findAll().observe(this, classifies -> {
            adapter.setNewInstance(classifies);
            binding.classifyRv.setRefreshing(false);
        });
    }
}
