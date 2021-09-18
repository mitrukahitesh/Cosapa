package com.skywalkers.cosapa;


import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.skywalkers.cosapa.fragments.Doctors;
import com.skywalkers.cosapa.fragments.FeedFragment;
import com.skywalkers.cosapa.fragments.HealthDashboard;
import com.skywalkers.cosapa.fragments.MoreDetails;
import com.skywalkers.cosapa.fragments.NearbyFragment;
import com.skywalkers.cosapa.fragments.OTPFrag;
import com.skywalkers.cosapa.fragments.RegistrationFrag;

public class LoginActivity extends AppCompatActivity {

    final Fragment registerfrag = new RegistrationFrag();
    final Fragment otpfrag = new OTPFrag();
    final Fragment moredetfrag = new MoreDetails();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = registerfrag;
    private Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        contextOfApplication = getApplicationContext();

        fm.beginTransaction().add(R.id.main_container2, moredetfrag, "3").hide(moredetfrag).commit();
        fm.beginTransaction().add(R.id.main_container2, otpfrag, "2").hide(otpfrag).commit();
        fm.beginTransaction().add(R.id.main_container2,registerfrag, "1").commit();
    }
}