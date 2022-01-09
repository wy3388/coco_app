package com.github.coco.ui.info;

import android.os.Bundle;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.github.coco.R;
import com.github.coco.base.BaseVMActivity;
import com.github.coco.common.AppDatabase;
import com.github.coco.common.BundleBuilder;
import com.github.coco.databinding.ActivityInfoBinding;
import com.github.coco.entity.Episodes;
import com.github.coco.entity.History;
import com.github.coco.ui.play.PlayActivity;
import com.github.coco.utils.ActivityUtil;

import java.util.Collections;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class InfoActivity extends BaseVMActivity<ActivityInfoBinding, InfoViewModel> {

    private int currentPosition = -1;

    private History history = null;

    private boolean isHistory = false;

    private String url = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_info;
    }

    @Override
    protected Class<InfoViewModel> mClass() {
        return InfoViewModel.class;
    }

    @Override
    protected void init() {
        binding.setLifecycleOwner(this);
        binding.setViewModel(model);
        url = getIntent().getExtras().getString("url");
        if (url != null && !"".equals(url)) {
            binding.swipeRefreshLayout.setRefreshing(true);
            model.loadData(url);
            model.loadStarStatus(url);
            model.loadHistory(url);
        }
        binding.infoRv.setAdapter(model.getAdapter());
        binding.infoRv.setLayoutManager(new GridLayoutManager(this, 3));
        model.getAdapter().setOnItemClickListener((baseQuickAdapter, view, position) -> {
            if (currentPosition != position) {
                model.getAdapter().notifyItemChanged(position);
                if (currentPosition > -1) {
                    model.getAdapter().notifyItemChanged(currentPosition);
                }
                currentPosition = position;
            }
            Episodes episodes = model.getAdapter().getData().get(position);
            history.setEpisodesUrl(episodes.getUrl());
            history.setEpisodesName(episodes.getName());
            history.setEpisodesIndex(position);
            // 添加历史记录
            model.insertOrUpdateHistory(url, history);
            Bundle bundle = BundleBuilder.builder()
                    .putString("baseUrl", url)
                    .putString("url", episodes.getUrl())
                    .putLong("infoId", episodes.getInfoId())
                    .putLong("episodesId", episodes.getId())
                    .putInt("episodesPosition", position)
                    .build();
            ActivityUtil.start(this, PlayActivity.class, bundle);
        });
        model.getAdapter().setOnSelectedListener((holder, position) -> {
            if (currentPosition > -1) {
                TextView textView = holder.itemView.findViewById(R.id.text_view);
                CardView cardView = holder.itemView.findViewById(R.id.card_view);
                if (currentPosition == position) {
                    cardView.setCardBackgroundColor(getResources().getColor(R.color.primary, null));
                    textView.setTextColor(getResources().getColor(R.color.white, null));
                } else {
                    cardView.setCardBackgroundColor(getResources().getColor(R.color.white, null));
                    textView.setTextColor(getResources().getColor(R.color.default_text_color, null));
                }
            }
        });
    }

    @Override
    protected void observer() {
        model.getInfo().observe(this, info -> {
            history = new History();
            history.setUrl(info.getUrl());
            history.setName(info.getName());
            history.setImage(info.getImage());
            history.setType(info.getType());
            history.setCreateTime(System.currentTimeMillis());
        });
        model.getHistory().observe(this, history1 -> {
            if (history1 == null) {
                isHistory = false;
            } else {
                isHistory = true;
                int tmp = currentPosition;
                currentPosition = history1.getEpisodesIndex();
                model.getAdapter().notifyItemChanged(currentPosition);
                model.getAdapter().notifyItemChanged(tmp);
            }
        });
        AppDatabase.getInstance().episodesDao().findAllByUrl(url).observe(this, episodes -> {
            Collections.reverse(episodes);
            model.getAdapter().setNewInstance(episodes);
        });
        model.getInfoStatus().observe(this, aBoolean -> {
            if (aBoolean) {
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.loadHistory(url);
    }
}
