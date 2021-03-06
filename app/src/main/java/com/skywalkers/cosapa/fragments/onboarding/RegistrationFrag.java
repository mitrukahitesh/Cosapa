package com.skywalkers.cosapa.fragments.onboarding;

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

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.skywalkers.cosapa.R;

public class RegistrationFrag extends Fragment {

    private TextView signintv;
    private Button cont;
    private TextInputEditText phone;

    public RegistrationFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                num = "+91" + num;
                Bundle bundle = new Bundle();
                bundle.putString("number", num);
                bundle.putBoolean("register", true);
                Navigation.findNavController(view).navigate(R.id.action_registrationFrag2_to_OTPFrag2, bundle);
            }
        });
        return view;
    }
}