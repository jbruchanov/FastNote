package com.scurab.android.idearecorder.presenter;

import android.content.Intent;
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

import com.scurab.android.idearecorder.I;
import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.activity.MainActivity;
import com.scurab.android.idearecorder.activity.SpeechActivity;
import com.scurab.android.idearecorder.activity.WriteActivity;
import com.scurab.android.idearecorder.adapter.IdeaListAdapter;
import com.scurab.android.idearecorder.interfaces.OnActivityStateChangeListener;
import com.scurab.android.idearecorder.interfaces.OnContextItemSelectedListener;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.DataProvider;

public class MainActivityPresenter extends BasePresenter implements OnCreateContextMenuListener, OnContextItemSelectedListener, OnActivityStateChangeListener
{
	private MainActivity mContext;
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
				Idea iv = (Idea) parent.getItemAtPosition(position);
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
		mContext.setOnActivityStateChangeListener(this);
	}
	
	protected void registerForContextMenu()
	{
		mContext.registerForContextMenu(mContext.getListView());
	}
	
	public void onListViewItemClick(Idea item)
	{
		Intent i = new Intent(mContext,getClassBytIdeaType(item.getIdeaType()));
		i.putExtra(I.Constants.IDEA_ID, item.getId());
		startActivity(i);
	}
	
	protected Class<?> getClassBytIdeaType(int type)
	{
		Class<?> c = null;
		switch(type)
		{
			case Idea.TYPE_TEXT:
				c = WriteActivity.class;
				break;
			case Idea.TYPE_AUDIO:
				c = SpeechActivity.class;
				break;
		}
		return c;
	}
	
	public void onWriteIdeaButtonClick()
	{
		startActivity(WriteActivity.class);
	}
	
	public void onAudioIdeaButtonClick()
	{
		startActivity(SpeechActivity.class);
	}
	
	public void onPhotoIdeaClick()
	{
		
	}
	
	public void onVideoIdeaClick()
	{
//		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,  Uri.parse("/sdcard/video"));
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//        startActivityForResult(intent,123);
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
			showError(e);
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
			showError(e);
		}
		return handled;
	}
	
	public void onDeleteItem(Idea i)
	{
		mDataProvider.delete(i.getId());
		((IdeaListAdapter)mContext.getListView().getAdapter()).remove(i);
	}

	@Override
	public void onResume()
	{
		loadData();
	}

	@Override
	public void onPause()
	{
		
	}
}
