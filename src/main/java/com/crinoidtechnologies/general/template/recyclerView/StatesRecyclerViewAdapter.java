package com.crinoidtechnologies.general.template.recyclerView;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author rockerhieu on 8/11/15.
 */
public class StatesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int STATE_NORMAL = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_EMPTY = 2;
    public static final int STATE_ERROR = 3;

    public static final int TYPE_LOADING = 1000;
    public static final int TYPE_EMPTY = 1001;
    public static final int TYPE_ERROR = 1002;

    private final View vLoadingView;
    private final View vEmptyView;
    private final View vErrorView;
    private final RecyclerView.Adapter adapter;
    @State
    private int state = STATE_NORMAL;

    public StatesRecyclerViewAdapter(@NonNull RecyclerView.Adapter adapter, @Nullable View loadingView, @Nullable View emptyView, @Nullable View errorView) {
        this.adapter = adapter;
        this.vLoadingView = loadingView;
        this.vEmptyView = emptyView;
        this.vErrorView = errorView;
        setState(STATE_NORMAL);
    }

    @State
    public int getState() {
        return state;
    }

    public void setState(@State int state) {
        this.state = state;
        notifyDataSetChanged();
        vLoadingView.setVisibility(View.GONE);
        vErrorView.setVisibility(View.GONE);
        vEmptyView.setVisibility(View.GONE);

        switch (state) {
            case STATE_LOADING:
                vLoadingView.setVisibility(View.VISIBLE);
                break;
            case STATE_EMPTY:
                vEmptyView.setVisibility(View.VISIBLE);
                break;
            case STATE_ERROR:
                vErrorView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LOADING:
                return new SimpleViewHolder(vLoadingView);
            case TYPE_EMPTY:
                return new SimpleViewHolder(vEmptyView);
            case TYPE_ERROR:
                return new SimpleViewHolder(vErrorView);
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (state) {
            case STATE_LOADING:
                onBindLoadingViewHolder(holder, position);
                break;
            case STATE_EMPTY:
                onBindEmptyViewHolder(holder, position);
                break;
            case STATE_ERROR:
                onBindErrorViewHolder(holder, position);
                break;
            default:
                adapter.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (state) {
            case STATE_LOADING:
                return TYPE_LOADING;
            case STATE_EMPTY:
                return TYPE_EMPTY;
            case STATE_ERROR:
                return TYPE_ERROR;
        }
        return adapter.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        switch (state) {
            case STATE_LOADING:
            case STATE_EMPTY:
            case STATE_ERROR:
                return 0;
        }
        return adapter.getItemCount();
    }

    public void onBindErrorViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    public void onBindEmptyViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    public void onBindLoadingViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    @IntDef({STATE_NORMAL, STATE_LOADING, STATE_EMPTY, STATE_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}