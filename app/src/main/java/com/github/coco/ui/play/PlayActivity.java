package com.github.coco.ui.play;

import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.github.coco.R;
import com.github.coco.base.BaseVMActivity;
import com.github.coco.databinding.ActivityPlayBinding;
import com.github.coco.utils.ToastUtil;
import com.github.lib.bean.VideoPlay;

import cn.jzvd.Jzvd;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class PlayActivity extends BaseVMActivity<ActivityPlayBinding, PlayViewModel> {

    private int currentPosition = 0;
    private String title = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    protected Class<PlayViewModel> mClass() {
        return PlayViewModel.class;
    }

    @Override
    protected void init() {
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

    @Override
    protected void observer() {
        model.getVideoPlay().observe(this, videoPlay -> {
            model.getAdapter().setNewInstance(videoPlay.getSources());
            if ("".equals(videoPlay.getUrl())) {
                ToastUtil.show(this, "获取播放地址失败");
                return;
            }
            binding.player.setUp(videoPlay.getUrl(), title);
            binding.player.startVideoAfterPreloading();
        });
        model.getUrl().observe(this, s -> {
            if ("".equals(s)) {
                ToastUtil.show(this, "获取播放地址失败");
                return;
            }
            if (Jzvd.CONTAINER_LIST.size() > 0) {
                Jzvd.releaseAllVideos();
            }
            binding.player.setUp(s, title);
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
