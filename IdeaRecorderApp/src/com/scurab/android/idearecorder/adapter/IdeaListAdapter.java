package com.scurab.android.idearecorder.adapter;

import java.util.List;

import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.view.IdeaView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class IdeaListAdapter extends ArrayAdapter<Idea>
{
	List<Idea> mData = null;
	
	public IdeaListAdapter(Context context, List<Idea> objects)
	{
		super(context, 0, objects);
		mData = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{	
		IdeaView iv = null;
		if(convertView != null)
			iv = (IdeaView)convertView;
		else
			iv = new IdeaView(getContext());
		iv.setIdea(mData.get(position));
		return iv;
	}	
}
