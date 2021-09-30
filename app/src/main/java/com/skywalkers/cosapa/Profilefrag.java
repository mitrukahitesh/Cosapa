package com.skywalkers.cosapa;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Profilefrag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private CardView profcard1,profcard2;

    public Profilefrag() {
        // Required empty public constructor
    }


    public static Profilefrag newInstance(String param1, String param2) {
        Profilefrag fragment = new Profilefrag();
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
        View view = inflater.inflate(R.layout.fragment_profilefrag, container, false);
        profcard1= view.findViewById(R.id.abtcard);
        profcard1.setBackgroundResource(R.drawable.dashed_border);
        profcard2=view.findViewById(R.id.acccard);
        profcard2.setBackgroundResource(R.drawable.dashed_border);
        return  view;
    }
}