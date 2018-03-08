package com.goldemo.beachpartner.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goldemo.beachpartner.R;
import com.goldemo.beachpartner.adpters.SliderAdapter;
import com.goldemo.beachpartner.calendar.myowncalendar.SlidingTab;


public class PartnerInviteFragmentTab extends Fragment {

    private ViewPager pager;
    private SlidingTab tabs;
    private SliderAdapter slideAdapter;
    private int numberOfTabs = 2;
    private CharSequence titles[] = {"Connections","Find Partners"};

    public PartnerInviteFragmentTab() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view   =   inflater.inflate(R.layout.fragment_partner_invite, container, false);
        initActivity(view);
        return view;
    }

    private void initActivity(View view) {

        pager           =  (ViewPager) view.findViewById(R.id.pager);
        tabs            =  (SlidingTab) view.findViewById(R.id.tabs);
        slideAdapter    =  new SliderAdapter(getFragmentManager(), titles, numberOfTabs);

        pager.setAdapter(slideAdapter);

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        tabs.setViewPager(pager);
        slideAdapter.notifyDataSetChanged();
        pager.setAdapter(slideAdapter);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }



}
