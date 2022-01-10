package com.github.coco.ui.star;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.coco.R;
import com.github.coco.base.BaseFragment;
import com.github.coco.common.AppDatabase;
import com.github.coco.common.BundleBuilder;
import com.github.coco.databinding.FragmentStarBinding;
import com.github.coco.entity.Star;
import com.github.coco.ui.info.InfoActivity;
import com.github.coco.utils.ActivityUtil;

/**
 * Created on 2022/1/3.
 *
 * @author wy
 */
public class StarFragment extends BaseFragment<FragmentStarBinding> {

    private StarAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_star;
    }

    @Override
    protected void init() {
        adapter = new StarAdapter();
        binding.starRv.setAdapter(adapter);
        binding.starRv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setEmptyView(R.layout.view_empty);
        adapter.setOnItemClickListener((baseQuickAdapter, view, position) -> {
            Star star = adapter.getData().get(position);
            Bundle bundle = BundleBuilder.builder()
                    .putString("url", star.getUrl())
                    .build();
                ActivityUtil.start(this, InfoActivity.class, bundle);
        });
    }

    @Override
    protected void observer() {
        AppDatabase.getInstance().starDao().findAll().observe(this, stars -> adapter.setNewInstance(stars));
    }
}
