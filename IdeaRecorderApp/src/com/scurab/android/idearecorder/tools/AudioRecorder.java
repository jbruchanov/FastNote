package com.scurab.android.idearecorder.tools;

import java.io.IOException;

import com.scurab.android.idearecorder.interfaces.IAudioRecorder;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;

public class AudioRecorder implements IAudioRecorder, OnCompletionListener
{
	private MediaRecorder mRecorder = null;
	private MediaPlayer mPlayer = null;
	private String mFile = null;
	private int mState = STATE_WATING;
	private OnStopPlayingListener mOnStopPlayingListener;
	private final static int STATE_WATING = 1;
	private final static int STATE_RECORDING = 2;
	
	public AudioRecorder(String file)
	{
		if(file == null)
			throw new NullPointerException("File!");
		mFile = file;
		init();
	}
	
	private void init()
	{
		MediaRecorder rec = new MediaRecorder();		
		rec.setAudioSource(MediaRecorder.AudioSource.MIC);		
		rec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);		
		rec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		rec.setOutputFile(mFile);
		setMediaRecorder(rec);
	}
	
	protected void setMediaRecorder(MediaRecorder recorder)
	{
		mRecorder = recorder;
	}
	
	@Override
	public void startRecording() throws IllegalStateException, IOException
	{
		mRecorder.prepare();
        mRecorder.start();
		mState = STATE_RECORDING;
	}

	@Override
	public boolean isRecording()
	{
		return mState == STATE_RECORDING;
	}

	@Override
	public void stopRecording()
	{
		mRecorder.stop();
		mRecorder.release();
		init();
		mState = STATE_WATING;
	}
	
	@Override
	public void release()
	{
		mRecorder.release();
		
	}

	@Override
	public void startPlaying() throws IllegalArgumentException, IllegalStateException, IOException
	{
		
		if(mPlayer != null)
		{
			if(mPlayer.isPlaying()) mPlayer.stop();
			mPlayer.reset();
		}
		else
		{
			mPlayer = new MediaPlayer();
			mPlayer.setOnCompletionListener(this);
		}
		mPlayer.setDataSource(mFile);
		mPlayer.prepare();
		mPlayer.start();				
	}

	@Override
	public boolean isPlaying()
	{
		return mPlayer != null && mPlayer.isPlaying();
	}
	
	@Override
	public void stopPlaying()
	{
		if(isPlaying())
			mPlayer.stop();
	}

	@Override
	public void setOnStopPlayingListener(OnStopPlayingListener listener)
	{
		mOnStopPlayingListener = listener;
	}

	@Override
	public void onCompletion(MediaPlayer mp)
	{
		mState = STATE_WATING;
		if(mOnStopPlayingListener != null)
			mOnStopPlayingListener.onStop();
	}

	@Override
	public String getFile()
	{
		return mFile;
	}
}
