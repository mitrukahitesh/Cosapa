package com.skywalkers.cosapa.fragments.home;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.models.Challenge;

public class ChallengeCompleted extends Fragment {

    private TextView save, countDisplay;
    private Challenge challenge;
    private String count;

    public ChallengeCompleted() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            challenge = (Challenge) getArguments().getParcelable("challenge");
            count = getArguments().getString("count");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenge_completed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateToHome(Navigation.findNavController(view));
            }
        });
        save = view.findViewById(R.id.save);
        countDisplay = view.findViewById(R.id.count_display);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHome(Navigation.findNavController(view));
            }
        });
        countDisplay.setText(String.format("Total %s: %s", challenge.getName(), count));
    }

    private void navigateToHome(NavController controller) {
        NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.feedFragment, true).build();
        controller.navigate(R.id.action_challengeCompleted_to_feedFragment, new Bundle(), options);
    }
}