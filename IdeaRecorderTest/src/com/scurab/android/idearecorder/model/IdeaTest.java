package com.scurab.android.idearecorder.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

public class IdeaTest extends TestCase
{
	private Random mRandom = new Random();
	
	public void testComparing()
	{
		List<Idea> data = new ArrayList<Idea>();
		for(int i = 0;i<10;i++)
		{
			data.add(getRandomIdea());
		}
		
		Collections.sort(data);
		for(int i = 0;i<9;i++)
		{
			assertTrue(data.get(i).getId() >= data.get(i+1).getId()); //first must be the newest
		}
	}
	
	private Idea getRandomIdea()
	{
		return new Idea().setId(mRandom.nextLong());
	}
}
