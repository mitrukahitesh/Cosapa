package com.skywalkers.cosapa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skywalkers.cosapa.models.doctor.Doctor;
import com.skywalkers.cosapa.rootfragments.DoctorsFragment;
import com.skywalkers.cosapa.rootfragments.HealthDashboardFragment;
import com.skywalkers.cosapa.rootfragments.HomeFragment;
import com.skywalkers.cosapa.rootfragments.MapFragment;
import com.skywalkers.cosapa.utility.RetrofitAccessObject;
import com.skywalkers.cosapa.utility.RetrofitCustom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new MapFragment();
    final Fragment fragment3 = new HealthDashboardFragment();
    final Fragment fragment4 = new DoctorsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    private Fragment active = fragment1;
    private Context contextOfApplication;
    public static String NAME = "Ramesh Kumar";
    public static String POSITION = "Health worker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setBackground(null);

        contextOfApplication = getApplicationContext();

        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").commit();
        active = fragment1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Map<String, Boolean> map = new HashMap<>();
        map.put("status", true);
        FirebaseFirestore.getInstance().collection("online").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).set(map);
//        RetrofitAccessObject.getRetrofitAccessObject().getDoctorsByNameOfSymptom(RetrofitAccessObject.getBodyDoctor()).enqueue(new Callback<ArrayList<Doctor>>() {
//            @Override
//            public void onResponse(Call<ArrayList<Doctor>> call, Response<ArrayList<Doctor>> response) {
//                Log.i("Cosapa", response.body().get(0).getMessage().getCatalog().getBppProviders().get(0).getDescriptor().getName());
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Doctor>> call, Throwable t) {
//
//            }
//        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Map<String, Boolean> map = new HashMap<>();
        map.put("status", false);
        FirebaseFirestore.getInstance().collection("online").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).set(map);
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
                    active = fragment4;
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
