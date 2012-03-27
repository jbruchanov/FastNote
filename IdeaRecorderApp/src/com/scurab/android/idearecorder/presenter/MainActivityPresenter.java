package com.scurab.android.idearecorder.presenter;

import android.R.integer;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.activity.MainActivity;
import com.scurab.android.idearecorder.adapter.IdeaListAdapter;
import com.scurab.android.idearecorder.interfaces.OnContextItemSelectedListener;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.DataProvider;
import com.scurab.android.idearecorder.view.IdeaView;

public class MainActivityPresenter extends BasePresenter implements OnCreateContextMenuListener, OnContextItemSelectedListener
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
		
		registerForContextMenu();
		mContext.setOnContextMenuCreateListener(this);
		mContext.setOnContextItemSelectedListener(this);
	}
	
	protected void registerForContextMenu()
	{
		mContext.registerForContextMenu(mContext.getListView());
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

	//not tested
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		try
		{
			MenuInflater inflater = mContext.getMenuInflater();
		    inflater.inflate(R.menu.ideascontextmenu, menu);
		    AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		    menu.setHeaderTitle(getIdea(info.position).getName());
		}
		catch(Exception e)
		{
			mContext.showError(e);
		}
	}
	
	private Idea getIdea(int position)
	{
		return ((Idea)mContext.getListView().getAdapter().getItem(position));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		boolean handled = false;
		try
		{
			int menuId = item.getItemId();
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			int position = info.position;
			switch(menuId)
			{
				case R.id.muDelete:
					onDeleteItem(getIdea(position));
					handled = true;
					break;
			}
		}
		catch(Exception e)
		{
			mContext.showError(e);
		}
		return handled;
	}
	
	public void onDeleteItem(Idea i)
	{
		mDataProvider.delete(i.getId());
	}
}
