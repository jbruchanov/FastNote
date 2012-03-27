package com.scurab.android.idearecorder.activity;

import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.interfaces.OnContextItemSelectedListener;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView.OnItemClickListener;
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
	private OnCreateContextMenuListener mOnCreateContextMenuListener;
	private OnItemClickListener mOnItemClickListener;
	private OnContextItemSelectedListener mOnContextItemSelectedListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		init();
		this.setContentView(mContentView);
	}
	
	protected void init()
	{
		mContentView = getContentView();
		lvData = (ListView) mContentView.findViewById(R.id.lvData);
		mPen = (ImageButton) mContentView.findViewById(R.id.btnWrite);
		mAudio = (ImageButton) mContentView.findViewById(R.id.btnAudio);
		mPhoto = (ImageButton) mContentView.findViewById(R.id.btnPhoto);
		mVideo = (ImageButton) mContentView.findViewById(R.id.btnVideo);
	}
	
	protected View getContentView()
	{
		if(mContentView == null)
			return View.inflate(this, R.layout.mainactivity, null);
		else
			return mContentView;
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

	public void setOnContextMenuCreateListener(OnCreateContextMenuListener listener)
	{
		mOnCreateContextMenuListener = listener;
	}	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		if(mOnContextItemSelectedListener != null)
			mOnCreateContextMenuListener.onCreateContextMenu(menu, v, menuInfo);
	}
	
	public void setOnContextItemSelectedListener(OnContextItemSelectedListener listener)
	{
		mOnContextItemSelectedListener = listener;
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{	
		if(mOnContextItemSelectedListener != null)
			return mOnContextItemSelectedListener.onContextItemSelected(item);
		return super.onContextItemSelected(item);
		
	}

	
}