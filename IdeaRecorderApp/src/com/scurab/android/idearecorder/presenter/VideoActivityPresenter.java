package com.scurab.android.idearecorder.presenter;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.scurab.android.idearecorder.I;
import com.scurab.android.idearecorder.IdeaRecorderApplication;
import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.activity.VideoActivity;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.IOUtils;
import com.scurab.android.idearecorder.tools.StringTools;

public class VideoActivityPresenter extends PhotoActivityPresenter
{
	private VideoActivity mContext;	
	private Idea mUpdatingIdea;
	private boolean videoTaken;
	private Uri mRecordedVideo;
	
	public VideoActivityPresenter(VideoActivity context)
	{
		super(context);
	}
	
	@Override
	protected void onAttachContext(Context context)
	{
		mContext = (VideoActivity) context;
		super.onAttachContext(context);
	}
	
	@Override
	protected void bind()
	{
		super.bind();
		
		mContext.getSaveButton().setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onSaveAsync();
			}
		});
	}
	
	@Override
	public void onPhotoButtonClick()
	{
		try
		{			
			if(mUpdatingIdea == null)
				startCreateVideoActivity();
			else
				startVideoPlayerActivity(mUpdatingIdea.getPath());
		}
		catch(Exception e)
		{
			showError(e);
		}
	}
	
	@Override
	protected void onLoadedIdea(Idea i)
	{
		mUpdatingIdea = i;
		videoTaken = true;
		if(!new File(i.getPath()).exists())
			showMessage(R.string.txtVideoNotFound);
		else
		{
			setImagePreview(i.getPath());
			mContext.getPhotoButton().setImageResource(R.drawable.ico_player_play);
		}
		super.onLoadedIdea(i);
	}

	private void startCreateVideoActivity()
	{
		Intent intent= new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(Intent.createChooser(intent,getString(R.string.lblVideo)), I.Constants.REQUEST_TAKE_VIDEO);
	}
	
	private void startVideoPlayerActivity(String file)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse(file), "video/*");
		startActivity(intent);

	}
	
	@Override
	public boolean onActivityResult(int requestCode, int resultCode, Intent data)
	{
		boolean result = false;
		if(requestCode == I.Constants.REQUEST_TAKE_VIDEO)
		{
			try
			{
				if(resultCode == Activity.RESULT_OK)
				{
					mRecordedVideo = data.getData();
//					setImagePreview(generateTempFileToSave());
					videoTaken = true;
					result = true;
				}
			}
			catch(Exception e)
			{
				showError(e);
			}
		}
		else
			result = super.onActivityResult(requestCode, resultCode, data);
		
		return result;
	}
	
	/**
	 * Shows progress dialog and start standard onSave in another thread
	 */
	public void onSaveAsync()
	{
		final ProgressDialog pd = ProgressDialog.show(mContext, getString(R.string.lblPlzWait), getString(R.string.lblSaving));
		pd.setCancelable(false);
		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if(onSave())
					mContext.runOnUiThread(new Runnable(){@Override public void run(){finish();}});
				pd.dismiss();
			}
		},"SavingVideoThread");
		t.start();
	}
	/**
	 * Calls synchronously => it's blocking if video is big
	 */
	@Override
	public boolean onSave()
	{
		boolean result = false;
		try
		{
			
			if(mUpdatingIdea == null)
			{
				Idea i = getOrcreateIdea();
				mDataProvider.save(i);
			}
			else
			{
				if(!(mUpdatingIdea.getName().equals(getIdeaName()) 
						&& mUpdatingIdea.getDescription() != null 
						&& mUpdatingIdea.getDescription().equals(getIdeaDescription())))//if some change with name or description
				{
					Idea i = getOrcreateIdea();
					mDataProvider.udpate(i);
				}
			}
				
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
		if(!videoTaken)
			throw new IllegalAccessException(getString(R.string.errInvalidValueMissingArg0, getString(R.string.lblVideo)));
		String name = getIdeaName();
		if(name == null)
			throw new IllegalArgumentException(getString(R.string.errInvalidValueMissingArg0, getString(R.string.lblName)));
		String desc = getIdeaDescription();		
		Idea i = null;
		if(mUpdatingIdea == null)
		{
			i = new Idea();
			i.setDescription(desc);
			i.setIdeaType(Idea.TYPE_VIDEO);
			i.setName(name);
			String realPath = moveTempFile();
			i.setPath(realPath);
			i.setSaveTime(System.currentTimeMillis());
		}
		else
		{
			i = mUpdatingIdea;
			i.setName(name);
			i.setDescription(desc);
		}
		
		return i;
	}
	
	@Override
	protected String moveTempFile() throws IOException
	{
		IdeaRecorderApplication ira = (IdeaRecorderApplication)(mContext.getApplicationContext());
		String to = ira.getMediaFolder(Environment.DIRECTORY_MOVIES) + 
				"/" + StringTools.getFileNameFromIdeaName(mContext.getNameEditText().getText().toString()) + "_" + System.currentTimeMillis() + ".mp4";
		boolean b = IOUtils.moveFile(mContext, mRecordedVideo, to);
		if(!b)
			throw new IOException(getString(R.string.errUnableToMoveFile));
		return to;
	}
	
	@Override
	public void setImagePreview(String file)
	{
	}
	
	@Override
	protected String generateRandomFileToSave(String directory, String ext)
	{
		IdeaRecorderApplication ira = (IdeaRecorderApplication)getContext().getApplicationContext();
		return ira.getMediaFolder(Environment.DIRECTORY_MOVIES) + "/_temp_video.mp4";
	}

}
