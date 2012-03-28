package com.scurab.android.idearecorder.tools;

import junit.framework.TestCase;

public class StringToolsTest  extends TestCase
{
	
	//NULL OR EMPTY 
	public void testIsNullOrEmptyNullValue()
	{
		String value = null;		
		boolean result = StringTools.isNullOrEmpty(value);
		assertTrue(result);
	}
	
	public void testIsNullOrEmptyValue()
	{
		String value = "";		
		boolean result = StringTools.isNullOrEmpty(value);
		assertTrue(result);
	}
	
	public void testIsNullOrEmptySomeValue()
	{
		String value = "213456";		
		boolean result = StringTools.isNullOrEmpty(value);
		assertFalse(result);
	}
	
	public void testIsNullOrEmptyWhiteCharsValue()
	{
		String value = " \n \t";		
		boolean result = StringTools.isNullOrEmpty(value);
		assertFalse(result);
	}
	
	//END NULL OR EMPTY
	
	//NULL OR TRIMMED EMPTY 
	public void testIsNullOrTrimmedEmptyNullValue()
	{
		String value = null;		
		boolean result = StringTools.isNullOrTrimmedEmpty(value);
		assertTrue(result);
	}
	
	public void testIsNullOrTrimmedEmptyValue()
	{
		String value = "";		
		boolean result = StringTools.isNullOrTrimmedEmpty(value);
		assertTrue(result);
	}
	
	public void testIsNullOrTrimmedEmptySomeValue()
	{
		String value = "213456";		
		boolean result = StringTools.isNullOrTrimmedEmpty(value);
		assertFalse(result);
	}
	
	public void testIsNullOrTrimmedEmptyWhiteCharsValue()
	{
		String value = " \n \t";		
		boolean result = StringTools.isNullOrTrimmedEmpty(value);
		assertTrue(result);
	}
	//END NULL OR TRIMMED EMPTY
	
	
	//NullIfTrimmedEmpty
	public void testNullIfTrimmedEmpty_whitechars()
	{
		String value = " \n \t";		
		String result = StringTools.nullIfTrimmedEmpty(value);
		assertNull(result);
	}
	
	public void testNullIfTrimmedEmpty_somethink()
	{
		String value = " \n1\t";		
		String result = StringTools.nullIfTrimmedEmpty(value);
		assertEquals(value, result);
	}
	
	public void testNullIfTrimmedEmpty_emptystring()
	{
		String value = "";		
		String result = StringTools.nullIfTrimmedEmpty(value);
		assertNull(result);
	}
	
	public void testNullIfTrimmedEmpty_null()
	{
		String value = "123asdf";		
		String result = StringTools.nullIfTrimmedEmpty(value);
		assertEquals(value, result);
	}
	
	
	public void testGetFileNameFromIdeaNameNull()
	{
		String value = null;
		String expected = "";
		String result = StringTools.getFileNameFromIdeaName(value);
		assertEquals(expected,result);
	}
	
	public void testGetFileNameFromIdeaNameEmptyString()
	{
		String value = "";
		String expected = "";
		String result = StringTools.getFileNameFromIdeaName(value);
		assertEquals(expected,result);
	}
	
	public void testGetFileNameFromIdeaNameSimpleString()
	{
		String value = "123456abcde";
		String expected = "123456abcde";
		String result = StringTools.getFileNameFromIdeaName(value);
		assertEquals(expected,result);
	}
	
	public void testGetFileNameFromIdeaNameComplexString()
	{
		String value = " a asd sdfa\n ěščáíě\t ůšěč§pú§ů.šě+čav";
		String expected = "aasdsdfaěščáíěůšěčpúůšěčav";
		String result = StringTools.getFileNameFromIdeaName(value);
		assertEquals(expected,result);
	}
}
