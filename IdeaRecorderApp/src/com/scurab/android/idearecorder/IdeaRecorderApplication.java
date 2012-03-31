package com.scurab.android.idearecorder;

import com.scurab.android.idearecorder.tools.DataProvider;
import com.scurab.android.idearecorder.tools.PropertyProvider;

import android.app.Application;

public class IdeaRecorderApplication extends Application
{
	private DataProvider mDataProvider = null;
	private PropertyProvider mPropertyProvider = null;
	
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

	public PropertyProvider getPropertyProvider()
	{
		if(mPropertyProvider == null)
			mPropertyProvider = PropertyProvider.getProvider(this);
		return mPropertyProvider;
	}
	
}
