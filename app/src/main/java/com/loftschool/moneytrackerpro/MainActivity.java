package com.loftschool.moneytrackerpro;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        final ViewPager pages = (ViewPager) findViewById(R.id.pages);
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


            String name_page = (String) getPageTitle(position);
            if (position != 2) {
                final ItemsFragment fragment = new ItemsFragment();
                Bundle args = new Bundle();
                switch (position) {
                    case 0:
                        args.putString(ItemsFragment.ARG_TYPE, Item.TYPE_EXPENSE);
                        break;
                    case 1:
                        args.putString(ItemsFragment.ARG_TYPE, Item.TYPE_INCOME);
                        break;
                }
                fragment.setArguments(args);
                return fragment;
            }

            final BalanceFragment fragment = new BalanceFragment();

            return fragment;
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