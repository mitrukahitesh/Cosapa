package com.skywalkers.cosapa;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;


public class LanguageBottomsheet extends BottomSheetDialogFragment{


    MainActivity main;
    Locale myLocale;
    String currentLang;
    String currentLanguage = "en";
    CardView englishCard,hindiCard,bengalicard,punjabiCard,teluguCard,gujaratiCard;


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        //Set the custom view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.language_content ,null);
        dialog.setContentView(view);
        main = new MainActivity();

        englishCard= view.findViewById(R.id.engcard);
        hindiCard=view.findViewById(R.id.hincard);
        bengalicard=view.findViewById(R.id.bengalicard);
        punjabiCard=view.findViewById(R.id.punjabicard);
        teluguCard=view.findViewById(R.id.telugucard);
        gujaratiCard=view.findViewById(R.id.gujraticard);


        currentLanguage = getActivity().getIntent().getStringExtra(currentLang);

        //case for making the tick mark alive
        switch(LocaleHelper.getLanguage(getContext()))
        {
            case "en":
                Toast.makeText(getActivity().getApplicationContext(), "English Selected", Toast.LENGTH_SHORT).show();
                break;
            case "hi":
                Toast.makeText(getActivity().getApplicationContext(), "Hindi Selected", Toast.LENGTH_SHORT).show();
                break;
            case "bn":
                Toast.makeText(getActivity().getApplicationContext(), "Urdu Selected", Toast.LENGTH_SHORT).show();
                break;
            case "pa":
                Toast.makeText(getActivity().getApplicationContext(), "Punjabi Selected", Toast.LENGTH_SHORT).show();
                break;
            case "gu":
                Toast.makeText(getActivity().getApplicationContext(), "Gujrati Selected", Toast.LENGTH_SHORT).show();
            case "te":
                Toast.makeText(getActivity().getApplicationContext(), "Telugu Selected", Toast.LENGTH_SHORT).show();
            default:
                System.out.println("no match");
        }

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        final CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    String state = "";

                    switch (newState) {
                        case BottomSheetBehavior.STATE_DRAGGING: {
                            state = "DRAGGING";
                            break;
                        }
                        case BottomSheetBehavior.STATE_SETTLING: {
                            state = "SETTLING";
                            break;
                        }
                        case BottomSheetBehavior.STATE_EXPANDED: {
                            state = "EXPANDED";
                            break;
                        }
                        case BottomSheetBehavior.STATE_COLLAPSED: {
                            state = "COLLAPSED";
                            break;
                        }
                        case BottomSheetBehavior.STATE_HIDDEN: {
                            dismiss();
                            state = "HIDDEN";
                            break;
                        }
                    }
//                    Toast.makeText(getContext(), "Bottom Sheet State Changed to: " + state, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        }
        //onclick on english language

        englishCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("en");
                //to close the bottom sheet
                ((BottomSheetBehavior) behavior).setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        hindiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("hi");
                //to close the bottom sheet
                ((BottomSheetBehavior) behavior).setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        bengalicard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("bn");
                //to close the bottom sheet
                ((BottomSheetBehavior) behavior).setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        punjabiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("pa");
                //to close the bottom sheet
                ((BottomSheetBehavior) behavior).setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        teluguCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("te");
                //to close the bottom sheet
                ((BottomSheetBehavior) behavior).setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        gujaratiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("gu");
                //to close the bottom sheet
                ((BottomSheetBehavior) behavior).setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    //to change the language and refresh the screen
    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            Context context = LocaleHelper.setLocale(getContext(), localeName);
            //Resources resources = context.getResources();
            myLocale = new Locale(localeName);
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(getContext(), MainActivity.class);
            refresh.putExtra(currentLang, localeName);
            startActivity(refresh);
        } else {
            //Toast.makeText(getActivity(), "Language already selected!", Toast.LENGTH_SHORT).show();
        }
    }
}