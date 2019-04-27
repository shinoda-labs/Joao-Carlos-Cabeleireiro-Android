package com.shinodalabs.joaocarloscabeleireiro.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shinodalabs.joaocarloscabeleireiro.Fragments.MySchedules.ScheduleAllFragment;
import com.shinodalabs.joaocarloscabeleireiro.Fragments.MySchedules.ScheduleCanceledFragment;
import com.shinodalabs.joaocarloscabeleireiro.Fragments.MySchedules.ScheduleDoneFragment;
import com.shinodalabs.joaocarloscabeleireiro.Fragments.MySchedules.ScheduleToDoFragment;

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
                ScheduleAllFragment scheduleAllFragment = new ScheduleAllFragment();
                return scheduleAllFragment;
            case 1:
                ScheduleToDoFragment scheduleToDoFragment = new ScheduleToDoFragment();
                return  scheduleToDoFragment;
            case 2:
                ScheduleDoneFragment scheduleDoneFragment = new ScheduleDoneFragment();
                return scheduleDoneFragment;
            case 3:
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
