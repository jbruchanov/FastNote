package com.scurab.android.idearecorder;

import com.scurab.android.idearecorder.tools.DataProvider;

import android.app.Application;
import android.content.Context;

public class IdeaRecorderApplication extends Application
{
	private DataProvider mDataProvider = null;
	
	public DataProvider getDatabase()
	{
		if(mDataProvider == null)
			mDataProvider = new DataProvider(this);
		return mDataProvider;
	}
	
}
