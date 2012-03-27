package manual;

import com.scurab.android.idearecorder.TestHelper;
import com.scurab.android.idearecorder.tools.DataProvider;

import android.database.DatabaseUtils;
import android.test.AndroidTestCase;
import junit.framework.TestCase;

public class ByHand extends AndroidTestCase
{
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		DataProvider db = new DataProvider(mContext);
		if(db.getTables().size() == 0)
			db.onCreate(db.getWritableDatabase());
	}
	
	public void testAddItems()
	{
		DataProvider db = new DataProvider(mContext);
		for(int i = 0;i<10;i++)
			db.save(TestHelper.getRandomIdea());
	}
}
