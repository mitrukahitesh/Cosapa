package com.skywalkers.cosapa.fragments.nearby;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.adapters.StoreAdapter;

public class NearbyFragment extends Fragment {

    private TextInputEditText search;
    private RecyclerView recyclerView;
    private RadioGroup radioGroup;
    private ConstraintLayout root;
    private boolean store = true;

    public NearbyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nearby, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = view.findViewById(R.id.root);
        search = view.findViewById(R.id.search);
        search.setHint("Search medical stores for");
        radioGroup = view.findViewById(R.id.radio_group);
        radioGroup.check(R.id.store);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new StoreAdapter(NearbyFragment.this.requireContext(), store, root));
        ImageView filterImg = view.findViewById(R.id.filter);
        filterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioGroup.getVisibility() == View.GONE) {
                    radioGroup.setVisibility(View.VISIBLE);
                    radioGroup.setAlpha(0.0f);
                    radioGroup.animate().alpha(1.0f).setDuration(500);
                } else {
                    radioGroup.animate().alpha(0.0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            radioGroup.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.lab) {
                    search.setHint("Search labs for");
                    store = false;
                } else {
                    search.setHint("Search medical stores for");
                    store = true;
                }
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (search.getText() != null && !search.getText().toString().trim().equals(""))
                        recyclerView.setAdapter(new StoreAdapter(NearbyFragment.this.requireContext(), store, root));
                    else {
                        Snackbar.make(root, "Empty search", Snackbar.LENGTH_SHORT).show();
                        search.requestFocus();
                    }
                    closeKeyboard();
                }
                return false;
            }
        });
    }

    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}