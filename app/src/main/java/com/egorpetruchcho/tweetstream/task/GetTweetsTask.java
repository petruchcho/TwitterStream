package com.egorpetruchcho.tweetstream.task;


import com.egorpetruchcho.tweetstream.task.result.TweetsResult;
import com.egorpetruchcho.twitterstreamingapi.api.TwitterClient;

public class GetTweetsTask extends BackgroundTask<TweetsResult> {

    public GetTweetsTask() {
        super(TweetsResult.class);
    }

    @Override
    protected TweetsResult execute() throws Exception {
        TwitterClient twitterClient = new TwitterClient();
        TwitterClient.StreamingTweetsRequest request = new TwitterClient.StreamingTweetsRequest.Builder()
                .setFilterLevel("low")
                .setLanguage("en")
                .build();
        return new TweetsResult(twitterClient.downloadTweets(request));
    }
}
