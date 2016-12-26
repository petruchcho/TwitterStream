package com.egorpetruchcho.tweetstream.task;


import com.egorpetruchcho.tweetstream.analysis.NaiveBayes;
import com.egorpetruchcho.tweetstream.model.TweetLikeable;
import com.egorpetruchcho.tweetstream.task.result.TweetsResult;
import com.egorpetruchcho.twitterstreamingapi.api.TwitterClient;
import com.egorpetruchcho.twitterstreamingapi.model.Tweet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GetSmartTweetsTask extends BackgroundTask<TweetsResult> {

    public GetSmartTweetsTask() {
        super(TweetsResult.class);
    }

    @Override
    protected TweetsResult execute() throws Exception {
        TwitterClient twitterClient = new TwitterClient();
        TwitterClient.StreamingTweetsRequest request = new TwitterClient.StreamingTweetsRequest.Builder()
                .setFilterLevel("low")
                .setLanguage("en")
                .setTweetsCount(200)
                .build();

        List<Tweet> tweets = twitterClient.downloadTweets(request);
        Collections.sort(tweets, new Comparator<Tweet>() {
            @Override
            public int compare(Tweet tweet1, Tweet tweet2) {
                return -Double.compare(NaiveBayes.getInstance().calculateNaiveBayes(tweet1),
                        NaiveBayes.getInstance().calculateNaiveBayes(tweet2));
            }
        });
        List<Tweet> bestTweets = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            bestTweets.add(tweets.get(i));
        }

        return new TweetsResult(bestTweets);
    }
}
