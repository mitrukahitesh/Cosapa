package com.skywalkers.cosapa.fragments.healthDashboard;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.adapters.DoctorAdapter;


public class Doctors extends Fragment {

    private RecyclerView recyclerView;
    private NavController controller;
    private CardView cardView1, cardView2, cardView3, cardView4;
    private TextView cardText1, cardText2, cardText3, cardText4;
    public static final String ALL = "";
    public static final String DERMATOLOGY = "DERMATOLOGY";
    public static final String FAM_MED = "FAMILY MEDICINE";
    public static final String ENT = "ENT";
    private ConstraintLayout root;

    public Doctors() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctors, container, false);
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
        controller = Navigation.findNavController(view);
        cardView1 = view.findViewById(R.id.card1);
        cardView2 = view.findViewById(R.id.card2);
        cardView3 = view.findViewById(R.id.card3);
        cardView4 = view.findViewById(R.id.card4);
        cardText1 = view.findViewById(R.id.cardText1);
        cardText2 = view.findViewById(R.id.cardText2);
        cardText3 = view.findViewById(R.id.cardText3);
        cardText4 = view.findViewById(R.id.cardText4);
        setCardListeners();
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        root = view.findViewById(R.id.root);
        recyclerView.setAdapter(new DoctorAdapter(getContext(), ALL, root, controller));
    }

    private void setCardListeners() {
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultFilterColors();
                if (getActivity() == null)
                    return;
                cardView1.setCardBackgroundColor(getActivity().getColor(R.color.col2));
                cardText1.setTextColor(getActivity().getColor(R.color.white));
                recyclerView.setAdapter(new DoctorAdapter(getContext(), ALL, root, controller));
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultFilterColors();
                if (getActivity() == null)
                    return;
                cardView2.setCardBackgroundColor(getActivity().getColor(R.color.col2));
                cardText2.setTextColor(getActivity().getColor(R.color.white));
                recyclerView.setAdapter(new DoctorAdapter(getContext(), DERMATOLOGY, root, controller));
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultFilterColors();
                if (getActivity() == null)
                    return;
                cardView3.setCardBackgroundColor(getActivity().getColor(R.color.col2));
                cardText3.setTextColor(getActivity().getColor(R.color.white));
                recyclerView.setAdapter(new DoctorAdapter(getContext(), FAM_MED, root, controller));
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultFilterColors();
                if (getActivity() == null)
                    return;
                cardView4.setCardBackgroundColor(getActivity().getColor(R.color.col2));
                cardText4.setTextColor(getActivity().getColor(R.color.white));
                recyclerView.setAdapter(new DoctorAdapter(getContext(), ENT, root, controller));
            }
        });
    }

    private void setDefaultFilterColors() {
        if (getActivity() == null)
            return;
        cardView1.setCardBackgroundColor(getActivity().getColor(R.color.white));
        cardView2.setCardBackgroundColor(getActivity().getColor(R.color.white));
        cardView3.setCardBackgroundColor(getActivity().getColor(R.color.white));
        cardView4.setCardBackgroundColor(getActivity().getColor(R.color.white));
        cardText1.setTextColor(getActivity().getColor(R.color.black));
        cardText2.setTextColor(getActivity().getColor(R.color.black));
        cardText3.setTextColor(getActivity().getColor(R.color.black));
        cardText4.setTextColor(getActivity().getColor(R.color.black));
    }
}