package com.scurab.android.idearecorder.presenter;

import java.util.ArrayList;
import java.util.List;

import com.scurab.android.idearecorder.I;
import com.scurab.android.idearecorder.TestHelper;
import com.scurab.android.idearecorder.activity.WriteActivity;
import com.scurab.android.idearecorder.help.HelpImageButton;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.DataProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.speech.RecognizerIntent;
import android.test.AndroidTestCase;
import android.widget.ImageButton;

public class WriteActivityPresenterTest extends AndroidTestCase {
    DataProvider dp = null;

    @Override
    public void setContext(Context context) {
	super.setContext(context);
	dp = new DataProvider(mContext);
    }

    @Override
    protected void setUp() throws Exception {
	super.setUp();
	dp.deleteAllData();
    }

    @Override
    protected void tearDown() throws Exception {
	super.tearDown();
	dp.deleteAllData();
    }

    public void testBinding() {
	MockWriteActivity mwa = new MockWriteActivity();
	WriteActivityPresenter wap = new WriteActivityPresenter(mwa);
	wap.bind();
	assertNotNull(mwa.btnNameToText.getOnClickListener());
	assertNotNull(mwa.btnDescriptionToText.getOnClickListener());
	assertNotNull(mwa.btnSave.getOnClickListener());
	assertNotNull(mwa.btnCancel.getOnClickListener());
	assertNotNull(mwa.mOnActivityResultListener);
    }

    public void testCallingBindedControls() {
	MockWriteActivity mwa = new MockWriteActivity();
	MockWriteActivityPresenter wap = new MockWriteActivityPresenter(mwa);
	wap.bind();
	mwa.btnNameToText.getOnClickListener().onClick(mwa.btnNameToText);
	assertTrue(wap.clickNameToText);
	mwa.btnDescriptionToText.getOnClickListener().onClick(
		mwa.btnDescriptionToText);
	assertTrue(wap.clickDescriptionToText);
	mwa.btnSave.getOnClickListener().onClick(mwa.btnSave);
	assertTrue(wap.clickSave);
	mwa.btnCancel.getOnClickListener().onClick(mwa.btnCancel);
	assertTrue(wap.clickCancel);
    }

    public void testCallingNameToText() {
	MockWriteActivity mwa = new MockWriteActivity();
	MockWriteActivityPresenter2 map = new MockWriteActivityPresenter2(mwa);
	map.bind();
	map.onNameToText();
	assertNotNull(map.startIntent);
	assertEquals(map.reqCode,
		I.Constants.VOICE_RECOGNITION_REQUEST_CODE_NAME);
    }

