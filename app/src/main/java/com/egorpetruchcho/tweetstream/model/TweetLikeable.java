package com.egorpetruchcho.tweetstream.model;


import com.egorpetruchcho.twitterstreamingapi.model.Tweet;
import com.egorpetruchcho.twitterstreamingapi.model.User;

public class TweetLikeable {
    private final com.egorpetruchcho.twitterstreamingapi.model.Tweet tweet;
    private boolean liked;

    public TweetLikeable(Tweet tweet) {
        this.tweet = tweet;
    }

    public String getText() {
        return tweet.getText();
    }

    public User getUser() {
        return tweet.getUser();
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
