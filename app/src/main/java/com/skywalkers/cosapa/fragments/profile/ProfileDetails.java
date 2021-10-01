package com.skywalkers.cosapa.fragments.profile;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.fragments.onboarding.TakePicture;

import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDetails extends Fragment {

    private TextView name, status, occupation, location, phone, email;
    private FrameLayout root;
    private CircleImageView dp;

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
        return inflater.inflate(R.layout.fragment_profilefrag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = view.findViewById(R.id.root);
        name = view.findViewById(R.id.name);
        status = view.findViewById(R.id.status);
        occupation = view.findViewById(R.id.occupation);
        location = view.findViewById(R.id.location);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        dp = view.findViewById(R.id.profile_image);
        view.findViewById(R.id.card1).setBackgroundResource(R.drawable.dashed_border);
        view.findViewById(R.id.card2).setBackgroundResource(R.drawable.dashed_border);
        fetchData();
    }

    private void fetchData() {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getData() != null && task.getResult().exists()) {
                            Map<String, Object> map = task.getResult().getData();
                            name.setText((String) map.get("name"));
                            phone.setText((String) map.get("number"));
                            email.setText((String) map.get("email"));
                            occupation.setText((String) map.get("occupation"));
                            status.setText(String.format("I am %s", (String) map.get("name")));
                            // location.setText("");
                        } else {
                            Snackbar.make(root, "Could not load profile..", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
        FirebaseStorage.getInstance()
                .getReference()
                .child("profile_pic")
                .child(FirebaseAuth.getInstance().getUid())
                .getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Glide.with(ProfileDetails.this).load(task.getResult()).fitCenter().into(dp);
                        } else {
                            Log.i("Cosapa: ProfileDetails", task.getException() == null ? "Some error occurred" : task.getException().getMessage());
                        }
                    }
                });
    }
}