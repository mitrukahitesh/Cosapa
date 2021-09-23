package com.skywalkers.cosapa.fragments.home;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.models.Challenge;

import java.util.HashMap;
import java.util.Map;

public class ChallengeRepetition extends Fragment {

    private Challenge challenge;
    private TextView save;
    private EditText count;
    private ConstraintLayout root;
    private NavController controller;

    public ChallengeRepetition() {
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
        return inflater.inflate(R.layout.fragment_challenge_repetition, container, false);
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
        save = view.findViewById(R.id.save);
        count = view.findViewById(R.id.count);
        root = view.findViewById(R.id.root);
        controller = Navigation.findNavController(view);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count.getText() != null && count.getText().toString().length() > 0 && Integer.parseInt(count.getText().toString()) >= 1) {
                    int c = Integer.parseInt(count.getText().toString());
                    saveChallengeCompletion(FirebaseAuth.getInstance().getUid(), c);
                } else {
                    count.requestFocus();
                    Snackbar.make(root, "Invalid count", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveChallengeCompletion(String uid, int c) {
        Map<String, Object> map = new HashMap<>();
        map.put("count", c);
        FirebaseFirestore.getInstance().collection("users").document(uid).collection("challenges_completed").document(challenge.getId()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    controller.navigate(R.id.action_challengeRepetition_to_challengeCompleted);
                } else {
                    Snackbar.make(root, "Request failed", Snackbar.LENGTH_SHORT).show();
                    Log.i("Cosapa: Challange Repetition", task.getException().getMessage());
                }
            }
        });
    }
}