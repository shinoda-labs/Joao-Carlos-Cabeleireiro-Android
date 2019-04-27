package com.shinodalabs.joaocarloscabeleireiro.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shinodalabs.joaocarloscabeleireiro.Fragments.ScheduleCanceledFragment;
import com.shinodalabs.joaocarloscabeleireiro.Fragments.ScheduleDoneFragment;

public class PageAdapter extends FragmentStatePagerAdapter {

    int countTab;

    public PageAdapter(FragmentManager fm, int countTab) {
        super(fm);
        this.countTab = countTab;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                ScheduleDoneFragment scheduleDoneFragment = new ScheduleDoneFragment();
                return scheduleDoneFragment;
            case 1:
                ScheduleCanceledFragment scheduleCanceledFragment = new ScheduleCanceledFragment();
                return scheduleCanceledFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return countTab;
    }
}
