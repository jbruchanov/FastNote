package com.scurab.android.idearecorder.presenter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.scurab.android.idearecorder.activity.MainActivity;
import com.scurab.android.idearecorder.adapter.IdeaListAdapter;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.DataProvider;
import com.scurab.android.idearecorder.view.IdeaView;

public class MainActivityPresenter extends BasePresenter
{
	private MainActivity mContext = null;
	private DataProvider mDataProvider = null;
	
	public MainActivityPresenter(MainActivity activity)
	{
		super(activity);
		mDataProvider = getDatabase();
		mContext = activity;		
		bind();
	}
	
	private void bind()
	{
		mContext.getListView().setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				IdeaView iv = (IdeaView) parent.getItemAtPosition(position);
				onListViewItemClick(iv);
			}
		});
		
		mContext.getWriteIdeaButton().setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{			
				onWriteIdeaButtonClick();
			}
		});
		
		mContext.getAudioIdeaButton().setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onAudioIdeaButtonClick();
			}
		});
		
		mContext.getPhotoIdeaButton().setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{			
				onPhotoIdeaClick();
			}
		});
		
		mContext.getVideoIdeaButton().setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onVideoIdeaClick();
			}
		});
	}
	
	public void onListViewItemClick(IdeaView item)
	{
		
	}
	
	public void onWriteIdeaButtonClick()
	{
		
	}
	
	public void onAudioIdeaButtonClick()
	{
		
	}
	
	public void onPhotoIdeaClick()
	{
		
	}
	
	public void onVideoIdeaClick()
	{
		
	}

	public void loadData()
	{	
		mContext.getListView().setAdapter(new IdeaListAdapter(mContext, mDataProvider.getIdeas()));
	}
}
