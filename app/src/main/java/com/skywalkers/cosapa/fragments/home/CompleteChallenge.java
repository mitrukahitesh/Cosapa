package com.skywalkers.cosapa.fragments.home;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Pair;
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
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.models.Challenge;
import com.skywalkers.cosapa.utility.SocketObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class CompleteChallenge extends Fragment {

    private TextView steps, completed, play, counter;
    private Challenge challenge;
    private PlayerView playerView;
    private SimpleExoPlayer exoPlayer;
    private ConstraintLayout root;
    private boolean playing = false;
    private Map<String, Pair<String, Integer>> exerciseId = new HashMap<>();
    private Map<String, Float> calories = new HashMap<>();
    private Socket socket;
    private NavController controller;
    private Emitter emitter;

    private final Emitter.Listener newCount = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("cosapa", args[0].toString());
            CompleteChallenge.this.requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    counter.setText(args[0].toString() + "");
                }
            });
        }
    };

    private final Emitter.Listener onStop = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("cosapa", args[0].toString());
            CompleteChallenge.this.requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    counter.setText(args[0].toString() + "");
                }
            });
            Bundle bundle = new Bundle();
            bundle.putParcelable("challenge", challenge);
            bundle.putString("count", counter.getText().toString());
            saveChallengeCompletion(FirebaseAuth.getInstance().getUid(), Integer.parseInt(counter.getText().toString()));
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("ID")
                    .setValue(0)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            CompleteChallenge.this.requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    controller.navigate(R.id.action_completeChallenge_to_challengeCompleted, bundle);
                                }
                            });
                        }
                    });
        }
    };

    public CompleteChallenge() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().getParcelable("challenge") != null) {
            challenge = getArguments().getParcelable("challenge");
        }
        exerciseId.put("PUSHUPS", new Pair<>("Ex1", 4));
        exerciseId.put("SITUPS", new Pair<>("Ex2", 5));
        exerciseId.put("SQUATS", new Pair<>("Ex3", 6));
        calories.put("PUSHUPS", 0.3f);
        calories.put("SITUPS", 0.2f);
        calories.put("SQUATS", 0.2f);
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
                socket.emit("stop", Objects.requireNonNull(exerciseId.get(challenge.getTitle().toUpperCase())).second);
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("ID")
                        .setValue(0);
                Navigation.findNavController(view).popBackStack();
            }
        });
        try {
            socket = SocketObject.getSocket();
        } catch (URISyntaxException e) {
            Log.i("cosapa", "Socket error in measurement fragment");
            e.printStackTrace();
        }
        socket.once("final_reading", onStop);
        socket.emit("exercise", Objects.requireNonNull(exerciseId.get(challenge.getTitle().toUpperCase())).second);
        controller = Navigation.findNavController(view);
        playing = false;
        counter = view.findViewById(R.id.counter);
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
                socket.emit("stop", Objects.requireNonNull(exerciseId.get(challenge.getTitle().toUpperCase())).second);
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
        startDevice();
    }

    @Override
    public void onStart() {
        super.onStart();
        emitter = socket.on("reading", newCount);
    }

    private void saveChallengeCompletion(String uid, int c) {
        Map<String, Object> map = new HashMap<>();
        map.put("count", c);
        FirebaseFirestore.getInstance().collection("users").document(uid).collection("challenges_completed").document(challenge.getId()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .update("calories", FieldValue.increment(calories.get(challenge.getTitle().toUpperCase()) * (float) c));
    }

    private void startDevice() {
        FirebaseDatabase.getInstance()
                .getReference()
                .child("ID")
                .setValue(Objects.requireNonNull(exerciseId.get(challenge.getTitle().toUpperCase())).second)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        exoPlayer.stop();
        emitter.off();
    }
}