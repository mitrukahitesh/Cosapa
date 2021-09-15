package com.skywalkers.cosapa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skywalkers.cosapa.fragments.Doctors;
import com.skywalkers.cosapa.fragments.FeedFragment;
import com.skywalkers.cosapa.fragments.HealthDashboard;
import com.skywalkers.cosapa.fragments.NearbyFragment;

public class MainActivity extends AppCompatActivity {

    /*final FeedFragment fragment1 =new FeedFragment();
    final NearbyFragment fragment2 = new NearbyFragment();
    final Doctors fragment3 = new Doctors();
    final HealthDashboard fragment4= new HealthDashboard();
    final FragmentManager fm = getSupportFragmentManager();
    FeedFragment active = fragment1;*/

    final Fragment fragment1 = new FeedFragment();
    final Fragment fragment2 = new NearbyFragment();
    final Fragment fragment3 = new HealthDashboard();
    final Fragment fragment4 = new Doctors();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    private Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setBackground(null);

        contextOfApplication = getApplicationContext();

        fm.beginTransaction().add(R.id.main_container,fragment4,"4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container,fragment1, "1").commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:

                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;

                case R.id.nearby:

                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;

                case R.id.health_dashboard:

                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;

                case R.id.profile2:

                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment2;
                    return true;



            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }
}
