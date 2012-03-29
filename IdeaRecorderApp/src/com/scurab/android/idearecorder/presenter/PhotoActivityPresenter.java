package com.scurab.android.idearecorder.presenter;

import android.content.Context;
import android.view.View;

import com.scurab.android.idearecorder.activity.PhotoActivity;

public class PhotoActivityPresenter extends WriteActivityPresenter
{
	private PhotoActivity mContext;
	
	public PhotoActivityPresenter(PhotoActivity context)
	{
		super(context);
		mContext = context;
	}
	
	@Override
	protected void onAttachContext(Context context)
	{
		mContext = (PhotoActivity) context;
		super.onAttachContext(context);
	}
	
	@Override
	protected void bind()
	{
		super.bind();
		mContext.getPhotoButton().setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onTakePhoto();
			}
		});
	}	
	
	public void onTakePhoto()
	{
		try
		{
			
		}
		catch(Exception e)
		{
			showError(e);
		}
	}
}
