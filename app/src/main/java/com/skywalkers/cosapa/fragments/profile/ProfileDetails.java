package com.skywalkers.cosapa.fragments.profile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.skywalkers.cosapa.LanguageBottomsheet;
import com.skywalkers.cosapa.LocaleHelper;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.fragments.onboarding.TakePicture;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDetails extends Fragment {

    private TextView name, status, occupation, location, phone, email;
    private FrameLayout root;
    private CircleImageView dp;
    private ImageView languageSelection,profileShare;
    private ConstraintLayout shareLayout;
    public static final String TAG = "bottom_sheet";

    public ProfileDetails() {
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
        return inflater.inflate(R.layout.fragment_profilefrag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = view.findViewById(R.id.root);
        name = view.findViewById(R.id.name);
        status = view.findViewById(R.id.status);
        occupation = view.findViewById(R.id.occupation);
        location = view.findViewById(R.id.location);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        dp = view.findViewById(R.id.profile_image);
        profileShare=view.findViewById(R.id.profile_share);
        languageSelection=view.findViewById(R.id.profile_edit);
        view.findViewById(R.id.card1).setBackgroundResource(R.drawable.dashed_border);
        view.findViewById(R.id.card2).setBackgroundResource(R.drawable.dashed_border);
        fetchData();

        languageSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LanguageBottomsheet fragment = new LanguageBottomsheet();
                fragment.show(getParentFragmentManager(), TAG);
            }
        });
        profileShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareLayout = (ConstraintLayout) LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.patient_details_card, null);
                shareLayout.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                ConstraintLayout contentView = (ConstraintLayout) shareLayout.findViewById(R.id.shareLayout);
                try {
                    Bitmap bitmap = getBitmapFromView(contentView);

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(getActivity().getApplicationContext(), bitmap));
                    shareIntent.setType("image/jpeg");
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));

                }catch (Exception e){
                    e.getMessage();
                }
            }
        });


    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        if (ActivityCompat.checkSelfPermission(inContext, Manifest.permission. WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //Ask for permission
           requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private Bitmap getBitmapFromView(ConstraintLayout view) {
        try {

            view.setDrawingCacheEnabled(true);

            view.measure(View.MeasureSpec.makeMeasureSpec(720, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(1280, View.MeasureSpec.EXACTLY));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

            view.buildDrawingCache(true);
            Bitmap returnedBitmap = Bitmap.createBitmap(view.getDrawingCache());

            //Define a bitmap with the same size as the view
            view.setDrawingCacheEnabled(false);

            return returnedBitmap;
        }catch (Exception e){

        }
        return null;
    }

    private void fetchData() {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getData() != null && task.getResult().exists()) {
                            Map<String, Object> map = task.getResult().getData();
                            name.setText((String) map.get("name"));
                            phone.setText((String) map.get("number"));
                            email.setText((String) map.get("email"));
                            occupation.setText((String) map.get("occupation"));
                            status.setText(String.format("I am %s", (String) map.get("name")));
                            // location.setText("");
                        } else {
                            Snackbar.make(root, "Could not load profile..", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
        FirebaseStorage.getInstance()
                .getReference()
                .child("profile_pic")
                .child(FirebaseAuth.getInstance().getUid())
                .getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Glide.with(ProfileDetails.this).load(task.getResult()).fitCenter().into(dp);
                        } else {
                            Log.i("Cosapa: ProfileDetails", task.getException() == null ? "Some error occurred" : task.getException().getMessage());
                        }
                    }
                });
    }
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
        System.exit(0);
    }

}