package com.skywalkers.cosapa.azuremaps;


import static com.azure.android.maps.control.options.AnimationOptions.animationDuration;
import static com.azure.android.maps.control.options.AnimationOptions.animationType;
import static com.azure.android.maps.control.options.CameraOptions.center;
import static com.azure.android.maps.control.options.CameraOptions.zoom;
import static com.azure.android.maps.control.options.StyleOptions.style;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.azure.android.maps.control.AzureMaps;
import com.azure.android.maps.control.MapControl;
import com.azure.android.maps.control.controls.TrafficControl;
import com.azure.android.maps.control.layer.SymbolLayer;
import com.azure.android.maps.control.options.AnimationType;
import com.azure.android.maps.control.options.MapStyle;
import com.azure.android.maps.control.source.DataSource;
import com.mapbox.geojson.Point;
import com.skywalkers.cosapa.R;

public class MapActivity extends AppCompatActivity {

    static {
        AzureMaps.setSubscriptionKey("gOkBubrRwmfVe8i7HNfwS3YAzlDCMS-lL1cRW2lPmZ4");

        //Alternatively use Azure Active Directory authenticate.
        //AzureMaps.setAadProperties("<Your aad clientId>", "<Your aad AppId>", "<Your aad Tenant>");
    }

    MapControl mapControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapControl = findViewById(R.id.mapcontrol);

        mapControl.onCreate(savedInstanceState);

        //Wait until the map resources are ready.
        mapControl.onReady(map -> {
            //Add your post map load code here.
            map.setStyle(style(MapStyle.HIGH_CONTRAST_DARK));
            map.controls.add(new TrafficControl());
            map.setCamera(center(Point.fromLngLat(81.878357,
                    25.473034)),
                    zoom(30),
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
    protected void onStart(){
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
    protected void onDestroy() {
        super.onDestroy();
        mapControl.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapControl.onSaveInstanceState(outState);
    }}