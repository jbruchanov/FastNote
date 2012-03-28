package com.scurab.android.idearecorder.presenter;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

import com.scurab.android.idearecorder.IdeaRecorderApplication;
import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.activity.SpeechActivity;
import com.scurab.android.idearecorder.interfaces.IAudioRecorder;
import com.scurab.android.idearecorder.interfaces.OnActivityStateChangeListener;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.AudioRecorder;
import com.scurab.android.idearecorder.tools.StringTools;

public class SpeechActivityPresenter extends IdeaActivityPresenter implements OnActivityStateChangeListener
{
	SpeechActivity mContext = null;
	IAudioRecorder mAudioRecorder;
	Idea mUpdatingIdea;
	
	public SpeechActivityPresenter(SpeechActivity context)
	{
		super(context);
		mContext = context;
		bind();
	}
	
	public void bind()
	{
		if(mAudioRecorder == null) //is not null if is initialized by existing value in onLoaded
			mAudioRecorder = getAudioRecorder(null);
		mContext.getAudioRecordingWidget().setAudioRecorder(mAudioRecorder);
		
		if(mUpdatingIdea != null)
		{
			mContext.getAudioRecordingWidget().setReadOnly(true);
		}
		
		mContext.setOnActivityStateChangeListener(this);
	}
	
	protected IAudioRecorder getAudioRecorder(String file)
	{
		
		return new AudioRecorder(file == null ? generateRandomFileToSave() : file);		
	}
	
	private String generateRandomFileToSave()
	{
		IdeaRecorderApplication ira = (IdeaRecorderApplication)(mContext.getApplicationContext());
		return ira.getMediaFolder(Environment.DIRECTORY_MUSIC) + "/" + System.currentTimeMillis() + ".3gp";
	}
	
	@Override
	public void finish()
	{
		try
		{
			if(mAudioRecorder.isPlaying() || mAudioRecorder.isRecording())
				onUnableToFinish();
			else
				super.finish();
		}
		catch(Exception e)
		{
			showError(e);
		}
	}
	
	@Override
	protected void onLoadedIdea(Idea i)
	{
		super.onLoadedIdea(i);
		mUpdatingIdea = i;
		mAudioRecorder = getAudioRecorder(i.getPath());		
	}
	
	protected void onUnableToFinish()
	{
		throw new IllegalStateException(getString(R.string.txtStopActivityFirst));
	}
		
	
	@Override
	public boolean onSave()
	{
		boolean result = false;
		try
		{
			Idea i = getOrcreateIdea();
			if(mUpdatingIdea != null)
				mDataProvider.udpate(i);
			else
				mDataProvider.save(i);
			finish();
			result = true;
		}
		catch(Exception e)
		{
			showError(e);
		}
		return result;
	}
	
	private Idea getOrcreateIdea() throws IllegalAccessException, IOException
	{
		if(!checkFile())
			throw new IllegalAccessException(getString(R.string.errInvalidValueMissingArg0, getString(R.string.lblAudioRecord)));
		String name = StringTools.nullIfTrimmedEmpty(mContext.getNameEditText().getText().toString().trim());
		if(name == null)
			throw new IllegalArgumentException(getString(R.string.errInvalidValueMissingArg0, getString(R.string.lblName)));
		
		Idea i = new Idea();
		i.setDescription(null);
		i.setIdeaType(Idea.TYPE_AUDIO);
		i.setName(name);
		String realPath = moveTempFile();
		i.setPath(realPath);
		i.setSaveTime(System.currentTimeMillis());
		return i;
	}
	
	private String moveTempFile() throws IOException
	{
		IdeaRecorderApplication ira = (IdeaRecorderApplication)(mContext.getApplicationContext());
		String tmpFile = mAudioRecorder.getFile();
		File from = new File(tmpFile);
		String to = ira.getMediaFolder(Environment.DIRECTORY_MUSIC) + 
				"/" + StringTools.getFileNameFromIdeaName(mContext.getNameEditText().getText().toString()) + "_" + System.currentTimeMillis() + ".3gp";
		boolean b = from.renameTo(new File(to));
		if(!b)
			throw new IOException(getString(R.string.errUnableToMoveFile));
		return to;
	}

	private boolean checkFile()
	{
		String file = mAudioRecorder.getFile();
		boolean result = false;
		if(file != null)
		{
			File f = new File(file);
			result = f.exists() && f.length() > 0;
		}
		return result;
	}

	@Override
	public void onResume()
	{
		
	}

	@Override
	public void onPause()
	{
		mContext.getAudioRecordingWidget().release();
	}
}
