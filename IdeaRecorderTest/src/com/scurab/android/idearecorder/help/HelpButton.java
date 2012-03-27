package com.scurab.android.idearecorder.help;

import android.content.Context;
import android.util.AttributeSet;


public class HelpButton extends android.widget.Button
{
	

	private OnClickListener mOnClickListener;
	public HelpButton(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public HelpButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HelpButton(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setOnClickListener(OnClickListener l)
	{
		super.setOnClickListener(l);
		mOnClickListener = l;
	}
	
	public OnClickListener getOnClickListener()
	{
		return mOnClickListener;
	}
}
