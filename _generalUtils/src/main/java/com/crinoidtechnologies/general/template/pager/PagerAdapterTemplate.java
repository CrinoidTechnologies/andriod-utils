package com.crinoidtechnologies.general.template.pager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ${Vivek} on 2/1/2016
 */
public class PagerAdapterTemplate<T> extends PagerAdapter {

    public int layoutId = 0;
    protected List<T> items;
    protected Context context;

    public PagerAdapterTemplate(Context context, List<T> items, int layoutId) {
        this.items = items;
        this.context = context;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutId, container, false);

        if (position < items.size()) {
            T item = items.get(position);
            ((ViewPager) container).addView(view);
            updateViewWithData(view, item);
        }

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        (container).removeView(view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    public void updateViewWithData(View view, T item) {

    }

    public void addItem(T item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public List<T> getItems() {
        return items;
    }
}
