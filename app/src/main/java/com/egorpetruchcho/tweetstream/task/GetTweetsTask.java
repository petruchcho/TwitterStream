package com.egorpetruchcho.tweetstream.task;


import com.egorpetruchcho.tweetstream.task.result.TweetsResult;

public class GetTweetsTask extends BackgroundTask<TweetsResult> {

    public GetTweetsTask() {
        super(TweetsResult.class);
    }

    @Override
    protected TweetsResult execute() throws Exception {
        return new TweetsResult(new com.egorpetruchcho.twitterstreamingapi.api.TwitterClient().downloadTweets());
    }
}
