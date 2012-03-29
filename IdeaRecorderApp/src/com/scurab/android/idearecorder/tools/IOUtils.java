package com.scurab.android.idearecorder.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;

public class IOUtils
{

	private IOUtils()
	{
	}

	public static void copyFileFrom(Context c, Uri inputStream, String toFile) throws IOException
	{
		InputStream fis = c.getContentResolver().openInputStream(inputStream);
		copyFile(fis, toFile);
	}
	
	public static void copyFile(InputStream from, String to) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(to);
		byte[] buf = new byte[1024];
		int len;
		while ((len = from.read(buf)) > 0)
		{
			fos.write(buf, 0, len);
		}
		fos.close();
	}
	
	public static void copyFile(Context context, Uri uriFrom, String to) throws IOException
	{
		FileInputStream from = new FileInputStream(getPhysicalLocation(context, uriFrom));
		FileOutputStream fos = new FileOutputStream(to);
		byte[] buf = new byte[1024];
		int len;
		while ((len = from.read(buf)) > 0)
		{
			fos.write(buf, 0, len);
		}
		fos.close();
	}
	
	public static boolean moveFile(Context context, Uri uriFrom, String to)
	{
		File f = new File(getPhysicalLocation(context,uriFrom));
		return f.renameTo(new File(to));
	}

	public static String getPhysicalLocation(Context context, Uri uri)
	{
		String result = null;
		try
		{
			// can post image
			String[] proj = { MediaColumns.DATA };
			Cursor cursor = context.getContentResolver().query(uri, proj, // Which
																			// columns
																			// to
																			// return
					null, // WHERE clause; which rows to return (all rows)
					null, // WHERE clause selection arguments (none)
					null); // Order-by clause (ascending by name)
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
	
			result = cursor.getString(column_index);
			cursor.close();
		}
		catch(Exception e)
		{
			//just return null
		}
		return result;
	}
}
