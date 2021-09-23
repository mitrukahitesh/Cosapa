package com.skywalkers.cosapa.fragments.home;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.models.Challenge;

public class CompleteChallenge extends Fragment {

    private TextView steps, completed;
    private Challenge challenge;

    public CompleteChallenge() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().getParcelable("challenge") != null) {
            challenge = getArguments().getParcelable("challenge");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_complete_challenge, container, false);
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
        completed = view.findViewById(R.id.complete);
        steps = view.findViewById(R.id.steps);
        steps.setText(challenge.getText());
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("challenge", challenge);
                Navigation.findNavController(view).navigate(R.id.action_completeChallenge_to_challengeRepetition, bundle);
            }
        });
    }
}