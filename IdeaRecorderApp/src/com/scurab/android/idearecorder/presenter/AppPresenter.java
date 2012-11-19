package com.scurab.android.idearecorder.presenter;

import android.content.Context;
import android.content.Intent;

import com.scurab.android.idearecorder.IdeaRecorderApplication;
import com.scurab.android.idearecorder.tools.DataProvider;
import com.scurab.android.idearecorder.tools.PropertyProvider;

public abstract class AppPresenter {
    private Context mContext;
    private IdeaRecorderApplication mApplication = null;

    public AppPresenter(Context context) {
	onAttachContext(context);
	mApplication = (IdeaRecorderApplication) mContext
		.getApplicationContext();
    }

    protected void onAttachContext(Context context) {
	mContext = context;
    }

    public DataProvider getDatabase() {
	return mApplication.getDatabase();
    }

    public PropertyProvider getPropertyProvider() {
	return mApplication.getPropertyProvider();
    }

    public void startActivity(Class<?> activityClass) {
	startActivity(new Intent(mContext, activityClass));
    }

    public void startActivity(Intent intent) {
	mContext.startActivity(intent);
    }

    public String getString(int resId, Object... args) {
	return mContext.getString(resId, args);
    }

}
