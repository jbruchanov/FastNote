package com.scurab.android.idearecorder.tools;

import java.io.File;
import java.io.IOException;

import com.scurab.android.idearecorder.interfaces.IAudioRecorder;

import android.os.Environment;
import android.test.AndroidTestCase;

public class AudioRecorderTest extends AndroidTestCase {
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

    public void testRecording() {
	try {
	    IAudioRecorder ar = new AudioRecorder(FILE);
	    ar.startRecording();
	    assertTrue(ar.isRecording());
	    Thread.sleep(1000);
	    ar.stopRecording();
	    assertFalse(ar.isRecording());
	    File f = new File(FILE);
	    assertTrue(f.exists());
	    assertTrue(f.length() > 1024);
	    ar.release();
	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    public void testRecordAndPlay() {
	try {
	    IAudioRecorder ar = new AudioRecorder(FILE);
	    ar.startRecording();
	    assertTrue(ar.isRecording());
	    Thread.sleep(2000);
	    ar.stopRecording();
	    ar.startPlaying();
	    assertTrue(ar.isPlaying());
	    Thread.sleep(2500);
	    assertFalse(ar.isPlaying());
	    ar.release();
	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    public void testRecordRepeatedlyAndPlayEnd() {
	try {
	    IAudioRecorder ar = new AudioRecorder(FILE);
	    for (int i = 0; i < 3; i++) {
		ar.startRecording();
		assertTrue(ar.isRecording());
		Thread.sleep(2000);
		ar.stopRecording();
	    }
	    ar.startPlaying();
	    assertTrue(ar.isPlaying());
	    Thread.sleep(2500);
	    assertFalse(ar.isPlaying());
	    ar.release();
	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    public void testRecordOnceAndRepeatedlyPlayEnd() {
	try {
	    IAudioRecorder ar = new AudioRecorder(FILE);

	    ar.startRecording();
	    assertTrue(ar.isRecording());
	    Thread.sleep(2000);
	    ar.stopRecording();

	    for (int i = 0; i < 3; i++) {
		ar.startPlaying();
		assertTrue(ar.isPlaying());
		Thread.sleep(2500);
		assertFalse(ar.isPlaying());
	    }
	    ar.release();
	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    public void testPlay() {
	try {
	    IAudioRecorder ar = new AudioRecorder(FILE);
	    ar.startRecording();
	    assertTrue(ar.isRecording());
	    Thread.sleep(2000);
	    ar.stopRecording();
	    ar.release();

	    ar = new AudioRecorder(FILE);
	    ar.startPlaying();
	    assertTrue(ar.isPlaying());
	    Thread.sleep(2500);
	    assertFalse(ar.isPlaying());
	    ar.release();
	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    public void testPlayAndStopByEndOfAudio() throws IllegalStateException,
	    IOException, InterruptedException {
	IAudioRecorder ar = new AudioRecorder(FILE);
	ar.startRecording();
	Thread.sleep(1000);
	ar.stopRecording();

	ar.startPlaying();
	Thread.sleep(2000);
	assertFalse(ar.isPlaying());
    }
}
