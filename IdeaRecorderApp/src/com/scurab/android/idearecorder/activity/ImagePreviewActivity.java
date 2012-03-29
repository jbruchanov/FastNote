package com.scurab.android.idearecorder.activity;

import com.scurab.android.idearecorder.I;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Just simple activity to show picture in browser<br/>
 * Can zoom and scroll
 * @author Joe Scurab
 *
 */
public class ImagePreviewActivity extends Activity
{
	private WebView  mWebView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mWebView = new WebView(this);
		setContentView(mWebView);
		initWeb(getIntent().getStringExtra(I.Constants.IDEA_IMAGE_PATH));		
	}
	
	private void initWeb(final String link)
	{
		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{					
				final String html = String.format("<html><head><meta http-equiv=\"pragma\" content=\"no-cache\"></head><body style=\"background:#000;\"><img src=\"%s\"/></body></html>",link);
				mWebView.loadDataWithBaseURL("fake://not/needed", html, "text/html", "utf-8", "");
				mWebView.getSettings().setBuiltInZoomControls(true);
			}
		});
		t.start();
	}
}
