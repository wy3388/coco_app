package com.github.coco.ui.history;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.coco.R;
import com.github.coco.base.BaseVMFragment;
import com.github.coco.common.AppDatabase;
import com.github.coco.common.BundleBuilder;
import com.github.coco.databinding.FragmentHistoryBinding;
import com.github.coco.entity.History;
import com.github.coco.ui.play.PlayActivity;
import com.github.coco.utils.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class HistoryFragment extends BaseVMFragment<FragmentHistoryBinding, HistoryViewModel> {

    private HistoryAdapter adapter;
    private boolean selectType = false;
    private boolean selected = false;
    private final List<History> selectList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    protected Class<HistoryViewModel> mClass() {
        return HistoryViewModel.class;
    }

    @Override
    protected void init() {
        adapter = new HistoryAdapter();
        binding.historyRv.setAdapter(adapter);
        binding.historyRv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setEmptyView(R.layout.view_empty);
        binding.editBtn.setOnClickListener(view -> {
            if (!selectType) {
                selectList.clear();
                selectType = true;
                adapter.setSelect(true);
                adapter.selected(false);
                binding.editTv.setText("取消");
                binding.deleteBtn.setVisibility(View.VISIBLE);
                binding.deleteView.setVisibility(View.VISIBLE);
            } else {
                selectType = false;
                adapter.setSelect(false);
                binding.editTv.setText("编辑");
                binding.deleteBtn.setVisibility(View.GONE);
                binding.deleteView.setVisibility(View.GONE);
            }
        });
        binding.selectAllBtn.setOnClickListener(view -> {
            if (selectType) {
                // 全选
                if (!selected) {
                    selected = true;
                    selectList.clear();
                    selectList.addAll(adapter.getData());
                    adapter.selectedAll(true);
                    binding.selectAllBtn.setText("反选");
                } else { // 反选
                    selectList.clear();
                    adapter.selectedAll(false);
                    selected = false;
                    binding.selectAllBtn.setText("全选");
                }
            }
        });
        binding.deleteBtn.setOnClickListener(view -> {
            // 删除数据
            History[] histories = new History[selectList.size()];
            for (int i = 0; i < selectList.size(); i++) {
                histories[i] = selectList.get(i);
            }
            model.deleteHistory(histories);
        });
        adapter.setOnItemClickListener((baseQuickAdapter, view, position) -> {
            if (selectType) {
                if (!selectList.contains(adapter.getData().get(position))) {
                    selectList.add(adapter.getData().get(position));
                    adapter.selected(true);
                } else {
                    selectList.remove(adapter.getData().get(position));
                    adapter.selected(false);
                }
                adapter.notifyItemChanged(position);
            } else {
                History history = adapter.getData().get(position);
                Bundle bundle = BundleBuilder.builder()
                        .putString("baseUrl", history.getUrl())
                        .putString("url", history.getEpisodesUrl())
                        .putLong("infoId", history.getInfoId())
                        .putLong("episodesId", history.getEpisodesId())
                        .putInt("episodesPosition", history.getEpisodesIndex())
                        .build();
                ActivityUtil.start(this, PlayActivity.class, bundle);
            }
        });
    }

    @Override
    protected void observer() {
        AppDatabase.getInstance().historyDao().findAll().observe(this, histories -> adapter.setNewInstance(histories));
    }
}
