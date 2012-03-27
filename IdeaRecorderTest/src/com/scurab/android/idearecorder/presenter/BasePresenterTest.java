package com.scurab.android.idearecorder.presenter;

import android.content.Context;
import android.test.AndroidTestCase;

public abstract class BasePresenterTest extends AndroidTestCase
{
	
	public void testGetDataProvider()
	{
		MockPresenter mp = new MockPresenter(mContext);
		assertNotNull(mp.getDatabase());
	}
	
	public class MockPresenter extends BasePresenter
	{
		public MockPresenter(Context context)
		{
			super(context);
		}

	}
}
