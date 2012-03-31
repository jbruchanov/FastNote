package com.scurab.android.idearecorder.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.view.View;
import android.view.View.OnClickListener;

import com.scurab.android.idearecorder.I;
import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.activity.WriteActivity;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.StringTools;

public class WriteActivityPresenter extends IdeaActivityPresenter implements OnActivityResultListener
{
	private WriteActivity mContext;
	private Idea mUpdatingIdea;
	
	public WriteActivityPresenter(WriteActivity context)
	{
		super(context);
		bind();
	}
	
	@Override
	protected void onAttachContext(Context context)
	{	
		mContext = (WriteActivity) context;
		super.onAttachContext(context);		
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
			finish();
		}
		catch(Exception e)
		{
			showError(e);
		}
		return result;
	}
	
	private Idea getOrcreateIdea()
	{
		String name = getIdeaName();
		if(name == null)
			throw new IllegalArgumentException(getString(R.string.errInvalidValueMissingArg0, getString(R.string.lblName)));
		String desc = getIdeaDescription();	
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
		String  curText = mContext.getDescriptionEditText().getText().toString();
		String text = "";
		if(curText.length() > 0)
			text = String.format("%s\n%s",curText,value);
		else
			text = value;
		mContext.getDescriptionEditText().setText(text);
	}
	
	protected String getIdeaDescription()
	{
		return StringTools.nullIfTrimmedEmpty(mContext.getDescriptionEditText().getText().toString().trim());
	}
}	
