package com.scurab.android.idearecorder;

import java.util.Random;
import java.util.UUID;

import com.scurab.android.idearecorder.model.Idea;

public class TestHelper
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
}
