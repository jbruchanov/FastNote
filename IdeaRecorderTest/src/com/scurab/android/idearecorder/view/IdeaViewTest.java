package com.scurab.android.idearecorder.view;

import com.scurab.android.idearecorder.TestHelper;
import com.scurab.android.idearecorder.model.Idea;

import android.test.AndroidTestCase;

public class IdeaViewTest extends AndroidTestCase {

    public void testCreation() {
	Idea i = new Idea();
	IdeaView iv = new IdeaView(mContext, i);
	assertEquals(i, iv.getIdea());
    }

    public void testGuiBinding() {
	Idea i = TestHelper.getRandomIdea();
	IdeaView iv = new IdeaView(mContext, i);
	TestHelper.assertIdeaView(i, iv);
    }

    public void testChangeValueGuiBinding() {
	Idea i1 = TestHelper.getRandomIdea();
	Idea i = TestHelper.getRandomIdea();
	IdeaView iv = new IdeaView(mContext, i1);
	iv.setIdea(i);
	TestHelper.assertIdeaView(i, iv);
    }
}
