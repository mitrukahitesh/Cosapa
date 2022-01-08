package com.skywalkers.cosapa.fragments.onboarding;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skywalkers.cosapa.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MoreDetails extends Fragment {

    private TextInputEditText name, email;
    private Button button;
    private Bundle bundle;
    private NavController controller;
    private FrameLayout frameLayout;

    public MoreDetails() {
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
        return inflater.inflate(R.layout.fragment_more_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        button = view.findViewById(R.id.continue_btn);
        frameLayout = view.findViewById(R.id.root);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText() == null || name.getText().toString().equals("")) {
                    name.requestFocus();
                    return;
                }
                if (email.getText() == null || email.getText().toString().equals("")) {
                    email.requestFocus();
                    return;
                }
                Map<String, Object> details = new HashMap<>();
                details.put("name", name.getText().toString());
                details.put("email", email.getText().toString());
                details.put("number", bundle.getString("number"));
                details.put("occupation", bundle.getString("occupation"));
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                        .update(details)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    controller.navigate(R.id.action_moreDetails_to_takePicture);
                                } else {
                                    Snackbar.make(frameLayout, "Please try again", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}