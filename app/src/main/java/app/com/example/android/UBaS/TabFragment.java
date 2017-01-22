package app.com.example.android.UBaS;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import buyBorrowExchange.Fragment_Borrow;
import buyBorrowExchange.Fragment_Buy;
import buyBorrowExchange.Fragment_Exchange;

/**
 * Created by MandipSilwal on 11/14/16.
 *
 * Some materials taken from: https://androidbelieve.com/navigation-drawer-with-swipe-tabs-using-design-support-library/
 */

public class TabFragment extends Fragment {

    public TabLayout tabLayout;
    public ViewPager viewPager;
    public int int_items = 3 ;
    public Fragment buy;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.tab_layout,null);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabsAdapter(getChildFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return rootView;

    }
    class TabsAdapter extends FragmentPagerAdapter {

        public TabsAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new Fragment_Buy();
                case 1 : return new Fragment_Borrow();
                case 2 : return new Fragment_Exchange();
            }
            return null;
        }
        @Override
        public int getCount() {

            return int_items;
        }
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "BUY";
                case 1 :
                    return "BORROW";
                case 2 :
                    return "EXCHANGE";
            }
            return null;
        }
    }
}
