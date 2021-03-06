package kr.o3selab.sunmoonbus.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import kr.o3selab.sunmoonbus.fragment.Tab1;
import kr.o3selab.sunmoonbus.fragment.Tab2;
import kr.o3selab.sunmoonbus.fragment.Tab3;
import kr.o3selab.sunmoonbus.fragment.Tab4;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    private int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    private int type;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, int type) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.type = type;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {    // if the position is 0 we are returning the First tab
            Bundle args = new Bundle();
            args.putInt("type", type);
            Tab1 tab1 = new Tab1();
            tab1.setArguments(args);
            return tab1;
        } else if (position == 1) { // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            Bundle args = new Bundle();
            args.putInt("type", type);
            Tab2 tab2 = new Tab2();
            tab2.setArguments(args);
            return tab2;
        } else if (position == 2) {
            Bundle args = new Bundle();
            args.putInt("type", type);
            Tab3 tab3 = new Tab3();
            tab3.setArguments(args);
            return tab3;
        } else {
            Bundle args = new Bundle();
            args.putInt("type", type);
            Tab4 tab4 = new Tab4();
            tab4.setArguments(args);
            return tab4;
        }
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
