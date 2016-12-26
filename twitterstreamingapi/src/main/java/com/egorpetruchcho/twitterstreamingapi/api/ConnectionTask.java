package com.egorpetruchcho.twitterstreamingapi.api;

import android.support.annotation.Nullable;
import android.util.Log;

import com.egorpetruchcho.twitterstreamingapi.model.Tweet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class ConnectionTask. The async task to connect to the twitter API, parse
 * the response and notify the UI thread.
 */
public class ConnectionTask {

    /**
     * The twitter client.
     */
    private TwitterClient twitterClient;

    /**
     * Instantiates a new twitter task.
     *
     * @param client the client
     */
    public ConnectionTask(TwitterClient client) {
        this.twitterClient = client;
    }

    public List<Tweet> downloadTweets(TwitterClient.StreamingTweetsRequest request) {
        List<Tweet> result = null;
        try {
            /* The client response. */
            Response clientResponse = twitterClient.getResponse(request);
            twitterClient = null;
            if (clientResponse != null && clientResponse.isSuccess()) {
                /* The twitter task. */
                TwitterTask twitterTask = new TwitterTask();
                result = twitterTask.getTweets(clientResponse, request.getTweetsCount());
            } else if (clientResponse != null && !clientResponse.isSuccess()) {
                String response = clientResponse.streamReader().readLine();
                Log.e(ConnectionTask.class.toString(), response);
            }
        } catch (IOException e) {
            Log.e(e.toString(), e.toString());
            result = new ArrayList<>(0);
        }
        return result;
    }
}
