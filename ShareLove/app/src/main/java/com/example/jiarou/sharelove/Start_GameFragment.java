package com.example.jiarou.sharelove;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link Start_GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Start_GameFragment extends Fragment implements LocationListener,FragmentBackHandler {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Start_game mListener;
    private GoogleMap mMap;
    private MapView mapView;
    static final int MIN_TINE = 5000;
    static final float MIN_DIST = 5;
    public  static  final int LOCATION_REQUEST_CODE=1;
    LocationManager mgr;
    TextView txv;
    Button get,open;
    String get_location;
    String shop; //店家地址
    double Latitude; //店家緯度
    double Longitude; //店家經度
    double distance; //距離
    double myla=24.9849998;  //我的緯度
    double mylo=121.5761281;  //我的經度
    double idis;
    LatLng nccu;
    String number;
    String check_where,test;
    Long  userLongId;



    public Start_GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment Start_GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Start_GameFragment newInstance() {
        Start_GameFragment fragment = new Start_GameFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        number = bundle.getString("number");

        get_location = bundle.getString("address");
        // test = bundle.getString("123");



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_start_game, container, false);


        mgr = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        GlobalVariable globalVariable = (GlobalVariable) getActivity().getApplicationContext();
        String userId =  globalVariable.setUserId(AccessToken.getCurrentAccessToken().getUserId());
        userLongId = Long.parseLong(userId, 10);
        //fragment加入地圖頁面
        mapView = (MapView) view.findViewById(R.id.game_map);
        mapView.onCreate(savedInstanceState);
        setUpMapIfNeed();
        mapView.onResume();// needed to get the map to display immediately



        mMap = mapView.getMap();



        txv = (TextView) view.findViewById(R.id.txv);

/**  開啟ＧＰＳ
 open = (Button) view.findViewById(R.id.open);
 open.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
//get_now();
// Log.d("test01", "test01" + check_where);

Intent it = new Intent((Settings.ACTION_LOCATION_SOURCE_SETTINGS));
startActivity(it);

}
});

 **/
        get = (Button) view.findViewById(R.id.get);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                distance= getDistanceMeter(Latitude, Longitude, myla, mylo);
                idis=Math.floor(distance);
                if ( idis<=10000){
                    find_number(number);
                    mListener.Start_game("hi");




                }else {
                    Toast.makeText(getActivity(),"必須距離50公尺才算到達喔！", Toast.LENGTH_LONG).show();
                }

            }


        });

