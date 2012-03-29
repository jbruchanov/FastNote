package com.scurab.android.idearecorder;

import com.scurab.android.idearecorder.tools.DataProvider;

import android.app.Application;

public class IdeaRecorderApplication extends Application
{
	private DataProvider mDataProvider = null;
	
	public DataProvider getDatabase()
	{
		if(mDataProvider == null)
			mDataProvider = new DataProvider(this);
		return mDataProvider;
	}

	/**
	 * Returns absolute path to some app dir
	 * @param dir ie. {@link Environment.#DIRECTORY_PICTURES}
	 * @return
	 */
	
	public String getMediaFolder(String dir)
	{
		return getExternalFilesDir(dir).getAbsolutePath();
	}
	
}
