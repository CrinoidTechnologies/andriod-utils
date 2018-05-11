package com.crinoidtechnologies.general.template.fragments;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewTreeObserver;

import com.crinoidtechnologies.general.R;
import com.crinoidtechnologies.general.template.adapters.BaseListAdapter;
import com.crinoidtechnologies.general.template.recyclerView.BaseRecyclerViewAdapter;
import com.crinoidtechnologies.general.template.recyclerView.StatesRecyclerViewAdapter;
import com.crinoidtechnologies.general.utils.GeneralUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Vivek} on 4/30/2016 for Avante.Be careful
 */
public abstract class BaseListFragment<T> extends SearchHelperFragment<T> implements BaseListAdapter.BaseListAdapterListener, ViewTreeObserver.OnGlobalLayoutListener {

    protected int mColumnCount = 1;
    protected int columnDimension = 100;
    protected boolean isStaggeredView = false;
    protected RecyclerView recyclerView;
    protected StatesRecyclerViewAdapter statesAdapter;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected View loadView;
    protected View emptyView;
    protected View errorView;
    protected boolean isReverse = false;
    protected int orientation = LinearLayoutManager.VERTICAL;

    public BaseListFragment() {
    }

    @Override
    protected void initView() {
        setViews();

        Context context = view.getContext();
        if (mColumnCount == 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context, orientation, isReverse));
        } else if (isStaggeredView) {
            final StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(Math.max(1, mColumnCount), orientation);
            recyclerView.setLayoutManager(manager);
        } else {
            final GridLayoutManager manager = new GridLayoutManager(context, Math.max(1, mColumnCount));
            recyclerView.setLayoutManager(manager);
        }

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshItems();
                }
            });

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    int topRowVerticalPosition =
                            (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                    swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);

                }
            });
        }

        if (errorView != null) {
            View view = errorView.findViewById(R.id.retry_button);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    statesAdapter.setState(StatesRecyclerViewAdapter.STATE_LOADING);
                    refreshItems();
                }
            });
        }
    }

    protected void refreshItems() {

    }

    /**
     * recyclerView =
     * loadView = view.findViewById(R.id.layout_loading);
     * emptyView = view.findViewById(R.id.layout_empty);
     * errorView = view.findViewById(R.id.layout_error);
     */
    protected abstract void setViews();

    protected abstract RecyclerView.Adapter getAdapter();

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    @Override
    public void onDestroyView() {
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }
        super.onDestroyView();
    }

    @Override
    public void onGlobalLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        int viewWidth = recyclerView.getMeasuredWidth();
        int newSpanCount = (int) Math.floor(viewWidth / GeneralUtils.convertDpToPixel(columnDimension, getActivity()));
        GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
        manager.setSpanCount(Math.max(1, newSpanCount));
        manager.requestLayout();
    }

    protected List<T> getShowingItems() {

        List<T> item = new ArrayList<>();

        if (statesAdapter != null && statesAdapter.getAdapter() != null && statesAdapter.getAdapter() instanceof BaseRecyclerViewAdapter) {
            item = ((BaseRecyclerViewAdapter) statesAdapter.getAdapter()).getItems();
        }

        return item;
    }

}
