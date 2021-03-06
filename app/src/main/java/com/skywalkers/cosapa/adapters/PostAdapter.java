package com.skywalkers.cosapa.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.skywalkers.cosapa.R;
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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.CustomVH> {

    private final Context context;
    private final NavController controller;
    private final List<Post> posts;
    private Long last = System.currentTimeMillis();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Set<Integer> updatedAt = new HashSet<>();
    private final Map<String, ListenerRegistration> listeners = new HashMap<>();
    private final Set<String> viewed = new HashSet<>();

    public PostAdapter(Context context, NavController controller) {
        this.context = context;
        this.controller = controller;
        posts = new ArrayList<>();
        fetchData();
    }

    public void fetchData() {
        CollectionReference reference = db.collection("posts");
        Query query = reference.orderBy("time", Query.Direction.DESCENDING).whereLessThan("time", last).limit(20);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() == null)
                        return;
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        posts.add(snapshot.toObject(Post.class));
                        posts.get(posts.size() - 1).setId(snapshot.getId());
                        getDpUrl(posts.get(posts.size() - 1), posts.size() - 1);
                        notifyItemInserted(posts.size() - 1);
                    }
                    if (!task.getResult().isEmpty())
                        last = posts.get(posts.size() - 1).getTime();
                    Log.i("Cosapa", "fetched " + task.getResult().size() + " " + last);
                } else {
                    Log.i("Cosapa", "get failed with ", task.getException());
                }
            }
        });
    }

    private void getDpUrl(Post post, int pos) {
        FirebaseStorage.getInstance()
                .getReference()
                .child("profile_pic")
                .child(post.getUid())
                .getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            post.setDp(task.getResult());
                            notifyItemChanged(pos);
                        }
                    }
                });
    }

    @NonNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view, parent, false);
        return new CustomVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomVH holder, int position) {
        holder.setPost(posts.get(position), position);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        private final TextView title, name, text, reactions, views, pos;
        private final CircleImageView dp;
        private final LinearLayout on, off, comment;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.top_name);
            name = itemView.findViewById(R.id.name);
            text = itemView.findViewById(R.id.post);
            reactions = itemView.findViewById(R.id.reactions);
            pos = itemView.findViewById(R.id.position);
            views = itemView.findViewById(R.id.views);
            dp = itemView.findViewById(R.id.dp);
            on = itemView.findViewById(R.id.online);
            off = itemView.findViewById(R.id.offline);
            comment = itemView.findViewById(R.id.comment);
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", posts.get(getAbsoluteAdapterPosition()).getTitle());
                    bundle.putString("name", posts.get(getAbsoluteAdapterPosition()).getName());
                    bundle.putString("dp", posts.get(getAbsoluteAdapterPosition()).getDp() == null ? "" : posts.get(getAbsoluteAdapterPosition()).getDp().toString());
                    bundle.putString("id", posts.get(getAbsoluteAdapterPosition()).getId());
                    controller.navigate(R.id.action_feedFragment_to_comments, bundle);
                }
            });
        }

        public void setPost(Post post, int position) {
            if ((position + 1) % 15 == 0 && !updatedAt.contains(position)) {
                fetchData();
                updatedAt.add(position);
            }
            if (!viewed.contains(post.getId())) {
                FirebaseFirestore.getInstance().collection("posts").document(post.getId()).update("views", FieldValue.increment(1));
                viewed.add(post.getId());
            }
            title.setText(post.getTitle());
            name.setText(post.getName());
            text.setText(post.getText());
            pos.setText(post.getPosition());
            name.setText(post.getName());
            if (post.isOnline()) {
                on.setVisibility(View.VISIBLE);
                off.setVisibility(View.GONE);
            } else {
                on.setVisibility(View.GONE);
                off.setVisibility(View.VISIBLE);
            }
            if (post.getDp() == null) {
                Glide.with(context).load(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_usercircle, context.getTheme())).centerCrop().into(dp);
            } else {
                Glide.with(context).load(post.getDp()).centerCrop().into(dp);
            }
            reactions.setText(String.format(Locale.getDefault(), "%d", post.getReactions()));
            views.setText(String.format(Locale.getDefault(), "%d", post.getViews()));
            if (!listeners.containsKey(post.getId())) {
                ListenerRegistration registration = FirebaseFirestore.getInstance().collection("online").document(post.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null && value.exists()) {
                            if (Boolean.valueOf(post.isOnline()).equals(value.getBoolean("status")))
                                return;
                            post.setOnline(value.getBoolean("status"));
                            notifyItemChanged(position);
                        }
                    }
                });
                listeners.put(post.getId(), registration);
            }
        }
    }
}
