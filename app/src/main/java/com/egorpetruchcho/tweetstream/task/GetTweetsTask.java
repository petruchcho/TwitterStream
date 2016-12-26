package com.egorpetruchcho.tweetstream.task;


import com.egorpetruchcho.tweetstream.analysis.NaiveBayes;
import com.egorpetruchcho.tweetstream.task.result.TweetsResult;
import com.egorpetruchcho.twitterstreamingapi.api.TwitterClient;

public class GetTweetsTask extends BackgroundTask<TweetsResult> {

    private int tweetsCount = 15;

    public GetTweetsTask() {
        super(TweetsResult.class);
    }

    public GetTweetsTask(int tweetsCount) {
        super(TweetsResult.class);
        this.tweetsCount = tweetsCount;
    }

    @Override
    protected TweetsResult execute() throws Exception {
        TwitterClient twitterClient = new TwitterClient();
        TwitterClient.StreamingTweetsRequest request = new TwitterClient.StreamingTweetsRequest.Builder()
                .setFilterLevel("low")
                .setLanguage("en")
                .setTweetsCount(tweetsCount)
                .build();

        TweetsResult result = new TweetsResult(twitterClient.downloadTweets(request));
        NaiveBayes.getInstance().addTweets(result.tweets);
        return result;
    }
}
