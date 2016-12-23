package com.egorpetruchcho.twitterstreamingapi.api;

import android.os.SystemClock;
import android.util.Log;

import com.egorpetruchcho.twitterstreamingapi.model.Tweet;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class TwitterTask. The async task to connect to the twitter API, parse the response
 * and notify the UI thread.
 */
public class TwitterTask {

    /**
     * The Constant MAX_TWEETS. Maximum number of tweets to fetch in a single task.
     */
    private static final int MAX_TWEETS = 50;

    /**
     * The Constant MAX_TWEETS. The delay time(milliseconds) to read a single line. A delay
     * of more than 3 seconds to read a single line will cause the
     */
    private static final int MAX_DELAY = 3000;

    /**
     * The gson.
     */
    private static Gson GSON = new Gson();

    /**
     * The sys time.
     */
    private long sysTime;

    /**
     * Gets the tweets.
     */
    public List<Tweet> getTweets(Response response) throws IOException {
        List<Tweet> tweetList = new ArrayList<>(MAX_TWEETS);
        BufferedReader reader = response.streamReader();
        String json = null;
        int count = 0;
        sysTime = SystemClock.elapsedRealtime();
        do {
            try {
                json = reader.readLine();
                //Returns the list if taking a long time to read from stream.
                if (duration(sysTime) > MAX_DELAY) {
                    break;
                }
                Tweet tweet = getParsedTweet(json);
                if (tweet != null) {
                    tweetList.add(tweet);
                }
            } catch (IOException e) {
                response.releaseResources();
                break;
            } catch (JSONException e) {
                Log.e(e.toString(), e.getMessage());
            }
            count++;
        } while (count < MAX_TWEETS && json != null);
        return tweetList;
    }

    /**
     * Gets the parsed tweet from the JSON string.
     *
     * @param json the json
     * @return the parsed tweet object
     * @throws JSONException the JSON exception
     */
    private Tweet getParsedTweet(String json) throws JSONException {
        Tweet tweet = null;
        if (json != null && !json.isEmpty()) {
            tweet = GSON.fromJson(json, Tweet.class);
            if (tweet.getText() == null)
                return null;
        }
        return tweet;
    }

    /**
     * Duration. Measures the time interval(in seconds) from the last Systime.
     *
     * @param time the previous time whose difference from current time
     *             will be measured.
     * @return the duration
     */
    private long duration(long time) {
        long current = SystemClock.elapsedRealtime();
        long duration = current - time;
        this.sysTime = current;
        return duration;
    }
}
