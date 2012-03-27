package com.scurab.android.idearecorder.view;

import java.util.Date;

import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.model.Idea;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IdeaView extends LinearLayout
{	
	private Idea mItem = null;
	private ViewGroup mContentView = null;
	private TextView mName = null;
	private TextView mDescription = null;
	private TextView mTime = null;
	private ImageView mIcon = null;
	
	public IdeaView(Context context)
	{
		super(context);
		init();
	}
	
	public IdeaView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public IdeaView(Context context, Idea i)
	{
		this(context);		
		setIdea(i);
	}
	
	private void init()
	{
		mContentView = (ViewGroup) inflate(getContext(), R.layout.ideaview, null);
		addView(mContentView);
		mName = (TextView) mContentView.findViewById(R.id.tvName);
		mDescription = (TextView) mContentView.findViewById(R.id.tvDescription);
		mTime = (TextView) mContentView.findViewById(R.id.tvTime);
		mIcon = (ImageView) mContentView.findViewById(R.id.ivIcon);
	}
	
	public void setIdea(Idea item)
	{
		mItem = item;
		mName.setText(mItem.getName());
		mDescription.setText(mItem.getDescription());
		mTime.setText(new Date(mItem.getSaveTime()).toLocaleString());
		setImageResource(mItem.getIdeaType());
	}
	
	protected void setImageResource(int type)
	{
		switch(type)
		{
			default:
				mIcon.setImageResource(R.drawable.ic_launcher);
		}
	}

	public Idea getIdea()
	{
		return mItem;
	}

}
