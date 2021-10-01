package com.skywalkers.cosapa.fragments.onboarding;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.skywalkers.cosapa.MainActivity;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.fragments.home.AddChallenge;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class TakePicture extends Fragment {

    private CircleImageView img;
    private Button upload;
    private Uri uri;
    public static final int MEDIA_SELECT = 1;
    private FrameLayout frameLayout;

    public TakePicture() {
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
        return inflater.inflate(R.layout.fragment_take_picture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img = view.findViewById(R.id.img);
        upload = view.findViewById(R.id.upload);
        frameLayout = view.findViewById(R.id.root);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"), MEDIA_SELECT);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri == null) {
                    completeRegistration();
                }
                try {
                    Snackbar.make(frameLayout, "Uploading...", Snackbar.LENGTH_LONG).show();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile_pic").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                    storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                completeRegistration();
                            } else {
                                Snackbar.make(frameLayout, "Please try again", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    Snackbar.make(frameLayout, "Please try again", Snackbar.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void completeRegistration() {
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
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
                Glide.with(TakePicture.this).load(uri).fitCenter().into(img);
            }
        }
    }
}