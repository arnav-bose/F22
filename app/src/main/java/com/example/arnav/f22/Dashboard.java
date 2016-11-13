package com.example.arnav.f22;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class Dashboard extends AppCompatActivity {

    private Toolbar toolbarDashboard;
    private ViewPager viewPagerDashboard;
    private ViewPagerAdapterDashboard viewPagerAdapterDashboard;
    private TabLayout tabLayoutDashboard;
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Toolbar, TabLayout, ViewPager
        toolbarDashboard = (Toolbar)findViewById(R.id.toolbarDashboard);
        tabLayoutDashboard = (TabLayout)findViewById(R.id.tabLayoutDashboard);
        viewPagerDashboard = (ViewPager)findViewById(R.id.viewPagerDashboard);
        setSupportActionBar(toolbarDashboard);

        //ViewPager Adapter
        viewPagerAdapterDashboard = new ViewPagerAdapterDashboard(getSupportFragmentManager());
        viewPagerDashboard.setAdapter(viewPagerAdapterDashboard);

        //TabLayout
        final TabLayout.Tab tabContacts = tabLayoutDashboard.newTab();
        final TabLayout.Tab tabReminder = tabLayoutDashboard.newTab();

        tabContacts.setText("Contacts");
        tabReminder.setText("Reminders");

        tabLayoutDashboard.addTab(tabContacts, 0);
        tabLayoutDashboard.addTab(tabReminder, 1);

        tabLayoutDashboard.setTabTextColors(ContextCompat.getColorStateList(this, R.color.colorPrimaryDark));
        tabLayoutDashboard.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent));

        tabLayoutDashboard.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPagerDashboard));
        viewPagerDashboard.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutDashboard));

        //onChangeTab Toolbar Title
        tabLayoutDashboard.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        viewPagerDashboard.setCurrentItem(0);
                        toolbarDashboard.setTitle("Contacts");
                        break;
                    case 1:
                        viewPagerDashboard.setCurrentItem(1);
                        toolbarDashboard.setTitle("Reminders");
                        break;
                    default:
                        viewPagerDashboard.setCurrentItem(tab.getPosition());
                        toolbarDashboard.setTitle("F22 Labs");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


}
