package manual;

import com.scurab.android.idearecorder.TestHelper;
import com.scurab.android.idearecorder.tools.DataProvider;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.MediaColumns;
import android.test.AndroidTestCase;

public class ByHand extends AndroidTestCase
{
	int debug = 0;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		DataProvider db = new DataProvider(mContext);
		if (db.getTables().size() == 0)
			db.onCreate(db.getWritableDatabase());
	}

	public void testAddItems()
	{
		if (debug == 0)
		{

			DataProvider db = new DataProvider(mContext);
			for (int i = 0; i < 500; i++)
				db.save(TestHelper.getRandomIdea());
		}
	}

	public void testDeleteAllTables()
	{
		if (debug == 1)
		{
			DataProvider db = new DataProvider(mContext);
			db.deleteAllData();
		}
	}

	public void testXXX()
	{
		if (debug == 2)
		{
			
			Uri uri = Uri.parse("content://media/external/video/media/105");
			 // can post image
	        String [] proj={MediaColumns.DATA};	        
	        Cursor cursor = mContext.getContentResolver().query( uri,
	                        proj, // Which columns to return
	                        null,       // WHERE clause; which rows to return (all rows)
	                        null,       // WHERE clause selection arguments (none)
	                        null); // Order-by clause (ascending by name)
	        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
	        cursor.moveToFirst();
	        
	        String path =  cursor.getString(column_index);
	        cursor.close();
		}
	}
}
