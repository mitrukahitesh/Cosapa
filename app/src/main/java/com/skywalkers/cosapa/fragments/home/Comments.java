package com.skywalkers.cosapa.fragments.home;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.skywalkers.cosapa.MainActivity;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.adapters.CommentAdapter;
import com.skywalkers.cosapa.models.Comment;
import com.skywalkers.cosapa.utility.RewardPopup;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comments extends Fragment {

    private TextView title, name;
    private CircleImageView dp;
    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private Bundle bundle;
    private List<Comment> comments = new ArrayList<>();
    private EditText comment;
    private ImageButton send;

    public Comments() {
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
        return inflater.inflate(R.layout.fragment_comments, container, false);
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
        recyclerView = view.findViewById(R.id.recycler);
        title = view.findViewById(R.id.title);
        name = view.findViewById(R.id.name);
        dp = view.findViewById(R.id.dp);
        comment = view.findViewById(R.id.message);
        send = view.findViewById(R.id.send);
        title.setText(bundle.getString("title"));
        name.setText(bundle.getString("name"));
        if (!bundle.getString("dp").equals(""))
            Glide.with(requireContext()).load(Uri.parse(bundle.getString("dp"))).into(dp);
        adapter = new CommentAdapter(requireContext(), comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        FirebaseFirestore.getInstance()
                .collection("COMMENTS")
                .document(bundle.getString("id"))
                .collection("COMMENTS")
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null)
                            return;
                        for (DocumentChange change : value.getDocumentChanges()) {
                            switch (change.getType()) {
                                case ADDED:
                                    comments.add(change.getDocument().toObject(Comment.class));
                                    adapter.notifyItemInserted(comments.size() - 1);
                                    recyclerView.scrollToPosition(comments.size() - 1);
                            }
                        }
                    }
                });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comment.getText().toString().trim().equals(""))
                    return;
                Comment c = new Comment();
                c.setMessage(comment.getText().toString().trim());
                c.setTime(System.currentTimeMillis());
                c.setSender(FirebaseAuth.getInstance().getUid());
                c.setName(MainActivity.NAME);
                comment.setText("");
                FirebaseFirestore.getInstance()
                        .collection("COMMENTS")
                        .document(bundle.getString("id"))
                        .collection("COMMENTS")
                        .add(c);
                if (MainActivity.POSITION.equalsIgnoreCase("General"))
                    return;
                RewardPopup rewardPopup = new RewardPopup(requireContext(), "15");
                rewardPopup.showDialog();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        changeStatusBarColor(R.color.purple_500);
    }

    @Override
    public void onPause() {
        super.onPause();
        changeStatusBarColor(R.color.white);
    }

    private void changeStatusBarColor(int color) {
        Window window = requireActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(color, null));
    }
}