package com.skywalkers.cosapa.fragments.healthDashboard;

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

import com.google.android.material.snackbar.Snackbar;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.models.doctorStatus.DoctorStatus;
import com.skywalkers.cosapa.utility.RetrofitAccessObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Confirmation extends Fragment {

    private String orderId;
    private ConstraintLayout root;

    public Confirmation() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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
                                // Details here in object "status"
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