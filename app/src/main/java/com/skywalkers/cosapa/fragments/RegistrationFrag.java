package com.skywalkers.cosapa.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.skywalkers.cosapa.R;

import java.util.concurrent.TimeUnit;

public class RegistrationFrag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView signintv;
    private Button cont;
    private TextInputEditText phone;

    private String mParam1;
    private String mParam2;

    public RegistrationFrag() {
        // Required empty public constructor
    }

    public static RegistrationFrag newInstance(String param1, String param2) {
        RegistrationFrag fragment = new RegistrationFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        signintv = view.findViewById(R.id.sign_in_tv);
        cont = view.findViewById(R.id.register1);
        phone = view.findViewById(R.id.phone);
        String text = "Already have a account? sign-in";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //onclick
            }
        };
        ss.setSpan(clickableSpan1, 24, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signintv.setText(ss);
        signintv.setMovementMethod(LinkMovementMethod.getInstance());
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone.getText() == null) {
                    phone.requestFocus();
                    return;
                }
                if (phone.getText().toString().length() != 10) {
                    phone.requestFocus();
                    return;
                }
                String num = phone.getText().toString();
                num ="+91" + num;
                Bundle bundle = new Bundle();
                bundle.putString("number", num);
                bundle.putBoolean("register", true);
                Navigation.findNavController(view).navigate(R.id.action_registrationFrag2_to_OTPFrag2, bundle);
            }
        });
        return view;
    }
}