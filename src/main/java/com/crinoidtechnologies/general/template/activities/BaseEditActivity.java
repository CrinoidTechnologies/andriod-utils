package com.crinoidtechnologies.general.template.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.crinoidtechnologies.general.R;

/**
 * Created by ${Vivek} on 5/4/2016 for Avante.Be careful
 */
public abstract class BaseEditActivity extends BasePermissionActivity {

    public static final String EXTRA_ID_KEY = "id";
    protected String mainTitle = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   fetchItem();
        if (getIntent().getExtras() != null) {
            int value = getIntent().getExtras().getInt(EXTRA_ID_KEY);
            if (value > 0) {
                setTitle(getString(R.string.edit) + " " + mainTitle);
            }
        }
        setTitle(getString(R.string.add) + " " + mainTitle);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
