package com.scurab.android.idearecorder.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.scurab.android.idearecorder.IdeaRecorderApplication;
import com.scurab.android.idearecorder.R;
import com.scurab.android.idearecorder.model.Idea;

public class DataProvider extends SQLiteOpenHelper {
    public static final String NAME = "/IdeaRecorder.sqlite";
    private static final String FOLDER = "/IdeaRecorder";
    private static final int VERSION = 1;
    private Context mContext;

    public DataProvider(Context context) {
	super(context, getDatabaseLocation(context), null, VERSION);
	mContext = context;
    }

    public static String getDatabaseLocation(Context c) {
	IdeaRecorderApplication ira = (IdeaRecorderApplication) c
		.getApplicationContext();
	PropertyProvider pp = ira.getPropertyProvider();
	String selectedStorageLocation = pp.getString(
		R.string.PROPERTY_STORAGE, null);
	File dir = null;
	if (selectedStorageLocation != null) {
	    dir = new File("/storage/" + selectedStorageLocation + FOLDER);
	} else {
	    dir = new File(Environment.getExternalStorageDirectory()
		    + "/IdeaRecorder");
	}
	if (!dir.exists()) {
	    dir.mkdir();
	}
	return dir.getAbsolutePath() + NAME;
    }

    /**
     * Returns all existing app defined tables
     * 
     * @return
     */
    public List<String> getTables() {
	List<String> tables = new ArrayList<String>();
	String sql = getString(R.string.SQL_SELECT_ALL_TABLES);

	Cursor c = getReadableDatabase().rawQuery(sql, null);

	while (c.moveToNext()) {
	    tables.add(c.getString(0));
	}
	c.close();
	return tables;
    }

    public void dropAllTables() {
	SQLiteDatabase db = getWritableDatabase();
	for (String table : Structure.ALL_TABLES) {
	    String sql = String.format("DROP TABLE IF EXISTS [%s];", table);
	    db.execSQL(sql);
	}
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	for (int i : Structure.ALL_TABLE_CREATE_QUERIES) {
	    String sql = getString(i);
	    db.execSQL(sql);
	}
    }

    private String getString(int resId) {
	return mContext.getString(resId);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	// not yet implemented
    }

    public void save(Idea i) {
	SQLiteDatabase db = getWritableDatabase();

	ContentValues cv = create(i);
	long id = db.insertOrThrow(Structure.TABLE_IDEAS, null, cv);
	i.setId(id);
    }

    public Idea getIdea(long id) {
	List<Idea> data = getIdeas(id);
	if (data.size() == 1) {
	    return data.get(0);
	} else {
	    return null;
	}
    }

    public List<Idea> getIdeas() {
	return getIdeas(null);
    }

    /**
     * Implemenation of getting data from db
     * 
     * @param id
     *            for value, if id is null all recorda are returned
     * @return
     */
    private List<Idea> getIdeas(Long id) {
	List<Idea> data = new ArrayList<Idea>();
	Cursor c = null;
	if (id == null) {
	    c = getReadableDatabase().query(Structure.TABLE_IDEAS,
		    Structure.Ideas.ALL_COLUMNS, null, null, null, null,
		    String.format("%s DESC", Structure.Ideas.ID));
	} else {
	    c = getReadableDatabase().query(Structure.TABLE_IDEAS,
		    Structure.Ideas.ALL_COLUMNS,
		    String.format("%s = ?", Structure.Ideas.ID),
		    new String[] { String.valueOf(id) }, null, null,
		    String.format("%s DESC", Structure.Ideas.SAVETIME));
	}

	while (c.moveToNext()) {
	    Idea i = new Idea();
	    i.setDescription(c.getString(c
		    .getColumnIndex(Structure.Ideas.DESCRIPTION)));
	    i.setId(c.getLong(c.getColumnIndex(Structure.Ideas.ID)));
	    i.setIdeaType(c.getInt(c.getColumnIndex(Structure.Ideas.TYPE)));
	    i.setName(c.getString(c.getColumnIndex(Structure.Ideas.NAME)));
	    i.setPath(c.getString(c.getColumnIndex(Structure.Ideas.PATH)));
	    i.setSaveTime(c.getLong(c.getColumnIndex(Structure.Ideas.SAVETIME)));
	    data.add(i);
	}

	c.close();
	return data;
    }

    public void udpate(Idea i) {
	SQLiteDatabase db = getWritableDatabase();

	ContentValues cv = create(i);

	@SuppressWarnings("unused")
	int res = db.update(Structure.TABLE_IDEAS, cv,
		String.format("%s = ?", Structure.Ideas.ID),
		new String[] { String.valueOf(i.getId()) });
    }

    private ContentValues create(Idea i) {
	ContentValues cv = new ContentValues();
	cv.put(Structure.Ideas.TYPE, i.getIdeaType());
	cv.put(Structure.Ideas.NAME, i.getName());
	cv.put(Structure.Ideas.DESCRIPTION, i.getDescription());
	cv.put(Structure.Ideas.PATH, i.getPath());
	cv.put(Structure.Ideas.SAVETIME, i.getSaveTime());
	return cv;
    }

    public static class Structure {
	public static String TABLE_IDEAS = "Ideas";
	public static String[] ALL_TABLES = new String[] { TABLE_IDEAS };
	public static int[] ALL_TABLE_CREATE_QUERIES = new int[] { R.string.SQL_CREATE_IDEAS_TABLE };

	public static class Ideas {
	    public static String ID = "Id";
	    public static String TYPE = "IdeaType";
	    public static String NAME = "Name";
	    public static String DESCRIPTION = "Description";
	    public static String PATH = "Path";
	    public static String SAVETIME = "SaveTime";
	    public static String[] ALL_COLUMNS = new String[] { ID, TYPE, NAME,
		    DESCRIPTION, PATH, SAVETIME };
	}
    }

    public void delete(long id) {
	SQLiteDatabase db = getWritableDatabase();

	@SuppressWarnings("unused")
	int res = db.delete(Structure.TABLE_IDEAS,
		String.format("%s = ?", Structure.Ideas.ID),
		new String[] { String.valueOf(id) });
    }

    public void deleteAllData() {
	SQLiteDatabase db = getWritableDatabase();
	for (String table : Structure.ALL_TABLES) {
	    String sql = String.format("DELETE FROM [%s];", table);
	    db.execSQL(sql);
	}
    }

}
