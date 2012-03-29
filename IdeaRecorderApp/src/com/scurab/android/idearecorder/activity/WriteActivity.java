package com.scurab.android.idearecorder.activity;

import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.presenter.WriteActivityPresenter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class WriteActivity extends BaseIdeaActivity
{
	private View mContentView = null;
	private EditText mNameEditText = null;
	private EditText mDescriptionEditText = null;
	
	private ImageButton mNameRecordButton = null;
	private ImageButton mDescriptionRecorderButton = null;
	
	private ImageButton mSaveButton = null;
	private ImageButton mCancelButton = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		init();
		setContentView(mContentView);
	}
	
	protected void initPresenter()
	{
		new WriteActivityPresenter(this);
	}
	
	protected void init()
	{
		mContentView = getContentView();
		mNameEditText = (EditText) mContentView.findViewById(R.id.etName);
		mDescriptionEditText = (EditText) mContentView.findViewById(R.id.etDescription);
		
		mNameRecordButton = (ImageButton)mContentView.findViewById(R.id.btnNameToText);
		mDescriptionRecorderButton = (ImageButton)mContentView.findViewById(R.id.btnDescriptionToText);
		
		mSaveButton = (ImageButton)mContentView.findViewById(R.id.btnSave);
		mCancelButton = (ImageButton)mContentView.findViewById(R.id.btnCancel);
	}
		
	public EditText getNameEditText()
	{
		return mNameEditText;
	}

	public EditText getDescriptionEditText()
	{
		return mDescriptionEditText;
	}

	public ImageButton getNameRecordButton()
	{
		return mNameRecordButton;
	}

	public ImageButton getDescriptionRecorderButton()
	{
		return mDescriptionRecorderButton;
	}

	public ImageButton getSaveButton()
	{
		return mSaveButton;
	}

	public ImageButton getCancelButton()
	{
		return mCancelButton;
	}

	@Override
	protected View getContentView()
	{
		if(mContentView == null)
			mContentView = View.inflate(this, R.layout.writeactivity, null);
		return mContentView;
	}
	

}
