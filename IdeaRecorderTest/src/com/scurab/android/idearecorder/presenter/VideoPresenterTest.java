package com.scurab.android.idearecorder.presenter;

import java.io.File;
import java.io.IOException;
import com.scurab.android.idearecorder.I;
import com.scurab.android.idearecorder.TestHelper;
import com.scurab.android.idearecorder.activity.VideoActivity;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.DataProvider;

import android.content.Context;
import android.content.Intent;
import android.test.AndroidTestCase;

public class VideoPresenterTest extends AndroidTestCase {
    private static String fileToDelete;
    private static String fileToDelete2;
    private DataProvider dp;

    @Override
    public void setContext(Context context) {
	super.setContext(context);
	dp = new DataProvider(context);
    }

    @Override
    protected void setUp() throws Exception {
	super.setUp();
	dp.deleteAllData();
    }

    @Override
    protected void tearDown() throws Exception {
	super.tearDown();
	if (fileToDelete != null) {
	    new File(fileToDelete).delete();
	    fileToDelete = null;
	}
	if (fileToDelete2 != null) {
	    new File(fileToDelete2).delete();
	    fileToDelete2 = null;
	}
	dp.deleteAllData();
    }

    public void testStartVideoIntent() {
	MockVideoActivity mpa = new MockVideoActivity();
	mpa.init();
	MockVideoActivityPresenter par = new MockVideoActivityPresenter(mpa);
	par.onPhotoButtonClick();

	assertNotNull(par.startIntent);
	assertEquals(I.Constants.REQUEST_TAKE_VIDEO, par.reqCode);
	// assertEquals(MediaStore.ACTION_VIDEO_CAPTURE,par.startIntent.getAction());
	assertEquals(Intent.ACTION_CHOOSER, par.startIntent.getAction());
	// assertTrue(par.startIntent.hasExtra(MediaStore.EXTRA_OUTPUT));
	// assertTrue(par.startIntent.getExtras().get(MediaStore.EXTRA_OUTPUT).toString().length()
	// >
	// Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).length());
    }

    public void testSaveNoVideo() {
	MockVideoActivity mpa = new MockVideoActivity();
	mpa.init();
	MockVideoActivityPresenter par = new MockVideoActivityPresenter(mpa);
	assertFalse(par.onSave());
    }

    // tested, but changed to use content resources

    public void testSaveOK() throws IOException {
	// MockVideoActivity mpa = new MockVideoActivity();
	// mpa.init();
	// String n = "Name01";
	// String d= "Desc1";
	// mpa.getNameEditText().setText(n);
	// mpa.getDescriptionEditText().setText(d);
	//
	// MockVideoActivityPresenter par = new MockVideoActivityPresenter(mpa);
	// fileToDelete = par.generateRandomFileToSave(null, null);
	// TestHelper.genRandomFile(fileToDelete,2048);
	//
	// assertTrue(par.onActivityResult(I.Constants.REQUEST_TAKE_VIDEO,
	// Activity.RESULT_OK, new Intent()));
	// assertTrue(par.onSave());
	//
	// List<Idea> ideas = dp.getIdeas();
	// assertEquals(1,ideas.size());
	// Idea i = ideas.get(0);
	// assertEquals(n,i.getName());
	// assertEquals(d,i.getDescription());
	// assertEquals(Idea.TYPE_VIDEO,i.getIdeaType());
	// assertTrue(i.getSaveTime() > 0);
    }

    public void testOpenExistingAndOpenVideo() throws IOException {
	fileToDelete = "/sdcard/video.mp4";
	TestHelper.genRandomFile(fileToDelete, 2048);
	Idea i = TestHelper.getRandomIdea();
	i.setPath(fileToDelete);
	dp.save(i);

	Intent intent = new Intent();
	intent.putExtra(I.Constants.IDEA_ID, i.getId());

	MockVideoActivity mpa = new MockVideoActivity();
	mpa.setIntent(intent);
	mpa.init();
	MockVideoActivityPresenter par = new MockVideoActivityPresenter(mpa);

	assertEquals(i.getName(), mpa.getNameEditText().getText().toString());
	assertEquals(i.getDescription(), mpa.getDescriptionEditText().getText()
		.toString());

	par.onPhotoButtonClick();

	assertNotNull(par.startIntent);
	assertEquals(Intent.ACTION_VIEW, par.startIntent.getAction());
	assertTrue(par.startIntent.getData().toString().contains(fileToDelete));
	// assertEquals(1,par.startIntent.getIntExtra(MediaStore.EXTRA_VIDEO_QUALITY,
	// -1));
	assertEquals("video/*", par.startIntent.getType());
    }

    public void testOpenExistingAndUpdate() throws IOException {
	fileToDelete = "/sdcard/video.mp4";
	TestHelper.genRandomFile(fileToDelete, 2048);
	Idea i = TestHelper.getRandomIdea();
	i.setIdeaType(Idea.TYPE_VIDEO);
	i.setPath(fileToDelete);
	dp.save(i);

	Intent intent = new Intent();
	intent.putExtra(I.Constants.IDEA_ID, i.getId());

	MockVideoActivity mpa = new MockVideoActivity();
	mpa.setIntent(intent);
	mpa.init();
	PhotoActivityPresenter par = new PhotoActivityPresenter(mpa);

	String n = "Name01abc";
	String d = "Desc02cde";
	mpa.getNameEditText().setText(n);
	mpa.getDescriptionEditText().setText(d);
	assertTrue(par.onSave());

	Idea fromDb = dp.getIdea(i.getId());

	assertEquals(n, fromDb.getName());
	assertEquals(d, fromDb.getDescription());
	assertEquals(Idea.TYPE_VIDEO, fromDb.getIdeaType());
	assertEquals(i.getSaveTime(), fromDb.getSaveTime());
	assertEquals(i.getPath(), fromDb.getPath());
	assertTrue(new File(fromDb.getPath()).exists());
    }

    private class MockVideoActivity extends VideoActivity {
	public MockVideoActivity() {
	    attachBaseContext(mContext);
	}

	@Override
	protected void init() {
	    super.init();
	}
    }

    private class MockVideoActivityPresenter extends VideoActivityPresenter {
	public Intent startIntent;
	public int reqCode;

	public MockVideoActivityPresenter(VideoActivity context) {
	    super(context);
	}

	@Override
	public void startActivity(Intent intent) {
	    startIntent = intent;
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
	    startIntent = intent;
	    reqCode = requestCode;
	}
    }

}
