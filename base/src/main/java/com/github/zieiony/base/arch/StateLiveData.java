package com.github.zieiony.base.arch;

import androidx.lifecycle.LiveData;

import com.github.zieiony.base.util.Logger;

public class StateLiveData<T extends BaseState> extends LiveData<T> {

    private Logger logger;

    public StateLiveData(Logger logger) {
        super();
        this.logger = logger;
    }

    @Override
    public void setValue(T value) {
        logger.log("state change: " + value.toString());
        super.setValue(value);
    }
}
