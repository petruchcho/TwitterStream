package com.egorpetruchcho.tweetstream.task.result;

import com.egorpetruchcho.tweetstream.model.TweetLikeable;
import com.egorpetruchcho.twitterstreamingapi.model.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetsResult {
    public final List<TweetLikeable> tweets;

    public TweetsResult(List<Tweet> tweets) {
        this.tweets = new ArrayList<>();
        for (Tweet tweet : tweets) {
            this.tweets.add(new TweetLikeable(tweet));
        }
    }
}
