package com.skywalkers.cosapa.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.models.Challenge;
import com.skywalkers.cosapa.models.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.CustomVH> {

    private final Context context;
    private final NavController controller;
    private final List<Challenge> challenges = new ArrayList<>();
    private Long last = System.currentTimeMillis();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Set<Integer> updatedAt = new HashSet<>();
    private final Map<String, Long> completed = new HashMap();

    public ChallengeAdapter(Context context, View rootView) {
        this.context = context;
        this.controller = Navigation.findNavController(rootView);
        fetchCompleted();
        fetchData();
    }

    private void fetchCompleted() {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection("challenges_completed")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.i("Cosapa: Challenge Adapter", error.getLocalizedMessage());
                            return;
                        }
                        if (value == null)
                            return;
                        completed.clear();
                        for (DocumentSnapshot snapshot : value) {
                            completed.put(snapshot.getId(), (Long) snapshot.get("count"));
                        }
                    }
                });
    }

    @NonNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.challenge_view, parent, false));
    }

    public void fetchData() {
        CollectionReference reference = db.collection("challenges");
        Query query = reference.orderBy("time", Query.Direction.DESCENDING).whereLessThan("time", last).limit(20);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() == null)
                        return;
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        challenges.add(snapshot.toObject(Challenge.class));
                        challenges.get(challenges.size() - 1).setId(snapshot.getId());
                        getDpUrl(challenges.get(challenges.size() - 1), challenges.size() - 1);
                        notifyItemInserted(challenges.size() - 1);
                    }
                    if (!task.getResult().isEmpty())
                        last = challenges.get(challenges.size() - 1).getTime();
                    Log.i("Cosapa", "fetched " + task.getResult().size() + " challenges " + last);
                } else {
                    Log.i("Cosapa", "[challenge adapter] get failed with ", task.getException());
                }
            }
        });
    }

    private void getDpUrl(Challenge challenge, int pos) {
        FirebaseStorage.getInstance()
                .getReference()
                .child("profile_pic")
                .child(challenge.getUid())
                .getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            challenge.setDp(task.getResult().toString());
                            notifyItemChanged(pos);
                        }
                    }
                });
    }

    @Override
    public void onBindViewHolder(@NonNull CustomVH holder, int position) {
        holder.setView(challenges.get(position), position);
    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView image;
        private TextView done;
        private TextView title;
        private CircleImageView dp;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            dp = itemView.findViewById(R.id.dp);
            done = itemView.findViewById(R.id.done);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("challenge", challenges.get(pos));
                    controller.navigate(R.id.action_feedFragment_to_startChallenge, bundle);
                }
            });
        }

        public void setView(Challenge challenge, int position) {
            if ((position + 1) % 15 == 0 && !updatedAt.contains(position)) {
                fetchData();
                updatedAt.add(position);
            }
            if (completed.containsKey(challenge.getId())) {
                done.setText(String.format(Locale.getDefault(), "%d", completed.get(challenge.getId())));
            } else {
                done.setVisibility(View.GONE);
            }
            title.setText(challenge.getTitle());
            if (challenge.getDp() == null || challenge.getDp().equals("")) {
                Glide.with(context).load(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_usercircle, context.getTheme())).centerCrop().into(dp);
            } else {
                Glide.with(context).load(Uri.parse(challenge.getDp())).centerCrop().into(dp);
            }
            if (position % 2 == 0) {
                image.setBackground(AppCompatResources.getDrawable(context, R.drawable.grad1));
                Glide.with(context).load(AppCompatResources.getDrawable(context, R.drawable.dumble_bg)).into(image);
            } else {
                image.setBackground(AppCompatResources.getDrawable(context, R.drawable.grad2));
                Glide.with(context).load(AppCompatResources.getDrawable(context, R.drawable.challenge_card_2)).into(image);
            }

        }

    }
}
