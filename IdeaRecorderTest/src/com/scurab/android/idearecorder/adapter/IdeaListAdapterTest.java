package com.scurab.android.idearecorder.adapter;

import java.util.List;

import com.scurab.android.idearecorder.TestHelper;
import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.view.IdeaView;

import android.test.AndroidTestCase;

public class IdeaListAdapterTest extends AndroidTestCase {
    public void testCreation() {
	List<Idea> data = TestHelper.getRandomIdeas(15);
	IdeaListAdapter ila = new IdeaListAdapter(mContext, data);
	assertEquals(data.size(), ila.getCount());
    }

    public void testViews() {
	List<Idea> data = TestHelper.getRandomIdeas(15);
	IdeaListAdapter ila = new IdeaListAdapter(mContext, data);
	for (int i = 0; i < data.size(); i++) {
	    assertTrue(ila.getView(i, null, null) instanceof IdeaView);
	}
    }

    public void testBindingView() {
	List<Idea> data = TestHelper.getRandomIdeas(15);
	IdeaListAdapter ila = new IdeaListAdapter(mContext, data);
	int position = TestHelper.sRandom.nextInt(data.size());
	IdeaView iv = (IdeaView) ila.getView(position, null, null);
	TestHelper.assertIdeaView(data.get(position), iv);
    }
}
