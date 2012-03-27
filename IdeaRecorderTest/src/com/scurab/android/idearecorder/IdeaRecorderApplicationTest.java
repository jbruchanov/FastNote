package com.scurab.android.idearecorder;

import android.content.Context;
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
}