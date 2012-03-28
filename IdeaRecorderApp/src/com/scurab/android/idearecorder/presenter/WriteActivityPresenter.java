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

public class WriteActivityPresenter extends BasePresenter implements OnActivityResultListener
{
	private WriteActivity mContext = null;
	private DataProvider mDataProvider = null;
	private Idea mUpdatingIdea = null;
	
	public WriteActivityPresenter(WriteActivity context)
	{
		super(context);
		mContext = context;
		mDataProvider = getDatabase();
		bind();
	}
	
	protected void bind()
	{
		mContext.getNameRecordButton().setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onNameToText();
			}
		});
		mContext.getDescriptionRecorderButton().setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onDescriptionToText();
			}
		});
		
		mContext.getSaveButton().setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onSave();
			}
		});
		
		mContext.getCancelButton().setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				onCancel();
			}
		});
		
		initSpeechRecognition();
		initIntent();
		mContext.setOnActivityResultListener(this);
		
	}
	
	protected void initSpeechRecognition()
	{
		boolean has = hasPhoneSpeechRecognition();
		mContext.getNameRecordButton().setEnabled(has);
		mContext.getDescriptionRecorderButton().setEnabled(has);
	}
	
	protected void initIntent()
	{
		Intent i = mContext.getIntent();
		if(i != null && i.hasExtra(I.Constants.IDEA_ID))
		{
			long id =  i.getLongExtra(I.Constants.IDEA_ID, 0);
			if(id != 0)
			{
				mUpdatingIdea = mDataProvider.getIdea(id);
				mContext.getNameEditText().setText(mUpdatingIdea.getName());
				mContext.getDescriptionEditText().setText(mUpdatingIdea.getDescription());
			}
		}
	}

	
	private boolean hasPhoneSpeechRecognition()
	{
		PackageManager pm = mContext.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return activities.size() != 0;
	}

	public void onNameToText()
	{
		try
		{
			startSpeechRecognition(I.Constants.VOICE_RECOGNITION_REQUEST_CODE_NAME);
		}
		catch(Exception e)
		{
			showError(e);
		}
	}
	
	private void startSpeechRecognition(int src)
	{
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        if(src == I.Constants.VOICE_RECOGNITION_REQUEST_CODE_NAME)
        	intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.txtIdeaRecorderName));
        else if(src == I.Constants.VOICE_RECOGNITION_REQUEST_CODE_DESCRIPTION)
        	intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.txtIdeaRecorderName));
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);	        
        startActivityForResult(intent, src);
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

	public void onCancel()
	{
		try
		{
			finish();
		}
		catch(Exception e)
		{
			showError(e);
		}
	}

	@Override
	public boolean onActivityResult(int requestCode, int resultCode, Intent data)
	{
		boolean handled = false;
		if(resultCode == Activity.RESULT_OK)
		{
			List<String> d = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String value = null;
			if(d.size() > 0)
				value = d.get(0);
			if (requestCode == I.Constants.VOICE_RECOGNITION_REQUEST_CODE_NAME) 
			{
				onRecognizedNameValue(value);
				handled = true;
	        }
			else if(requestCode == I.Constants.VOICE_RECOGNITION_REQUEST_CODE_DESCRIPTION)
			{
				onRecognizedDescriptionValue(value);
				handled = true;
			}
		}        
		return handled;
	}	
	
	protected void onRecognizedNameValue(String value)
	{
		mContext.getNameEditText().setText(value);
	}
	
	protected void onRecognizedDescriptionValue(String value)
	{
		mContext.getDescriptionEditText().setText(value);
	}
}	
