package com.github.coco.ui.play;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.coco.R;
import com.github.coco.databinding.ActivityPlayBinding;
import com.github.coco.entity.Episodes;
import com.github.coco.entity.Play;
import com.github.coco.entity.PlayHistory;

import cn.jzvd.Jzvd;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class PlayActivity extends AppCompatActivity {

    private int currentPosition = 0;
    private int episodesPosition = -1;
    private PlaySourceAdapter sourceAdapter;
    private PlayEpisodesAdapter episodesAdapter;
    private String url = "";
    private boolean flag = true;

    private PlayViewModel model;
    private ActivityPlayBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play);
        model = new ViewModelProvider(this).get(PlayViewModel.class);
        init();
        observer();
    }

    protected void init() {
        sourceAdapter = new PlaySourceAdapter();
        episodesAdapter = new PlayEpisodesAdapter();
        url = getIntent().getExtras().getString("url");
        String baseUrl = getIntent().getExtras().getString("baseUrl");
        long episodesId = getIntent().getExtras().getLong("episodesId", -1);
        long infoId = getIntent().getExtras().getLong("infoId", -1);
        episodesPosition = getIntent().getExtras().getInt("episodesPosition", -1);
        if (url != null && !"".equals(url) && episodesId != -1) {
            model.playInfo(url, episodesId, infoId);
        }
        binding.episodesRv.setLayoutManager(new GridLayoutManager(this, 3));
        binding.episodesRv.setAdapter(episodesAdapter);
        binding.sourceRv.setAdapter(sourceAdapter);
        binding.sourceRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        sourceAdapter.setOnItemClickListener((baseQuickAdapter, view, position) -> {
            if (currentPosition == position) {
                return;
            }
            sourceAdapter.notifyItemChanged(position);
            sourceAdapter.notifyItemChanged(currentPosition);
            currentPosition = position;
            Play play = sourceAdapter.getData().get(position);
            Episodes episodes = episodesAdapter.getData().get(episodesPosition);
            // 更新播放历史
            PlayHistory playHistory = new PlayHistory();
            playHistory.setTitle(episodes.getName());
            playHistory.setSourceIndex(position);
            playHistory.setPlayUrl(play.getPlayUrl());
            model.insertOrUpdatePlayHistory(url, playHistory);
            Jzvd.releaseAllVideos();
            binding.player.setUp(play.getPlayUrl(), episodes.getName());
            binding.player.startVideoAfterPreloading();
        });
        episodesAdapter.setOnItemClickListener((baseQuickAdapter, view, position) -> {
            flag = false;
            if (episodesPosition == -1 || episodesPosition == position) {
                return;
            }
            episodesAdapter.notifyItemChanged(position);
            episodesAdapter.notifyItemChanged(episodesPosition);
            episodesPosition = position;
            // 重新获取播放源
            Episodes episodes = episodesAdapter.getData().get(position);
            sourceAdapter.setNewInstance(null);
            currentPosition = 0;
            model.playInfo(episodes.getUrl(), episodes.getId(), episodes.getInfoId());
            // 更新历史记录
            model.updateHistory(baseUrl, episodes.getUrl(), episodes.getName(), episodes.getId(), position);
            Jzvd.releaseAllVideos();
        });
        binding.player.setNormalClickListener(view -> finish());
        sourceAdapter.setOnSelectedListener((holder, position) -> {
            TextView textView = holder.itemView.findViewById(R.id.text_view);
            CardView cardView = holder.itemView.findViewById(R.id.card_view);
            if (currentPosition == position) {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.primary, null));
                textView.setTextColor(getResources().getColor(R.color.white, null));
            } else {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.white, null));
                textView.setTextColor(getResources().getColor(R.color.default_text_color, null));
            }
        });
        episodesAdapter.setOnSelectedListener((holder, position) -> {
            TextView textView = holder.itemView.findViewById(R.id.text_view);
            CardView cardView = holder.itemView.findViewById(R.id.card_view);
            if (episodesPosition == position) {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.primary, null));
                textView.setTextColor(getResources().getColor(R.color.white, null));
            } else {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.white, null));
                textView.setTextColor(getResources().getColor(R.color.default_text_color, null));
            }
        });

    }

    protected void observer() {
        model.getPlayInfo().observe(this, playInfo -> {
            if (flag) {
                episodesAdapter.setNewInstance(playInfo.getEpisodes());
            }
            sourceAdapter.setNewInstance(playInfo.getPlays());
            sourceAdapter.notifyItemChanged(currentPosition);
            sourceAdapter.notifyItemChanged(playInfo.getSourceIndex());
            currentPosition = playInfo.getSourceIndex();
            binding.player.setUp(playInfo.getPlayUrl(), playInfo.getTitle());
            binding.player.startVideoAfterPreloading();
        });
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
}
