package com.skywalkers.cosapa.fragments.onboarding;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.skywalkers.cosapa.R;

public class SelectOccupation extends Fragment implements View.OnClickListener {

    private Bundle bundle;
    private NavController controller;
    private View view;

    public SelectOccupation() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundle = getArguments();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_occupation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
        this.view = view;
        view.findViewById(R.id.general).setOnClickListener(this);
        view.findViewById(R.id.healthworker).setOnClickListener(this);
        view.findViewById(R.id.doctors).setOnClickListener(this);
        view.findViewById(R.id.volunteer).setOnClickListener(this);
        view.findViewById(R.id.ngo).setOnClickListener(this);
        view.findViewById(R.id.fitnessexperts).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        boolean cardClicked = false;
        String occupation = "";
        if (id == view.findViewById(R.id.general).getId()) {
            cardClicked = true;
            occupation = ((TextView) view.findViewById(R.id.gentv)).getText().toString();
        } else if (id == view.findViewById(R.id.healthworker).getId()) {
            cardClicked = true;
            occupation = ((TextView) view.findViewById(R.id.healthproftv)).getText().toString();
        } else if (id == view.findViewById(R.id.doctors).getId()) {
            cardClicked = true;
            occupation = ((TextView) view.findViewById(R.id.doctv)).getText().toString();
        } else if (id == view.findViewById(R.id.volunteer).getId()) {
            cardClicked = true;
            occupation = ((TextView) view.findViewById(R.id.voltv)).getText().toString();
        } else if (id == view.findViewById(R.id.ngo).getId()) {
            cardClicked = true;
            occupation = ((TextView) view.findViewById(R.id.ngotv)).getText().toString();
        } else if (id == view.findViewById(R.id.fitnessexperts).getId()) {
            cardClicked = true;
            occupation = ((TextView) view.findViewById(R.id.fitness_tv)).getText().toString();
        } else {

        }
        if (cardClicked) {
            bundle.putString("occupation", occupation);
            controller.navigate(R.id.action_selectOccupation_to_moreDetails, bundle);
        }
    }
}