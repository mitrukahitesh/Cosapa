package com.skywalkers.cosapa.fragments.doctor.azuremaps;

import static android.app.Activity.RESULT_OK;
import static com.azure.android.maps.control.options.AnimationOptions.animationDuration;
import static com.azure.android.maps.control.options.AnimationOptions.animationType;
import static com.azure.android.maps.control.options.CameraOptions.center;
import static com.azure.android.maps.control.options.CameraOptions.zoom;
import static com.azure.android.maps.control.options.StyleOptions.style;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.azure.android.maps.control.AzureMaps;
import com.azure.android.maps.control.MapControl;
import com.azure.android.maps.control.controls.TrafficControl;
import com.azure.android.maps.control.options.AnimationType;
import com.azure.android.maps.control.options.MapStyle;
import com.azure.android.maps.control.source.DataSource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.skywalkers.cosapa.R;

import java.util.ArrayList;
import java.util.Locale;

public class AzureMapsFragment extends Fragment {
    MapControl mapControl;
    CardView cardViewmap;
    private String searchedLocation;
    private final int REQ_CODE_SPEECH_INPUT = 100;


    public AzureMapsFragment() {
        // Required empty public constructor
    }

    static {
        AzureMaps.setSubscriptionKey("8SHf9GjUSMs_NLwAM59IPu9k-Pwgsbm8z09WpMbNh8E");

        //Alternatively use Azure Active Directory authenticate.
        //AzureMaps.setAadProperties("<Your aad clientId>", "<Your aad AppId>", "<Your aad Tenant>");
    }

    public static AzureMapsFragment newInstance(String param1, String param2) {
        AzureMapsFragment fragment = new AzureMapsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_azure_maps, container, false);

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
        mapControl = view.findViewById(R.id.mapcontrol);
        mapControl.onCreate(savedInstanceState);
        FloatingActionButton findFab = view.findViewById(R.id.findFab);

        mapControl.onReady(map -> {
            //Add your post map load code here.
            map.setStyle(style(MapStyle.ROAD));
            map.controls.add(new TrafficControl());
            map.setCamera(center(Point.fromLngLat(81.8229,
                    25.4319)),
                    zoom(16),
                    animationType(AnimationType.FLY),
                    animationDuration(3000));

            //loading geojson
            map.images.add("hospital", R.drawable.ic_heart);
            DataSource source = new DataSource();
            map.sources.add(source);
            Feature feature = Feature.fromGeometry(Point.fromLngLat(81.8229, 25.4319));
            source.add(feature);


        });
        findFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity().getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //txtSpeechInput.setText(result.get(0));
                    searchedLocation = result.get(0);
                    Toast.makeText(getActivity().getApplicationContext(), result.get(0), Toast.LENGTH_SHORT).show();
                    if (searchedLocation.equalsIgnoreCase("Hospital Bed")) {
                        onMapRefresh(81.82171305531897, 25.427889005315237);
                        //,
                    } else if (searchedLocation.equalsIgnoreCase("Medical Store")) {
                        onMapRefresh(81.81974808592769, 25.43062813370959);
                    }
                    //,
                    else if (searchedLocation.equalsIgnoreCase("Oxygen")) {
                        onMapRefresh(81.81831698407267, 25.430756965742464);
                    } else if (searchedLocation.equalsIgnoreCase("Blood Test")) {
                        onMapRefresh(81.81614752454469, 25.427420947778323);
                    }


                }
                break;
            }

        }
    }

    public void onMapRefresh(Double longitude, Double latitude) {
        mapControl.onReady(map -> {
            //Add your post map load code here.
            map.setStyle(style(MapStyle.ROAD));
            map.setCamera(center(Point.fromLngLat(longitude,
                    latitude)),
                    zoom(16),
                    animationType(AnimationType.FLY),
                    animationDuration(3000));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapControl.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapControl.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapControl.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapControl.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapControl.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapControl.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapControl.onSaveInstanceState(outState);
    }
}