    public void testReceiveNameToText() {
	MockWriteActivity2 mwa = new MockWriteActivity2();
	mwa.init();
	WriteActivityPresenter map = new WriteActivityPresenter(mwa);
	map.bind();

	String text = "Pokus abcde";
	Intent i = new Intent();

	ArrayList<String> data = new ArrayList<String>();
	data.add(text);

	i.putStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS, data);
	map.onActivityResult(I.Constants.VOICE_RECOGNITION_REQUEST_CODE_NAME,
		Activity.RESULT_OK, i);
	assertEquals(text, mwa.getNameEditText().getText().toString());
    }

    public void testCallingDescriptionToText() {
	MockWriteActivity mwa = new MockWriteActivity();
	MockWriteActivityPresenter2 map = new MockWriteActivityPresenter2(mwa);
	map.bind();
	map.onDescriptionToText();
	assertNotNull(map.startIntent);
	assertEquals(map.reqCode,
		I.Constants.VOICE_RECOGNITION_REQUEST_CODE_DESCRIPTION);
    }

    public void testReceiveDescriptionToText() {
	MockWriteActivity2 mwa = new MockWriteActivity2();
	mwa.init();
	WriteActivityPresenter map = new WriteActivityPresenter(mwa);
	map.bind();

	String text = "Pokus abcde ahoj fahoj gahoj lahoj";
	Intent i = new Intent();

	ArrayList<String> data = new ArrayList<String>();
	data.add(text);

	i.putStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS, data);
	map.onActivityResult(
		I.Constants.VOICE_RECOGNITION_REQUEST_CODE_DESCRIPTION,
		Activity.RESULT_OK, i);
	assertEquals(text, mwa.getDescriptionEditText().getText().toString());
    }

    public void testReceiveDescriptionToTextRepeatedly() {
	MockWriteActivity2 mwa = new MockWriteActivity2();
	mwa.init();
	WriteActivityPresenter map = new WriteActivityPresenter(mwa);
	map.bind();

	String text = "Pokus";
	Intent i = new Intent();

	ArrayList<String> data = new ArrayList<String>();
	data.add(text);

	i.putStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS, data);
	map.onActivityResult(
		I.Constants.VOICE_RECOGNITION_REQUEST_CODE_DESCRIPTION,
		Activity.RESULT_OK, i);

	String text2 = "Pokus2";
	i = new Intent();

	data = new ArrayList<String>();
	data.add(text2);

	i.putStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS, data);
	map.onActivityResult(
		I.Constants.VOICE_RECOGNITION_REQUEST_CODE_DESCRIPTION,
		Activity.RESULT_OK, i);

	String result = String.format("%s\n%s", text, text2);
	assertEquals(result, mwa.getDescriptionEditText().getText().toString());
    }

    public void testCancel() {
	MockWriteActivity2 mwa = new MockWriteActivity2();
	mwa.init();
	WriteActivityPresenter map = new WriteActivityPresenter(mwa);
	map.onCancel();
	assertTrue(mwa.quit);
    }

    public void testSaveNameAndDescription() {
	String n = "NAME01";
	String d = "DESCRIPTION01";

	MockWriteActivity2 mwa = new MockWriteActivity2();
	mwa.init();
	mwa.getNameEditText().setText(n);
	mwa.getDescriptionEditText().setText(d);
	WriteActivityPresenter map = new WriteActivityPresenter(mwa);
	assertTrue(map.onSave());
	assertTrue(mwa.quit);

	List<Idea> fromDb = new DataProvider(mContext).getIdeas();
	assertEquals(1, fromDb.size());
	Idea i = fromDb.get(0);
	assertEquals(n, i.getName());
	assertEquals(d, i.getDescription());
	assertEquals(Idea.TYPE_TEXT, i.getIdeaType());
	assertNull(i.getPath());
	assertTrue(i.getSaveTime() > 0);
	assertTrue(i.getSaveTime() < System.currentTimeMillis());
    }

    public void testSaveNameAndDescriptionTrimmed() {
	String n = " NAME01\n\t abc\n";
	String d = " DESCRIPTION01 \n";

	MockWriteActivity2 mwa = new MockWriteActivity2();
	mwa.init();
	mwa.getNameEditText().setText(n);
	mwa.getDescriptionEditText().setText(d);
	WriteActivityPresenter map = new WriteActivityPresenter(mwa);
	assertTrue(map.onSave());
	assertTrue(mwa.quit);

	List<Idea> fromDb = new DataProvider(mContext).getIdeas();
	assertEquals(1, fromDb.size());
	Idea i = fromDb.get(0);
	assertEquals("NAME01\n\t abc", i.getName());
	assertEquals("DESCRIPTION01", i.getDescription());
	assertEquals(Idea.TYPE_TEXT, i.getIdeaType());
	assertNull(i.getPath());
	assertTrue(i.getSaveTime() > 0);
	assertTrue(i.getSaveTime() < System.currentTimeMillis());
    }

    public void testSaveNameAndNoDescription() {
	String n = "NAME01";
	String d = "";

	MockWriteActivity2 mwa = new MockWriteActivity2();
	mwa.init();
	mwa.getNameEditText().setText(n);
	mwa.getDescriptionEditText().setText(d);
	WriteActivityPresenter map = new WriteActivityPresenter(mwa);
	assertTrue(map.onSave());
	assertTrue(mwa.quit);

	List<Idea> fromDb = new DataProvider(mContext).getIdeas();
	assertEquals(1, fromDb.size());
	Idea i = fromDb.get(0);
	assertEquals(n, i.getName());
	assertNull(d, i.getDescription());
	assertEquals(Idea.TYPE_TEXT, i.getIdeaType());
	assertNull(i.getPath());
	assertTrue(i.getSaveTime() > 0);
	assertTrue(i.getSaveTime() < System.currentTimeMillis());
    }

    public void testSaveWrongNameAndDescription() {
	String n = " \t ";
	String d = "ABC";

	MockWriteActivity2 mwa = new MockWriteActivity2();
	mwa.init();
	mwa.getNameEditText().setText(n);
	mwa.getDescriptionEditText().setText(d);
	WriteActivityPresenter map = new WriteActivityPresenter(mwa);
	assertFalse(map.onSave());
	assertFalse(mwa.quit);

	List<Idea> fromDb = new DataProvider(mContext).getIdeas();
	assertEquals(0, fromDb.size());
    }

    public void testOpenItemToEdit() {
	Idea i = TestHelper.getRandomIdea();
	dp.save(i);

	MockWriteActivity2 mwa = new MockWriteActivity2();
	Intent intent = new Intent();
	intent.putExtra(I.Constants.IDEA_ID, i.getId());
	mwa.setIntent(intent);
	mwa.init();
	WriteActivityPresenter wap = new WriteActivityPresenter(mwa);

	assertEquals(i.getName(), mwa.getNameEditText().getText().toString());
	assertEquals(i.getDescription(), mwa.getDescriptionEditText().getText()
		.toString());
    }

    public void testOpenItemToEditAndSave() throws InterruptedException {
	Idea i = TestHelper.getRandomIdea();
	i.setSaveTime(System.currentTimeMillis());
	dp.save(i);

	MockWriteActivity2 mwa = new MockWriteActivity2();
	Intent intent = new Intent();
	intent.putExtra(I.Constants.IDEA_ID, i.getId());
	mwa.setIntent(intent);
	mwa.init();
	WriteActivityPresenter wap = new WriteActivityPresenter(mwa);

	Idea i2 = TestHelper.getRandomIdea();
	mwa.getNameEditText().setText(i2.getName());
	mwa.getDescriptionEditText().setText(i2.getDescription());

	Thread.sleep(100);// just w8 few msecs
	assertTrue(wap.onSave());

	Idea fromDb = dp.getIdea(i.getId());
	assertEquals(i2.getName(), fromDb.getName());
	assertEquals(i2.getDescription(), fromDb.getDescription());
	assertEquals(Idea.TYPE_TEXT, fromDb.getIdeaType());
	assertTrue(fromDb.getSaveTime() > i2.getSaveTime());
    }

    private class MockWriteActivity extends WriteActivity {

	HelpImageButton btnNameToText = new HelpImageButton(mContext);
	HelpImageButton btnDescriptionToText = new HelpImageButton(mContext);

	HelpImageButton btnSave = new HelpImageButton(mContext);
	HelpImageButton btnCancel = new HelpImageButton(mContext);
	OnActivityResultListener mOnActivityResultListener;

	public MockWriteActivity() {
	    attachBaseContext(mContext);
	}

	@Override
	public void init() {
	    super.init();
	}

	@Override
	public ImageButton getNameRecordButton() {
	    return btnNameToText;
	}

	@Override
	public ImageButton getDescriptionRecorderButton() {
	    return btnDescriptionToText;
	}

	@Override
	public ImageButton getSaveButton() {
	    return btnSave;
	}

	@Override
	public ImageButton getCancelButton() {
	    return btnCancel;
	}

	@Override
	public void setOnActivityResultListener(
		OnActivityResultListener listener) {
	    mOnActivityResultListener = listener;
	}
    }

    private class MockWriteActivity2 extends WriteActivity {
	boolean quit = false;

	public MockWriteActivity2() {
	    attachBaseContext(mContext);
	}

	@Override
	public void init() {
	    super.init();
	}

	@Override
	public void finish() {
	    quit = true;
	}
    }

    private class MockWriteActivityPresenter extends WriteActivityPresenter {
	public boolean clickNameToText = false;
	public boolean clickDescriptionToText = false;
	public boolean clickSave = false;
	public boolean clickCancel = false;

	public MockWriteActivityPresenter(WriteActivity context) {
	    super(context);
	}

	@Override
	public void onNameToText() {
	    clickNameToText = true;
	}

	@Override
	public void onDescriptionToText() {
	    clickDescriptionToText = true;
	}

	@Override
	public boolean onSave() {
	    clickSave = true;
	    return false;
	}

	@Override
	public void onCancel() {
	    clickCancel = true;
	}
    }

    private class MockWriteActivityPresenter2 extends WriteActivityPresenter {
	public Intent startIntent = null;
	public int reqCode = 0;

	public MockWriteActivityPresenter2(WriteActivity context) {
	    super(context);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
	    startIntent = intent;
	    reqCode = requestCode;
	}
    }
}
