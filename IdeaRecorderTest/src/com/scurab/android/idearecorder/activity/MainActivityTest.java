package com.scurab.android.idearecorder.activity;


import android.test.AndroidTestCase;

public class MainActivityTest extends AndroidTestCase
{
	public void testFindingViews()
	 {
		 MockMainActivity ma = new MockMainActivity();		 
		 ma.init();
		 assertNotNull(ma.getListView());
		 assertNotNull(ma.getWriteIdeaButton());
		 assertNotNull(ma.getAudioIdeaButton());
		 assertNotNull(ma.getPhotoIdeaButton());
		 assertNotNull(ma.getVideoIdeaButton());
	 }
	
	private class MockMainActivity extends MainActivity
	{
		public MockMainActivity()
		{
			attachBaseContext(mContext);
		}
		
	}
}
