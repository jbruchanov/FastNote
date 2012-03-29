package com.scurab.android.idearecorder.presenter;

import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.activity.MainActivity;

import android.content.Context;
import android.test.AndroidTestCase;

public class AppPresenterTest extends AndroidTestCase
{
	
	public void testGetDataProvider()
	{
		MockPresenter mp = new MockPresenter(mContext);
		assertNotNull(mp.getDatabase());
	}
	
	public void testStartActivity()
	{
		MockPresenter mp = new MockPresenter(mContext);
		mp.startActivity(MainActivity.class);
		assertEquals(MainActivity.class,mp.startedActivity);
	}
	
	public void testGetString()
	{
		MockPresenter mp = new MockPresenter(mContext);
		assertEquals(mp.getString(R.string.lblDelete),"Delete");
	}
	
	public void testGetStringArgs()
	{
		MockPresenter mp = new MockPresenter(mContext);
		assertEquals(mp.getString(R.string.errInvalidValueMissingArg0, "ABCDEF"),"Invalid values, missing ABCDEF");
	}
	

	public class MockPresenter extends AppPresenter
	{
		public Class<?> startedActivity = null;
		
		public MockPresenter(Context context)
		{
			super(context);
		}

		@Override
		public void startActivity(Class<?> activityClass)
		{
			//super.startActivity(activityClass);
			startedActivity = activityClass;
			
		}
	}
	
	
}
