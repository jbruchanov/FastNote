package com.scurab.android.idearecorder.presenter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;

import com.scurab.android.idearecorder.I;
import com.scurab.android.idearecorder.IdeaRecorderApplication;
import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.activity.BaseIdeaActivity;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.DataProvider;
import com.scurab.android.idearecorder.tools.StringTools;

/**
 * Base clase for few control on basic idea write acitivity<br />
 * This class doesn't have any explicit tests, beacuse it was refactored in the middle of developping => tested through other tests
 *  
 * @author Joe Scurab
 *
 */
public abstract class IdeaActivityPresenter extends BasePresenter implements OnActivityResultListener
{
	private BaseIdeaActivity mContext;
	protected DataProvider mDataProvider = null;
	
	public IdeaActivityPresenter(BaseIdeaActivity context)
	{
		super(context);
		mDataProvider = getDatabase();
		bind();
	}
	
	@Override
	protected void onAttachContext(Context context)
	{
		mContext = (BaseIdeaActivity) context;
		super.onAttachContext(context);		
	}
	
	protected final BaseIdeaActivity getContext()
	{
		return mContext;
	}
	
	private void bind()
	{
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
		
		mContext.getNameRecordButton().setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onNameToText();
			}
		});
		
		mContext.setOnActivityResultListener(this);
		onInitIntent();
	}
	
	protected void onInitIntent()
	{
		Intent i = mContext.getIntent();
		if(i != null && i.hasExtra(I.Constants.IDEA_ID))
		{
			long id =  i.getLongExtra(I.Constants.IDEA_ID, 0);
			if(id != 0)
			{
				onLoadedIdea(mDataProvider.getIdea(id));
			}
		}
	}

	/**
	 * Called when input intent has id about existing idea
	 * @param i
	 */
	protected void onLoadedIdea(Idea i)
	{
		mContext.getNameEditText().setText(i.getName());
	}

	
	protected boolean hasPhoneSpeechRecognition()
	{
		PackageManager pm = mContext.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return activities.size() != 0;
	}
	
	protected void initSpeechRecognition()
	{
		boolean has = hasPhoneSpeechRecognition();
		mContext.getNameRecordButton().setEnabled(has);
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
	
	protected void startSpeechRecognition(int src)
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
	
	@Override
	public boolean onActivityResult(int requestCode, int resultCode, Intent data)
	{
		boolean handled = false;
		if(resultCode == Activity.RESULT_OK)
		{
			if (requestCode == I.Constants.VOICE_RECOGNITION_REQUEST_CODE_NAME) 
			{
				onRecognizedNameValue(getRecognizedTextFromSpeech(data));
				handled = true;
	        }
		}        
		return handled;
	}	
	
	protected String getRecognizedTextFromSpeech(Intent data)
	{
		List<String> d = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
		String value = null;
		if(d.size() > 0)
			value = d.get(0);
		return value;
	}
	protected void onRecognizedNameValue(String value)
	{
		mContext.getNameEditText().setText(value);
	}

	public abstract boolean onSave();
	
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
	
	/**
	 * Returns temp file by input values
	 * @param directory {@link Environment.#DIRECTORY_PICTURES} for example
	 * @param ie ".3gp", can be null
	 * @return
	 */
	protected String generateRandomFileToSave(String directory, String ext)
	{
		IdeaRecorderApplication ira = (IdeaRecorderApplication)(mContext.getApplicationContext());
		return ira.getMediaFolder(directory) + "/" + System.currentTimeMillis() + (ext == null ? "" : ext);
	}
	
	protected String getIdeaName()
	{
		return StringTools.nullIfTrimmedEmpty(mContext.getNameEditText().getText().toString().trim());
	}
}
