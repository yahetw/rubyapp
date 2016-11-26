package com.example.jiarou.sharelove;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {



    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Button mButton;
    private TextView textview2;
    String zip_number;
    String  zip_areas;
    Button user_btn, vendor_btn, lovecode_btn,index_btn,game_btn;
    String vendorTitle;
    Spinner list;

    TextView textView21;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //初始化臉書
        FacebookSdk.sdkInitialize(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Firebase.setAndroidContext(this);
       final Firebase myFirebaseRef = new Firebase("https://vendor-5acbc.firebaseio.com/Vendors");

        textView21 = (TextView)findViewById(R.id.textView21);

        //如果找不到臉書是用者相關資料，跳轉至登入頁面
      if(AccessToken.getCurrentAccessToken() == null){
            Intent intent = new Intent();
            intent.setClass(MapsActivity.this, Login.class);
            startActivity(intent);
        }else{
          GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
          globalVariable.setUserId(AccessToken.getCurrentAccessToken().getUserId());
            textView21.setText("登出");
            textView21.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(MapsActivity.this, Login.class);
                    startActivity(intent);
                }
            });
        } 


        index_btn=(Button)findViewById(R.id.index);
        index_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setClass(MapsActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        game_btn=(Button)findViewById(R.id.game);
        game_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setClass(MapsActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        user_btn=(Button)findViewById(R.id.user_btn);
        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setClass(MapsActivity.this, User_Activity.class);
                startActivity(intent);
            }
        });


        lovecode_btn=(Button)findViewById(R.id.lovecode_btn);
        lovecode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setClass(MapsActivity.this, LoveCodeMainActivity.class);
                startActivity(intent);
            }
        });

        vendor_btn = (Button)findViewById(R.id.vendor_btn);
        vendor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setClass(MapsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



        mButton = (Button) findViewById(R.id.search_button);
        mButton.setOnClickListener(new Button.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MapsActivity.this, IndexActivity.class);
                startActivityForResult(intent, 1);




            }

        });

//requestCode(識別碼) 型別為 int ,從B傳回來的物件將會有一樣的requestCode


//requestCode(識別碼) 型別為 int ,從B傳回來的物件將會有一樣的requestCode


       /** get "hi" from searchActivity
        Bundle bundle =getIntent().getExtras();
//透過Bundle 接收MainActiviy傳遞過來的資料
        String msg = bundle.getString("message");
        textview2=(TextView) this.findViewById(R.id.textView2);
        textview2.setText(msg);
        */
//取出MainActivty的key

       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);

        mapFragment.getMapAsync(this);



        final ListView list = (ListView) findViewById(R.id.listView);
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Toast.makeText(MapsActivity.this, "get", Toast.LENGTH_LONG).show();
                                            FragmentManager fm = getFragmentManager();
                                            FragmentTransaction ft=fm.beginTransaction();
                                           // **Fragment fragment = fm.findFragmentById(R.id.show_vender);

                                         /**   if(fragment==null){
                                                getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .add(com.example.peter.focus.R.id.focus_root, VendedInfoFragment.newInstance(), "Focus")
                                                        .commit();

                                            }**/

                                        }
                                    }
        );



        ChildEventListener childEventListener = myFirebaseRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                adapter.add(
                        (String) dataSnapshot.child("Information/Name").getValue());

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

    private AdapterView.OnItemSelectedListener vender = new AdapterView.OnItemSelectedListener(){
        public void onItemSelected(AdapterView<?> parent, View v, int position,long id){
            vendorTitle = list.getSelectedItem().toString();
            Toast.makeText(MapsActivity.this, "get", Toast.LENGTH_LONG).show();



        }

        public void onNothingSelected(AdapterView<?> arg0){

        }

    };








    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        LatLng nccu = new LatLng(24.9849998, 121.5761281);
        mMap.addMarker(new MarkerOptions().position(nccu).title("Marker in NCCU"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(nccu));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        final Firebase myFirebaseRef = new Firebase("https://vendor-5acbc.firebaseio.com/Vendors");
        ChildEventListener childEventListener = myFirebaseRef.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    String Latitude = dataSnapshot.child("Location/Latitude").getValue().toString();
                    String Longitude = dataSnapshot.child("Location/Longitude").getValue().toString();


                    double location_left = Double.parseDouble(Latitude);
                    double location_right = Double.parseDouble( Longitude);
                    String name =(String) dataSnapshot.child("Information/Name").getValue();
                    LatLng cod = new LatLng( location_left, location_right);
                    mMap.addMarker(new MarkerOptions().position(cod).title(name));


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

    Intent intent = getIntent();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {//resultCode是剛剛妳A切換到B時設的resultCode
            case 1://當B傳回來的Intent的requestCode 等於當初A傳出去的話

                zip_areas =  data.getExtras().getString("name");
                textview2 = (TextView) this.findViewById(R.id.textView2);
                textview2.setText(zip_areas);

                zip_number =zip_areas.substring(1,4);
                        Geocoder geoCoder = new Geocoder(MapsActivity.this, Locale.getDefault());

                List<Address> addressLocation = null;
                try {
                    addressLocation = geoCoder.getFromLocationName(zip_areas, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                double latitude = addressLocation.get(0).getLatitude();
                double longitude = addressLocation.get(0).getLongitude();

                LatLng area_type = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(area_type));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(14));

                ListView list = (ListView) findViewById(R.id.listView);
                final ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(this,
                                android.R.layout.simple_list_item_1,
                                android.R.id.text1);
                list.setAdapter(adapter);

                final Firebase myFirebaseRef = new Firebase("https://vendor-5acbc.firebaseio.com/Vendors");


                ChildEventListener childEventListener = myFirebaseRef.addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        if (dataSnapshot.child("Location/ZIP").getValue().toString().equals(zip_number)) {
                            adapter.add((String) dataSnapshot.child("Information/Name").getValue());
                            int store_number;
                            store_number = ((String) dataSnapshot.child("Information/Name").getValue()).length();
                            if (store_number > 0) {
                                Toast.makeText(MapsActivity.this, "目前有" + store_number + "筆", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MapsActivity.this, "您沒有選擇任何項目" + store_number, Toast.LENGTH_LONG).show();
                            }
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





                break;

        }
    }








    }










