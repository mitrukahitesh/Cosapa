package com.skywalkers.cosapa.fragments.profile;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skywalkers.cosapa.R;

public class ProfileDetails extends Fragment {

    private String mParam1;
    private String mParam2;
    private CardView profCard1, profCard2;

    public ProfileDetails() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profilefrag, container, false);
        profCard1 = view.findViewById(R.id.abtcard);
        profCard1.setBackgroundResource(R.drawable.dashed_border);
        profCard2 = view.findViewById(R.id.acccard);
        profCard2.setBackgroundResource(R.drawable.dashed_border);
        return view;
    }
}