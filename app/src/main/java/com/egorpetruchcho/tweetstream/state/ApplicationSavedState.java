package com.egorpetruchcho.tweetstream.state;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.egorpetruchcho.tweetstream.core.TweetsApplication;

public class ApplicationSavedState {

    private static final String PREFERENCES_NAME = "TweetsCarrot.ApplicationSavedState";

    private static ApplicationSavedState instance;
    private SharedPreferences sharedPreferences;

    private ApplicationSavedState() {
        sharedPreferences = TweetsApplication.getInstance().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static ApplicationSavedState getInstance() {
        if (instance == null) {
            instance = new ApplicationSavedState();
        }
        return instance;
    }

    @Nullable
    private SharedPreferences getPreferences() {
        return sharedPreferences;
    }
}

