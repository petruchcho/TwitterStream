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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.egorpetruchcho.twitterstreamingapi.model.Tweet;
import com.egorpetruchcho.tweetstream.core.TweetsActivity;
import com.egorpetruchcho.tweetstream.operations.R;
import com.egorpetruchcho.tweetstream.task.GetTweetsTask;
import com.egorpetruchcho.tweetstream.task.result.TweetsResult;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends TweetsActivity implements SwipeRefreshLayout.OnRefreshListener {

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
                Toast.makeText(MainActivity.this, "nu net", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRequestSuccess(TweetsResult tweetsResult) {
                adapter.updateTimeEntries(tweetsResult.tweets);
                swipeLayout.setRefreshing(false);
            }
        });
    }

    private class TimeEntriesAdapter extends ArrayAdapter<Tweet> {

        private final LayoutInflater layoutInflater;
        private final List<Tweet> tweets;

        TimeEntriesAdapter(Context context) {
            this(context, new ArrayList<Tweet>());
        }

        TimeEntriesAdapter(Context context, List<Tweet> tweets) {
            super(context, View.NO_ID, tweets);
            this.tweets = tweets;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void updateTimeEntries(List<Tweet> tweets) {
            this.tweets.clear();
            this.tweets.addAll(tweets);
            notifyDataSetChanged();
        }

        @SuppressWarnings("ConstantConditions")
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_layout, parent, false);
            }
            Tweet tweet = getItem(position);
            ((TextView) convertView.findViewById(R.id.user_id)).setText(tweet.getUser().getName());
            ((TextView) convertView.findViewById(R.id.tweet)).setText(tweet.getText());
            ((TextView) convertView.findViewById(R.id.user_name)).setText(tweet.getUser().getScreen_name());

            return convertView;
        }
    }
}
