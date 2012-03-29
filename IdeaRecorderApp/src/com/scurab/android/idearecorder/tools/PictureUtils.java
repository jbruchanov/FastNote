package com.scurab.android.idearecorder.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class PictureUtils
{
	public static final int IMAGE_COMPRESS = 80;
	public static final int MAX_IMAGE_PART_SIZE = 400; //width of image view in activity (approx)
	public static Bitmap resizePicture(InputStream is, int inSampleSize)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();	                
		options.inSampleSize = inSampleSize;		
        final Bitmap bm = BitmapFactory.decodeStream(is, null, options);
        return bm;
	}
	
	public static String resizePicture(String file, int inSampleSize) throws IOException
	{
		FileInputStream fis = new FileInputStream(file);
		Bitmap b = resizePicture(fis, inSampleSize);		
		String resizedFile = file + "-resized.jpg";
		FileOutputStream fos = new FileOutputStream(resizedFile);
		b.compress(CompressFormat.JPEG, IMAGE_COMPRESS, fos);		
		fis.close();
		fos.close();
		return resizedFile;
	}
	
	public static void rotatePicture(String url, int angle) throws FileNotFoundException
	{
		final Bitmap source = BitmapFactory.decodeFile(url);
		Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        final Bitmap rotated = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true); 
        source.recycle();
        File f= new File(url);
        if(f.exists())
        	f.delete();
        FileOutputStream fos = new FileOutputStream(f);
        rotated.compress(CompressFormat.JPEG, IMAGE_COMPRESS, fos);
        rotated.recycle();
	}
	
	/**
	 * Get ratio to resize picture, by default the bigger size has 1000px at most.
	 * @param physLink
	 * @return
	 */
	public static int getRatioJpeg(String physLink)
	{
		int result = 2;
		try
		{				
			ExifInterface ei = new ExifInterface(physLink);
			int width = Integer.parseInt(ei.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
			int height = Integer.parseInt(ei.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
			int max = Math.max(width, height);
			float pomer = max/(float)MAX_IMAGE_PART_SIZE;
//			float pomer = (width / (float)dispWidth);  //600 / 400 
			if(pomer < 2)
				result = 1;
			else if (pomer > 8)
				result = 8;
			else if (pomer > 2)
				result = 4;
		}
		catch(Exception e)
		{
			//dont care, just 2
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param physLink jpeg file
	 * @return Max(width,height) px
	 */
	public static int getMaxJpegSize(String physLink)
	{
		int max = 0;
		try
		{				
			ExifInterface ei = new ExifInterface(physLink);
			int width = Integer.parseInt(ei.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
			int height = Integer.parseInt(ei.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
			max = Math.max(width, height);
		}
		catch(Exception e)
		{
			//dont care, just 2
		}
		return max;
	}
	
}
