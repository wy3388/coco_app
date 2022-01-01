package com.github.coco.ui.home;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.coco.R;
import com.github.coco.base.BaseVMFragment;
import com.github.coco.databinding.FragmentHomeBinding;

/**
 * Created on 2022/1/1.
 *
 * @author wy
 */
public class HomeFragment extends BaseVMFragment<FragmentHomeBinding, HomeViewModel> {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected Class<HomeViewModel> mClass() {
        return HomeViewModel.class;
    }

    @Override
    protected void init() {
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(model);
        binding.homeRv.setAdapter(model.adapter);
        binding.homeRv.setLayoutManager(new LinearLayoutManager(getContext()));
        model.loadData();
    }

    @Override
    protected void observer() {
        model.getVideos().observe(this, videos -> {
            Log.e("====>", "observer: " + videos);
            model.adapter.addData(videos);
        });
    }
}
