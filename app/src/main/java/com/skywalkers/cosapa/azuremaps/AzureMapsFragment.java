package com.skywalkers.cosapa.azuremaps;

import static com.azure.android.maps.control.options.AnimationOptions.animationDuration;
import static com.azure.android.maps.control.options.AnimationOptions.animationType;
import static com.azure.android.maps.control.options.CameraOptions.center;
import static com.azure.android.maps.control.options.CameraOptions.zoom;
import static com.azure.android.maps.control.options.StyleOptions.style;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.azure.android.maps.control.AzureMaps;
import com.azure.android.maps.control.MapControl;
import com.azure.android.maps.control.controls.TrafficControl;
import com.azure.android.maps.control.layer.SymbolLayer;
import com.azure.android.maps.control.options.AnimationType;
import com.azure.android.maps.control.options.MapStyle;
import com.azure.android.maps.control.options.SymbolLayerOptions;
import com.azure.android.maps.control.source.DataSource;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.skywalkers.cosapa.R;

public class AzureMapsFragment extends Fragment {
    MapControl mapControl;
    CardView cardViewmap;


    public AzureMapsFragment() {
        // Required empty public constructor
    }
    static {
        AzureMaps.setSubscriptionKey("gOkBubrRwmfVe8i7HNfwS3YAzlDCMS-lL1cRW2lPmZ4");

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
        mapControl=view.findViewById(R.id.mapcontrol);
        mapControl.onCreate(savedInstanceState);
        cardViewmap=view.findViewById(R.id.mapcard);
        cardViewmap.setBackgroundResource(R.drawable.bottomsheet_curved);
        mapControl.onReady(map -> {
            //Add your post map load code here.
            map.setStyle(style(MapStyle.ROAD));
            map.controls.add(new TrafficControl());
            map.setCamera(center(Point.fromLngLat(81.8229,
                    25.4319)),
                    zoom(30),
                    animationType(AnimationType.FLY),
                    animationDuration(3000));

            //loading geojson
            map.images.add("hospital",R.drawable.ic_heart);
            DataSource source = new DataSource();
            map.sources.add(source);
            Feature feature = Feature.fromGeometry(Point.fromLngLat(81.8229, 25.4319));
            source.add(feature);


        });
        Button exploreButton= view.findViewById(R.id.explorebtn);
        exploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.TransparentBottomSheetDialogTheme);
                bottomSheetDialog.setContentView(R.layout.nearby_bottomsheet);
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapControl.onResume();
    }

    @Override
    public void onStart(){
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