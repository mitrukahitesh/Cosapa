package com.skywalkers.cosapa.fragments.healthDashboard;

import static com.skywalkers.cosapa.fragments.healthDashboard.HealthDashboard.MEASUREMENTS;
import static com.skywalkers.cosapa.fragments.healthDashboard.HealthDashboard._2;
import static com.skywalkers.cosapa.fragments.healthDashboard.HealthDashboard._3;
import static com.skywalkers.cosapa.fragments.healthDashboard.HealthDashboard._7;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.skywalkers.cosapa.R;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Measurement extends Fragment {

    private HealthDashboard.Test test;
    private TextView name, instruction, countdown;
    private Button start, finish;
    private Map<Integer, String> keyNames = new HashMap<>();
    private boolean measuring = false;
    private NavController controller;
    private FrameLayout root;

    public Measurement() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            test = getArguments().getParcelable("test");
        }
        keyNames.put(3, _3);
        keyNames.put(7, _7);
        keyNames.put(2, _2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_measurement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).popBackStack();
            }
        });
        root = view.findViewById(R.id.root);
        name = view.findViewById(R.id.name);
        instruction = view.findViewById(R.id.instruction_tv);
        name.setText(test.getName());
        instruction.setText(test.getInstruction());
        countdown = view.findViewById(R.id.coundown_timertv);
        start = view.findViewById(R.id.start);
        finish = view.findViewById(R.id.finish);
        controller = Navigation.findNavController(view);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                measuring = true;
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("ID")
                        .setValue(test.getId())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Snackbar.make(root, "Measurement started..", Snackbar.LENGTH_SHORT).show();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (int i = 0; i < 60 && measuring; ++i) {
                                                try {
                                                    final int x = i;
                                                    Thread.sleep(1000);
                                                    if (measuring)
                                                        requireActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                countdown.setText(String.format(Locale.getDefault(), "%d", 60 - x - 1));
                                                            }
                                                        });
                                                    else {
                                                        break;
                                                    }
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }).start();
                                }
                            }
                        });
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResult();
            }
        });
    }

    private void getResult() {
        measuring = false;
        countdown.setText("Done!");
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Objects.requireNonNull(keyNames.get(test.getId())))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getValue() != null) {
                            Log.i("Cosapa: Measurement", task.getResult().getValue().toString());
                            SharedPreferences preferences = requireContext().getSharedPreferences(MEASUREMENTS, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(keyNames.get(test.getId()), task.getResult().getValue().toString());
                            editor.commit();
                            FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("ID")
                                    .setValue(0);
                            NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.healthDashboard, true).build();
                            controller.navigate(R.id.action_measurement_to_healthDashboard, new Bundle(), navOptions);
                        }
                    }
                });
    }
}