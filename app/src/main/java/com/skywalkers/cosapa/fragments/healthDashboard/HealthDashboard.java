package com.skywalkers.cosapa.fragments.healthDashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.fragments.profile.ProfileDetails;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HealthDashboard extends Fragment {

    private CardView callNow;
    private ConstraintLayout heartbeat, o2, temperature;
    private NavController controller;
    private String heartbeatIns = "Please wear the device correctly\nPress \"Start now\" button\nTake deep breaths\nPress \"Finish\" after countdown";
    private String oxygenIns = "Please wear the device correctly\nPress \"Start now\" button\nTake deep breaths\nPress \"Finish\" after countdown";
    private String temperatureIns = "Please wear the device correctly\nPress \"Start now\" button\nTake deep breaths\nPress \"Finish\" after countdown";
    public static final String _2 = "heart";
    public static final String _3 = "Spo2";
    public static final String _7 = "temp";
    public static final String MEASUREMENTS = "measurements";
    private TextView hbCount, tempCount, oxyLevel, name1, name2, calories;
    private CircleImageView dp;

    public HealthDashboard() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_health_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
        callNow = view.findViewById(R.id.call_now);
        heartbeat = view.findViewById(R.id.heartbeat);
        o2 = view.findViewById(R.id.o2);
        temperature = view.findViewById(R.id.temperature);
        hbCount = view.findViewById(R.id.hbcount);
        tempCount = view.findViewById(R.id.tempcount);
        oxyLevel = view.findViewById(R.id.oxylevel);
        dp = view.findViewById(R.id.profile_image);
        name1 = view.findViewById(R.id.name1);
        name2 = view.findViewById(R.id.name2);
        calories = view.findViewById(R.id.cal_count);
        fetchData();
        callNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_healthDashboard_to_doctors3);
            }
        });
        heartbeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Test test = new Test();
                test.setName("Heartbeat Measurement");
                test.setId(2);
                test.setInstruction(heartbeatIns);
                bundle.putParcelable("test", test);
                controller.navigate(R.id.action_healthDashboard_to_measurement, bundle);
            }
        });
        o2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Test test = new Test();
                test.setName("Oxygen Level Measurement");
                test.setId(3);
                test.setInstruction(oxygenIns);
                bundle.putParcelable("test", test);
                controller.navigate(R.id.action_healthDashboard_to_measurement, bundle);
            }
        });
        temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Test test = new Test();
                test.setName("Temperature Measurement");
                test.setId(7);
                test.setInstruction(temperatureIns);
                bundle.putParcelable("test", test);
                controller.navigate(R.id.action_healthDashboard_to_measurement, bundle);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = requireContext().getSharedPreferences(MEASUREMENTS, Context.MODE_PRIVATE);
        hbCount.setText(String.format("%s bpm", preferences.getString(_2, "--")));
        tempCount.setText(String.format("%s °C", preferences.getString(_7, "--")));
        oxyLevel.setText(String.format("%s", preferences.getString(_3, "--") + "%"));
    }

    private void fetchData() {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            calories.setText(String.format(Locale.getDefault(), "%.0f Cal", (Double) task.getResult().get("calories")));
                        }
                    }
                });
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getData() != null && task.getResult().exists()) {
                            Map<String, Object> map = task.getResult().getData();
                            name1.setText((String) map.get("name"));
                            name2.setText((String) map.get("name"));
                            // location.setText("");
                        } else {
                            Log.i("Cosapa: HealthDashboard", task.getException() == null ? "Some error occurred" : task.getException().getMessage());
                        }
                    }
                });
        FirebaseStorage.getInstance()
                .getReference()
                .child("profile_pic")
                .child(FirebaseAuth.getInstance().getUid())
                .getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Glide.with(HealthDashboard.this).load(task.getResult()).fitCenter().into(dp);
                        } else {
                            Log.i("Cosapa: HealthDashboard", task.getException() == null ? "Some error occurred" : task.getException().getMessage());
                        }
                    }
                });
    }

    public static class Test implements Parcelable {
        int id;
        String name, instruction;

        protected Test(Parcel in) {
            id = in.readInt();
            name = in.readString();
            instruction = in.readString();
        }

        public Test() {
        }

        public static final Creator<Test> CREATOR = new Creator<Test>() {
            @Override
            public Test createFromParcel(Parcel in) {
                return new Test(in);
            }

            @Override
            public Test[] newArray(int size) {
                return new Test[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInstruction() {
            return instruction;
        }

        public void setInstruction(String instruction) {
            this.instruction = instruction;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(instruction);
        }
    }
}