package com.scurab.android.idearecorder.activity;

import android.test.AndroidTestCase;

public class VideoActivityTest extends AndroidTestCase {
    public void testFindViews() {
	MockVideoActivity pa = new MockVideoActivity();
	pa.init();

	assertNotNull(pa.getNameEditText());
	assertNotNull(pa.getDescriptionEditText());
	assertNotNull(pa.getNameRecordButton());
	assertNotNull(pa.getDescriptionRecorderButton());
	assertNotNull(pa.getSaveButton());
	assertNotNull(pa.getCancelButton());
	assertNotNull(pa.getPhotoButton());
    }

    private class MockVideoActivity extends VideoActivity {
	public MockVideoActivity() {
	    attachBaseContext(mContext);
	}

	@Override
	public void init() {
	    super.init();
	}
    }
}
