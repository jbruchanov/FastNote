package com.scurab.android.idearecorder.activity;


import com.scurab.android.idearecorder.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.ViewParent;

public class PreferencesActivity extends PreferenceActivity
{
	@Override
	public void onAttachedToWindow()
	{	
		initHeaderBackgroundColor();
		super.onAttachedToWindow();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference_activity);
	}
	
	private void initHeaderBackgroundColor()
	{
		try
		{
			View titleView = getWindow().findViewById(android.R.id.title);
			if (titleView != null)
			{
				ViewParent parent = titleView.getParent();
				if (parent != null && (parent instanceof View))
				{
					View parentView = (View) parent;
					parentView.setBackgroundResource(R.drawable.headerbackground);
//					parentView.getLayoutParams().height = (int)(40 * getResources().getDisplayMetrics().density);
				}
			}
		}
		catch (Exception e)
		{
			//ignore error, it is workaround and can be different in versions
		}
	}
}
