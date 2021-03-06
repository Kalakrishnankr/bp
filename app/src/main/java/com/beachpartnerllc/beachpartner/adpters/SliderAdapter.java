package com.beachpartnerllc.beachpartner.adpters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.beachpartnerllc.beachpartner.calendar.compactcalendarview.domain.Event;
import com.beachpartnerllc.beachpartner.fragments.BPFinderFragment;
import com.beachpartnerllc.beachpartner.fragments.ConnectionsTabFragment;

/**
 * Created by seq-kala on 7/3/18.
 */

public class SliderAdapter extends FragmentStatePagerAdapter {

    CharSequence titles[];
    int numberOfTabs;
    long eventDate;
    private static boolean isBPActive = false;
    private static boolean isPartner = false;
    private Event eventObject;

    public  SliderAdapter(FragmentManager fragmentManager, CharSequence[] titles, int numberOfTabs,long eventDate,Event objectevent) {
        super(fragmentManager);
        this.eventDate = eventDate;
        this.titles =titles;
        this.numberOfTabs=numberOfTabs;
        this.eventObject=objectevent;

    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
    @Override
    public Fragment getItem(int position) {
        if(position==0){

            ConnectionsTabFragment connectionsTabFragment = new ConnectionsTabFragment();
            Bundle argDate = new Bundle();
            argDate.putLong("eventDate",eventDate);
            argDate.putParcelable("eventObj",eventObject);
            connectionsTabFragment.setArguments(argDate);
            return connectionsTabFragment;

        }else {
            isBPActive =false;
            isPartner =true;
            BPFinderFragment bpFinderFragment = new BPFinderFragment(isBPActive,isPartner);
            return bpFinderFragment;
        }


    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

}
