package com.scurab.android.idearecorder.activity;

import android.app.Activity;
import android.widget.Toast;

public abstract class BaseActivity extends Activity
{
	public void showError(final Throwable t)
	{		
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				showErrorImpl(t);		
			}
		});		
	}
	
	public void showMessage(final String msg)
	{		
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				showMessageImpl(msg);		
			}
		});		
	}
	
	public void showMessage(int resId)
	{		
		final String msg = getString(resId);
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				showMessageImpl(msg);		
			}
		});		
	}
	
	protected void showErrorImpl(Throwable t)
	{
		getToast(t.getMessage()).show();
	}
	
	protected void showMessageImpl(String msg)
	{
		getToast(msg).show();
	}
	
	protected Toast getToast(String msg)
	{
		return Toast.makeText(this, msg, Toast.LENGTH_LONG);
	}	
}
