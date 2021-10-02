package com.skywalkers.cosapa.fragments.healthDashboard;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skywalkers.cosapa.R;

public class HealthDashboard extends Fragment {

    private CardView callNow;
    private ConstraintLayout heartbeat, o2, temperature;
    private NavController controller;

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
                test.setName("Beats per minute");
                test.setId(2);
                bundle.putParcelable("test", test);
                controller.navigate(R.id.action_healthDashboard_to_measurement, bundle);
            }
        });
        o2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Test test = new Test();
                test.setName("SpO2");
                test.setId(3);
                bundle.putParcelable("test", test);
                controller.navigate(R.id.action_healthDashboard_to_measurement, bundle);
            }
        });
        temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Test test = new Test();
                test.setName("Temperature");
                test.setId(7);
                bundle.putParcelable("test", test);
                controller.navigate(R.id.action_healthDashboard_to_measurement, bundle);
            }
        });
    }

    public static class Test implements Parcelable {
        int id;
        String name;

        protected Test(Parcel in) {
            id = in.readInt();
            name = in.readString();
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
        }
    }
}