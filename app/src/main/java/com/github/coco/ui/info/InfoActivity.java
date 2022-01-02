package com.github.coco.ui.info;

import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.github.coco.R;
import com.github.coco.base.BaseVMActivity;
import com.github.coco.databinding.ActivityInfoBinding;

import java.util.Collections;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class InfoActivity extends BaseVMActivity<ActivityInfoBinding, InfoViewModel> {

    private int currentPosition = -1;

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
        }
        binding.infoRv.setAdapter(model.getAdapter());
        binding.infoRv.setLayoutManager(new GridLayoutManager(this, 3));
        model.getAdapter().setOnItemClickListener((baseQuickAdapter, view, position) -> {
            model.getAdapter().notifyItemChanged(position);
            if (currentPosition > -1) {
                model.getAdapter().notifyItemChanged(currentPosition);
            }
            currentPosition = position;
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
            Collections.reverse(videoInfo.getEpisodes());
            model.getAdapter().setNewInstance(videoInfo.getEpisodes());
        });
    }
}
