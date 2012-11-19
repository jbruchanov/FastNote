package com.scurab.android.idearecorder.activity;

import android.test.AndroidTestCase;

public class PhotoActivityTest extends AndroidTestCase {
    public void testFindViews() {
	MockPhotoActivity pa = new MockPhotoActivity();
	pa.init();

	assertNotNull(pa.getNameEditText());
	assertNotNull(pa.getDescriptionEditText());
	assertNotNull(pa.getNameRecordButton());
	assertNotNull(pa.getDescriptionRecorderButton());
	assertNotNull(pa.getSaveButton());
	assertNotNull(pa.getCancelButton());
	assertNotNull(pa.getImagePreview());
	assertNotNull(pa.getPhotoButton());
    }

    private class MockPhotoActivity extends PhotoActivity {
	public MockPhotoActivity() {
	    attachBaseContext(mContext);
	}

	@Override
	public void init() {
	    super.init();
	}
    }
}