/**
 // Obtain the SupportMapFragment and get notified when the map is ready to be used.
 SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
 .findFragmentById(R.id.fragment);

 mapFragment.getMapAsync(this);

 */


        /*

        myFirebaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Location loca = postSnapshot.getValue(Location.class);
                   myFirebaseRef.child(snapshot.getKey()).setValue(loca);
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });

    }

**/
/**
 Lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
@Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
new Firebase("https://vendor-5acbc.firebaseio.com/Vendors")
.orderByChild("Location")
.equalTo((String) Lst.getItemAtPosition(position))
.addListenerForSingleValueEvent(new ValueEventListener() {
public void onDataChange(DataSnapshot dataSnapshot) {
if (dataSnapshot.hasChildren()) {
DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
firstChild.getRef().removeValue();
}
}

public void onCancelled(FirebaseError firebaseError) {
}
});
}
});
 }
 */


/**
 ListView listView =(ListView) findViewById(R.id.listView);

 final List<Location> locations=new LinkedList<>();
 final ArrayAdapter<Location> adapter=new ArrayAdapter<Location>(
 this, android.R.layout.two_line_list_item,locations
 ){
@Override public View getView(int position, View view, ViewGroup parent) {
if (view == null) {
view = getLayoutInflater().inflate(android.R.layout.two_line_list_item, parent, false);
}
Location loca = locations.get(position);
((TextView) view.findViewById(android.R.id.text1)).setText(loca.Address);
((TextView) view.findViewById(android.R.id.text2)).setText(loca.ZIP);

((TextView) view.findViewById(android.R.id.text1)).setText(String.format("%.2f", loca.getLongitude()));
((TextView) view.findViewById(android.R.id.text2)).setText(String.format("%.2f", loca.getLatitude()));

return view;
}
};

 listView.setAdapter(adapter);

 ChildEventListener childEventListener = rootRef.addChildEventListener(new ChildEventListener() {
@Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
Location loca = dataSnapshot.getValue(Location.class);
locations.add(loca);
adapter.notifyDataSetChanged();

}

@Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {

}

@Override public void onChildRemoved(DataSnapshot dataSnapshot) {

}

@Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {

}

@Override public void onCancelled(FirebaseError firebaseError) {

}

});
 */


                /**
                 * Manipulates the map once available.
                 * This callback is triggered when the map is ready to be used.
                 * This is where we can add markers or lines, add listeners or move the camera. In this case,
                 * we just add a marker near Sydney, Australia.
                 * If Google Play services is not installed on the device, the user will be prompted to install
                 * it inside the SupportMapFragment. This method will only be triggered once the user has
                 * installed Google Play services and returned to the app.
                 */




