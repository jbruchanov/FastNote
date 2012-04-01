package com.scurab.android.idearecorder.presenter;

import java.io.File;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.scurab.android.idearecorder.I;
import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.activity.MainActivity;
import com.scurab.android.idearecorder.activity.PhotoActivity;
import com.scurab.android.idearecorder.activity.PreferencesActivity;
import com.scurab.android.idearecorder.activity.SpeechActivity;
import com.scurab.android.idearecorder.activity.VideoActivity;
import com.scurab.android.idearecorder.activity.WriteActivity;
import com.scurab.android.idearecorder.adapter.IdeaListAdapter;
import com.scurab.android.idearecorder.interfaces.OnActivityKeyDownListener;
import com.scurab.android.idearecorder.interfaces.OnActivityStateChangeListener;
import com.scurab.android.idearecorder.interfaces.OnContextItemSelectedListener;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.DataProvider;
import com.scurab.android.idearecorder.tools.IdeaOrder;

public class MainActivityPresenter extends BasePresenter implements OnCreateContextMenuListener, OnContextItemSelectedListener, OnActivityStateChangeListener, OnActivityKeyDownListener
{
	private MainActivity mContext;
	private DataProvider mDataProvider = null;
	private boolean mConfigButtonVisible = false;
	private Animation[] mAnimations;
	private AnimationListener mUpAnimListener;
	private AnimationListener mDownAnimListener;
	private boolean mInAnimation;
	
	public MainActivityPresenter(MainActivity activity)
	{
		super(activity);
		mDataProvider = getDatabase();
		mContext = activity;		
		bind();
		
	}
	
	protected void bind()
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
		
		mContext.getConfigButton().setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onConfiguButtonClick();
			}
		});
		
		mContext.setOnActivityKeyDownListener(this);
		
		registerForContextMenu();
		mContext.setOnContextMenuCreateListener(this);
		mContext.setOnContextItemSelectedListener(this);
		mContext.setOnActivityStateChangeListener(this);
		
		
		bindAnimations();
	}
	
	protected void bindAnimations()
	{
		mAnimations = new Animation[] {AnimationUtils.loadAnimation(mContext, R.anim.scroll_up_def),
				   						AnimationUtils.loadAnimation(mContext, R.anim.scroll_down_def)};

		mDownAnimListener = new AnimationListener()
		{
			@Override
			public void onAnimationStart(Animation animation)
			{
			}
			
			@Override
			public void onAnimationRepeat(Animation animation)
			{
			}
			
			@Override
			public void onAnimationEnd(Animation animation)
			{
				handleSwitchBottomPanelButtons();
			}
		};
		mAnimations[0].setAnimationListener(mDownAnimListener);		
		
		mUpAnimListener = new AnimationListener()
		{
			@Override
			public void onAnimationStart(Animation animation)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation)
			{
				mInAnimation = false;
			}
		};
		mAnimations[1].setAnimationListener(mUpAnimListener);
	}
	
	public void onConfiguButtonClick()
	{
		startActivity(PreferencesActivity.class);
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
			case Idea.TYPE_IMAGE:
				c = PhotoActivity.class;
				break;
			case Idea.TYPE_VIDEO:
				c = VideoActivity.class;
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
		startActivity(PhotoActivity.class);
	}
	
	public void onVideoIdeaClick()
	{
		startActivity(VideoActivity.class);
	}

	public void loadData()
	{	
		int order = Integer.parseInt(getPropertyProvider().getString(R.string.PROPERTY_ITEMS_ORDER, String.valueOf(IdeaOrder.ORDER_BY_NAME_DESCENDING)));
		List<Idea> data = mDataProvider.getIdeas();
		Collections.sort(data,IdeaOrder.getComparator(order));
		mContext.getListView().setAdapter(new IdeaListAdapter(mContext, data));
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
				case R.id.muSend:
					onSendItem(getIdea(position));
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

	public void onSendItem(Idea i)
	{
		String text = i.getName();
		String description = i.getDescription();
		if(description != null)
			text = String.format("%s\n%s",text,description).trim();
		
		Intent intent = new Intent(Intent.ACTION_SEND);
		String type = I.MimeType.APPLICATION_OCTETSTREAM;
		switch(i.getIdeaType())
		{
			case Idea.TYPE_TEXT:
				type = I.MimeType.TEXT_PLAIN;
				break;
			case Idea.TYPE_AUDIO:
				type = I.MimeType.AUDIO_3GPP;				
				break;
			case Idea.TYPE_IMAGE:
				type = I.MimeType.IMAGE_JPEG;
				break;
			case Idea.TYPE_VIDEO:
				type = I.MimeType.VIDEO_MP4;
				break;	
		}
		if(i.getIdeaType() != Idea.TYPE_TEXT && i.getPath() != null && (new File(i.getPath()).exists()))
			intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(String.format("file://%s",i.getPath())));
		intent.setType(type);
		intent.putExtra(Intent.EXTRA_TEXT, text);	    
	    startActivity(Intent.createChooser(intent, "?"));
	}
	
	@Override
	public void onResume()
	{
		loadData();
		setConfigButtonInvisible();
	}

	@Override
	public void onPause()
	{
		
	}

	public void onOptionButtonClick()
	{		
		if(!mInAnimation)
		{
			mInAnimation = true;
			View v = mContext.findViewById(R.id.bottomPanel);		
			v.startAnimation(mAnimations[0]);//move bottom panel down
		}
	}
	
	private void handleSwitchBottomPanelButtons()
	{
		View v = mContext.findViewById(R.id.bottomPanel);
		v.startAnimation(mAnimations[1]);//move bottom panel up
		if(mConfigButtonVisible)
			setConfigButtonInvisible();
		else
			setConfigButtonVisible();
	}
	
	protected void setConfigButtonVisible()
	{
		mContext.getConfigButton().setVisibility(View.VISIBLE);
		mContext.getWriteIdeaButton().setVisibility(View.GONE);
		mContext.getAudioIdeaButton().setVisibility(View.GONE);
		mContext.getPhotoIdeaButton().setVisibility(View.GONE);
		mContext.getVideoIdeaButton().setVisibility(View.GONE);
		mConfigButtonVisible = true;
	}
	
	protected void setConfigButtonInvisible()
	{
		mContext.getConfigButton().setVisibility(View.GONE);
		mContext.getWriteIdeaButton().setVisibility(View.VISIBLE);
		mContext.getAudioIdeaButton().setVisibility(View.VISIBLE);
		mContext.getPhotoIdeaButton().setVisibility(View.VISIBLE);
		mContext.getVideoIdeaButton().setVisibility(View.VISIBLE);
		mConfigButtonVisible = false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_MENU)
		{
			onOptionButtonClick();
			return true;
		}
		return false;
	}
}
