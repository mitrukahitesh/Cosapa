package com.skywalkers.cosapa.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skywalkers.cosapa.R;

public class RewardPopup {
    private Context context;
    private String val;

    public RewardPopup(Context context, String val) {
        this.context = context;
        this.val = val;
    }

    public void showDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.rewards_popup, new LinearLayout(context));
        TextView amount = view.findViewById(R.id.amount);
        amount.setText(String.format("%s %s", "₹", val));
        new AlertDialog.Builder(context).setView(view).setCancelable(true).show();
        addAmountToProfile(Integer.parseInt(val));
    }

    private void addAmountToProfile(int val) {
        if (FirebaseAuth.getInstance().getUid() == null)
            return;
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(val));
    }
}
