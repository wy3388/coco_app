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
import com.github.coco.common.parcelable.EpisodesParcelable;
import com.github.coco.databinding.ActivityPlayBinding;
import com.github.lib.bean.VideoInfo;
import com.github.lib.bean.VideoPlay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.jzvd.Jzvd;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class PlayActivity extends AppCompatActivity {

    private int currentPosition = 0;
    private String title = "";
    private String baseUrl = "";
    private boolean isHistory = false;
    private int episodesPosition = -1;
    private PlaySourceAdapter sourceAdapter;
    private PlayEpisodesAdapter episodesAdapter;

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
        String baseUrl = getIntent().getExtras().getString("baseUrl");
        if (baseUrl != null && !"".equals(baseUrl)) {
            this.baseUrl = baseUrl;
            model.findOneByUrl(baseUrl);
        }
        String url = getIntent().getExtras().getString("url");
        if (url != null && !"".equals(url)) {
            model.playInfo(url);
        }
        isHistory = getIntent().getExtras().getBoolean("isHistory", false);
        if (isHistory) {
            model.episodesList(url);
        }
        String title = getIntent().getExtras().getString("title");
        if (title != null) {
            this.title = title;
        }
        binding.episodesRv.setLayoutManager(new GridLayoutManager(this, 3));
        binding.episodesRv.setAdapter(episodesAdapter);
        ArrayList<EpisodesParcelable> parcelables = getIntent().getExtras().getParcelableArrayList("episodes");
        if (parcelables != null) {
            episodesAdapter.setNewInstance(parcelables);
        }
        episodesPosition = getIntent().getExtras().getInt("episodesIndex", -1);
        binding.sourceRv.setAdapter(sourceAdapter);
        binding.sourceRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        sourceAdapter.setOnItemClickListener((baseQuickAdapter, view, position) -> {
            if (currentPosition == position) {
                return;
            }
            sourceAdapter.notifyItemChanged(position);
            sourceAdapter.notifyItemChanged(currentPosition);
            currentPosition = position;
            // 更新数据库
            VideoPlay.Play play = sourceAdapter.getData().get(position);
            model.updatePlayUrl(baseUrl, play.getPlayUrl(), currentPosition, episodesPosition);
            Jzvd.releaseAllVideos();
            binding.player.setUp(play.getPlayUrl(), title);
        });
        episodesAdapter.setOnItemClickListener((baseQuickAdapter, view, position) -> {
            if (episodesPosition == -1 || episodesPosition == position) {
                return;
            }
            episodesAdapter.notifyItemChanged(position);
            episodesAdapter.notifyItemChanged(episodesPosition);
            episodesPosition = position;
            // 重新获取播放源
            EpisodesParcelable parcelable = episodesAdapter.getData().get(position);
            isHistory = false;
            sourceAdapter.setNewInstance(null);
            currentPosition = 0;
            model.playInfo(parcelable.getUrl());
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
        model.getVideoPlay().observe(this, videoPlay -> {
            sourceAdapter.setNewInstance(videoPlay.getPlays());
            if (videoPlay.getPlays().size() > 0) {
                if (!isHistory && !"".equals(baseUrl)) {
                    model.updatePlayUrl(baseUrl, videoPlay.getPlays().get(0).getPlayUrl(), currentPosition, episodesPosition);
                    binding.player.setUp(videoPlay.getPlays().get(0).getPlayUrl(), title);
                    binding.player.startVideoAfterPreloading();
                }
            }
        });
        model.getHistory().observe(this, history -> {
            if (history != null) {
                int tmp = currentPosition;
                int tmp1 = episodesPosition;
                currentPosition = history.getSourceIndex();
                episodesPosition = history.getEpisodesIndex();
                sourceAdapter.notifyItemChanged(currentPosition);
                sourceAdapter.notifyItemChanged(tmp);
                sourceAdapter.notifyItemChanged(episodesPosition);
                sourceAdapter.notifyItemChanged(tmp1);
                binding.player.setUp(history.getPlayUrl(), history.getEpisodesName());
                binding.player.startVideoAfterPreloading();
            }
        });
        model.getEpisodes().observe(this, episodes -> {
            List<EpisodesParcelable> list = new ArrayList<>();
            for (VideoInfo.Episodes episode : episodes) {
                EpisodesParcelable parcelable = new EpisodesParcelable();
                parcelable.setName(episode.getName());
                parcelable.setUrl(episode.getUrl());
                list.add(parcelable);
            }
            Collections.reverse(list);
            episodesAdapter.setNewInstance(list);
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
