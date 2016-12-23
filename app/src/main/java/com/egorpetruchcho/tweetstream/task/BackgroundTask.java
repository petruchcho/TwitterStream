package com.egorpetruchcho.tweetstream.task;

import com.octo.android.robospice.request.SpiceRequest;

public abstract class BackgroundTask<T> extends SpiceRequest<T> {

    public BackgroundTask(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public T loadDataFromNetwork() throws Exception {
        return execute();
    }

    protected abstract T execute() throws Exception;
}