/**

 view.setOnKeyListener(new View.OnKeyListener() {
@Override
public boolean onKey(View v, int keyCode, KeyEvent event) {

if(keyCode == KeyEvent.KEYCODE_BACK){
return true;
}
return false;
}

});


 **/


        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addressLocation= geocoder.getFromLocationName(get_location,1);
            Latitude= addressLocation.get(0).getLatitude();
            Longitude=addressLocation.get(0).getLongitude();
            nccu = new LatLng(Latitude, Longitude);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(nccu));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        } catch (IOException e) {
            e.printStackTrace();
        }




        final Firebase myFirebaseRef = new Firebase("https://vendor-5acbc.firebaseio.com/Vendors");
        ChildEventListener childEventListener = myFirebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("Location/Address").getValue().toString().equals(get_location)) {
                    shop= (String) dataSnapshot.child("Information/Name").getValue();
                    mMap.addMarker(new MarkerOptions().position(nccu).title(shop));
                    /**  int store_number;
                     store_number=((String) dataSnapshot.child("Information/Name").getValue()).length();
                     if(store_number>0) {
                     Toast.makeText(SearchActivity.this, "目前有"+ zip_areas+"筆", Toast.LENGTH_LONG).show();
                     }else {
                     Toast.makeText(SearchActivity.this, "您沒有選擇任何項目"+store_number, Toast.LENGTH_LONG).show();
                     } **/
                }
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



        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && (ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                    mgr.requestLocationUpdates(mgr.GPS_PROVIDER
                            ,1000*60,2, (LocationListener) this);    android.location.Location location = mgr.getLastKnownLocation(mgr.GPS_PROVIDER);
                }
            }
        }
    }



    private   void  firebase(final String location,final String num){
        final Firebase myFirebaseRef = new Firebase("https://member-activity.firebaseio.com/Activity");
        // Map<String, Object> used_shop= new HashMap<String, Object>();
        // used_shop.put("used",location);
        Query memberQuery = myFirebaseRef.orderByChild("Facebook_ID").equalTo(userLongId);
        memberQuery.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String memberKey = dataSnapshot.getKey();
                myFirebaseRef.child(memberKey).child(num).setValue(location);
                myFirebaseRef.child(memberKey).child("times").setValue(num);
                myFirebaseRef.child(memberKey).child("condition").setValue("false");


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


    private   void  get_now(){
        final Firebase myFirebaseRef = new Firebase("https://member-activity.firebaseio.com/Activity");
        // Map<String, Object> used_shop= new HashMap<String, Object>();
        // used_shop.put("used",location);
        Query memberQuery = myFirebaseRef.orderByChild("Facebook_ID").equalTo(userLongId);
        memberQuery.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                check_where=(String)dataSnapshot.child("now").getValue();
                Log.d("test", "test" + check_where);


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



    private  void  find_number(String a){

        if(a=="1"){
            firebase(get_location, "two");

        }else  if (a=="2"){
            firebase(get_location,"three");

        }else  if(a=="3"){
            firebase(get_location,"four");
        }else if(a=="4"){

            firebase(get_location,"done");
        }





    }


    @Override
    public void onResume() {
        super.onResume();


        setUpMapIfNeed();

        String best = mgr.getBestProvider(new Criteria(), true);
        if (best != null) {
            txv.setText("剩下"+idis+"公尺喔！");
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {           mgr.requestLocationUpdates(mgr.GPS_PROVIDER
                    ,1000*60,2, (LocationListener) this);
                android.location.Location location = mgr.getLastKnownLocation(mgr.GPS_PROVIDER);
            }
            mgr.requestLocationUpdates(best, MIN_TINE, MIN_DIST, (LocationListener) getActivity());
        } else {
            txv.setText("請確認已開啟定位功能！！");
        }


    }

    private void setUpMapIfNeed() {
        if (mMap != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {           mgr.requestLocationUpdates(mgr.GPS_PROVIDER
                    ,1000*60,2, (LocationListener) this);
                android.location.Location location = mgr.getLastKnownLocation(mgr.GPS_PROVIDER);
            }
            mMap.setMyLocationEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.moveCamera(CameraUpdateFactory.zoomTo(18));

        }

    }

    private  double getDistanceMeter(double lat1,double lon1, double lat2 , double lon2) {
        float[] results= new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);

        return  results[0];


    }

    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {           mgr.requestLocationUpdates(mgr.GPS_PROVIDER
                ,1000*60,2, (LocationListener) this);
            android.location.Location location = mgr.getLastKnownLocation(mgr.GPS_PROVIDER);
        }
        mgr.removeUpdates((LocationListener) getActivity());


    }


    /**

     // TODO: Rename method, update argument and hook method into UI event
     public void onButtonPressed(Uri uri) {
     if (mListener != null) {
     mListener. Start_game();
     }
     }
     **/
    public static boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            return true;
        }
        return true;

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof  Start_game) {
            mListener = ( Start_game) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement  Start_game");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void onLocationChanged(android.location.Location location) {
        String str="定位提供者："+ location.getProvider();
        str+= String.format("\n緯度:%.5f\n經度:%.5f",
                location.getLatitude(),
                location.getLongitude());
        // myla=location.getLatitude();
        //  mylo=location.getLongitude();

        txv.setText(str);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));



    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }







    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Start_game {
        // TODO: Update argument type and name
        void  Start_game(String check);
    }

    @Override
    public boolean onBackPressed() {

        if (!BackHandlerHelper.handleBackPress(this)) {
            AlertDialog.Builder bdr = new AlertDialog.Builder(getActivity());
            bdr.setMessage("確定離開遊戲嘛？");
            bdr.setTitle("提醒");
            bdr.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                    final Intent intent = new Intent();
                    intent.setClass(getActivity(), GameActivity.class);

                    startActivity(intent);
                }
            });

            bdr.setNegativeButton("繼續", null);
            bdr.show();

        }
            return true;

    }
}
