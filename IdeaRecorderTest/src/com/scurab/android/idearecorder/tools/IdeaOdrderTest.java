package com.scurab.android.idearecorder.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.scurab.android.idearecorder.TestHelper;
import com.scurab.android.idearecorder.model.Idea;

import junit.framework.TestCase;

public class IdeaOdrderTest extends TestCase {
    public void testOrderByNameAscending() {
	List<Idea> data = new ArrayList<Idea>();

	Idea i1 = TestHelper.getRandomIdea();
	i1.setName("Z");
	data.add(i1);
	Idea i2 = TestHelper.getRandomIdea();
	i2.setName("V");
	data.add(i2);
	Idea i3 = TestHelper.getRandomIdea();
	i3.setName("A");
	data.add(i3);
	Idea i4 = TestHelper.getRandomIdea();
	i4.setName("B");
	data.add(i4);

	Collections.sort(data,
		IdeaOrder.getComparator(IdeaOrder.ORDER_BY_NAME_ASCENDING));

	assertEquals(i3, data.get(0));
	assertEquals(i4, data.get(1));
	assertEquals(i2, data.get(2));
	assertEquals(i1, data.get(3));
    }

    public void testOrderByNameDescending() {
	List<Idea> data = new ArrayList<Idea>();

	Idea i1 = TestHelper.getRandomIdea();
	i1.setName("Z");
	data.add(i1);
	Idea i2 = TestHelper.getRandomIdea();
	i2.setName("V");
	data.add(i2);
	Idea i3 = TestHelper.getRandomIdea();
	i3.setName("A");
	data.add(i3);
	Idea i4 = TestHelper.getRandomIdea();
	i4.setName("B");
	data.add(i4);

	Collections.sort(data,
		IdeaOrder.getComparator(IdeaOrder.ORDER_BY_NAME_DESCENDING));

	assertEquals(i1, data.get(0));
	assertEquals(i2, data.get(1));
	assertEquals(i4, data.get(2));
	assertEquals(i3, data.get(3));
    }

    public void testOrderByDateAscending() {
	List<Idea> data = new ArrayList<Idea>();

	Idea i1 = TestHelper.getRandomIdea();
	i1.setSaveTime(new Date(10, 1, 1).getTime());
	data.add(i1);
	Idea i2 = TestHelper.getRandomIdea();
	i2.setSaveTime(new Date(9, 1, 1).getTime());
	data.add(i2);
	Idea i3 = TestHelper.getRandomIdea();
	i3.setSaveTime(new Date(8, 1, 1).getTime());
	data.add(i3);
	Idea i4 = TestHelper.getRandomIdea();
	i4.setSaveTime(new Date(11, 1, 1).getTime());
	data.add(i4);

	Collections.sort(data,
		IdeaOrder.getComparator(IdeaOrder.ORDER_BY_DATE_ASCENDING));

	assertEquals(i3, data.get(0));
	assertEquals(i2, data.get(1));
	assertEquals(i1, data.get(2));
	assertEquals(i4, data.get(3));
    }

    public void testOrderByDateDescending() {
	List<Idea> data = new ArrayList<Idea>();

	Idea i1 = TestHelper.getRandomIdea();
	i1.setSaveTime(new Date(10, 1, 1).getTime());
	data.add(i1);
	Idea i2 = TestHelper.getRandomIdea();
	i2.setSaveTime(new Date(9, 1, 1).getTime());
	data.add(i2);
	Idea i3 = TestHelper.getRandomIdea();
	i3.setSaveTime(new Date(8, 1, 1).getTime());
	data.add(i3);
	Idea i4 = TestHelper.getRandomIdea();
	i4.setSaveTime(new Date(11, 1, 1).getTime());
	data.add(i4);

	Collections.sort(data,
		IdeaOrder.getComparator(IdeaOrder.ORDER_BY_DATE_DESCENDING));

	assertEquals(i4, data.get(0));
	assertEquals(i1, data.get(1));
	assertEquals(i2, data.get(2));
	assertEquals(i3, data.get(3));
    }
}
