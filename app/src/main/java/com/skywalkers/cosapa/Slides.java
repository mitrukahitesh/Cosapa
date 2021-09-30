package com.skywalkers.cosapa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import com.skywalkers.cosapa.fragments.onboarding.slides.StepOneFragment;
import com.skywalkers.cosapa.fragments.onboarding.slides.StepThreeFragment;
import com.skywalkers.cosapa.fragments.onboarding.slides.StepTwoFragment;

import java.util.ArrayList;
import java.util.List;

public class Slides extends AppCompatActivity {



    final Fragment frag1 = new StepOneFragment();
    final Fragment frag2= new StepTwoFragment();
    final Fragment frag3 = new StepThreeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);


        // Initialize ViewPager view
        ViewPager viewPager = findViewById(R.id.viewPagerOnBoarding);
        // create ViewPager adapter
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add All Fragments to ViewPager
        viewPagerAdapter.addFragment(frag1);
        viewPagerAdapter.addFragment(frag2);
        viewPagerAdapter.addFragment(frag3);




        // Set Adapter for ViewPager
        viewPager.setAdapter(viewPagerAdapter);

        // Setup dot's indicator
        TabLayout tabLayout = findViewById(R.id.tabLayoutIndicator);
        tabLayout.setupWithViewPager(viewPager);
    }

    // ViewPager Adapter class
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        @Override
        public Fragment getItem(int i) {
            return mList.get(i);
        }
        @Override
        public int getCount() {
            return mList.size();
        }
        public void addFragment(Fragment fragment) {
            mList.add(fragment);
        }
    }
}
