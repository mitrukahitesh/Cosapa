package com.skywalkers.cosapa.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.models.Challenge;
import com.skywalkers.cosapa.models.Post;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.CustomVH> {

    private final Context context;
    private final List<Challenge> challenges = new ArrayList<>();
    private Long last = System.currentTimeMillis();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Set<Integer> updatedAt = new HashSet<>();

    public ChallengeAdapter(Context context) {
        this.context = context;
        fetchData();
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
        private TextView title;
        private CircleImageView dp;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            dp = itemView.findViewById(R.id.dp);
        }

        public void setView(Challenge challenge, int position) {
            if ((position + 1) % 15 == 0 && !updatedAt.contains(position)) {
                fetchData();
                updatedAt.add(position);
            }
            title.setText(challenge.getTitle());
            if (position % 2 == 0) {
                image.setBackground(AppCompatResources.getDrawable(context, R.drawable.grad1));
                Glide.with(context).load(AppCompatResources.getDrawable(context, R.drawable.challenge_card_1)).into(image);
            } else {
                image.setBackground(AppCompatResources.getDrawable(context, R.drawable.grad2));
                Glide.with(context).load(AppCompatResources.getDrawable(context, R.drawable.challenge_card_2)).into(image);
            }

        }

    }
}
