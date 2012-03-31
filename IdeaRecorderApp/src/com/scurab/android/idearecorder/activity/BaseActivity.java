package com.scurab.android.idearecorder.activity;

import com.scurab.android.idearecorder.interfaces.OnActivityKeyDownListener;
import com.scurab.android.idearecorder.interfaces.OnActivityStateChangeListener;
import com.scurab.android.idearecorder.interfaces.OnContextItemSelectedListener;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.Toast;

public abstract class BaseActivity extends Activity
{
	private OnCreateContextMenuListener mOnCreateContextMenuListener;
	private OnContextItemSelectedListener mOnContextItemSelectedListener;
	private OnActivityResultListener mOnActivityResultListener;
	private OnActivityStateChangeListener mOnActivityStateChangeListener;
	private OnActivityKeyDownListener mOnActivityKeyDownListener;
	
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
	
	protected abstract View getContentView();
	
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
	
	public void setOnContextMenuCreateListener(OnCreateContextMenuListener listener)
	{
		mOnCreateContextMenuListener = listener;
	}	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		if(mOnContextItemSelectedListener != null)
			mOnCreateContextMenuListener.onCreateContextMenu(menu, v, menuInfo);
	}
	
	public void setOnContextItemSelectedListener(OnContextItemSelectedListener listener)
	{
		mOnContextItemSelectedListener = listener;
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{	
		if(mOnContextItemSelectedListener != null)
			return mOnContextItemSelectedListener.onContextItemSelected(item);
		return super.onContextItemSelected(item);
	}
	
	public void setOnActivityResultListener(OnActivityResultListener listener)
	{
		mOnActivityResultListener = listener;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(mOnActivityResultListener != null)
			mOnActivityResultListener.onActivityResult(requestCode, resultCode, data);
		else			
			super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onResume()
	{	
		super.onResume();
		if(mOnActivityStateChangeListener != null)
			mOnActivityStateChangeListener.onResume();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		if(mOnActivityStateChangeListener != null)
			mOnActivityStateChangeListener.onPause();
	};
	
	public void setOnActivityStateChangeListener(OnActivityStateChangeListener listener)
	{
		mOnActivityStateChangeListener = listener;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		boolean b = false;
		if(mOnActivityKeyDownListener != null)
			b = mOnActivityKeyDownListener.onKeyDown(keyCode,event);	
		if(b)
			return b;
		else
			return super.onKeyDown(keyCode, event);
	}

	public void setOnActivityKeyDownListener(OnActivityKeyDownListener onActivityKeyDownListener)
	{
		mOnActivityKeyDownListener = onActivityKeyDownListener;
	}
}
