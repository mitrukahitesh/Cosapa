package com.skywalkers.cosapa.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.adapters.PostAdapter;
import com.skywalkers.cosapa.models.Post;


public class FeedFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CardView card1, card2;
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private FloatingActionButton fab;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        card1 = view.findViewById(R.id.card1);
        card2 = view.findViewById(R.id.card2);
        recyclerView = view.findViewById(R.id.recycler);
        adapter = new PostAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Post post = new Post();
            post.setTitle("Oxygen cylinder in Punjab");
            post.setUid(FirebaseAuth.getInstance().getUid());
            post.setPosition("Health worker");
            post.setName("Ramesh Kumar");
            post.setText("Hello ! Guy’s my son was infected by covid.\n" +
                    "His health was so serious,aurgent requirement\n" +
                    "of oxygen cylinder");
            post.setReactions(120);
            post.setViews(680);
            post.setTime(System.currentTimeMillis());
            FirebaseFirestore.getInstance().collection("posts").add(post).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        Log.i("Cosapa", "successful");
                    } else {
                        Log.i("Cosapa", "get failed with ", task.getException());
                    }
                }
            });
            recyclerView.setAdapter(new PostAdapter(getContext()));
        });
        return view;
    }
}