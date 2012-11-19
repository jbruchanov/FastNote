package com.scurab.android.idearecorder.widget;

import java.io.File;
import java.io.IOException;

import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.help.HelpImageButton;
import com.scurab.android.idearecorder.interfaces.IAudioRecorder;
import com.scurab.android.idearecorder.tools.AudioRecorder;

import android.os.Environment;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.ImageButton;

public class AudioRecordingWidgetTest extends AndroidTestCase {
    public static final String FILE = Environment.getExternalStorageDirectory()
	    .getAbsolutePath() + "/test.3gpp";

    @Override
    protected void setUp() throws Exception {
	super.setUp();
	deleteFile();
    }

    @Override
    protected void tearDown() throws Exception {
	super.tearDown();
	deleteFile();
    }

    private void deleteFile() {
	File f = new File(FILE);
	if (f.exists())
	    f.delete();
    }

    private boolean isOkRecorderFile() {
	File f = new File(FILE);
	return f.exists() && f.length() > 1024;
    }

    public void testFindingViewsState() {
	AudioRecordingWidget arw = new AudioRecordingWidget(mContext);
	assertNotNull(arw.getTime());
	assertNotNull(arw.getStopButton());
	assertNotNull(arw.getPlayButton());
	assertNotNull(arw.getRecordButton());
    }

    public void testInitState() {
	AudioRecordingWidget arw = new AudioRecordingWidget(mContext);
	assertFalse(isEnabled(arw, R.id.btnStop));
	assertFalse(isEnabled(arw, R.id.btnPlay));
	assertTrue(isEnabled(arw, R.id.btnRecord));
	assertEquals(AudioRecordingWidget.STATE_WAITING_FOR_RECORDING,
		arw.getState());
    }

    public void testInitStateReadOnly() {
	AudioRecordingWidget arw = new AudioRecordingWidget(mContext);
	arw.setReadOnly(true);
	assertEquals(View.GONE, arw.findViewById(R.id.btnRecord)
		.getVisibility());
    }

    public void testRecordingState() {
	AudioRecordingWidget arw = new AudioRecordingWidget(mContext);
	arw.setAudioRecorder(new MockAudioRecorder());
	arw.startRecording();
	assertTrue(isEnabled(arw, R.id.btnStop));
	assertFalse(isEnabled(arw, R.id.btnPlay));
	assertFalse(isEnabled(arw, R.id.btnRecord));
	assertEquals(AudioRecordingWidget.STATE_RECORDING, arw.getState());
    }

    public void testRecordAndStopState() {
	AudioRecordingWidget arw = new AudioRecordingWidget(mContext);
	arw.setAudioRecorder(new MockAudioRecorder());
	arw.startRecording();
	arw.stopRecording();
	assertFalse(isEnabled(arw, R.id.btnStop));
	assertTrue(isEnabled(arw, R.id.btnPlay));
	assertTrue(isEnabled(arw, R.id.btnRecord));
	assertEquals(AudioRecordingWidget.STATE_WAITING_FOR_PLAY_OR_RECORD,
		arw.getState());
    }

    public void testRecordAndStopAndPlayState() {
	AudioRecordingWidget arw = new AudioRecordingWidget(mContext);
	arw.setAudioRecorder(new MockAudioRecorder());
	arw.startRecording();
	arw.stopRecording();
	arw.startPlaying();
	assertTrue(isEnabled(arw, R.id.btnStop));
	assertFalse(isEnabled(arw, R.id.btnPlay));
	assertFalse(isEnabled(arw, R.id.btnRecord));
	assertEquals(AudioRecordingWidget.STATE_PLAYING, arw.getState());
    }

    public void testRecordAndStopAndPlayAndStopState() {
	AudioRecordingWidget arw = new AudioRecordingWidget(mContext);
	arw.setAudioRecorder(new MockAudioRecorder());
	arw.startRecording();
	arw.stopRecording();
	arw.startPlaying();
	arw.stopPlaying();
	assertFalse(isEnabled(arw, R.id.btnStop));
	assertTrue(isEnabled(arw, R.id.btnPlay));
	assertTrue(isEnabled(arw, R.id.btnRecord));
	assertEquals(AudioRecordingWidget.STATE_WAITING_FOR_PLAY_OR_RECORD,
		arw.getState());
    }

    public void testRealRecording() throws InterruptedException {
	AudioRecordingWidget arw = new AudioRecordingWidget(mContext);
	AudioRecorder ar = new AudioRecorder(FILE);
	arw.setAudioRecorder(ar);
	arw.startRecording();
	assertTrue(ar.isRecording());
	Thread.sleep(1000);
	arw.stopRecording();
	Thread.sleep(500);
	assertFalse(ar.isRecording());
	ar.release();
	assertTrue(isOkRecorderFile());
    }

