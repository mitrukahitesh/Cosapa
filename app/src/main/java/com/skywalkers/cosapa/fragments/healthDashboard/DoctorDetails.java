package com.skywalkers.cosapa.fragments.healthDashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.adapters.DoctorAdapter;

import java.util.ArrayList;
import java.util.List;

public class DoctorDetails extends Fragment {

    private DoctorAdapter.Doctor doctor;
    private RadioGroup radioGroup;
    private TextView docPrice, name, category, clinic;
    private final List<Pair<Pair<String, String>, String>> prices = new ArrayList<>();
    private String selectedId = "";
    private String selectedSlot = "";

    public DoctorDetails() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            doctor = getArguments().getParcelable("doctor");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_details, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        selectedId = "";
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
        docPrice = view.findViewById(R.id.doc_price);
        name = view.findViewById(R.id.docheader_docname);
        category = view.findViewById(R.id.docheader_doccat);
        clinic = view.findViewById(R.id.loc_name);
        name.setText(doctor.getName());
        category.setText(doctor.getCategory());
        clinic.setText(doctor.getClinic());
        clinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:" + doctor.getLatLon() + "?q=" + doctor.getClinic());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        Button book = view.findViewById(R.id.book);
        radioGroup = view.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                --checkedId;
                docPrice.setVisibility(View.VISIBLE);
                docPrice.setText(prices.get(checkedId).second);
                selectedId = prices.get(checkedId).first.first;
                selectedSlot = prices.get(checkedId).first.second;
            }
        });
        setRadioButtons();
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedId.equals("")) {
                    Snackbar.make(view.findViewById(R.id.root2), "Please select a slot", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putParcelable("doctor", doctor);
                bundle.putString("id", selectedId);
                bundle.putString("slot", selectedSlot);
                Navigation.findNavController(view).navigate(R.id.action_doctorDetails_to_checkout, bundle);
            }
        });
    }

    private void setRadioButtons() {
        radioGroup.removeAllViews();
        prices.clear();
        int i = 0;
        for (DoctorAdapter.Timing timing : doctor.getTimings()) {
            RadioButton button = new RadioButton(getContext());
            button.setText(String.format("%s - %s", timing.getStart(), timing.getEnd()));
            radioGroup.addView(button, i, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            prices.add(new Pair<>(new Pair<>(timing.getId(), String.format("%s - %s", timing.getStart(), timing.getEnd())), timing.getCost() + " " + timing.getCurrency()));
            ++i;
        }
    }
}