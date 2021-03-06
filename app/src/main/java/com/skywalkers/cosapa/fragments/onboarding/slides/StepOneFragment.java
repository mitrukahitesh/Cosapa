package com.skywalkers.cosapa.fragments.onboarding.slides;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skywalkers.cosapa.LoginActivity;
import com.skywalkers.cosapa.R;

public class StepOneFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView skiptv1;
    private String mParam1;
    private String mParam2;

    public StepOneFragment() {
        // Required empty public constructor
    }

    public static StepOneFragment newInstance(String param1, String param2) {
        StepOneFragment fragment = new StepOneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_one, container, false);

         skiptv1 = view.findViewById(R.id.skiptv1);
         skiptv1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(getActivity(), LoginActivity.class);
                 startActivity(intent);
                 getActivity().finish();
             }
         });
        return view;
    }
}