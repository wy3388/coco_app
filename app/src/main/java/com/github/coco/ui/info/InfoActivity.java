package com.github.coco.ui.info;

import android.os.Bundle;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.github.coco.R;
import com.github.coco.base.BaseVMActivity;
import com.github.coco.common.BundleBuilder;
import com.github.coco.common.parcelable.EpisodesParcelable;
import com.github.coco.databinding.ActivityInfoBinding;
import com.github.coco.entity.History;
import com.github.coco.ui.play.PlayActivity;
import com.github.coco.utils.ActivityUtil;
import com.github.lib.bean.VideoInfo;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class InfoActivity extends BaseVMActivity<ActivityInfoBinding, InfoViewModel> {

    private int currentPosition = -1;

    private final History history = new History();

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
            model.loadData(url);
            model.loadStarStatus(url);
            model.loadHistory(url);
        }
        binding.infoRv.setAdapter(model.getAdapter());
        binding.infoRv.setLayoutManager(new GridLayoutManager(this, 3));
        model.getAdapter().setOnItemClickListener((baseQuickAdapter, view, position) -> {
            if (currentPosition != position) {
                history.setSourceIndex(0);
                model.getAdapter().notifyItemChanged(position);
                if (currentPosition > -1) {
                    model.getAdapter().notifyItemChanged(currentPosition);
                }
                currentPosition = position;
            }
            VideoInfo.Episodes episodes = model.getAdapter().getData().get(position);
            ArrayList<EpisodesParcelable> parcelableList = new ArrayList<>();
            for (VideoInfo.Episodes e : model.getAdapter().getData()) {
                EpisodesParcelable parcelable = new EpisodesParcelable();
                parcelable.setName(e.getName());
                parcelable.setUrl(e.getUrl());
                parcelableList.add(parcelable);
            }
            history.setEpisodesUrl(episodes.getUrl());
            history.setEpisodesName(episodes.getName());
            history.setEpisodesIndex(position);
            // 添加历史记录
            model.insertHistory(url, history);
            Bundle bundle = BundleBuilder.builder()
                    .putString("baseUrl", url)
                    .putString("url", episodes.getUrl())
                    .putString("title", episodes.getName())
                    .putParcelableArrayList("episodes", parcelableList)
                    .putInt("episodesIndex", position)
                    .putBoolean("isHistory", isHistory)
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.loadHistory(url);
    }
}
