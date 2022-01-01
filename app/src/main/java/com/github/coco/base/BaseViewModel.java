package com.github.coco.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.coco.common.LoadStatus;
import com.github.coco.common.ThreadHelper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 2022/1/1.
 *
 * @author wy
 */
public class BaseViewModel extends ViewModel {
    private final MutableLiveData<LoadStatus> status = new MutableLiveData<>();

    public LiveData<LoadStatus> getStatus() {
        return status;
    }

    protected void async(PagerCallback pagerCallback) {
        ThreadHelper.getInstance().execute(() -> {
            try {
                Pager pager = pagerCallback.accept();
                if (pager.getPage() < pager.getTotalPage()) {
                    status.postValue(LoadStatus.LOADING);
                } else {
                    status.postValue(LoadStatus.LOADING_END);
                }
            } catch (Exception e) {
                e.printStackTrace();
                status.postValue(LoadStatus.FAILED);
            }
        });
    }

    protected interface AsyncCallback {
        void accept();
    }

    protected interface ExceptionCallback {
        void error(Throwable throwable);
    }

    protected interface PagerCallback {
        Pager accept();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    protected static class Pager {
        private Integer page;
        private Integer totalPage;
    }
}
