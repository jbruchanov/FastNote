package com.scurab.android.idearecorder.presenter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.scurab.android.idearecorder.I;
import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.TestHelper;
import com.scurab.android.idearecorder.activity.SpeechActivity;
import com.scurab.android.idearecorder.interfaces.IAudioRecorder;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.AudioRecorder;
import com.scurab.android.idearecorder.tools.DataProvider;
import com.scurab.android.idearecorder.widget.AudioRecordingWidget;

import android.content.Context;
import android.content.Intent;
import android.test.AndroidTestCase;
import android.view.View;

public class SpeechActivityPresenterTest extends AndroidTestCase
{
	DataProvider dp = null;
	private final static String TEMP_FILE = "/sdcard/test.3gp";
	
	@Override
	public void setContext(Context context)
	{
		super.setContext(context);
		dp = new DataProvider(mContext);
	}
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		dp.deleteAllData();
		new File(TEMP_FILE).delete();
	}
	
	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		List<Idea> ids = dp.getIdeas();
		for(Idea i : ids)
		{
			if(i.getPath() != null)
				new File(i.getPath()).delete();
		}
		dp.deleteAllData();
		new File(TEMP_FILE).delete();
	}
	
	
	public void testSaveNothingRecorderNoName()
	{
		MockSpeechActivity msa = new MockSpeechActivity();
		msa.init();
		SpeechActivityPresenter sap = new SpeechActivityPresenter(msa);
		assertFalse(sap.onSave());
	}
	
	public void testSaveNothingRecorder()
	{
		MockSpeechActivity msa = new MockSpeechActivity();
		msa.init();
		SpeechActivityPresenter sap = new SpeechActivityPresenter(msa);
		msa.getNameEditText().setText("Name01");
		assertFalse(sap.onSave());
	}
	
	public void testSaveOk() throws InterruptedException
	{
		MockSpeechActivity msa = new MockSpeechActivity();
		msa.init();
		SpeechActivityPresenter sap = new SpeechActivityPresenter(msa);
		
		msa.getAudioRecordingWidget().startRecording();
		Thread.sleep(1500);
		msa.getAudioRecordingWidget().stopRecording();
		
		String name = UUID.randomUUID().toString();
		msa.getNameEditText().setText(name);
		assertTrue(sap.onSave());
		
		List<Idea> ids = dp.getIdeas();
		assertEquals(1,ids.size());
		Idea i = ids.get(0);
		assertEquals(name,i.getName());
		assertNotNull(i.getPath());
		
		String path = i.getPath();
		File f = new File(path);
		
		assertTrue(f.exists());
		assertTrue(f.isFile());
		assertTrue(f.length() > 0);
	}
	
	public void testCloseWhenPlaying() throws InterruptedException
	{
		MockSpeechActivity msa = new MockSpeechActivity();
		msa.init();
		MockSpeechActivityPresenter sap = new MockSpeechActivityPresenter(msa);		
		msa.getAudioRecordingWidget().startRecording();
		Thread.sleep(100);
		sap.finish();
		assertTrue(sap.unableToFinish);
		msa.getAudioRecordingWidget().stopRecording();
	}
	
	public void testCloseWhenRecording() throws InterruptedException
	{
		MockSpeechActivity msa = new MockSpeechActivity();
		msa.init();
		MockSpeechActivityPresenter sap = new MockSpeechActivityPresenter(msa);		
		msa.getAudioRecordingWidget().startRecording();
		Thread.sleep(1000);
		sap.finish();
		assertTrue(sap.unableToFinish);
		msa.getAudioRecordingWidget().stopRecording();
	}
	
	public void testCloseOk() throws InterruptedException
	{
		MockSpeechActivity msa = new MockSpeechActivity();
		msa.init();
		MockSpeechActivityPresenter sap = new MockSpeechActivityPresenter(msa);		
		msa.getAudioRecordingWidget().startRecording();
		Thread.sleep(1000);
		msa.getAudioRecordingWidget().stopRecording();
		sap.finish();
		assertFalse(sap.unableToFinish);
	}
	
	public void testOpenExisting() throws IllegalStateException, IOException, InterruptedException
	{
		Idea i = TestHelper.getRandomIdea();
		i.setIdeaType(Idea.TYPE_AUDIO);
		String file = TEMP_FILE; 				
		AudioRecorder ar = new AudioRecorder(file);
		ar.startRecording();
		Thread.sleep(1000);
		ar.stopRecording();
		ar.release();
		i.setPath(file);
		dp.save(i);
		
		Intent intent = new Intent(mContext, SpeechActivity.class);
		intent.putExtra(I.Constants.IDEA_ID, i.getId());
		
		
		MockSpeechActivity msa = new MockSpeechActivity();
		msa.setIntent(intent);
		msa.init();
		MockSpeechActivityPresenter sap = new MockSpeechActivityPresenter(msa);
		
		assertEquals(file, sap.audioRecorder.getFile());
		assertEquals(View.GONE, msa.getAudioRecordingWidget().findViewById(R.id.btnRecord).getVisibility());
		assertEquals(AudioRecordingWidget.STATE_WAITING_FOR_PLAY_OR_RECORD,msa.getAudioRecordingWidget().getState());
	}
	
	private class MockSpeechActivity extends SpeechActivity
	{
		public MockSpeechActivity()
		{
			attachBaseContext(mContext);
		}
		
		public void init()
		{
			super.init();
		}
	}
	
	private class MockSpeechActivityPresenter extends SpeechActivityPresenter
	{
		private boolean unableToFinish = false;
		public IAudioRecorder audioRecorder;
		public MockSpeechActivityPresenter(SpeechActivity context)
		{
			super(context);
		}
		
		@Override
		protected IAudioRecorder getAudioRecorder(String file)
		{
			audioRecorder = super.getAudioRecorder(file);
			return audioRecorder;
		}
		
		@Override
		protected void onUnableToFinish()
		{
			unableToFinish = true;
			super.onUnableToFinish();
		}
	}
}
