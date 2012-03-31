package com.scurab.android.idearecorder.tools;

import java.util.Comparator;

import com.scurab.android.idearecorder.model.Idea;

public class IdeaOrder
{

	public static final int ORDER_BY_NAME_ASCENDING = 1;
	public static final int ORDER_BY_NAME_DESCENDING = 2;
	public static final int ORDER_BY_DATE_ASCENDING = 3;
	public static final int ORDER_BY_DATE_DESCENDING = 4;
	
	private static Comparator<Idea> sOrderByName;
	private static Comparator<Idea> sOrderByNameDesc;
	private static Comparator<Idea> sOrderByDate;
	private static Comparator<Idea> sOrderByDateDesc;

	public static Comparator<Idea> getComparator(int orderByNameAscending)
	{
		Comparator<Idea> c = null;
		switch(orderByNameAscending)
		{
			case ORDER_BY_NAME_ASCENDING:
				c = getOrderByNameComparator();
				break;
			case ORDER_BY_NAME_DESCENDING:
				c = getOrderByNameDescComparator();
				break;
			case ORDER_BY_DATE_ASCENDING:
				c = getOrderByDateComparator();
				break;
			case ORDER_BY_DATE_DESCENDING:
				c = getOrderByDateDescComparator();
				break;
		}
		return c;
	}
	
	public static Comparator<Idea> getOrderByNameComparator()
	{
		if(sOrderByName == null)
		{
			sOrderByName = new Comparator<Idea>()
			{
				@Override
				public int compare(Idea lhs, Idea rhs)
				{
					return lhs.getName().compareTo(rhs.getName());
				}
			};
		}
		return sOrderByName;
	}
	
	public static Comparator<Idea> getOrderByNameDescComparator()
	{
		if(sOrderByNameDesc == null)
		{
			sOrderByNameDesc = new Comparator<Idea>()
			{
				@Override
				public int compare(Idea lhs, Idea rhs)
				{
					return rhs.getName().compareTo(lhs.getName());
				}
			};
		}
		return sOrderByNameDesc;
	}
	
	public static Comparator<Idea> getOrderByDateComparator()
	{
		if(sOrderByDate == null)
		{
			sOrderByDate = new Comparator<Idea>()
			{
				@Override
				public int compare(Idea lhs, Idea rhs)
				{
					//return ((Long)lhs.getId()).compareTo(rhs.getId());
					return new Long(lhs.getSaveTime()).compareTo(new Long(rhs.getSaveTime()));
				}
			};
		}
		return sOrderByDate;
	}
	
	public static Comparator<Idea> getOrderByDateDescComparator()
	{
		if(sOrderByDateDesc == null)
		{
			sOrderByDateDesc = new Comparator<Idea>()
			{
				@Override
				public int compare(Idea lhs, Idea rhs)
				{
					//return ((Long)lhs.getId()).compareTo(rhs.getId());
					return new Long(rhs.getSaveTime()).compareTo(new Long(lhs.getSaveTime()));
				}
			};
		}
		return sOrderByDateDesc;
	}
}
