package com.egorpetruchcho.twitterstreamingapi.api;

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
     * The gson.
     */
    private static Gson GSON = new Gson();

    /**
     * Gets the tweets.
     */
    public List<Tweet> getTweets(Response response, int tweetsCount) throws IOException {
        List<Tweet> tweetList = new ArrayList<>(tweetsCount);
        BufferedReader reader = response.streamReader();
        String json = null;
        int count = 0;
        do {
            try {
                json = reader.readLine();
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
        } while (count < tweetsCount && json != null);
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
}
