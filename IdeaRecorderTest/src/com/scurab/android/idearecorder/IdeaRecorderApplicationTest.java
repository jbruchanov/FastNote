package com.scurab.android.idearecorder;

import android.content.Context;
import android.os.Environment;
import android.test.AndroidTestCase;

public class IdeaRecorderApplicationTest extends AndroidTestCase
{
	public void testApplicationContextIsMyClass()
	{
		Context c = getContext().getApplicationContext();
		assertTrue(c instanceof IdeaRecorderApplication);
	}
	
	public void testGetDatabase()
	{
		IdeaRecorderApplication c = (IdeaRecorderApplication) getContext().getApplicationContext();
		assertNotNull(c.getDatabase());
	}
	
	public void testGetMediaFolder()
	{
		IdeaRecorderApplication c = (IdeaRecorderApplication) getContext().getApplicationContext();
		assertTrue(c.getMediaFolder(Environment.DIRECTORY_PICTURES).contains("Android/data/com.scurab.android.idearecorder/files/Pictures"));
	}
	
	public void testGetPropertyProvider()
	{
		IdeaRecorderApplication c = (IdeaRecorderApplication) getContext().getApplicationContext();
		assertNotNull(c.getPropertyProvider());
	}
}
