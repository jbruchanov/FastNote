package com.scurab.android.idearecorder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Application;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.scurab.android.idearecorder.tools.DataProvider;
import com.scurab.android.idearecorder.tools.IOUtils;
import com.scurab.android.idearecorder.tools.PropertyProvider;

public class IdeaRecorderApplication extends Application {
    private DataProvider mDataProvider = null;
    private PropertyProvider mPropertyProvider = null;

    public DataProvider getDatabase() {
	try {
	    if (mDataProvider == null) {
		if (hasStorage()) {
		    mDataProvider = new DataProvider(this);
		}
	    }
	} catch (Exception e) {
	    Log.e("IdeaRecorderApplication.getDatabase", e.getMessage());
	}
	return mDataProvider;
    }

    @Override
    public void onCreate() {
	super.onCreate();
	if (hasStorage()) {
	    try {
		tryMoveLocalToSDCard();
	    } catch (Exception e) {
		Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
	    }
	}
    }

    public static boolean hasStorage() {
	String state = Environment.getExternalStorageState();
	if (Environment.MEDIA_MOUNTED.equals(state)) {
	    return true;
	} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	    return true;
	}
	return false;
    }

    private void tryMoveLocalToSDCard() throws FileNotFoundException,
	    IOException {
	File dbLocal = new File(
		"/data/data/com.scurab.android.idearecorder/databases"
			+ DataProvider.NAME);
	File dbMemCard = new File(DataProvider.getDatabaseLocation(this));
	if (dbLocal.exists() && !dbMemCard.exists()) {
	    IOUtils.copyFile(new FileInputStream(dbLocal),
		    dbMemCard.getAbsolutePath());
	}
    }

    /**
     * Returns absolute path to some app dir
     * 
     * @param dir
     *            ie. {@link Environment.#DIRECTORY_PICTURES}
     * @return
     */

    public String getMediaFolder(String dir) {
	return getExternalFilesDir(dir).getAbsolutePath();
    }

    public PropertyProvider getPropertyProvider() {
	if (mPropertyProvider == null) {
	    mPropertyProvider = PropertyProvider.getProvider(this);
	}
	return mPropertyProvider;
    }

    public void onStorageChange() {
	if (mDataProvider != null) {
	    mDataProvider.close();
	}
	mDataProvider = null;

    }

}
