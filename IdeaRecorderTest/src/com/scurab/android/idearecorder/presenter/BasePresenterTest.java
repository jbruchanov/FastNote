package com.scurab.android.idearecorder.presenter;

import android.app.Application;
import android.content.Context;
import android.test.AndroidTestCase;

import com.scurab.android.idearecorder.activity.BaseActivity;
import com.scurab.android.idearecorder.tools.DataProvider;

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
