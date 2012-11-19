package com.scurab.android.idearecorder.presenter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.test.AndroidTestCase;
import android.widget.ImageButton;

import com.scurab.android.idearecorder.I;
import com.scurab.android.idearecorder.TestHelper;
import com.scurab.android.idearecorder.activity.ImagePreviewActivity;
import com.scurab.android.idearecorder.activity.PhotoActivity;
import com.scurab.android.idearecorder.help.HelpImageButton;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.DataProvider;

public class PhotoActivityPresenterTest extends AndroidTestCase {
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

    public void testBinding() {
	MockPhotoActivity mpa = new MockPhotoActivity();
	mpa.init();
	PhotoActivityPresenter par = new PhotoActivityPresenter(mpa);
	assertNotNull(mpa.photo.getOnClickListener());
    }

    public void testStartPhotoIntent() {
	MockPhotoActivity mpa = new MockPhotoActivity();
	mpa.init();
	MockPhotoActivityPresenter par = new MockPhotoActivityPresenter(mpa);
	par.onPhotoButtonClick();

	assertNotNull(par.startingIntent);
	assertEquals(I.Constants.REQUEST_TAKE_PHOTO, par.startcode);
	assertEquals(MediaStore.ACTION_IMAGE_CAPTURE,
		par.startingIntent.getAction());
	assertTrue(par.startingIntent.hasExtra(MediaStore.EXTRA_OUTPUT));
	assertTrue(par.startingIntent.getExtras().get(MediaStore.EXTRA_OUTPUT)
		.toString().trim().length() > Environment
		.getExternalStoragePublicDirectory(
			Environment.DIRECTORY_PICTURES).toString().trim()
		.length());
    }

    public void testReceivePhotoIntent() throws IOException {

	MockPhotoActivity mpa = new MockPhotoActivity();
	mpa.init();
	MockPhotoActivityPresenter par = new MockPhotoActivityPresenter(mpa);
	fileToDelete = par.generateRandomFileToSave(null, null);
	assertNull(mpa.getImagePreview().getDrawable());
	assertTrue(par.onActivityResult(I.Constants.REQUEST_TAKE_PHOTO,
		Activity.RESULT_OK, new Intent()));
	assertEquals(fileToDelete, par.imagePreviewFile);

    }

    public void testReceivePhotoIntentAndShowPicture() throws IOException {
	MockPhotoActivity mpa = new MockPhotoActivity();
	mpa.init();
	PhotoActivityPresenter par = new PhotoActivityPresenter(mpa);
	fileToDelete = par.generateRandomFileToSave(null, null);
	TestHelper.genRandomJPG(fileToDelete);
	assertNull(mpa.getImagePreview().getDrawable());
	assertTrue(par.onActivityResult(I.Constants.REQUEST_TAKE_PHOTO,
		Activity.RESULT_OK, new Intent()));
	assertNotNull(mpa.getImagePreview().getDrawable());
    }

    public void testSaveMissingPhoto() throws IOException {
	MockPhotoActivity mpa = new MockPhotoActivity();
	mpa.init();
	PhotoActivityPresenter par = new PhotoActivityPresenter(mpa);
	assertFalse(par.onSave());
    }

    public void testSaveOK() throws IOException {
	MockPhotoActivity mpa = new MockPhotoActivity();
	mpa.init();
	String n = "Name01";
	String d = "Desc1";
	mpa.getNameEditText().setText(n);
	mpa.getDescriptionEditText().setText(d);
	MockPhotoActivityPresenter2 par = new MockPhotoActivityPresenter2(mpa);
	fileToDelete = par.generateRandomFileToSave(null, null);
	TestHelper.genRandomJPG(fileToDelete);
	assertTrue(par.onActivityResult(I.Constants.REQUEST_TAKE_PHOTO,
		Activity.RESULT_OK, new Intent()));
	assertTrue(par.onSave());
	assertTrue(new File(fileToDelete2).exists());

	List<Idea> ideas = dp.getIdeas();
	assertEquals(1, ideas.size());
	Idea i = ideas.get(0);
	assertEquals(n, i.getName());
	assertEquals(d, i.getDescription());
	assertEquals(Idea.TYPE_IMAGE, i.getIdeaType());
	assertEquals(fileToDelete2, i.getPath());
	assertTrue(i.getSaveTime() > 0);
    }

    public void testOpenExisting() throws IOException {
	fileToDelete = "/sdcard/temp.jpg";
	TestHelper.genRandomJPG(fileToDelete);
	Idea i = TestHelper.getRandomIdea();
	i.setPath(fileToDelete);
	dp.save(i);

	Intent intent = new Intent();
	intent.putExtra(I.Constants.IDEA_ID, i.getId());

	MockPhotoActivity mpa = new MockPhotoActivity();
	mpa.setIntent(intent);
	mpa.init();
	PhotoActivityPresenter par = new PhotoActivityPresenter(mpa);

	assertEquals(i.getName(), mpa.getNameEditText().getText().toString());
	assertEquals(i.getDescription(), mpa.getDescriptionEditText().getText()
		.toString());
	assertNotNull(mpa.getImagePreview().getDrawable());
    }

