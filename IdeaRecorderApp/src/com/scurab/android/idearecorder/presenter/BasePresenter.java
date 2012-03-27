package com.scurab.android.idearecorder.presenter;

import android.content.Context;

import com.scurab.android.idearecorder.IdeaRecorderApplication;
import com.scurab.android.idearecorder.tools.DataProvider;

public abstract class BasePresenter
{
	private Context mContext = null;
	private IdeaRecorderApplication mApplication = null;
	
	public BasePresenter(Context context)
	{
		mContext = context;
		mApplication = (IdeaRecorderApplication) mContext.getApplicationContext();
	}
	
	public DataProvider getDatabase()
	{
		return mApplication.getDatabase();
	}
}
