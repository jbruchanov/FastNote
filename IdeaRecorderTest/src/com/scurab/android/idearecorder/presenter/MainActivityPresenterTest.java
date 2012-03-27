package com.scurab.android.idearecorder.presenter;



import java.util.List;

import com.scurab.android.idearecorder.I;
import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.TestHelper;
import com.scurab.android.idearecorder.activity.MainActivity;
import com.scurab.android.idearecorder.activity.WriteActivity;
import com.scurab.android.idearecorder.help.HelpContextMenu;
import com.scurab.android.idearecorder.help.HelpImageButton;
import com.scurab.android.idearecorder.help.HelpListView;
import com.scurab.android.idearecorder.help.HelpMenuItem;
import com.scurab.android.idearecorder.interfaces.OnActivityStateChangeListener;
import com.scurab.android.idearecorder.interfaces.OnContextItemSelectedListener;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.DataProvider;

import android.content.Context;
import android.content.Intent;
import android.test.AndroidTestCase;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class MainActivityPresenterTest extends AndroidTestCase
{
	DataProvider db = null;
	@Override
	public void setContext(Context context)
	{
		super.setContext(context);
		db = new DataProvider(mContext);
		if(db.getTables().size() == 0)
			db.onCreate(db.getWritableDatabase());
	}
	
	@Override
	protected void setUp() throws Exception
	{	
		super.setUp();
		db.deleteAllData();
	}
	
	@Override
	protected void tearDown() throws Exception
	{	
		super.tearDown();
		db.deleteAllData();
	}
	
	 public void testBindingListView()
	 {
		 MainActivity ma = new MockMainActivity1();
		 MainActivityPresenter map = new MainActivityPresenter(ma);
		 assertNotNull(ma.getListView().getOnItemClickListener());
	 }
	 
	 public void testBindingButtons()
	 {
		 MainActivity ma = new MockMainActivity1();		 
		 MainActivityPresenter map = new MainActivityPresenter(ma);
		 assertNotNull(((HelpImageButton)ma.getWriteIdeaButton()).getOnClickListener());
		 assertNotNull(((HelpImageButton)ma.getAudioIdeaButton()).getOnClickListener());
		 assertNotNull(((HelpImageButton)ma.getPhotoIdeaButton()).getOnClickListener());		 
		 assertNotNull(((HelpImageButton)ma.getVideoIdeaButton()).getOnClickListener());
	 }
	 
	 
	 
	 public void testLoadData()
	 {
		 MockMainActivity2 ma = new MockMainActivity2();
		 ma.init();
		 List<Idea> data = TestHelper.getRandomIdeas(10);
 		 for(Idea i : data)
			 db.save(i);
		 
		 MainActivityPresenter map = new MainActivityPresenter(ma);
		 map.loadData();
		 
		 assertNotNull(ma.getListView().getAdapter());
		 assertEquals(data.size(),ma.getListView().getAdapter().getCount());
	 }
	 
	 public void testRegisterForCreateContextMenu()
	 {
		 MockMainActivity2 ma = new MockMainActivity2();
		 ma.init();
		 MainActivityPresenter map = new MainActivityPresenter(ma);
		 assertEquals(ma.getListView(), ma.contextMenuListener);
	 }
	 
	 public void testBindForCreateContextMenu()
	 {
		 MockMainActivity2 ma = new MockMainActivity2();
		 ma.init();
		 MainActivityPresenter map = new MainActivityPresenter(ma);
		 assertNotNull(ma.createListener);
	 }
	 
	 public void testBindOnSelectContextMenuItem()
	 {
		 MockMainActivity2 ma = new MockMainActivity2();
		 ma.init();
		 MainActivityPresenter map = new MainActivityPresenter(ma);
		 assertNotNull(ma.itemClickListener);
	 }
	 
	 public void testBindOnResumePauseListener()
	 {
		 MockMainActivity2 ma = new MockMainActivity2();
		 ma.init();
		 MainActivityPresenter map = new MainActivityPresenter(ma);
		 assertNotNull(ma.onActivityStateChangeListener);
	 }
	 
	 public void testDeleteIdeaByContextMenu()
	 {
		 MockMainActivity2 ma = new MockMainActivity2();
		 ma.init();
		 List<Idea> data = TestHelper.getRandomIdeas(1);
 		 for(Idea i : data)
			 db.save(i);
		 
 		 assertEquals(1,db.getIdeas().size());
 		 
		 MainActivityPresenter map = new MainActivityPresenter(ma);
		 map.loadData();
		 
		 HelpMenuItem hmi = new HelpMenuItem();		 
		 hmi.setMenuInfo(new AdapterContextMenuInfo(null, 0, 0));
		 hmi.setItemId(R.id.muDelete);		 
		 map.onContextItemSelected(hmi);
		 
		 assertEquals(0,db.getIdeas().size());
		 
	 }
	 
	 public void testDeleteIdeaByContextMenuAndRefreshListBug()
	 {
		 MockMainActivity2 ma = new MockMainActivity2();
		 ma.init();
		 int len = 3;
		 List<Idea> data = TestHelper.getRandomIdeas(len);
 		 for(Idea i : data)
			 db.save(i);
		 
 		 assertEquals(len,db.getIdeas().size());
 		 
		 MainActivityPresenter map = new MainActivityPresenter(ma);
		 map.loadData();
		 
		 HelpMenuItem hmi = new HelpMenuItem();		 
		 hmi.setMenuInfo(new AdapterContextMenuInfo(null, 0, 0));
		 hmi.setItemId(R.id.muDelete);		 
		 map.onContextItemSelected(hmi);
		 
		 assertEquals(len-1,db.getIdeas().size());
		 assertEquals(len-1,ma.getListView().getAdapter().getCount());
	 }
	 
	 public void testStartWriteIdeaActivity()
	 {
		 MockMainActivity2 ma = new MockMainActivity2();
		 ma.init();
		 MainActivityPresenter map = new MainActivityPresenter(ma);
		 map.onWriteIdeaButtonClick();
		 assertEquals(WriteActivity.class.getName(), ma.startActivityClass);
	 }
	 
	 public void testOpenItemToEdit()
	 {
		 Idea i = TestHelper.getRandomIdea();
		 i.setIdeaType(Idea.TYPE_TEXT);
		 db.save(i);
		 
		 MockMainActivity2 ma = new MockMainActivity2();
		 ma.init();
		 MainActivityPresenter map = new MainActivityPresenter(ma);
		 map.loadData();
		 map.onListViewItemClick(i);
		 assertEquals(WriteActivity.class.getName(),ma.startActivityClass);
		 assertTrue(ma.startIntent.hasExtra(I.Constants.IDEA_ID));
		 assertEquals(i.getId(), ma.startIntent.getLongExtra(I.Constants.IDEA_ID, 0));
	 }
	 
	 
	 private class MockMainActivity1 extends MainActivity
	 {
		HelpListView hListView = new HelpListView(mContext);
		HelpImageButton mPen = new HelpImageButton(mContext);
		HelpImageButton mSpeech = new HelpImageButton(mContext);
		HelpImageButton mAudio = new HelpImageButton(mContext);
		HelpImageButton mVideo = new HelpImageButton(mContext);
		
		public MockMainActivity1()
		{
			attachBaseContext(mContext);
		}
		@Override
		public ListView getListView()
		{
			return hListView;
		}
		
		@Override
		public HelpImageButton getWriteIdeaButton()
		{
			return mPen;
		}
		
		@Override
		public HelpImageButton getPhotoIdeaButton()
		{
			return mSpeech;
		}
		
		@Override
		public HelpImageButton getAudioIdeaButton()
		{
			return mAudio;
		}
		
		@Override
		public HelpImageButton getVideoIdeaButton()
		{
			return mVideo;
		}
	 }
	 
	 private class MockMainActivity2 extends MainActivity
	 {		 
		 public OnCreateContextMenuListener createListener = null;
		 public View contextMenuListener = null;
		 public OnContextItemSelectedListener itemClickListener = null;
		 public String startActivityClass = null;
		 public OnActivityStateChangeListener onActivityStateChangeListener;
		 public Intent startIntent = null;
		 
		public MockMainActivity2()
		{
			attachBaseContext(mContext);
		}
		 
		 @Override
		public void init()
		{
			super.init();
						
		}
		 		
		 		
		@Override
		protected View getContentView()
		{
			 return View.inflate(mContext, R.layout.mainactivity, null);
		}
		
		@Override
		public void registerForContextMenu(View view)
		{
			super.registerForContextMenu(view);
			contextMenuListener = view;
		}
		@Override
		public void setOnContextMenuCreateListener(OnCreateContextMenuListener listener)
		{
			super.setOnContextMenuCreateListener(listener);
			createListener = listener;
		}
		
		@Override
		public void setOnContextItemSelectedListener(OnContextItemSelectedListener listener)
		{
			super.setOnContextItemSelectedListener(listener);
			itemClickListener = listener;
		}
		
		@Override
		public void startActivity(android.content.Intent intent) 
		{
			startIntent = intent;
			startActivityClass = intent.getComponent().getClassName();
		};
		
		@Override
		public void setOnActivityStateChangeListener(OnActivityStateChangeListener listener)
		{
			onActivityStateChangeListener = listener;
		}
	 }
}
