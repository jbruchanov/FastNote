package com.scurab.android.idearecorder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import junit.framework.TestCase;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scurab.android.idearecorder.model.Idea;
import com.scurab.android.idearecorder.tools.PictureUtils;

public class TestHelper extends TestCase
{
	public static Random sRandom = new Random();
	
	public static Idea getRandomIdea()
	{
		Idea i = new Idea().setName(UUID.randomUUID().toString())
				.setDescription(UUID.randomUUID().toString())
				.setIdeaType(1 + sRandom.nextInt(4))
				.setSaveTime(System.currentTimeMillis())
				.setPath(UUID.randomUUID().toString());
		return i;
	}
	
	public static List<Idea> getRandomIdeas(int howMany)
	{
		List<Idea> result = new ArrayList<Idea>();
		for(int i = 0;i<howMany;i++)
		{
			Idea id = new Idea().setName(UUID.randomUUID().toString())
								.setDescription(UUID.randomUUID().toString())
								.setIdeaType(1 + sRandom.nextInt(4))
								.setSaveTime(System.currentTimeMillis())
								.setPath(UUID.randomUUID().toString());
			result.add(id);
		}
		return result;
	}
	
	public static void assertIdeaView(Idea i, View iv)
	{
		TextView tvName = (TextView) iv.findViewById(R.id.tvName);
		TextView tvDescription = (TextView) iv.findViewById(R.id.tvDescription);
		TextView tvTime = (TextView) iv.findViewById(R.id.tvTime);
		ImageView icon = (ImageView) iv.findViewById(R.id.ivIcon);
		
		assertEquals(i.getName(),tvName.getText().toString());
		assertEquals(i.getDescription(),tvDescription.getText().toString());
		assertEquals(new Date(i.getSaveTime()).toLocaleString(),tvTime.getText().toString());	
		assertNotNull(icon);
	}
	
	/**
	 * Simple method to generate random file with some content
	 * @param file where to save
	 * @param length filesize, don't use any big numbers!
	 * @throws IOException 
	 */
	public static void genRandomFile(String file, int length) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		for(int i = 0;i<length;i++)
		{			
			fos.write(sRandom.nextInt(256));
		}
		fos.close();
	}
	
	public void testGenRandomFile() throws IOException
	{
		String file = "/sdcard/test.bin";
		File f = new File(file);
		f.delete();
		try
		{
			int size = 2048;
			genRandomFile(file, size);
			f = new File(file);
			assertTrue(f.exists());
			assertTrue(f.isFile());
			assertEquals(size,f.length());
		}
		catch(Exception e)
		{
			f.delete();
			fail(e.getMessage());
		}
	}
	
	public static void genRandomJPG(String file) throws IOException
	{
		Bitmap b = Bitmap.createBitmap(300, 300,Config.RGB_565);
		Drawable d = new BitmapDrawable(b);
		Paint p = new Paint();p.setColor(Color.WHITE);
		Canvas c = new Canvas(b);
		c.drawLine(0, 0, 30, 30, p);
		d.draw(c);
		FileOutputStream fos = new FileOutputStream(file);
		b.compress(CompressFormat.JPEG, 50, fos);
		fos.close();
	}
	
	public void testGenRandomJPG() throws IOException
	{
		String file = "/sdcard/test.jpg";
		new File(file).delete();
		genRandomJPG(file);
		assertEquals(300,PictureUtils.getMaxJpegSize(file));
		new File(file).delete();
	}
}
