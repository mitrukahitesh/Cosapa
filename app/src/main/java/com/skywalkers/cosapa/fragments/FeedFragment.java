package com.skywalkers.cosapa.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.adapters.ChallengeAdapter;
import com.skywalkers.cosapa.adapters.PostAdapter;
import com.skywalkers.cosapa.models.Post;


public class FeedFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView, recyclerChallenge;
    private PostAdapter adapter;
    private ChallengeAdapter challengeAdapter;
    private FloatingActionButton fab;
    private LinearLayout postOption, ll_c, ll_p;

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
        recyclerView = view.findViewById(R.id.recycler);
        adapter = new PostAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerChallenge = view.findViewById(R.id.recycler_challenge);
        challengeAdapter = new ChallengeAdapter(getContext());
        recyclerChallenge.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerChallenge.setAdapter(challengeAdapter);
        fab = view.findViewById(R.id.fab);
        postOption = view.findViewById(R.id.post_option);
        ll_c = view.findViewById(R.id.ll_challenge);
        ll_p = view.findViewById(R.id.ll_post);
        ll_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_feedFragment_to_addChallenge);
            }
        });
        ll_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_feedFragment_to_addPost);
            }
        });
        fab.setOnClickListener(v -> {
            if (postOption.getVisibility() == View.GONE)
                postOption.setVisibility(View.VISIBLE);
            else
                postOption.setVisibility(View.GONE);
        });
        return view;
    }
}