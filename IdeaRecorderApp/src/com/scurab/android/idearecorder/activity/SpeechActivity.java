package com.scurab.android.idearecorder.activity;

import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.tools.AudioRecorder;
import com.scurab.android.idearecorder.widget.AudioRecordingWidget;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class SpeechActivity extends BaseIdeaActivity
{	
	private View mContentView = null;
	private EditText mNameEditText = null;
	private ImageButton mNameRecordButton = null;
	
	private ImageButton mSaveButton = null;
	private ImageButton mCancelButton = null;
	private AudioRecordingWidget mAudioRecordingWidget = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		init();
		setContentView(mContentView);
	}

	protected void init()
	{		
		mContentView = getContentView();
		mNameEditText = (EditText) mContentView.findViewById(R.id.etName);
		mNameRecordButton = (ImageButton)mContentView.findViewById(R.id.btnNameToText);
		
		mSaveButton = (ImageButton)mContentView.findViewById(R.id.btnSave);
		mCancelButton = (ImageButton)mContentView.findViewById(R.id.btnCancel);
		
		mAudioRecordingWidget = (AudioRecordingWidget)mContentView.findViewById(R.id.audioRecordingWidget);
		mAudioRecordingWidget.setAudioRecorder(new AudioRecorder("/sdcard/hovno"));
	}

	@Override
	protected View getContentView()
	{
		return View.inflate(this, R.layout.speechactivity, null);
	}
	
	public EditText getNameEditText()
	{
		return mNameEditText;
	}	
	
	public ImageButton getNameRecordButton()
	{
		return mNameRecordButton;
	}
	
	public AudioRecordingWidget getRecordingWidget()
	{
		return  mAudioRecordingWidget;
	}
	
	public ImageButton getSaveButton()
	{
		return mSaveButton;
	}
	
	public ImageButton getCancelButton()
	{
		return mCancelButton;
	}
}
