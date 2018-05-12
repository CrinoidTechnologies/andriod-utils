package com.crinoidtechnologies.general.template.pager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crinoidtechnologies.general.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
abstract public class PagerDotsFragmentTemplate<T> extends Fragment implements ViewPager.OnPageChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public int accentColorId;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mainView;
    private View noItemView;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    // view indicators related
    private LinearLayout dotsIndicator;
    private List<TextView> dots = new ArrayList<>(2);
    private List<T> items = new ArrayList<>();

    public PagerDotsFragmentTemplate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PagerDotsFragmentTemplate.
     */
//    public static PagerDotsFragmentTemplate newInstance(String param1, String param2) {
//        PagerDotsFragmentTemplate fragment = new PagerDotsFragmentTemplate();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pager_dots, container, false);
        // noItemView =  view.findViewById(R.id.no_mission);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(this);

        dotsIndicator = (LinearLayout) view.findViewById(R.id.view_pager_indicator);
        setHasOptionsMenu(true);

        mainView = view;
        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        dataHandling();
        if (adapter.getCount() > 0) {
            setCurrentPage(0);
            noItemView.setVisibility(View.GONE);
        } else {
            noItemView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void onEditButtonClick() {

        if (adapter.getCount() > 0) {
            //Intent intent = new Intent(getActivity(), AddMissionActivity.class);
            //intent.putExtra("index", viewPager.getCurrentItem());
            //startActivity(intent);
        } else {
            //  Snackbar.make(mainView,R.string.no_mission_edit,Snackbar.LENGTH_SHORT).show();
        }
    }

    private void dataHandling() {
        updateAdapters();
        updatePageIndicator();
    }

    private void updatePageIndicator() {
        dotsIndicator.removeAllViews();
        if (dots.size() > adapter.getCount()) {
            while (dots.size() != adapter.getCount()) {
                dots.remove(0);
            }
        } else {
            while (dots.size() != adapter.getCount()) {
                TextView textview = new TextView(getContext());
                textview.setText(Html.fromHtml("&#8226;"));
                textview.setTextSize(40);
                dots.add(textview);
            }
        }

        for (int i = 0; i < adapter.getCount(); i++) {
            dotsIndicator.addView(dots.get(i));
        }

    }

    private void setCurrentPage(int number) {
        viewPager.setCurrentItem(number);
        updateDotsColor(number);
    }

    private void updateDotsColor(int number) {
        for (int i = dots.size() - 1; i > -1; i--) {
            if (i != number) {
                dots.get(i).setTextColor(getResources().getColor(android.R.color.white));
            } else {
                dots.get(i).setTextColor(getResources().getColor(accentColorId));

            }
        }
    }

    private void updateAdapters() {
        viewPager.setAdapter(null);
        adapter = null;
        adapter = getPagerAdapter();//new PagerAdapterTemplate(getActivity(), getListOfItems());
        viewPager.setAdapter(adapter);
    }

    abstract protected List<T> getListOfItems();

    abstract protected PagerAdapterTemplate getPagerAdapter();

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
