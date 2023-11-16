package com.realappraiser.gharvalue.convenyancereport.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.realappraiser.gharvalue.convenyancereport.DialyReportFragment;
import com.realappraiser.gharvalue.convenyancereport.WeeklyReportFragment;
public class ConvenyanceReportTabAdapter extends FragmentStatePagerAdapter {
    @SuppressWarnings("CanBeFinal")
    private String[] tabTitles = new String[]{"DAILY", "CUSTOM"};


    public ConvenyanceReportTabAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {

            // Jaipur Tab
            switch (position) {
                case 0:
                    @SuppressWarnings("UnnecessaryLocalVariable") DialyReportFragment tab1 = new DialyReportFragment();
                    return tab1;
                case 1:
                    @SuppressWarnings("UnnecessaryLocalVariable") WeeklyReportFragment tab2 = new WeeklyReportFragment();
                    return tab2;
                default:
                    return null;
            }


    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }


}