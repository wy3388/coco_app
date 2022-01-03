package com.github.coco.ui.play;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.github.coco.R;
import com.github.coco.databinding.ActivityPlayBinding;
import com.github.coco.utils.ToastUtil;
import com.github.lib.bean.VideoPlay;

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

    private PlayViewModel model;
    private ActivityPlayBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play);
        model = new ViewModelProvider(this).get(PlayViewModel.class);
        init();
        observer();
    }

    protected void init() {
        String baseUrl = getIntent().getExtras().getString("baseUrl");
        if (baseUrl != null) {
            this.baseUrl = baseUrl;
        }
        isHistory = getIntent().getExtras().getBoolean("isHistory", false);
        if (isHistory && !"".equals(baseUrl)) {
            model.findOneByUrl(baseUrl);
        }
        String url = getIntent().getExtras().getString("url");
        if (url != null && !"".equals(url)) {
            model.playInfo(url);
        }
        String title = getIntent().getExtras().getString("title");
        if (title != null) {
            this.title = title;
        }
        binding.sourceRv.setAdapter(model.getAdapter());
        binding.sourceRv.setLayoutManager(new GridLayoutManager(this, 3));
        model.getAdapter().setOnItemClickListener((baseQuickAdapter, view, position) -> {
            if (currentPosition == position) {
                return;
            }
            model.getAdapter().notifyItemChanged(position);
            model.getAdapter().notifyItemChanged(currentPosition);
            currentPosition = position;
            VideoPlay.Source source = model.getAdapter().getData().get(position);
            model.playUrl(source.getUrl());
            Jzvd.releaseAllVideos();
        });
        binding.player.setNormalClickListener(view -> finish());
        model.getAdapter().setOnSelectedListener((holder, position) -> {
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
    }

    protected void observer() {
        model.getVideoPlay().observe(this, videoPlay -> {
            model.getAdapter().setNewInstance(videoPlay.getSources());
            if ("".equals(videoPlay.getUrl())) {
                ToastUtil.show(this, "获取播放地址失败");
                return;
            }
            if (!isHistory) {
                if (!"".equals(baseUrl)) {
                    model.updatePlayUrl(baseUrl, videoPlay.getUrl(), currentPosition);
                }
                binding.player.setUp(videoPlay.getUrl(), title);
                binding.player.startVideoAfterPreloading();
            }
        });
        model.getUrl().observe(this, s -> {
            if ("".equals(s)) {
                ToastUtil.show(this, "获取播放地址失败");
                return;
            }
            if (Jzvd.CONTAINER_LIST.size() > 0) {
                Jzvd.releaseAllVideos();
            }
            if (!"".equals(baseUrl)) {
                model.updatePlayUrl(baseUrl, s, currentPosition);
            }
            binding.player.setUp(s, title);
            binding.player.startVideoAfterPreloading();
        });
        model.getHistory().observe(this, history -> {
            if (history != null) {
                int tmp = currentPosition;
                currentPosition = history.getSourceIndex();
                model.getAdapter().notifyItemChanged(currentPosition);
                model.getAdapter().notifyItemChanged(tmp);
                binding.player.setUp(history.getPlayUrl(), history.getEpisodesName());
                binding.player.startVideoAfterPreloading();
            }
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
