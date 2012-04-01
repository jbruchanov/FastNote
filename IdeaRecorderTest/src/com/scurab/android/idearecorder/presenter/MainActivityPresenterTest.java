package com.scurab.android.idearecorder.presenter;



import java.io.File;
import java.io.IOException;
import java.util.List;

import com.scurab.android.idearecorder.I;
import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.TestHelper;
import com.scurab.android.idearecorder.activity.MainActivity;
import com.scurab.android.idearecorder.activity.PreferencesActivity;
import com.scurab.android.idearecorder.activity.WriteActivity;
import com.scurab.android.idearecorder.help.HelpImageButton;
import com.scurab.android.idearecorder.help.HelpListView;
import com.scurab.android.idearecorder.help.HelpMenuItem;
import com.scurab.android.idearecorder.interfaces.OnActivityKeyDownListener;
import com.scurab.android.idearecorder.interfaces.OnActivityStateChangeListener;
import com.scurab.android.idearecorder.interfaces.OnContextItemSelectedListener;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.DataProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;


public class MainActivityPresenterTest extends AndroidTestCase
{
	DataProvider db = null;
	String fileToDelete = null;
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
		if(fileToDelete != null)
		{
			new File(fileToDelete).delete();
		}
	}
	
	 public void testBindingListView()
	 {
		 MainActivity ma = new MockMainActivity1();
		 MainActivityPresenter map = new MainActivityPresenter(ma);
		 assertNotNull(ma.getListView().getOnItemClickListener());
	 }
	 
	 public void testBinding()
	 {
		 MockMainActivity1 ma = new MockMainActivity1();		 
		 MainActivityPresenter map = new MainActivityPresenter(ma);
		 assertNotNull(ma.getWriteIdeaButton().getOnClickListener());
		 assertNotNull(ma.getAudioIdeaButton().getOnClickListener());
		 assertNotNull(ma.getPhotoIdeaButton().getOnClickListener());		 
		 assertNotNull(ma.getVideoIdeaButton().getOnClickListener());
		 assertNotNull(((HelpImageButton)ma.getConfigButton()).getOnClickListener());
		 assertNotNull(ma.mOnActivityKeyDownListener);
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
	 
	 public void testSendByContextMenu() throws IOException, InterruptedException
	 {
		 int[] types = new int[] {Idea.TYPE_AUDIO,Idea.TYPE_IMAGE,Idea.TYPE_TEXT,Idea.TYPE_VIDEO};
		 String[] mimes = new String[] {I.MimeType.AUDIO_3GPP,I.MimeType.IMAGE_JPEG,I.MimeType.TEXT_PLAIN,I.MimeType.VIDEO_MP4};
		 fileToDelete = "/sdcard/tmb.bin";
		 TestHelper.genRandomFile(fileToDelete, 2048);
		 for(int i = 0;i<types.length;i++)
		 {
			 MockMainActivity2 ma = new MockMainActivity2();
			 ma.init();
			 Idea data = TestHelper.getRandomIdeas(1).get(0);
			 if(types[i] != Idea.TYPE_TEXT)
				 data.setPath(fileToDelete);
	 		 data.setIdeaType(types[i]);
			 db.save(data);
	 		 
			 MainActivityPresenter map = new MainActivityPresenter(ma);
			 map.loadData();
			 
			 HelpMenuItem hmi = new HelpMenuItem();		 
			 hmi.setMenuInfo(new AdapterContextMenuInfo(null, 0, 0));
			 hmi.setItemId(R.id.muSend);		 
			 map.onContextItemSelected(hmi);
			 
			 assertNotNull(ma.startIntent);
			 assertTrue(ma.startIntent.hasExtra(Intent.EXTRA_INTENT));
			 assertEquals(Intent.ACTION_CHOOSER, ma.startIntent.getAction());
			 
			 Intent extraIntent = (Intent) ma.startIntent.getExtras().get(Intent.EXTRA_INTENT);
			 assertEquals(Intent.ACTION_SEND, extraIntent.getAction());
			 assertEquals(mimes[i], extraIntent.getType());
			 String extra = extraIntent.getStringExtra(Intent.EXTRA_TEXT); 
			 assertTrue(extra.contains(data.getName()) && extra.contains(data.getDescription()));
			 if(types[i] != Idea.TYPE_TEXT)
			 {
				 assertTrue(extraIntent.hasExtra(Intent.EXTRA_STREAM));
				 assertTrue(extraIntent.getExtras().get(Intent.EXTRA_STREAM) instanceof Uri);
				 assertEquals("file://" + data.getPath(), extraIntent.getExtras().get(Intent.EXTRA_STREAM).toString());
			 }
			 ma.startIntent = null;
			 db.deleteAllData();
		 }
	 }
	 
	 public void testShowConfigButton()
	 {
		 MockMainActivity2 ma = new MockMainActivity2();
		 ma.init();
		 MainActivityPresenter pres = new MainActivityPresenter(ma);
		 pres.bind();
		 
		 assertEquals(View.GONE,ma.getConfigButton().getVisibility());
		 assertEquals(View.VISIBLE,ma.getWriteIdeaButton().getVisibility());
		 assertEquals(View.VISIBLE,ma.getAudioIdeaButton().getVisibility());
		 assertEquals(View.VISIBLE,ma.getVideoIdeaButton().getVisibility());
		 assertEquals(View.VISIBLE,ma.getPhotoIdeaButton().getVisibility());
		 
		 pres.setConfigButtonVisible();
		 
		 assertEquals(View.VISIBLE,ma.getConfigButton().getVisibility());
		 assertEquals(View.GONE,ma.getWriteIdeaButton().getVisibility());
		 assertEquals(View.GONE,ma.getAudioIdeaButton().getVisibility());
		 assertEquals(View.GONE,ma.getVideoIdeaButton().getVisibility());
		 assertEquals(View.GONE,ma.getPhotoIdeaButton().getVisibility());
		 
		 pres.setConfigButtonInvisible();
		 
		 assertEquals(View.GONE,ma.getConfigButton().getVisibility());
		 assertEquals(View.VISIBLE,ma.getWriteIdeaButton().getVisibility());
		 assertEquals(View.VISIBLE,ma.getAudioIdeaButton().getVisibility());
		 assertEquals(View.VISIBLE,ma.getVideoIdeaButton().getVisibility());
		 assertEquals(View.VISIBLE,ma.getPhotoIdeaButton().getVisibility());
	 }
	 
	 public void testOpenPreferenceActivity()
	 {
		 MockMainActivity2 ma = new MockMainActivity2();
		 ma.init();
		 MainActivityPresenter pres = new MainActivityPresenter(ma);
		 pres.onConfiguButtonClick();
		 
		 assertNotNull(ma.startIntent);
		 assertEquals(PreferencesActivity.class.getName(),ma.startIntent.getComponent().getClassName());
	 }
	 
	 private class MockMainActivity1 extends MainActivity
	 {
		HelpListView hListView = new HelpListView(mContext);
		HelpImageButton mPen = new HelpImageButton(mContext);
		HelpImageButton mSpeech = new HelpImageButton(mContext);
		HelpImageButton mAudio = new HelpImageButton(mContext);
		HelpImageButton mVideo = new HelpImageButton(mContext);
		HelpImageButton mConfig = new HelpImageButton(mContext);
		public OnActivityKeyDownListener mOnActivityKeyDownListener = null;
		
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
		
		@Override
		public View getConfigButton()
		{
			return mConfig;
		}
		
		@Override
		public void setOnActivityKeyDownListener(OnActivityKeyDownListener onActivityKeyDownListener)
		{
			mOnActivityKeyDownListener = onActivityKeyDownListener;
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
			if(intent.getComponent() != null)
				startActivityClass = intent.getComponent().getClassName();
		};
		
		@Override
		public void setOnActivityStateChangeListener(OnActivityStateChangeListener listener)
		{
			onActivityStateChangeListener = listener;
		}
		
		
	 }
}
