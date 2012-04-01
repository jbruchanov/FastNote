package com.scurab.android.idearecorder.activity;

import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.presenter.MainActivityPresenter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainActivity extends BaseActivity
{
	private View mContentView = null;
	private ListView lvData = null;
	private ImageButton mPen = null;
	private ImageButton mAudio = null;
	private ImageButton mPhoto = null;
	private ImageButton mVideo = null;
	private ImageButton mConfig = null;

	@Override
	public void onAttachedToWindow()
	{	
		initHeaderBackgroundColor();
		super.onAttachedToWindow();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		init();
		this.setContentView(mContentView);
		new MainActivityPresenter(this);
	}

	private void initHeaderBackgroundColor()
	{
		try
		{
			View titleView = getWindow().findViewById(android.R.id.title);
			if (titleView != null)
			{
				ViewParent parent = titleView.getParent();
				if (parent != null && (parent instanceof View))
				{
					View parentView = (View) parent;
					parentView.setBackgroundResource(R.drawable.headerbackground);
//					parentView.getLayoutParams().height = (int)(40 * getResources().getDisplayMetrics().density);
				}
			}
		}
		catch (Exception e)
		{
			//ignore error, it is workaround and can be different in versions
		}
	}

	protected void init()
	{
		mContentView = getContentView();
		lvData = (ListView) mContentView.findViewById(R.id.lvData);
		mPen = (ImageButton) mContentView.findViewById(R.id.btnWrite);
		mAudio = (ImageButton) mContentView.findViewById(R.id.btnAudio);
		mPhoto = (ImageButton) mContentView.findViewById(R.id.btnPhoto);
		mVideo = (ImageButton) mContentView.findViewById(R.id.btnVideo);
		mConfig = (ImageButton) mContentView.findViewById(R.id.btnConfig);
	}

	@Override
	protected View getContentView()
	{
		View v = mContentView;
		if (v == null)
		{
			v = View.inflate(this, R.layout.mainactivity, null);
			v.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
		return v;
	}

	public ListView getListView()
	{
		return lvData;
	}

	public ImageButton getWriteIdeaButton()
	{
		return mPen;
	}

	public ImageButton getAudioIdeaButton()
	{
		return mAudio;
	}

	public ImageButton getPhotoIdeaButton()
	{
		return mPhoto;
	}

	public ImageButton getVideoIdeaButton()
	{
		return mVideo;
	}

	public View getConfigButton()
	{
		return mConfig;
	}
}
