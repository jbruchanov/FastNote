package com.scurab.android.idearecorder.widget;

import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.interfaces.IAudioRecorder;
import com.scurab.android.idearecorder.tools.AudioRecorder;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AudioRecordingWidget extends LinearLayout
{
	public interface OnExceptionListener
	{
		void onException(Throwable t);
	}
	
	public static final int STATE_WAITING_FOR_RECORDING = 0;
	public static final int STATE_RECORDING = 1;
	public static final int STATE_WAITING_FOR_PLAY_OR_RECORD = 2;
	public static final int STATE_PLAYING = 4;
	
	private int mState = STATE_WAITING_FOR_RECORDING;
	
	private TextView mTime = null;
	private ImageButton mStopButton = null;
	private ImageButton mPlayButton = null;
	private ImageButton mRecordButton = null;
	
	private IAudioRecorder mRecorder = null;
	
	private View mContentView = null;	
	
	private OnExceptionListener mOnExceptionListener = null;
	private TimeHandler mTimeHandler = null;
	
	public AudioRecordingWidget(Context context)
	{
		super(context);
		init();
	}
	
	public AudioRecordingWidget(Context context, IAudioRecorder ar)
	{
		super(context);
		init();
		setAudioRecorder(ar);
	}
	
	public AudioRecordingWidget(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}
	
	protected void init()
	{
		mContentView = View.inflate(getContext(), R.layout.audiorecordingwidget, null);
		mContentView.setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		addView(mContentView);
		
		mTime = (TextView) mContentView.findViewById(R.id.tvTime);
		mStopButton = (ImageButton) mContentView.findViewById(R.id.btnStop);
		mPlayButton = (ImageButton) mContentView.findViewById(R.id.btnPlay);
		mRecordButton = (ImageButton) mContentView.findViewById(R.id.btnRecord);
		
		setState(STATE_WAITING_FOR_RECORDING);
		mTimeHandler = new TimeHandler(getContext(), mTime);
		
		bind();
	}
	
	private void bind()
	{
		getStopButton().setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onStop();
			}
		});
		
		getPlayButton().setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onPlay();
			}
		});
		
		getRecordButton().setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onRecord();
			}
		});	
	}
	
	protected void onStop()
	{
		try
		{
			if(mState == STATE_PLAYING)
				stopPlaying();
			else if(mState == STATE_RECORDING)
				stopRecording();
		}
		catch(Exception e)
		{
			showError(e);
		}
	}
	
	protected void onPlay()
	{
		try
		{
			if(mState != STATE_WAITING_FOR_PLAY_OR_RECORD)
				onStop();
			startPlaying();
		}
		catch(Exception e)
		{
			showError(e);
		}
	}
	
	protected void onRecord()
	{
		try
		{
			if(!(mState == STATE_WAITING_FOR_PLAY_OR_RECORD || mState == STATE_WAITING_FOR_RECORDING))
				onStop();
			startRecording();
		}
		catch(Exception e)
		{
			showError(e);
		}
	}
	
	public int getState()
	{
		return mState;
	}
	
	protected void setState(int state)
	{
		switch (state)
		{
			case STATE_WAITING_FOR_RECORDING:
				mPlayButton.setEnabled(false);
				mStopButton.setEnabled(false);
				mRecordButton.setEnabled(true);
				break;
			case STATE_RECORDING:
				mPlayButton.setEnabled(false);
				mStopButton.setEnabled(true);
				mRecordButton.setEnabled(false);
				break;
			case STATE_WAITING_FOR_PLAY_OR_RECORD:
				mPlayButton.setEnabled(true);
				mStopButton.setEnabled(false);
				mRecordButton.setEnabled(true);
				break;
			case STATE_PLAYING:
				mPlayButton.setEnabled(false);
				mStopButton.setEnabled(true);
				mRecordButton.setEnabled(false);
				break;
		}
		
		mState = state;
	}

	public TextView getTime()
	{
		return mTime;
	}

	public ImageButton getStopButton()
	{
		return mStopButton;
	}

	public ImageButton getPlayButton()
	{
		return mPlayButton;
	}

	public ImageButton getRecordButton()
	{
		return mRecordButton;
	}

	public void startRecording()
	{
		try
		{
			mRecorder.startRecording();			
			mTimeHandler.start();
			setState(STATE_RECORDING);
		}
		catch(Exception e)
		{
			onException(e);
		}
	}

	public void stopRecording()
	{
		try
		{
			mRecorder.stopRecording();
			mTimeHandler.stop();
			setState(STATE_WAITING_FOR_PLAY_OR_RECORD);		
		}
		catch(Exception e)
		{
			onException(e);
		}
	}

	public void startPlaying()
	{
		try
		{
			mRecorder.startPlaying();
			mTimeHandler.start();
			setState(STATE_PLAYING);
		}
		catch(Exception e)
		{
			onException(e);
		}
	}
	
	public void stopPlaying()
	{
		try
		{
			mRecorder.stopPlaying();
			mTimeHandler.stop();
			setState(STATE_WAITING_FOR_PLAY_OR_RECORD);
		}
		catch(Exception e)
		{
			onException(e);
		}
	}

	public void setAudioRecorder(IAudioRecorder ar)
	{
		mRecorder = ar;
		mRecorder.setOnStopPlayingListener(new IAudioRecorder.OnStopPlayingListener()
		{
			@Override
			public void onStop()
			{
				setState(STATE_WAITING_FOR_PLAY_OR_RECORD);
				mTimeHandler.stop();
			}
		});
	}
	
	protected void onException(Throwable t)
	{
		if(mOnExceptionListener != null)
			mOnExceptionListener.onException(t);
	}

	public OnExceptionListener getOnExceptionListener()
	{
		return mOnExceptionListener;
	}

	public void setOnExceptionListener(OnExceptionListener onExceptionListener)
	{
		mOnExceptionListener = onExceptionListener;
	}
	
	protected void showError(final Throwable t)
	{		
		mContentView.post(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();				
			}
		});
	}
	
	private class TimeHandler extends Handler
	{
		private static final int TICK = 123;
		private static final int SECOND = 995;
		private static final int SEC = 60;
		int counter = 0;
		private TextView mTime = null;
		private boolean isRunning = false;
		
		public TimeHandler(Context c, TextView timeView)
		{
			super(c.getMainLooper());
			mTime = timeView;
		}
		
		public void start()
		{
			counter = 0;
			resetTime();
			sendEmptyMessageDelayed(TICK, SECOND);
			isRunning = true;
		}
		
		public void stop()
		{
			removeMessages(TICK);
			counter = 0;
			isRunning = false;
		}
		
		public void resetTime()
		{
			mTime.setText("00:00");
		}
		
		@Override
		public void handleMessage(Message msg)
		{
			if(isRunning)
			{
				if(msg.what == TICK)
				{
					onTick();
					sendEmptyMessageDelayed(TICK, SECOND);
				}
			}
		}
		
		public void onTick()
		{
			counter++;
			String time = String.format("%02d:%02d", counter/SEC, counter % SEC);
			mTime.setText(time);
		}
	}

	/**
	 * For readonly state, record button is not visible!
	 * @param b
	 */
	public void setReadOnly(boolean b)
	{
		mRecordButton.setVisibility(b ? View.GONE : View.VISIBLE);
		if(mRecorder != null)
		{
			if(mRecorder.isPlaying())
				mRecorder.stopPlaying();
			if(mRecorder.isRecording())
				mRecorder.stopRecording();
		}
		setState(STATE_WAITING_FOR_PLAY_OR_RECORD);
	}

	public void release()
	{
		mRecorder.release();
	}
}
