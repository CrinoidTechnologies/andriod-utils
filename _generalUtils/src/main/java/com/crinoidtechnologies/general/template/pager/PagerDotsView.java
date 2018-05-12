package com.crinoidtechnologies.general.template.pager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crinoidtechnologies.general.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
abstract public class PagerDotsView extends LinearLayout implements ViewPager.OnPageChangeListener {

    protected ViewPager viewPager;
    protected PagerAdapter adapter;

    protected LinearLayout dotsIndicator;

    protected List<TextView> dots = new ArrayList<>(2);

    protected int selectedDotColorId;
    protected int unSelectedDotColorId;
    protected float dotSize;

    public PagerDotsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public PagerDotsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PagerDotsView,
                0, 0);

        try {
            selectedDotColorId = a.getColor(R.styleable.PagerDotsView_selectedDotColor, context.getResources().getColor(R.color.colorAccent));
            unSelectedDotColorId = a.getColor(R.styleable.PagerDotsView_unSelectedDotColor, Color.WHITE);
            dotSize = a.getDimension(R.styleable.PagerDotsView_dotSize, 32f);
            // mTextPos = a.getInteger(R.styleable.PieChart_labelPosition, 0);
        } finally {
            a.recycle();
        }

        initView();
    }

    public PagerDotsView(Context context) {
        super(context);
        initView();
    }

    protected void initView() {
        View view = inflate(getContext(), R.layout.fragment_pager_dots, null);
        addView(view);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(this);
        dotsIndicator = (LinearLayout) view.findViewById(R.id.view_pager_indicator);
    }

//
//    @Override
//    public void onResume() {
//        super.onResume();
//        dataHandling();
//        if (adapter.getCount() > 0) {
//            setCurrentPage(0);
//            noItemView.setVisibility(View.GONE);
//        } else {
//            noItemView.setVisibility(View.VISIBLE);
//        }
//
//    }

    protected void updatePageIndicator() {
        dotsIndicator.removeAllViews();
        if (dots.size() > adapter.getCount()) {
            while (dots.size() != adapter.getCount()) {
                dots.remove(0);
            }
        } else {
            while (dots.size() != adapter.getCount()) {
                TextView textview = new TextView(getContext());
                textview.setText(Html.fromHtml("&#8226;"));
                textview.setTextSize(dotSize);
                dots.add(textview);
            }
        }

        for (int i = 0; i < adapter.getCount(); i++) {
            dotsIndicator.addView(dots.get(i));
        }

    }

    public void setCurrentPage(int number) {
        viewPager.setCurrentItem(number);
        updateDotsColor(number);
    }

    private void updateDotsColor(int number) {
        for (int i = dots.size() - 1; i > -1; i--) {
            if (i != number) {
                dots.get(i).setTextColor(getResources().getColor(unSelectedDotColorId));
            } else {
                dots.get(i).setTextColor(getResources().getColor(selectedDotColorId));
            }
        }
    }

    public void setAdapters(PagerAdapterTemplate adapter) {
        viewPager.setAdapter(null);
        this.adapter = null;
        this.adapter = adapter;//new PagerAdapterTemplate(getActivity(), getListOfItems());
        viewPager.setAdapter(adapter);
        updatePageIndicator();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updateDotsColor(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
