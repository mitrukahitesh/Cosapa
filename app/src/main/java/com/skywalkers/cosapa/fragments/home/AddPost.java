package com.skywalkers.cosapa.fragments.home;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skywalkers.cosapa.MainActivity;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.models.Post;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddPost extends Fragment {

    private TextView name, position, post;
    private CircleImageView dp;
    private EditText text, city, requirement;
    private NavController controller;

    public AddPost() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_post, container, false);
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
        name = view.findViewById(R.id.name);
        position = view.findViewById(R.id.position);
        dp = view.findViewById(R.id.dp);
        text = view.findViewById(R.id.text);
        city = view.findViewById(R.id.city);
        requirement = view.findViewById(R.id.requirement);
        post = view.findViewById(R.id.post);
        controller = Navigation.findNavController(view);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText() == null || city.getText() == null || requirement.getText() == null)
                    return;
                if (text.getText().toString().equals("")) {
                    text.requestFocus();
                    return;
                }
                if (city.getText().toString().equals("")) {
                    city.requestFocus();
                    return;
                }
                if (requirement.getText().toString().equals("")) {
                    requirement.requestFocus();
                    return;
                }
                String postText = text.getText().toString();
                String postCity = city.getText().toString();
                String postRequirement = requirement.getText().toString();
                addPost(postText.trim(), postCity.trim(), postRequirement.trim());
            }
        });
    }

    public void addPost(String text, String city, String req) {
        Post post = new Post();
        post.setTitle(req + " in " + city);
        post.setUid(FirebaseAuth.getInstance().getUid());
        post.setPosition(MainActivity.POSITION);
        post.setName(MainActivity.NAME);
        post.setText(text);
        post.setReactions(0);
        post.setViews(0);
        post.setTime(System.currentTimeMillis());
        FirebaseFirestore.getInstance().collection("posts").add(post).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Log.i("Cosapa", "successful");
                    NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.feedFragment, true).build();
                    controller.navigate(R.id.action_addPost_to_feedFragment, null, navOptions);
                } else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                    Log.i("Cosapa", "get failed with ", task.getException());
                }
            }
        });
    }
}