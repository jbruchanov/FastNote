package com.scurab.android.idearecorder.presenter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;

import com.scurab.android.idearecorder.I;
import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.activity.WriteActivity;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.DataProvider;
import com.scurab.android.idearecorder.tools.StringTools;

public class WriteActivityPresenter extends IdeaActivityPresenter implements OnActivityResultListener
{
	private WriteActivity mContext = null;
	private Idea mUpdatingIdea;
	
	public WriteActivityPresenter(WriteActivity context)
	{
		super(context);
		mContext = context;
		bind();
	}
	
	protected void bind()
	{
		mContext.getDescriptionRecorderButton().setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onDescriptionToText();
			}
		});
		
		initSpeechRecognition();
	}
	
	@Override
	protected void initSpeechRecognition()
	{
		super.initSpeechRecognition();
		boolean has = hasPhoneSpeechRecognition();
		mContext.getDescriptionRecorderButton().setEnabled(has);
	}
	
	@Override
	protected void onLoadedIdea(Idea i)
	{		
		super.onLoadedIdea(i);
		mUpdatingIdea = i;
		//don't use context, it's not initialized yet
		((WriteActivity)getContext()).getDescriptionEditText().setText(i.getDescription());
	}

	public void onDescriptionToText()
	{
		try
		{
			startSpeechRecognition(I.Constants.VOICE_RECOGNITION_REQUEST_CODE_DESCRIPTION);
		}
		catch(Exception e)
		{
			showError(e);
		}
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
	
	protected Idea getOrcreateIdea()
	{
		String name = StringTools.nullIfTrimmedEmpty(mContext.getNameEditText().getText().toString().trim());
		if(name == null)
			throw new IllegalArgumentException(getString(R.string.errInvalidValueMissingArg0, getString(R.string.lblName)));
		String desc = StringTools.nullIfTrimmedEmpty(mContext.getDescriptionEditText().getText().toString().trim());		
		Idea i = new Idea();
		if(mUpdatingIdea != null)
			i.setId(mUpdatingIdea.getId());
		i.setDescription(desc);
		i.setIdeaType(Idea.TYPE_TEXT);
		i.setName(name);
		i.setPath(null);
		i.setSaveTime(System.currentTimeMillis());
		return i;
	}

	@Override
	public boolean onActivityResult(int requestCode, int resultCode, Intent data)
	{
		boolean handled = super.onActivityResult(requestCode, resultCode, data);
		if(!handled && resultCode == Activity.RESULT_OK)
		{			
			if(requestCode == I.Constants.VOICE_RECOGNITION_REQUEST_CODE_DESCRIPTION)
			{
				onRecognizedDescriptionValue(getRecognizedTextFromSpeech(data));
				handled = true;
			}
		}        
		return handled;
	}	
	
	
	protected void onRecognizedDescriptionValue(String value)
	{
		mContext.getDescriptionEditText().setText(value);
	}
}	
