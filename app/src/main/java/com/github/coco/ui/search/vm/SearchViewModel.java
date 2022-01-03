package com.github.coco.ui.search.vm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.github.coco.base.BaseViewModel;
import com.github.coco.common.AppDatabase;
import com.github.coco.entity.SearchHistory;

/**
 * Created on 2022/1/3.
 *
 * @author wy
 */
public class SearchViewModel extends BaseViewModel {
    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    public void insertSearchHistory(String key) {
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setKey(key);
        searchHistory.setCreateTime(System.currentTimeMillis());
        async(() -> AppDatabase.getInstance().searchHistoryDao().insert(searchHistory));
    }

    public void clearSearchHistory() {
        async(() -> AppDatabase.getInstance().searchHistoryDao().deleteAll());
    }
}
