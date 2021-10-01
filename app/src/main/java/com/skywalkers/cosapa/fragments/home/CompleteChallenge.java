package com.skywalkers.cosapa.fragments.home;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.models.Challenge;

public class CompleteChallenge extends Fragment {

    private TextView steps, completed, play;
    private Challenge challenge;
    private PlayerView playerView;
    private SimpleExoPlayer exoPlayer;
    private ConstraintLayout root;
    private boolean playing = false;

    public CompleteChallenge() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().getParcelable("challenge") != null) {
            challenge = getArguments().getParcelable("challenge");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_complete_challenge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).popBackStack();
            }
        });
        playing = false;
        root = view.findViewById(R.id.root);
        completed = view.findViewById(R.id.complete);
        steps = view.findViewById(R.id.steps);
        play = view.findViewById(R.id.play);
        steps.setText(challenge.getText());
        playerView = view.findViewById(R.id.player);
        exoPlayer = new SimpleExoPlayer.Builder(requireContext()).build();
        playerView.setPlayer(exoPlayer);
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("challenge", challenge);
                Navigation.findNavController(view).navigate(R.id.action_completeChallenge_to_challengeRepetition, bundle);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing) {
                    exoPlayer.pause();
                    playerView.setVisibility(View.GONE);
                    playing = false;
                    play.setText("Watch Video");
                    return;
                }
                if (exoPlayer.getMediaItemCount() != 0) {
                    exoPlayer.play();
                    playerView.setVisibility(View.VISIBLE);
                    playing = true;
                    play.setText("Hide Player");
                    return;
                }
                playerView.setVisibility(View.VISIBLE);
                FirebaseStorage.getInstance()
                        .getReference()
                        .child("challenges")
                        .child(challenge.getId())
                        .child("steps")
                        .getDownloadUrl()
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    MediaItem item = MediaItem.fromUri(task.getResult());
                                    exoPlayer.setMediaItem(item);
                                    exoPlayer.prepare();
                                    exoPlayer.play();
                                    playing = true;
                                    play.setText("Hide Player");
                                } else {
                                    Snackbar.make(root, "Could not play video..", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        exoPlayer.stop();
    }
}