package com.crinoidtechnologies.general.template.fragments;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.crinoidtechnologies.general.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Vivek} on 7/5/2016 for GoonjCare.Be careful
 */

public abstract class SearchHelperFragment<T> extends BaseNetworkFragment implements SearchView.OnQueryTextListener {
    public View nothingHere;
    public TextView nothingHereTextView;
    protected String previousSearchString = "Q22131cgvb";
    protected String currentSearchString = "";
    protected List<T> searchedItems = new ArrayList<>();
    boolean isFindingItems = false;

    @Override
    public void onStart() {
        super.onStart();
        setNothingHereView();
        if (nothingHere != null) {
            nothingHere.setVisibility(View.GONE);
        }
    }

    protected abstract void setNothingHereView();

    @Override
    public boolean onQueryTextSubmit(String query) {
        doSearch(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        doSearch(newText);
        return true;
    }

    public void doSearch(final String newText) {

        if (newText == null) {
            return;
        }
        if (newText.isEmpty() && currentSearchString.isEmpty()) {
            return;
        }

        if (!newText.equals(currentSearchString)) {
            if (isFindingItems) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doSearch(newText);
                    }
                }, 300);
            } else {
                currentSearchString = newText;
                updateCursor();
            }
        }
    }

    public void updateCursor() {
        isFindingItems = true;
        new ItemGetterTask().execute(0);
    }

    protected abstract List<T> getEntityBasedOnSearchString(String newSearchString);

    protected abstract void onListItemUpdateBySearch(List<T> cursor);

    protected class ItemGetterTask extends AsyncTask<Integer, Void, List<T>> {

        @Override
        protected List<T> doInBackground(Integer... params) {
            if (currentSearchString != null) {
                return getEntityBasedOnSearchString(currentSearchString);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<T> cursor) {
            if (cursor != null) {
                Log.d("TAG", "onPostExecute()" + " called with: " + "cursor = [" + cursor.size() + "]");
            }
            previousSearchString = currentSearchString;
            if (nothingHere != null) {
                if (null != cursor && cursor.size() == 0) {
                    nothingHere.setVisibility(View.VISIBLE);
                    nothingHereTextView.setText(R.string.no_matching_item);
                } else {
                    nothingHere.setVisibility(View.GONE);
                }
            }

            onListItemUpdateBySearch(cursor);

            isFindingItems = false;
        }
    }

}
