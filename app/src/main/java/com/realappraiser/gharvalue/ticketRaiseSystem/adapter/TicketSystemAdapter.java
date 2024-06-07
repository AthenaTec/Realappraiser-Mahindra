package com.realappraiser.gharvalue.ticketRaiseSystem.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.realappraiser.gharvalue.ticketRaiseSystem.view.RaisedTicketFragment;
import com.realappraiser.gharvalue.ticketRaiseSystem.view.ViewTicketFragment;

public class TicketSystemAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitles = new String[]{"RAISED TICKETS", "VIEW TICKETS"};

    public TicketSystemAdapter(@NonNull FragmentManager fm) {
        super(fm);
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


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                @SuppressWarnings("UnnecessaryLocalVariable") RaisedTicketFragment tab1 = new RaisedTicketFragment();
                return tab1;
            case 1:
                @SuppressWarnings("UnnecessaryLocalVariable") ViewTicketFragment tab2 = new ViewTicketFragment();
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
