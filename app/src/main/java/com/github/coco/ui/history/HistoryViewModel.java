package com.github.coco.ui.history;

import android.app.Application;

import androidx.annotation.NonNull;

import com.github.coco.base.BaseViewModel;
import com.github.coco.common.AppDatabase;
import com.github.coco.entity.History;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class HistoryViewModel extends BaseViewModel {
    public HistoryViewModel(@NonNull Application application) {
        super(application);
    }

    public void deleteHistory(History... histories) {
        async(() -> AppDatabase.getInstance().historyDao().delete(histories));
    }
}