    public void testCountingTimeRecording() throws InterruptedException {
	AudioRecordingWidget arw = new AudioRecordingWidget(mContext);
	arw.setAudioRecorder(new MockAudioRecorder());
	arw.startRecording();
	assertEquals("00:00", getTime(arw));
	Thread.sleep(5500);
	arw.stopRecording();
	assertEquals("00:05", getTime(arw));
	Thread.sleep(1500);
	assertEquals("00:05", getTime(arw));
    }

    public void testCountingWithTimeResetRecording()
	    throws InterruptedException {
	AudioRecordingWidget arw = new AudioRecordingWidget(mContext);
	arw.setAudioRecorder(new MockAudioRecorder());
	arw.startRecording();
	Thread.sleep(1500);
	assertEquals("00:01", getTime(arw));
	arw.stopRecording();
	arw.startRecording();
	assertEquals("00:00", getTime(arw));
	Thread.sleep(1500);
	assertEquals("00:01", getTime(arw));
    }

    public void testCountingTimePlaying() throws InterruptedException {
	AudioRecordingWidget arw = new AudioRecordingWidget(mContext);
	arw.setAudioRecorder(new MockAudioRecorder());
	arw.startPlaying();
	assertEquals("00:00", getTime(arw));
	Thread.sleep(5500);
	arw.stopPlaying();
	assertEquals("00:05", getTime(arw));
	Thread.sleep(1500);
	assertEquals("00:05", getTime(arw));
    }

    // works, but test is bad
    //
    // public void testCountingTimePlayingStoppedByEndOfAudio() throws
    // InterruptedException
    // {
    // AudioRecordingWidget arw = new AudioRecordingWidget(mContext);
    // arw.setAudioRecorder(new AudioRecorder(FILE));
    // arw.startRecording();
    // Thread.sleep(2200);
    // arw.stopRecording();
    // arw.startPlaying();
    // assertEquals("00:00", getTime(arw));
    // Thread.sleep(5500);
    // assertEquals("00:02", getTime(arw));
    // }

    public void testCountingWithTimeResetPlaying() throws InterruptedException {
	AudioRecordingWidget arw = new AudioRecordingWidget(mContext);
	arw.setAudioRecorder(new MockAudioRecorder());
	arw.startPlaying();
	assertEquals("00:00", getTime(arw));
	Thread.sleep(1500);
	assertEquals("00:01", getTime(arw));
	arw.stopPlaying();
	arw.startPlaying();
	assertEquals("00:00", getTime(arw));
	Thread.sleep(1500);
	assertEquals("00:01", getTime(arw));
	arw.stopPlaying();
    }

    public void testBinding() {
	MockAudioRecordingWidget maw = new MockAudioRecordingWidget();
	assertNotNull(maw.stop.getOnClickListener());
	assertNotNull(maw.play.getOnClickListener());
	assertNotNull(maw.record.getOnClickListener());
    }

    private String getTime(AudioRecordingWidget arw) {
	return arw.getTime().getText().toString();
    }

    private boolean isEnabled(AudioRecordingWidget arw, int viewId) {
	return arw.findViewById(viewId).isEnabled();
    }

    private class MockAudioRecorder implements IAudioRecorder {

	private int mState = STATE_WATING;
	private final static int STATE_WATING = 1;
	private final static int STATE_RECORDING = 2;
	private final static int STATE_PLAYING = 3;

	@Override
	public void startRecording() throws IllegalStateException, IOException {
	    mState = STATE_RECORDING;
	}

	@Override
	public boolean isRecording() {
	    return mState == STATE_RECORDING;
	}

	@Override
	public void stopRecording() {
	    mState = STATE_WATING;
	}

	@Override
	public void release() {

	}

	@Override
	public void startPlaying() throws IllegalArgumentException,
		IllegalStateException, IOException {
	    mState = STATE_PLAYING;
	}

	@Override
	public boolean isPlaying() {
	    return mState == STATE_PLAYING;
	}

	@Override
	public void stopPlaying() {
	    mState = STATE_WATING;
	}

	@Override
	public void setOnStopPlayingListener(OnStopPlayingListener listener) {

	}

	@Override
	public String getFile() {
	    return null;
	}
    }

    private class MockAudioRecordingWidget extends AudioRecordingWidget {
	HelpImageButton stop;
	HelpImageButton play;
	HelpImageButton record;

	public MockAudioRecordingWidget() {
	    super(mContext);
	}

	@Override
	public ImageButton getStopButton() {
	    if (stop == null)
		stop = new HelpImageButton(mContext);
	    return stop;
	}

	@Override
	public ImageButton getPlayButton() {
	    if (play == null)
		play = new HelpImageButton(mContext);
	    return play;
	}

	@Override
	public ImageButton getRecordButton() {
	    if (record == null)
		record = new HelpImageButton(mContext);
	    return record;
	}
    }
}
