package com.loftschool.moneytrackerpro;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        final ViewPager pages = (ViewPager) findViewById(R.id.pages);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        pages.setAdapter(new MainPagerAdapter());
        tabs.setupWithViewPager(pages);
    }


    private class MainPagerAdapter extends FragmentPagerAdapter {
        private final String[] titles;

        MainPagerAdapter() {
            super(getSupportFragmentManager());
            titles = getResources().getStringArray(R.array.main_pager_titles);
        }

        @Override
        public Fragment getItem(int position) {
            final ItemsFragment fragment = new ItemsFragment();
            Bundle args = new Bundle();
            switch (position) {
                case 0:
                    args.putString(ItemsFragment.ARG_TYPE, Item.TYPE_EXPENSE);
                    fragment.setArguments(args);
                    return fragment;
                case 1:
                    args.putString(ItemsFragment.ARG_TYPE, Item.TYPE_INCOME);
                    fragment.setArguments(args);
                    return fragment;
                case 2:
                    return new BalanceFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
}