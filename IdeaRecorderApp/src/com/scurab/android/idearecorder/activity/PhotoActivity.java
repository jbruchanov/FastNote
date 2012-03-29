package com.scurab.android.idearecorder.activity;

import com.scurab.android.idearecorder.R;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PhotoActivity extends WriteActivity
{
	private View mContentView;
	private ImageButton mImagePreviewButton;
	private ImageView mImagePreview;
	
	@Override
	protected void init()
	{
		super.init();
		mContentView = getContentView();
		mImagePreview = (ImageView) mContentView.findViewById(R.id.imPreview);
		mImagePreviewButton = (ImageButton) mContentView.findViewById(R.id.btnPhoto);
	}
	
	@Override
	protected void initPresenter() 
	{
		
	}
	
	public ImageView getImagePreview()
	{
		return mImagePreview;
	}
	
	public ImageButton getPhotoButton()
	{
		return mImagePreviewButton;
	}
	
	@Override
	protected View getContentView()
	{
		if(mContentView == null)
			mContentView = View.inflate(this, R.layout.photoactivity, null);
		return mContentView;
	}
}
