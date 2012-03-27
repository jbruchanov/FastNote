package com.scurab.android.idearecorder.activity;

import com.scurab.android.idearecorder.activity.WriteActivity;

import android.test.AndroidTestCase;

public class WriteActivityTest extends AndroidTestCase
{
	public void testFindingViews()
	{
		MockWriteActivity mw = new MockWriteActivity();
		mw.init();
		assertNotNull(mw.getNameEditText());
		assertNotNull(mw.getDescriptionEditText());
		assertNotNull(mw.getNameRecordButton());
		assertNotNull(mw.getDescriptionRecorderButton());
		assertNotNull(mw.getSaveButton());
		assertNotNull(mw.getCancelButton());
	}
	
	private class MockWriteActivity extends WriteActivity
	{
		public MockWriteActivity()
		{
			attachBaseContext(mContext);
		}
		
		public void init()
		{
			super.init();
		}
	}
}
