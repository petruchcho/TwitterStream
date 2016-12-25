package com.egorpetruchcho.tweetstream.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.egorpetruchcho.tweetstream.model.TweetLikeable;
import com.egorpetruchcho.twitterstreamingapi.model.Tweet;
import com.egorpetruchcho.tweetstream.core.TweetsActivity;
import com.egorpetruchcho.tweetstream.operations.R;
import com.egorpetruchcho.tweetstream.task.GetTweetsTask;
import com.egorpetruchcho.tweetstream.task.result.TweetsResult;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

public class TweetsListActivity extends TweetsActivity implements SwipeRefreshLayout.OnRefreshListener {

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
        getBackgroundManager().execute(new GetTweetsTask(), new RequestListener<TweetsResult>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Toast.makeText(TweetsListActivity.this, "nu net", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRequestSuccess(TweetsResult tweetsResult) {
                adapter.updateTimeEntries(tweetsResult.tweets);
                swipeLayout.setRefreshing(false);
            }
        });
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

        @SuppressWarnings("ConstantConditions")
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.v_tweet, parent, false);
            }
            TweetLikeable tweet = getItem(position);
            ((TextView) convertView.findViewById(R.id.user_id)).setText(tweet.getUser().getName());
            ((TextView) convertView.findViewById(R.id.tweet)).setText(tweet.getText());
            ((TextView) convertView.findViewById(R.id.user_name)).setText(tweet.getUser().getScreen_name());
            ((CheckBox) convertView.findViewById(R.id.like_button)).setChecked(tweet.isLiked());

            return convertView;
        }
    }
}
