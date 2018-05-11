package com.crinoidtechnologies.general.template.activities;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.crinoidtechnologies.general.R;

import java.util.List;

/**
 * Created by ${Vivek} on 5/20/2016 for GoonjCare.Be careful
 */
public abstract class SearchHelperActivity<T> extends BasePermissionActivity {

    public View nothingHere;
    public TextView nothingHereTextView;
    protected String oldSearchString = "Q22131cgvb";
    protected String newSearchString = "";
    boolean isFindingItems = false;

    @Override
    protected void onStart() {
        super.onStart();
        setNothingHereView();
    }

    protected abstract void setNothingHereView();

    public void doSearch(final String newText) {

        if (!newText.equals(oldSearchString)) {
            if (isFindingItems) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doSearch(newText);
                    }
                }, 300);
            } else {
                newSearchString = newText;
                updateCursor();
            }
        }
    }

    public void updateCursor() {
        if (isFindingItems)
            return;
        isFindingItems = true;
        new ItemGetterTask().execute(0);
    }

    protected abstract List<T> getEntityBasedOnSearchString(String newSearchString);

    protected abstract void onListItemUpdateBySearch(List<T> cursor);

    class ItemGetterTask extends AsyncTask<Integer, Void, List<T>> {

        @Override
        protected List<T> doInBackground(Integer... params) {
            Cursor cursor = null;
            boolean removeUnwantedItem = false;
            if (newSearchString != null && newSearchString.length() > 0) {
                return getEntityBasedOnSearchString(newSearchString);

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            oldSearchString = newSearchString;
        }

        @Override
        protected void onPostExecute(List<T> cursor) {
            Log.d("TAG", "onPostExecute()" + " called with: " + "cursor = [" + cursor + "]");
            if (null != cursor && cursor.size() == 0) {
                nothingHere.setVisibility(View.VISIBLE);
                if (oldSearchString == null || oldSearchString.length() == 0) {
                    nothingHereTextView.setText(R.string.nothing_here);
                } else {
                    nothingHereTextView.setText(R.string.no_matching_item);
                }
            }
//            else {
//                nothingHere.setVisibility(View.GONE);
//            }

            onListItemUpdateBySearch(cursor);

            isFindingItems = false;
        }
    }

}
