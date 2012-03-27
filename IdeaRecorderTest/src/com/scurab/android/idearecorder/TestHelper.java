package com.scurab.android.idearecorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import junit.framework.TestCase;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scurab.android.idearecorder.model.Idea;

public class TestHelper extends TestCase
{
	public static Random sRandom = new Random();
	
	private TestHelper(){}
	
	public static Idea getRandomIdea()
	{
		Idea i = new Idea().setName(UUID.randomUUID().toString())
				.setDescription(UUID.randomUUID().toString())
				.setIdeaType(1 + sRandom.nextInt(4))
				.setSaveTime(System.currentTimeMillis())
				.setPath(UUID.randomUUID().toString());
		return i;
	}
	
	public static List<Idea> getRandomIdeas(int howMany)
	{
		List<Idea> result = new ArrayList<Idea>();
		for(int i = 0;i<howMany;i++)
		{
			Idea id = new Idea().setName(UUID.randomUUID().toString())
								.setDescription(UUID.randomUUID().toString())
								.setIdeaType(1 + sRandom.nextInt(4))
								.setSaveTime(System.currentTimeMillis())
								.setPath(UUID.randomUUID().toString());
			result.add(id);
		}
		return result;
	}
	
	public static void assertIdeaView(Idea i, View iv)
	{
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
