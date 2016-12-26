package com.egorpetruchcho.tweetstream.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.egorpetruchcho.tweetstream.analysis.NaiveBayes;
import com.egorpetruchcho.tweetstream.core.TweetsActivity;
import com.egorpetruchcho.tweetstream.model.TweetLikeable;
import com.egorpetruchcho.tweetstream.operations.R;
import com.egorpetruchcho.tweetstream.task.GetSmartTweetsTask;
import com.egorpetruchcho.tweetstream.task.result.TweetsResult;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

public class SmartTweetsListActivity extends TweetsActivity implements SwipeRefreshLayout.OnRefreshListener {

    private TimeEntriesAdapter adapter;
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        ListView listView = (ListView) findViewById(R.id.list);
        swipeLayout.setOnRefreshListener(this);
        listView.setAdapter(adapter = new TimeEntriesAdapter(this));
    }

    @Override
    public void onRefresh() {
        getBackgroundManager().execute(new GetSmartTweetsTask(), new RequestListener<TweetsResult>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Toast.makeText(SmartTweetsListActivity.this, spiceException.getMessage(), Toast.LENGTH_LONG).show();
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onRequestSuccess(TweetsResult tweetsResult) {
                adapter.updateTimeEntries(tweetsResult.tweets);
                swipeLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.smart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_data_item:
                NaiveBayes.getInstance().saveStats(getPercentage());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int getPercentage() {
        int liked = 0;
        for (TweetLikeable tweet : adapter.getTweets()) {
            if (tweet.isLiked()) {
                liked++;
            }
        }
        return liked / adapter.getCount();
    }

    private class TimeEntriesAdapter extends ArrayAdapter<TweetLikeable> {

        private final LayoutInflater layoutInflater;
        private final List<TweetLikeable> tweets;

        TimeEntriesAdapter(Context context) {
            this(context, new ArrayList<TweetLikeable>());
        }

        TimeEntriesAdapter(Context context, List<TweetLikeable> tweets) {
            super(context, View.NO_ID, tweets);
            this.tweets = tweets;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        void updateTimeEntries(List<TweetLikeable> tweets) {
            this.tweets.clear();
            this.tweets.addAll(tweets);
            notifyDataSetChanged();
        }

        public List<TweetLikeable> getTweets() {
            return tweets;
        }

        @Override
        public int getCount() {
            return tweets.size();
        }

        @SuppressWarnings("ConstantConditions")
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.v_tweet, parent, false);
            }
            final TweetLikeable tweet = getItem(position);
            ((TextView) convertView.findViewById(R.id.user_id)).setText(tweet.getUser().getName());
            ((TextView) convertView.findViewById(R.id.tweet)).setText(tweet.getText());
            ((TextView) convertView.findViewById(R.id.user_name)).setText(String.format("%.3f", NaiveBayes.getInstance().calculateNaiveBayes(tweet.getTweet())));

            CheckBox likeButton = (CheckBox) convertView.findViewById(R.id.like_button);

            likeButton.setOnCheckedChangeListener(null);
            likeButton.setChecked(tweet.isLiked());
            likeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    tweet.setLiked(checked);
                }
            });


            return convertView;
        }
    }
}
