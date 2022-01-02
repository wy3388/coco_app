package com.github.coco.ui.history;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.coco.R;
import com.github.coco.base.BaseFragment;
import com.github.coco.common.AppDatabase;
import com.github.coco.common.BundleBuilder;
import com.github.coco.databinding.FragmentHistoryBinding;
import com.github.coco.entity.History;
import com.github.coco.ui.play.PlayActivity;
import com.github.coco.utils.ActivityUtil;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class HistoryFragment extends BaseFragment<FragmentHistoryBinding> {

    private HistoryAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    protected void init() {
        adapter = new HistoryAdapter();
        binding.historyRv.setAdapter(adapter);
        binding.historyRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.historyRv.setSwipeRefreshLayoutEnabled(false);
        adapter.setOnItemClickListener((baseQuickAdapter, view, position) -> {
            History history = adapter.getData().get(position);
            Bundle bundle = BundleBuilder.builder()
                    .putString("title", history.getEpisodesName())
                    .putString("url", history.getEpisodesUrl())
                    .putBoolean("isHistory", true)
                    .putString("baseUrl", history.getUrl())
                    .build();
            ActivityUtil.start(requireContext(), PlayActivity.class, bundle);
        });
    }

    @Override
    protected void observer() {
        AppDatabase.getInstance().historyDao().findAll().observe(this, histories -> {
            binding.historyRv.setRefreshing(false);
            adapter.setNewInstance(histories);
        });
    }
}
