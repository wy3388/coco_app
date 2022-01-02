package com.github.coco.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.github.coco.R;
import com.github.coco.base.BaseActivity;
import com.github.coco.databinding.ActivityMainBinding;
import com.github.coco.ui.classify.ClassifyFragment;
import com.github.coco.ui.history.HistoryFragment;
import com.github.coco.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private final List<Fragment> fragments = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        fragments.add(new HomeFragment());
        fragments.add(new ClassifyFragment());
        fragments.add(new HistoryFragment());
        binding.viewPager2.setCurrentItem(0);
        binding.viewPager2.setOffscreenPageLimit(fragments.size());
        binding.viewPager2.setUserInputEnabled(false);
        binding.viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home_fragment) {
                binding.viewPager2.setCurrentItem(0);
                return true;
            }
            if (itemId == R.id.classify_fragment) {
                binding.viewPager2.setCurrentItem(1);
                return true;
            }
            if (itemId == R.id.history_fragment) {
                binding.viewPager2.setCurrentItem(2);
                return true;
            }
            return false;
        });
    }
}