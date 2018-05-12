package com.crinoidtechnologies.general.template.pager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.crinoidtechnologies.general.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
abstract public class ImagePagerDotsView extends PagerDotsView implements ViewPager.OnPageChangeListener {

    public ImagePagerDotsView(Context context) {
        super(context);
    }

    public void setImagesList(List<String> infos) {
        viewPager.setAdapter(null);
        this.adapter = null;
        this.adapter = new PagerAdapterTemplate<>(getContext(), infos, R.layout.image_view);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updatePageIndicator();
    }

}
