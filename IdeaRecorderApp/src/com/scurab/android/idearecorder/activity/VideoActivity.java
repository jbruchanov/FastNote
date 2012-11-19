package com.scurab.android.idearecorder.activity;

import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.presenter.VideoActivityPresenter;

import android.view.View;

public class VideoActivity extends PhotoActivity {
    private View mContentView;

    @Override
    protected void initPresenter() {
	new VideoActivityPresenter(this);
    }

    @Override
    protected View getContentView() {
	if (mContentView == null)
	    mContentView = View.inflate(this, R.layout.videoactivity, null);
	return mContentView;
    }
}
