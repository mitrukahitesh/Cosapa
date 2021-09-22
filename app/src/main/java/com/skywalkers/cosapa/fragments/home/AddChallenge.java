package com.skywalkers.cosapa.fragments.home;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.skywalkers.cosapa.MainActivity;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.models.Challenge;

public class AddChallenge extends Fragment {

    private EditText title, steps;
    private ConstraintLayout media;
    private Uri uri;
    private ImageView img;
    private TextView post;
    public static final int MEDIA_SELECT = 1;
    private LinearProgressIndicator progressIndicator;
    private ConstraintLayout root;

    public AddChallenge() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_challenge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.title);
        steps = view.findViewById(R.id.steps);
        media = view.findViewById(R.id.media_choose);
        img = view.findViewById(R.id.thumbnail);
        post = view.findViewById(R.id.post);
        progressIndicator = view.findViewById(R.id.progress);
        progressIndicator.setVisibility(View.GONE);
        root = view.findViewById(R.id.root);
        progressIndicator.setIndicatorColor(getResources().getColor(R.color.col1, getResources().newTheme()));
        media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), MEDIA_SELECT);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri == null || title.getText().toString().length() == 0 || steps.getText().toString().length() == 0)
                    return;
                try {
                    title.setEnabled(false);
                    steps.setEnabled(false);
                    media.setEnabled(false);
                    post.setEnabled(false);
                    progressIndicator.setVisibility(View.VISIBLE);
                    progressIndicator.setProgress(0, true);
                    DocumentReference reference = FirebaseFirestore.getInstance().collection("challenges").document();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("challenges").child(reference.getId()).child("steps." + getExtension(uri));
                    storageReference.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            long progress = ((snapshot.getBytesTransferred() * 100) / snapshot.getTotalByteCount());
                            progressIndicator.setProgress((int) progress, true);
                            if (progress == 100) {
                                try {
                                    Snackbar.make(root, "Challenge Posted", Snackbar.LENGTH_SHORT).show();
                                    NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.feedFragment, true).build();
                                    Navigation.findNavController(view).navigate(R.id.action_addChallenge_to_feedFragment, null, navOptions);
                                } catch (Exception e) {
                                    Log.i("Cosapa", e.getMessage());
                                }
                            }
                        }
                    });
                    Challenge challenge = new Challenge();
                    challenge.setTitle(title.getText().toString().trim());
                    challenge.setText(steps.getText().toString().trim());
                    challenge.setUid(FirebaseAuth.getInstance().getUid());
                    challenge.setPosition(MainActivity.POSITION);
                    challenge.setName(MainActivity.NAME);
                    challenge.setViews(0);
                    challenge.setTime(System.currentTimeMillis());
                    reference.set(challenge);
                } catch (Exception e) {
                    Log.i("Cosapa", e.getMessage());
                }
            }
        });
    }

    public String getExtension(Uri uri) throws Exception {
        if (getContext() == null)
            throw new Exception("No extension");
        ContentResolver resolver = getContext().getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(resolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == MEDIA_SELECT) {
                if (data == null || data.getData() == null)
                    return;
                uri = data.getData();
                Log.i("Cosapa", data.getData().toString());
                img.setImageURI(uri);
                media.setBackground(null);
                img.setLayoutParams(new ViewGroup.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                Glide.with(AddChallenge.this).load(uri).centerCrop().into(img);
            }
        }
    }
}