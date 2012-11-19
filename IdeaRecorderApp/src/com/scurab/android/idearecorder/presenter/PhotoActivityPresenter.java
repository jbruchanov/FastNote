package com.scurab.android.idearecorder.presenter;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.scurab.android.idearecorder.I;
import com.scurab.android.idearecorder.IdeaRecorderApplication;
import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.activity.ImagePreviewActivity;
import com.scurab.android.idearecorder.activity.PhotoActivity;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.PictureUtils;
import com.scurab.android.idearecorder.tools.StringTools;

public class PhotoActivityPresenter extends WriteActivityPresenter {
    private PhotoActivity mContext;
    private boolean mPhotoTaken;
    private Idea mUpdatingIdea;

    // dont use it directly!
    private static String TEMP_FILE;

    public PhotoActivityPresenter(PhotoActivity context) {
	super(context);
	bind();
    }

    @Override
    protected void onAttachContext(Context context) {
	mContext = (PhotoActivity) context;
	super.onAttachContext(context);
    }

    @Override
    protected void bind() {
	super.bind();
	mContext.getPhotoButton().setOnClickListener(
		new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			onPhotoButtonClick();
		    }
		});

	mContext.getImagePreview().setOnClickListener(
		new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			startImagePreviewActivity(mUpdatingIdea);
		    }
		});
    }

    public void onPhotoButtonClick() {
	try {
	    if (mUpdatingIdea != null)
		startImagePreviewActivity(mUpdatingIdea);
	    else
		startTakePhotoActivity();
	} catch (Exception e) {
	    showError(e);
	}
    }

    private void startTakePhotoActivity() throws IOException {

	TEMP_FILE = null;
	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	// must be from file, otherwise OK button doesn't work !
	intent.putExtra(MediaStore.EXTRA_OUTPUT,
		Uri.fromFile(new File(generateTempFileToSave())));
	startActivityForResult(intent, I.Constants.REQUEST_TAKE_PHOTO);
    }

    protected void startImagePreviewActivity(Idea i) {
	Intent intent = null;
	if (i == null) {
	    String file = generateTempFileToSave();
	    File f = new File(file);
	    if (f.exists() && f.length() > 0)
		intent = getShowImageIntent(file);
	} else
	    // already existing and saved idea
	    intent = getShowImageIntent(i.getPath());

	if (intent != null)
	    startActivity(intent);
    }

    private Intent getShowImageIntent(String file) {
	Intent intent = new Intent(mContext, ImagePreviewActivity.class);
	intent.putExtra(I.Constants.IDEA_IMAGE_PATH, "file://" + file);
	// Intent intent = new Intent(Intent.ACTION_VIEW);
	// intent.setDataAndType(Uri.parse(file), "image/*");
	return intent;
    }

    @Override
    protected void onLoadedIdea(Idea i) {
	mUpdatingIdea = i;
	mPhotoTaken = true;
	super.onLoadedIdea(i);
	String file = i.getPath();
	if (!new File(file).exists())
	    showMessage(R.string.txtImageNotFound);
	else
	    setImagePreview(i.getPath());
    }

    private String generateTempFileToSave() {
	return generateRandomFileToSave(null, null);
    }

    @Override
    protected String generateRandomFileToSave(String directory, String ext) {
	if (TEMP_FILE == null) {
	    IdeaRecorderApplication ira = (IdeaRecorderApplication) getContext()
		    .getApplicationContext();
	    TEMP_FILE = ira.getMediaFolder(Environment.DIRECTORY_PICTURES)
		    + "/_temp_" + System.currentTimeMillis() + ".jpg";
	}
	return TEMP_FILE;
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
	boolean result = false;
	if (requestCode == I.Constants.REQUEST_TAKE_PHOTO) {
	    try {
		if (resultCode == Activity.RESULT_OK) {
		    setImagePreview(generateTempFileToSave());
		    mPhotoTaken = true;
		    result = true;
		}
	    } catch (Exception e) {
		showError(e);
	    }
	} else
	    result = super.onActivityResult(requestCode, resultCode, data);

	return result;
    }

    @Override
    public boolean onSave() {
	boolean result = false;
	try {

	    if (mUpdatingIdea == null) {
		Idea i = getOrcreateIdea();
		mDataProvider.save(i);
	    } else {
		if (!(mUpdatingIdea.getName().equals(getIdeaName())
			&& mUpdatingIdea.getDescription() != null && mUpdatingIdea
			.getDescription().equals(getIdeaDescription())))// if
									// some
									// change
									// with
									// name
									// or
									// description
		{
		    Idea i = getOrcreateIdea();
		    mDataProvider.udpate(i);
		}
	    }

	    result = true;
	    finish();
	} catch (Exception e) {
	    showError(e);
	}
	return result;
    }

    private Idea getOrcreateIdea() throws IllegalAccessException, IOException {
	if (!mPhotoTaken)
	    throw new IllegalAccessException(getString(
		    R.string.errInvalidValueMissingArg0,
		    getString(R.string.lblPhoto)));
	String name = getIdeaName();
	if (name == null)
	    throw new IllegalArgumentException(getString(
		    R.string.errInvalidValueMissingArg0,
		    getString(R.string.lblName)));
	String desc = getIdeaDescription();
	Idea i = null;
	if (mUpdatingIdea == null) {
	    i = new Idea();
	    i.setDescription(desc);
	    i.setIdeaType(Idea.TYPE_IMAGE);
	    i.setName(name);
	    String realPath = moveTempFile();
	    i.setPath(realPath);
	    i.setSaveTime(System.currentTimeMillis());
	} else {
	    i = mUpdatingIdea;
	    i.setName(name);
	    i.setDescription(desc);
	}
	return i;
    }

    protected String moveTempFile() throws IOException {
	IdeaRecorderApplication ira = (IdeaRecorderApplication) (mContext
		.getApplicationContext());
	String tmpFile = generateTempFileToSave();
	File from = new File(tmpFile);
	String to = ira.getMediaFolder(Environment.DIRECTORY_PICTURES)
		+ "/"
		+ StringTools.getFileNameFromIdeaName(mContext
			.getNameEditText().getText().toString()) + "_"
		+ System.currentTimeMillis() + ".jpg";
	boolean b = from.renameTo(new File(to));
	if (!b)
	    throw new IOException(getString(R.string.errUnableToMoveFile));
	return to;
    }

    public void setImagePreview(String file) {
	BitmapFactory.Options opts = new BitmapFactory.Options();
	opts.inSampleSize = PictureUtils.getRatioJpeg(file);
	final Bitmap source = BitmapFactory.decodeFile(file, opts);
	mContext.getImagePreview().setImageBitmap(source);
    }
}
