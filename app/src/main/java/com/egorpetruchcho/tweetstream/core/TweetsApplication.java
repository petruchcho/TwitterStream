package com.egorpetruchcho.tweetstream.core;

import android.app.Application;

public class TweetsApplication extends Application {
    private static TweetsApplication instance;

    public TweetsApplication() {
        instance = this;
    }

    public static TweetsApplication getInstance() {
        return instance;
    }
}
