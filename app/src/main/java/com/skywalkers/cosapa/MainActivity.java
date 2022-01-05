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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skywalkers.cosapa.fragments.rootfragments.DoctorsFragment;
import com.skywalkers.cosapa.fragments.rootfragments.HealthDashboardFragment;
import com.skywalkers.cosapa.fragments.rootfragments.HomeFragment;
import com.skywalkers.cosapa.fragments.rootfragments.MapFragment;
import com.skywalkers.cosapa.utility.SocketObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new MapFragment();
    final Fragment fragment3 = new HealthDashboardFragment();
    final Fragment fragment4 = new DoctorsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    private Fragment active = fragment1;
    private Context contextOfApplication;
    public static String NAME = "Username";
    public static String POSITION = "Cosapa User";
    public static String PHONE, EMAIL, STATUS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            SocketObject.getSocket();
        } catch (URISyntaxException e) {
            Log.i("cosapa", "Socket error in main activity");
            e.printStackTrace();
            finish();
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setBackground(null);

        contextOfApplication = getApplicationContext();

        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").commit();
        active = fragment1;

        getPersonalDetails();
    }

    private void getPersonalDetails() {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getData() != null && task.getResult().exists()) {
                            Map<String, Object> map = task.getResult().getData();
                            NAME = (String) map.get("name");
                            PHONE = (String) map.get("number");
                            EMAIL = (String) map.get("email");
                            POSITION = (String) map.get("occupation");
                            STATUS = String.format("I am %s", (String) map.get("name"));
                            // location.setText("");
                        }
                    }
                });
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
