package com.egorpetruchcho.tweetstream.task.result;

import com.egorpetruchcho.twitterstreamingapi.model.Tweet;

import java.util.List;

public class TweetsResult {
    public final List<Tweet> tweets;

    public TweetsResult(List<Tweet> tweets) {
        this.tweets = tweets;
    }
}
