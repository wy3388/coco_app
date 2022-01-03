package com.github.coco.ui.info;

import android.os.Bundle;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.github.coco.R;
import com.github.coco.base.BaseVMActivity;
import com.github.coco.common.BundleBuilder;
import com.github.coco.databinding.ActivityInfoBinding;
import com.github.coco.entity.History;
import com.github.coco.ui.play.PlayActivity;
import com.github.coco.utils.ActivityUtil;
import com.github.lib.bean.VideoInfo;

import java.util.Collections;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class InfoActivity extends BaseVMActivity<ActivityInfoBinding, InfoViewModel> {

    private int currentPosition = -1;

    private final History history = new History();

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
        String url = getIntent().getExtras().getString("url");
        if (url != null && !"".equals(url)) {
            model.loadData(url);
            model.loadStarStatus(url);
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
            VideoInfo.Episodes episodes = model.getAdapter().getData().get(position);
            history.setEpisodesUrl(episodes.getUrl());
            history.setEpisodesName(episodes.getName());
            // 添加历史记录
            model.insertHistory(url, history);
            Bundle bundle = BundleBuilder.builder()
                    .putString("baseUrl", url)
                    .putString("url", episodes.getUrl())
                    .putString("title", episodes.getName())
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
        model.getVideoInfo().observe(this, videoInfo -> {
            history.setUrl(videoInfo.getUrl());
            history.setName(videoInfo.getName());
            history.setImage(videoInfo.getImage());
            history.setType(videoInfo.getType());
            history.setCreateTime(System.currentTimeMillis());
            Collections.reverse(videoInfo.getEpisodes());
            model.getAdapter().setNewInstance(videoInfo.getEpisodes());
        });
    }
}
