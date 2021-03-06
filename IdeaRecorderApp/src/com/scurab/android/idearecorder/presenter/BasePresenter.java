package com.scurab.android.idearecorder.presenter;

import android.content.Context;
import android.content.Intent;

import com.scurab.android.idearecorder.activity.BaseActivity;

public abstract class BasePresenter extends AppPresenter {
    private BaseActivity mContext;

    public BasePresenter(BaseActivity context) {
	super(context);
    }

    @Override
    protected void onAttachContext(Context context) {
	mContext = (BaseActivity) context;
	super.onAttachContext(context);
    }

    public void showError(final Throwable t) {
	mContext.showError(t);
    }

    public void showMessage(final String msg) {
	mContext.showMessage(msg);
    }

    public void showMessage(int resId) {
	mContext.showMessage(resId);
    }

    public void startActivityForResult(Class<?> activityClass, int requestCode) {
	startActivityForResult(new Intent(mContext, activityClass), requestCode);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
	mContext.startActivityForResult(intent, requestCode);
    }

    public void finish() {
	mContext.finish();
    }
}
