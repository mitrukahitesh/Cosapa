package com.skywalkers.cosapa.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.models.Comment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CustomVH> {

    private final Context context;
    private final List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_comment, parent, false);
        return new CustomVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomVH holder, int position) {
        TextView msg, time;
        if (comments.get(position).getSender().equals(FirebaseAuth.getInstance().getUid())) {
            holder.boxReceived.setVisibility(View.GONE);
            holder.boxSent.setVisibility(View.VISIBLE);
            msg = holder.msgSent;
            time = holder.timeSent;
        } else {
            holder.boxSent.setVisibility(View.GONE);
            holder.boxReceived.setVisibility(View.VISIBLE);
            msg = holder.msgReceived;
            time = holder.timeReceived;
            holder.sender.setText(comments.get(position).getName());
        }
        msg.setText(comments.get(position).getMessage());
        time.setText(getTime(comments.get(position).getTime()));
    }

    private String getTime(Long time) {
        TimeZone timeZone = TimeZone.getDefault();
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.getDefault());
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        LinearLayout boxSent, boxReceived;
        TextView msgSent, timeSent, msgReceived, timeReceived, sender;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            boxSent = itemView.findViewById(R.id.boxSent);
            msgSent = itemView.findViewById(R.id.messageSent);
            timeSent = itemView.findViewById(R.id.timeSent);
            boxReceived = itemView.findViewById(R.id.boxReceived);
            msgReceived = itemView.findViewById(R.id.messageReceived);
            timeReceived = itemView.findViewById(R.id.timeReceived);
            sender = itemView.findViewById(R.id.sender);
        }
    }
}
