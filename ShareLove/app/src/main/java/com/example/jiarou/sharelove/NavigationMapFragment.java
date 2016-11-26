package com.example.jiarou.sharelove;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Peter on 2016/10/16.
 */
public class NavigationMapFragment extends Fragment implements OnMapReadyCallback {

    final static String DB_URL = "https://vendor-5acbc.firebaseio.com/Vendors";
    private static final String ARGUMENT_TITLE = "VendorTitle";

    private MapView mapView;
    private GoogleMap mMap;
    //private Button goBack;

    //private OnGoBackSelected mListener;

    /*
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnGoBackSelected){
            mListener = (OnGoBackSelected)context;
        }else{
            throw new ClassCastException(context.toString() + "must implement OnGoBackSelected");
        }
    }
    */

    public static NavigationMapFragment newInstance(String vendorTitle) {

        final Bundle args = new Bundle();
        args.putString(ARGUMENT_TITLE, vendorTitle);

        NavigationMapFragment fragment = new NavigationMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public NavigationMapFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.navigation_use, container, false);
        mapView = (MapView)view.findViewById(R.id.navigationMap);
        //goBack = (Button)view.findViewById(R.id.goBackButton);

        final Bundle args = getArguments();

        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        MapsInitializer.initialize(getActivity());

        mMap = mapView.getMap();


        /*
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGoBackSelected(args.getString(ARGUMENT_TITLE));
            }
        });
        */

        getNavigation(args.getString(ARGUMENT_TITLE));

        return view;
    }

    public void getNavigation(final String vendorTitle){
        Firebase.setAndroidContext(this.getActivity());
        final Firebase navigate = new Firebase(DB_URL);
        Query navigates = navigate.orderByChild("Information/Name").equalTo(vendorTitle);

        navigates.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String navLatitude = dataSnapshot.child("Location/Latitude").getValue().toString();
                String navLongitude = dataSnapshot.child("Location/Longitude").getValue().toString();
                String location = dataSnapshot.child("Location/Address").getValue().toString();

                double location_left = Double.parseDouble(navLatitude);
                double location_right = Double.parseDouble(navLongitude);

                final LatLng navImp = new LatLng(location_left, location_right);
                mMap.addMarker(new MarkerOptions().position(navImp).title(vendorTitle)
                        .snippet(location)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                        .showInfoWindow();
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(navImp)
                        .zoom(15)
                        .build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                //mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /*
    public interface OnGoBackSelected{
        void onGoBackSelected(String vendorTitle);
    }
    */



    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
