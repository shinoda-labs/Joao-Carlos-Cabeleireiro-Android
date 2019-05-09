package com.shinodalabs.joaocarloscabeleireiro.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shinodalabs.joaocarloscabeleireiro.Fragments.UserFragments.MySchedules.ScheduleAllFragment;
import com.shinodalabs.joaocarloscabeleireiro.Fragments.UserFragments.MySchedules.ScheduleDoneFragment;
import com.shinodalabs.joaocarloscabeleireiro.Fragments.UserFragments.MySchedules.ScheduleToDoFragment;
import com.shinodalabs.joaocarloscabeleireiro.Model.User;

public class PageAdapter extends FragmentStatePagerAdapter {

    private int countTab;

    public PageAdapter(FragmentManager fm, int countTab) {
        super(fm);
        this.countTab = countTab;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                ScheduleAllFragment scheduleAllFragment = new ScheduleAllFragment();
                return scheduleAllFragment;
            case 1:
                ScheduleToDoFragment scheduleToDoFragment = new ScheduleToDoFragment();
                return  scheduleToDoFragment;
            case 2:
                ScheduleDoneFragment scheduleDoneFragment = new ScheduleDoneFragment();
                return scheduleDoneFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return countTab;
    }
}
