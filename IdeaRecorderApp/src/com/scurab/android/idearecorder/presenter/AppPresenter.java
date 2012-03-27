package com.scurab.android.idearecorder.presenter;

import android.content.Context;
import android.content.Intent;

import com.scurab.android.idearecorder.IdeaRecorderApplication;
import com.scurab.android.idearecorder.tools.DataProvider;

public abstract class AppPresenter
{
	private Context mContext = null;
	private IdeaRecorderApplication mApplication = null;
	
	public AppPresenter(Context context)
	{
		mContext = context;
		mApplication = (IdeaRecorderApplication) mContext.getApplicationContext();
	}
	
	public DataProvider getDatabase()
	{
		return mApplication.getDatabase();
	}

	public void startActivity(Class<?> activityClass)
	{
		startActivity(new Intent(mContext,activityClass));
	}
	
	public void startActivity(Intent intent)
	{
		mContext.startActivity(intent);		
	}
	
	public String getString(int resId, Object... args)
	{
		return mContext.getString(resId, args);
	}
	
}
