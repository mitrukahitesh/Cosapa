package com.skywalkers.cosapa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.skywalkers.cosapa.MainActivity;
import com.skywalkers.cosapa.R;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OTPFrag extends Fragment {

    private LinearLayout ll;
    private TextView sec, resend;
    private String number;
    private Button submit;
    private TextInputEditText code;
    private String verId;
    private boolean verified = false;

    public OTPFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        number = getArguments().getString("number");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_o_t_p, container, false);
        ll = view.findViewById(R.id.ll);
        sec = view.findViewById(R.id.resend_val);
        resend = view.findViewById(R.id.resend_code);
        submit = view.findViewById(R.id.submit);
        code = view.findViewById(R.id.otp);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code.getText() == null)
                    return;
                if (code.getText().toString().length() != 6) {
                    return;
                }
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verId, code.getText().toString());
                signInWithPhoneAuthCredential(credential);
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCode();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestCode();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.i("Cosapa", "Complete");
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.i("Cosapa", "Failed");
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.i("Cosapa", "Sent");
            submit.setEnabled(true);
            verId = s;
            ll.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Integer count = 60;
                    while (count > 0 && !verified) {
                        Integer finalCount = count;
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sec.setText(" 00:" + (finalCount >= 10 ? finalCount : "0" + finalCount));
                            }
                        });
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        --count;
                    }
                    if (!verified) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ll.setVisibility(View.GONE);
                                resend.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            }).start();
        }
    };

    private void requestCode() {
        ll.setVisibility(View.GONE);
        resend.setVisibility(View.GONE);
        submit.setEnabled(false);
        PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(getActivity())
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            verified = true;
                            FirebaseUser user = task.getResult().getUser();
//                            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
//                            getActivity().finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Log.i("Cosapa", "Incorrect OTP");
                            }
                        }
                    }
                });
    }
}