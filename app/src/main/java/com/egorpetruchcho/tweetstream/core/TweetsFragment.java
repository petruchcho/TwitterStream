package com.egorpetruchcho.tweetstream.core;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.egorpetruchcho.tweetstream.task.BackgroundSpiceService;
import com.octo.android.robospice.SpiceManager;

public abstract class TweetsFragment extends Fragment {

    private final SpiceManager backgroundManager = new SpiceManager(BackgroundSpiceService.class);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        backgroundManager.start(getActivity());
    }

    @Override
    public void onDetach() {
        backgroundManager.shouldStop();
        super.onDetach();
    }

    protected SpiceManager getBackgroundManager() {
        return backgroundManager;
    }
}