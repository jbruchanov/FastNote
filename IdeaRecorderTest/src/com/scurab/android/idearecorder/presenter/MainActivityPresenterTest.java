package com.scurab.android.idearecorder.presenter;



import java.util.List;

import junit.framework.TestCase;

import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.TestHelper;
import com.scurab.android.idearecorder.activity.MainActivity;
import com.scurab.android.idearecorder.adapter.IdeaListAdapter;
import com.scurab.android.idearecorder.help.HelpButton;
import com.scurab.android.idearecorder.help.HelpImageButton;
import com.scurab.android.idearecorder.help.HelpListView;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.DataProvider;

import android.content.Context;
import android.database.DatabaseUtils;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;


public class MainActivityPresenterTest extends AndroidTestCase
{

	@Override
	protected void setUp() throws Exception
	{	
		super.setUp();
		DataProvider db = new DataProvider(mContext);		
		db.dropAllTables();
		db.onCreate(db.getWritableDatabase());
	}
	
	@Override
	protected void tearDown() throws Exception
	{	
		super.tearDown();
		DataProvider db = new DataProvider(mContext);
		db.dropAllTables();
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
	 
	 public void testFindingViews()
	 {
		 MockMainActivity2 ma = new MockMainActivity2();		 
		 ma.init();
		 assertNotNull(ma.getListView());
		 assertNotNull(ma.getWriteIdeaButton());
		 assertNotNull(ma.getAudioIdeaButton());
		 assertNotNull(ma.getPhotoIdeaButton());
		 assertNotNull(ma.getVideoIdeaButton());
	 }
	 
	 public void testLoadData()
	 {
		 MockMainActivity2 ma = new MockMainActivity2();
		 ma.attachBaseContext(mContext);
		 ma.init();
		 DataProvider db = new DataProvider(mContext);		
		 List<Idea> data = TestHelper.getRandomIdeas(10);
 		 for(Idea i : data)
			 db.save(i);
		 
		 MainActivityPresenter map = new MainActivityPresenter(ma);
		 map.loadData();
		 
		 assertNotNull(ma.getListView().getAdapter());
		 assertEquals(data.size(),ma.getListView().getAdapter().getCount());
	 }
	 
	 private class MockMainActivity1 extends MainActivity
	 {
		HelpListView hListView = new HelpListView(mContext);
		HelpImageButton mPen = new HelpImageButton(mContext);
		HelpImageButton mSpeech = new HelpImageButton(mContext);
		HelpImageButton mAudio = new HelpImageButton(mContext);
		HelpImageButton mVideo = new HelpImageButton(mContext);
		
		
		
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
		 @Override
		public void attachBaseContext(Context newBase)
		{
			super.attachBaseContext(newBase);
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
	 }
}
