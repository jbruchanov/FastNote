package com.scurab.android.idearecorder.view;

import java.util.Date;

import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.TestHelper;
import com.scurab.android.idearecorder.model.Idea;

import android.test.AndroidTestCase;
import android.widget.ImageView;
import android.widget.TextView;

public class IdeaViewTest extends AndroidTestCase
{
	
	public void testCreation()
	{
		Idea i = new Idea();
		IdeaView iv = new IdeaView(mContext, i);
		assertEquals(i,iv.getIdea());
	}
	
	public void testGuiBinding()
	{
		Idea i = TestHelper.getRandomIdea();
		IdeaView iv = new IdeaView(mContext, i);
		TextView tvName = (TextView) iv.findViewById(R.id.tvName);
		TextView tvDescription = (TextView) iv.findViewById(R.id.tvDescription);
		TextView tvTime = (TextView) iv.findViewById(R.id.tvTime);
		ImageView icon = (ImageView) iv.findViewById(R.id.ivIcon);
		
		assertEquals(i.getName(),tvName.getText().toString());
		assertEquals(i.getDescription(),tvDescription.getText().toString());
		assertEquals(new Date(i.getSaveTime()).toLocaleString(),tvTime.getText().toString());	
		assertNotNull(icon);		
	}
	
	public void testChangeValueGuiBinding()
	{
		Idea i1 = TestHelper.getRandomIdea();
		Idea i = TestHelper.getRandomIdea();
		IdeaView iv = new IdeaView(mContext, i1);		
		iv.setIdea(i);
		
		TextView tvName = (TextView) iv.findViewById(R.id.tvName);
		TextView tvDescription = (TextView) iv.findViewById(R.id.tvDescription);
		TextView tvTime = (TextView) iv.findViewById(R.id.tvTime);
		ImageView icon = (ImageView) iv.findViewById(R.id.ivIcon);
		
		assertEquals(i.getName(),tvName.getText().toString());
		assertEquals(i.getDescription(),tvDescription.getText().toString());
		assertEquals(new Date(i.getSaveTime()).toLocaleString(),tvTime.getText().toString());	
		assertNotNull(icon);		
	}
}
