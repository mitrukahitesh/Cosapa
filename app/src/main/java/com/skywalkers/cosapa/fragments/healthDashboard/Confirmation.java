package com.skywalkers.cosapa.fragments.healthDashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.adapters.DoctorAdapter;
import com.skywalkers.cosapa.models.doctorStatus.DoctorStatus;
import com.skywalkers.cosapa.models.doctorStatus.Location;
import com.skywalkers.cosapa.utility.RetrofitAccessObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Confirmation extends Fragment {

    private String orderId;
    private ConstraintLayout root;
    private DoctorAdapter.Doctor doctor;
    private TextView confirmdoctorname, confirmcategory, confirmlocation, confirmtiming;
    private Button downloadreceiptbtn, callnowbtn;

    public Confirmation() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getParcelable("doctor") != null) {
                doctor = getArguments().getParcelable("doctor");
            }
            orderId = getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirmation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.healthDashboard, true).build();
                Navigation.findNavController(view).navigate(R.id.action_confirmation_to_healthDashboard, null, options);
            }
        });
        root = view.findViewById(R.id.root);
        confirmdoctorname = view.findViewById(R.id.confirm_docname);
        confirmcategory = view.findViewById(R.id.confirmdoccattv);
        confirmtiming = view.findViewById(R.id.confirmdoctimetv);
        confirmlocation = view.findViewById(R.id.confirmdocloctv);
        getOrderStatus();
    }

    private void getOrderStatus() {
        RetrofitAccessObject
                .getRetrofitAccessObject()
                .checkDoctorBookingStatus(RetrofitAccessObject.getBodyDoctorStatus())
                .enqueue(new Callback<ArrayList<DoctorStatus>>() {
                    @Override
                    public void onResponse(Call<ArrayList<DoctorStatus>> call, Response<ArrayList<DoctorStatus>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                DoctorStatus status = response.body().get(0);
                                confirmdoctorname.setText(doctor.getName());
                                confirmlocation.setText(doctor.getClinic());
                                confirmlocation.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Uri gmmIntentUri = Uri.parse("geo:" + doctor.getLatLon() + "?q=" + doctor.getClinic());
                                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                        mapIntent.setPackage("com.google.android.apps.maps");
                                        requireContext().startActivity(mapIntent);
                                    }
                                });
                                String startTime = status.getMessage().getOrder().getFulfillment().getEnd().getTime().getRange().getStart().substring(11, 16);
                                String endTime = status.getMessage().getOrder().getFulfillment().getEnd().getTime().getRange().getEnd().substring(11, 16);
                                confirmtiming.setText(String.format("%s - %s", startTime, endTime));


                            } else {
                                showErrorMessage();
                            }
                        } else {
                            showErrorMessage();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<DoctorStatus>> call, Throwable t) {

                    }
                });
    }

    private void showErrorMessage() {
        Snackbar.make(root, "Some error occurred", Snackbar.LENGTH_SHORT).show();
    }
}