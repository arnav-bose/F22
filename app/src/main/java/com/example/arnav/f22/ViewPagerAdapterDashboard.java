package com.example.arnav.f22;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Arnav on 13/11/2016.
 */
public class ViewPagerAdapterDashboard extends FragmentStatePagerAdapter{

    public ViewPagerAdapterDashboard(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new Contacts();
        }
        else{
            return new Reminder();
        }
    }

    public int getCount(){
        return 2;
    }

}
