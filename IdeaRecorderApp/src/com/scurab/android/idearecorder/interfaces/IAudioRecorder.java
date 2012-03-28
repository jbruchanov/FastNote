package com.scurab.android.idearecorder.interfaces;

import java.io.IOException;

public interface IAudioRecorder
{
	public interface OnStopPlayingListener
	{
		void onStop();
	}
	public void startRecording() throws IllegalStateException, IOException;

	public boolean isRecording();

	public void stopRecording();

	/**
	 * Don't forget to call this method to release source!
	 */
	public void release();

	public abstract void startPlaying() throws IllegalArgumentException, IllegalStateException, IOException;

	public boolean isPlaying();

	public void stopPlaying();
	
	public void setOnStopPlayingListener(OnStopPlayingListener listener);
	
	public String getFile();
}