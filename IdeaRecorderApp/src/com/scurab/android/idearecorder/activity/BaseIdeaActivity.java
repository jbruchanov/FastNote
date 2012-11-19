package com.scurab.android.idearecorder.activity;

import android.widget.EditText;
import android.widget.ImageButton;

public abstract class BaseIdeaActivity extends BaseActivity {
    public abstract ImageButton getSaveButton();

    public abstract ImageButton getCancelButton();

    public abstract EditText getNameEditText();

    public abstract ImageButton getNameRecordButton();

}
