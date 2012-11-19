package com.scurab.android.idearecorder.help;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class HelpImageButton extends ImageButton {

    private OnClickListener mOnClickListener;

    public HelpImageButton(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	// TODO Auto-generated constructor stub
    }

    public HelpImageButton(Context context, AttributeSet attrs) {
	super(context, attrs);
	// TODO Auto-generated constructor stub
    }

    public HelpImageButton(Context context) {
	super(context);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
	// TODO Auto-generated method stub
	super.setOnClickListener(l);
	mOnClickListener = l;
    }

    public OnClickListener getOnClickListener() {
	return mOnClickListener;
    }
}
