package com.scurab.android.idearecorder.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.scurab.android.idearecorder.TestHelper;
import com.scurab.android.idearecorder.model.Idea;

import android.content.Context;
import android.test.AndroidTestCase;

public class DataproviderTest extends AndroidTestCase
{
	DataProvider db = null;
	@Override
	public void setContext(Context context)
	{	
		super.setContext(context);
		db = new DataProvider(mContext);		
		db.dropAllTables();
		db.onCreate(db.getWritableDatabase());
	}
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		db.deleteAllData();
	}
	
	@Override
	protected void tearDown() throws Exception
	{	
		super.tearDown();
		db.deleteAllData();
	}
	
	public void testGetDatabase()
	{
		DataProvider db = new DataProvider(mContext);
		assertNotNull(db);
	}
	
	
	public void testCreateDatabaseTables()
	{
		DataProvider db = new DataProvider(mContext);
		
		if(db.getTables().size() == 0) //if is empty, it was deleted
			db.onCreate(db.getWritableDatabase());
		
		List<String> tables = db.getTables();
		assertEquals(1, tables.size());
		assertTrue(tables.contains(DataProvider.Structure.TABLE_IDEAS));
	}
	
	public void testDropAllTables()
	{
		DataProvider db = new DataProvider(mContext);
		db.dropAllTables();
		List<String> tables = db.getTables();
		assertEquals(0, tables.size());
	}
	
	public void testDeleteAllData()
	{
		DataProvider db = new DataProvider(mContext);
		db.save(TestHelper.getRandomIdea());
		db.save(TestHelper.getRandomIdea());
		db.save(TestHelper.getRandomIdea());
		
		assertEquals(3,db.getIdeas().size());
		db.deleteAllData();
		assertEquals(0,db.getIdeas().size());
	}
	
	public void testDatabaseTablesStructureDefinitionAllTables()
	{		
		int len = DataProvider.Structure.ALL_TABLES.length;
		int values = DataProvider.Structure.class.getFields().length;
		assertEquals(len,values-2);//1 is for all tables and all tables sql create script
	}
	
	public void testDatabaseTablesStructureDefinitionAllTableColumnsForIdeas()
	{		
		int len = 6; //id,name,desc,time,type,path
		int values = DataProvider.Structure.Ideas.class.getFields().length;
		assertEquals(len,values-1);//1 is for all tables and all tables sql create script
	}
	
	public void testAddIdeaItem()
	{
		Idea i = TestHelper.getRandomIdea();
		DataProvider db = new DataProvider(mContext);
		db.save(i);
		assertTrue(i.getId() != 0);
	}
	
	
	
	public void testAddAndReadIdeaItem()
	{
		Idea i = TestHelper.getRandomIdea();
		DataProvider db = new DataProvider(mContext);
		db.save(i);
		Idea i2 = db.getIdea(i.getId());
		assertIdeas(i,i2);
	}
	
	public void testReadNonExistingIdeaItem()
	{		
		DataProvider db = new DataProvider(mContext);		
		Idea i2 = db.getIdea(0);
		assertNull(i2);
	}
	
	public void testGetAllIdeasEmptyDB()
	{
		DataProvider db = new DataProvider(mContext);
		List<Idea> data = db.getIdeas();
		assertNotNull(data);
		assertEquals(0,data.size());
	}
	
	public void testGetAllIdeas3AddedDB()
	{			
		List<Idea> data = new ArrayList<Idea>();
		data.add(TestHelper.getRandomIdea());
		data.add(TestHelper.getRandomIdea());
		data.add(TestHelper.getRandomIdea());
		
		
		DataProvider db = new DataProvider(mContext);
		for(Idea i : data)
			db.save(i);
		Collections.sort(data);
		
		List<Idea> fromDb = db.getIdeas();		
		
		assertEquals(data.size(),fromDb.size());
		for(int i = 0;i<data.size();i++)
			assertIdeas(data.get(i), fromDb.get(i));
	}
	
	public void testDeleteIdea()
	{
		DataProvider db = new DataProvider(mContext);
		Idea i = TestHelper.getRandomIdea();
		db.save(i);
		List<Idea> data = db.getIdeas();
		assertNotNull(data);
		assertEquals(1,data.size());
		db.delete(i.getId());		
		data = db.getIdeas();
		assertEquals(0,data.size());
	}
	
	public void testUpdateIdea()
	{
		DataProvider db = new DataProvider(mContext);
		Idea i = TestHelper.getRandomIdea();
		db.save(i);
		Idea i2 = TestHelper.getRandomIdea();
		i2.setId(i.getId());
		db.udpate(i2);
		
		Idea fromDb = db.getIdea(i2.getId());
		assertNotNull(fromDb);
		assertIdeas(i2, fromDb);
	}
	
	
	private void assertIdeas(Idea i1, Idea i2)
	{
		assertEquals(i1.getDescription(), i2.getDescription());
		assertEquals(i1.getId(), i2.getId());
		assertEquals(i1.getIdeaType(), i2.getIdeaType());
		assertEquals(i1.getName(), i2.getName());
		assertEquals(i1.getPath(), i2.getPath());
		assertEquals(i1.getSaveTime(), i2.getSaveTime());
	}
}
