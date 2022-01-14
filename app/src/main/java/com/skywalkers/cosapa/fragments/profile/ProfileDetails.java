package com.skywalkers.cosapa.fragments.profile;

import static com.skywalkers.cosapa.fragments.healthDashboard.HealthDashboard.MEASUREMENTS;
import static com.skywalkers.cosapa.fragments.healthDashboard.HealthDashboard._2;
import static com.skywalkers.cosapa.fragments.healthDashboard.HealthDashboard._3;
import static com.skywalkers.cosapa.fragments.healthDashboard.HealthDashboard._7;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import android.os.SystemClock;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDetails extends Fragment {

    private TextView name, status, occupation, location, phone, email;
    private FrameLayout root;
    private CircleImageView dp;
    private ImageView languageSelection, profileShare;
    private ConstraintLayout shareLayout;
    private String cals = "0 cal";
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
        profileShare = view.findViewById(R.id.profile_share);
        languageSelection = view.findViewById(R.id.profile_edit);
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
                setData(shareLayout, requireContext().getSharedPreferences(MEASUREMENTS, Context.MODE_PRIVATE));
                try {
                    Bitmap bitmap = getBitmapFromView(contentView);

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(getActivity().getApplicationContext(), bitmap));
                    shareIntent.setType("image/jpeg");
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));

                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });


    }

    private void setData(View shareLayout, SharedPreferences preferences) {
        TextView heart, o2, cal, temp, userId, date, username;
        CircleImageView pic;
        pic = shareLayout.findViewById(R.id.imageView13);
        username = shareLayout.findViewById(R.id.username);
        userId = shareLayout.findViewById(R.id.userid);
        date = shareLayout.findViewById(R.id.date);
        heart = shareLayout.findViewById(R.id.hbcount);
        o2 = shareLayout.findViewById(R.id.oxylevel);
        cal = shareLayout.findViewById(R.id.cal_count);
        temp = shareLayout.findViewById(R.id.tempcount);
        //
        userId.setText(String.format("ID: %s##", Objects.requireNonNull(FirebaseAuth.getInstance().getUid()).substring(0, 7)));
        pic.setImageDrawable(dp.getDrawable());
        username.setText(name.getText().toString().substring(0, name.getText().toString().indexOf(" ") == -1 ? name.getText().toString().length() : name.getText().toString().indexOf(" ")));
        heart.setText(String.format("%s bpm", preferences.getString(_2, "80")));
        o2.setText(preferences.getString(_3, "95") + " %");
        cal.setText(cals);
        temp.setText(String.format("%s °C", preferences.getString(_7, "37")));
        date.setText(getDate(System.currentTimeMillis()));
    }

    private String getDate(Long time) {
        TimeZone timeZone = TimeZone.getDefault();
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault());
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        if (ActivityCompat.checkSelfPermission(inContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Ask for permission
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private Bitmap getBitmapFromView(ConstraintLayout view) {
        try {

            view.setDrawingCacheEnabled(true);

            view.measure(View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(1920, View.MeasureSpec.EXACTLY));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

            view.buildDrawingCache(false);
            Bitmap returnedBitmap = Bitmap.createBitmap(view.getDrawingCache());

            //Define a bitmap with the same size as the view
            view.setDrawingCacheEnabled(false);

            return returnedBitmap;
        } catch (Exception e) {

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
                            String s = "0";
                            if (task.getResult().get("calories") != null && !task.getResult().get("calories").equals("")) {
                                s = task.getResult().get("calories").toString();
                            }
                            cals = s.substring(0, s.indexOf('.') == -1 ? 1 : s.indexOf('.')) + " Cal";
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