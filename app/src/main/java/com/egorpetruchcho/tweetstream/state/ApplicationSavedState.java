package com.egorpetruchcho.tweetstream.state;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.egorpetruchcho.tweetstream.core.TweetsApplication;

public class ApplicationSavedState {

    private static final String PREFERENCES_NAME = "TweetsCarrot.ApplicationSavedState";

    private static final String COUNT_OF_ALL_TWEETS = "COUNT_OF_ALL_TWEETS";
    private static final String COUNT_OF_LIKED_TWEETS = "COUNT_OF_LIKED_TWEETS";

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

    public int getCountOfAllTweets() {
        SharedPreferences preferences = getPreferences();
        if (preferences != null) {
            return sharedPreferences.getInt(COUNT_OF_ALL_TWEETS, 0);
        }
        return 0;
    }

    public void setCountOfAllTweets(int count) {
        SharedPreferences preferences = getPreferences();
        if (preferences != null) {
            preferences.edit().putInt(COUNT_OF_ALL_TWEETS, count).commit();
        }
    }

    public int getCountOfLikedTweets() {
        SharedPreferences preferences = getPreferences();
        if (preferences != null) {
            return sharedPreferences.getInt(COUNT_OF_LIKED_TWEETS, 0);
        }
        return 0;
    }

    public void setCountOfLikedTweets(int count) {
        SharedPreferences preferences = getPreferences();
        if (preferences != null) {
            preferences.edit().putInt(COUNT_OF_LIKED_TWEETS, count).commit();
        }
    }
}

