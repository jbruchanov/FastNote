package com.scurab.android.idearecorder.activity;

import android.test.AndroidTestCase;

public class SpeechActivityTest extends AndroidTestCase
{
	public void testFindingViews()
	{
		MockSpeechActivity mw = new MockSpeechActivity();
		mw.init();
		assertNotNull(mw.getNameEditText());
		assertNotNull(mw.getNameRecordButton());
		assertNotNull(mw.getAudioRecordingWidget());
		assertNotNull(mw.getSaveButton());
		assertNotNull(mw.getCancelButton());
	}
	
	private class MockSpeechActivity extends SpeechActivity
	{
		public MockSpeechActivity()
		{
			attachBaseContext(mContext);
		}
		
		@Override
		public void init()
		{
			super.init();
		}
	}
}
