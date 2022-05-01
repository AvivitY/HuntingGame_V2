package com.example.huntinggame_v2.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.huntinggame_v2.R;
import com.example.huntinggame_v2.Score;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Fragment_Map extends Fragment {
    GoogleMap map;
    ArrayList<Score> scores = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        initMap();
        return view;
    }
    private void initMap() {
        //init map
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                map = googleMap;
                for(Score s : scores){
                    if(!(s.getLatitude() == 0 && s.getLongitude() ==0)) {
                        LatLng latLng = new LatLng(s.getLatitude(), s.getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(latLng)
                                .title("Location of " + s.getScore()));
                    }
                }
            }
        });
    }

    public void gotoLocation(double latitude,double longitude) {
        //TODO:change to go to marker?
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        map.moveCamera(cameraUpdate);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

   public void setList(ArrayList<Score> list){
        scores = list;
   }
}
