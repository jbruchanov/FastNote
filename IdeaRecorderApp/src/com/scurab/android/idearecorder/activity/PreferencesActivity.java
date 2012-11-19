package com.scurab.android.idearecorder.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.ViewParent;

import com.scurab.android.idearecorder.IdeaRecorderApplication;
import com.scurab.android.idearecorder.R;

public class PreferencesActivity extends PreferenceActivity {
    @Override
    public void onAttachedToWindow() {
	initHeaderBackgroundColor();
	super.onAttachedToWindow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	addPreferencesFromResource(R.xml.preference_activity);
	initStorage();
    }

    protected void initStorage() {
	if (Build.VERSION.SDK_INT > 15) {
	    try {
		final IdeaRecorderApplication app = (IdeaRecorderApplication) getApplication();
		File[] fs = new File("/storage").listFiles();
		if (fs.length > 0) {
		    List<String> dirs = new ArrayList<String>();
		    for (File f : fs) {
			dirs.add(f.getName());
		    }
		    Collections.sort(dirs);
		    String[] array = dirs.toArray(new String[dirs.size()]);
		    ListPreference lp = (ListPreference) findPreference(getString(R.string.PROPERTY_STORAGE));
		    lp.setEnabled(true);
		    lp.setEntries(array);
		    lp.setEntryValues(array);
		    lp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(
				Preference preference, Object newValue) {
			    app.onStorageChange();
			    return true;
			}
		    });
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    private void initHeaderBackgroundColor() {
	try {
	    View titleView = getWindow().findViewById(android.R.id.title);
	    if (titleView != null) {
		ViewParent parent = titleView.getParent();
		if (parent != null && (parent instanceof View)) {
		    View parentView = (View) parent;
		    parentView
			    .setBackgroundResource(R.drawable.headerbackground);
		    // parentView.getLayoutParams().height = (int)(40 *
		    // getResources().getDisplayMetrics().density);
		}
	    }
	} catch (Exception e) {
	    // ignore error, it is workaround and can be different in versions
	}
    }
}