    public void testOpenExistingAndClickPhotoButton() throws IOException {
	fileToDelete = "/sdcard/temp.jpg";
	TestHelper.genRandomJPG(fileToDelete);
	Idea i = TestHelper.getRandomIdea();
	i.setPath(fileToDelete);
	dp.save(i);

	Intent intent = new Intent();
	intent.putExtra(I.Constants.IDEA_ID, i.getId());

	MockPhotoActivity mpa = new MockPhotoActivity();
	mpa.setIntent(intent);
	mpa.init();
	MockPhotoActivityPresenter2 par = new MockPhotoActivityPresenter2(mpa);
	par.onPhotoButtonClick();
	assertNotNull(par.startingIntent);
	assertEquals(ImagePreviewActivity.class.getName(), par.startingIntent
		.getComponent().getClassName());
	assertTrue(par.startingIntent.hasExtra(I.Constants.IDEA_IMAGE_PATH));
	assertTrue(par.startingIntent.getStringExtra(
		I.Constants.IDEA_IMAGE_PATH).contains(fileToDelete));
    }

    public void testClickImageNoPhoto() {
	MockPhotoActivity mpa = new MockPhotoActivity();
	mpa.init();
	MockPhotoActivityPresenter2 par = new MockPhotoActivityPresenter2(mpa);
	par.startImagePreviewActivity(null);
	assertNull(par.startingIntent);
    }

    public void testClickImagePhotoWOSaved() throws IOException {
	MockPhotoActivity mpa = new MockPhotoActivity();
	mpa.init();
	MockPhotoActivityPresenter2 par = new MockPhotoActivityPresenter2(mpa);
	fileToDelete = par.generateRandomFileToSave(null, null);
	TestHelper.genRandomJPG(fileToDelete);
	par.startImagePreviewActivity(null);
	assertNotNull(par.startingIntent);
	assertEquals(ImagePreviewActivity.class.getName(), par.startingIntent
		.getComponent().getClassName());
	assertTrue(par.startingIntent.hasExtra(I.Constants.IDEA_IMAGE_PATH));
	assertTrue(par.startingIntent.getStringExtra(
		I.Constants.IDEA_IMAGE_PATH).contains(fileToDelete));
    }

    public void testOpenExistingAndUpdate() throws IOException {
	fileToDelete = "/sdcard/temp.jpg";
	TestHelper.genRandomJPG(fileToDelete);
	Idea i = TestHelper.getRandomIdea();
	i.setIdeaType(Idea.TYPE_IMAGE);
	i.setPath(fileToDelete);
	dp.save(i);

	Intent intent = new Intent();
	intent.putExtra(I.Constants.IDEA_ID, i.getId());

	MockPhotoActivity mpa = new MockPhotoActivity();
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
	assertEquals(Idea.TYPE_IMAGE, fromDb.getIdeaType());
	assertEquals(i.getSaveTime(), fromDb.getSaveTime());
	assertEquals(i.getPath(), fromDb.getPath());
	assertTrue(new File(fromDb.getPath()).exists());
    }

    private class MockPhotoActivity extends PhotoActivity {
	HelpImageButton photo;

	public MockPhotoActivity() {
	    attachBaseContext(mContext);
	}

	@Override
	protected void init() {
	    super.init();
	}

	@Override
	public ImageButton getPhotoButton() {
	    if (photo == null) {
		photo = new HelpImageButton(mContext);
	    }
	    return photo;
	}
    }

    private class MockPhotoActivityPresenter extends PhotoActivityPresenter {
	Intent startingIntent;
	int startcode;
	String imagePreviewFile;

	public MockPhotoActivityPresenter(PhotoActivity context) {
	    super(context);
	}

	@Override
	public void startActivity(Intent intent) {
	    startingIntent = intent;
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
	    startingIntent = intent;
	    startcode = requestCode;
	}

	@Override
	public void setImagePreview(String file) {
	    imagePreviewFile = file;
	}
    }

    private class MockPhotoActivityPresenter2 extends PhotoActivityPresenter {
	Intent startingIntent;

	public MockPhotoActivityPresenter2(PhotoActivity context) {
	    super(context);
	}

	@Override
	public void startActivity(Intent intent) {
	    startingIntent = intent;
	}

	@Override
	protected String moveTempFile() throws IOException {
	    fileToDelete2 = super.moveTempFile();
	    return fileToDelete2;
	}
    }
}
