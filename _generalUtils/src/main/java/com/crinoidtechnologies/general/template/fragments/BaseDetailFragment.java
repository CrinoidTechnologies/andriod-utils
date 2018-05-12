package com.crinoidtechnologies.general.template.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.crinoidtechnologies.general.models.BaseModel;

/**
 * Created by ${Vivek} on 5/4/2016 for Avante.Be careful
 */
public abstract class BaseDetailFragment extends BaseNetworkFragment {

    protected BaseModel object;
    protected boolean isEditing;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInfo();
        //   fetchItem();
    }

    protected abstract void setInfo();

    protected abstract void fetchItem();

    protected abstract void addItem();

    protected abstract void updateItem();

    public abstract void deleteItem();

    protected void endActivity() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

}
