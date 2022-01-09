package com.skywalkers.cosapa.rewards;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.skywalkers.cosapa.R;

public class RewardsActivity extends AppCompatActivity {
    TextView totalRewards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        totalRewards= findViewById(R.id.rewards_tv);
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        String coins= "0";
                        if(value!=null && value.contains("coins"))
                        {
                            coins= value.get("coins",Long.class).toString();

                        }

                        totalRewards.setText("₹ "+coins);
                    }
                });
    }
}