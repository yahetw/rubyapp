package com.example.jiarou.sharelove;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;

/**
 * Created by Peter on 2016/8/6
 */
public class FocusFragment extends Fragment {


    ListView focusListView;

    final static ArrayList<String> vendorTitleList = new ArrayList<>();
    final static ArrayList<String> vendorPicList = new ArrayList<>();
    final static ArrayList<String> vendorPhoneList = new ArrayList<>();
    final static ArrayList<String> vendorAddressList = new ArrayList<>();
    /*
    final static ArrayList<String> vendorPhoneList = new ArrayList<>();
    final static ArrayList<String> timeRemarkList= new ArrayList<>();
    final static ArrayList<String> monTimeList = new ArrayList<>();
    final static ArrayList<String> tueTimeList = new ArrayList<>();
    final static ArrayList<String> wedTimeList = new ArrayList<>();
    final static ArrayList<String> thurTimeList = new ArrayList<>();
    final static ArrayList<String> friTimeList = new ArrayList<>();
    final static ArrayList<String> satTimeList = new ArrayList<>();
    final static ArrayList<String> sunTimeList = new ArrayList<>();
    final static ArrayList<String> vendorAddressList = new ArrayList<>();
    final static ArrayList<String> vendorStoryList = new ArrayList<>();
    */

    private OnFocusSelected mListener;

    final static String DB_URL = "https://vendor-5acbc.firebaseio.com/Vendors";

    String imgurURL = "http://i.imgur.com/";

    public static FocusFragment newInstance(){
        Bundle args = new Bundle();

        FocusFragment fragment = new FocusFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public FocusFragment(){

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnFocusSelected){
            mListener = (OnFocusSelected)context;
        }else{
            throw new ClassCastException(context.toString() + "must implement OnFocusSelected");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        final View view = inflater.inflate(R.layout.focus_fragment, container, false);

        focusListView = (ListView)view.findViewById(R.id.focusListView);
        //Toolbar my_toolbar= (Toolbar)view.findViewById(R.id.my_toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(my_toolbar);
    //    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("焦點");

        connectToFirebase();

        return view;
    }

    public void connectToFirebase(){

        final CustomAdapter adapter = new CustomAdapter(this.getActivity(), vendorTitleList, vendorPicList,
                vendorPhoneList, vendorAddressList);

        Firebase.setAndroidContext(this.getActivity());

        final Firebase vendor = new Firebase(DB_URL);


        Query focusVendor = vendor.orderByChild("Focus").equalTo(true);

        focusVendor.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String title = (String)dataSnapshot.child("Information").child("Name").getValue();
                String picId = (String)dataSnapshot.child("Photos").child("Photo_ID").getValue();
                String phone = (String)dataSnapshot.child("Information").child("Phone").getValue();
                String address = (String)dataSnapshot.child("Location").child("Address").getValue();
                /*
                String phone = (String)dataSnapshot.child("Information").child("Phone").getValue();
                String remark = (String)dataSnapshot.child("Open_Days").child("Remark").getValue();
                String monOpen = (String)dataSnapshot.child("Open_Days").child("Mon").child("Open_At").getValue();
                String monClose = (String)dataSnapshot.child("Open_Days").child("Mon").child("Close_At").getValue();
                String mon = monOpen + "~" + monClose;
                String tueOpen = (String)dataSnapshot.child("Open_Days").child("Tue").child("Open_At").getValue();
                String tueClose = (String)dataSnapshot.child("Open_Days").child("Tue").child("Close_At").getValue();
                String tue = tueOpen + "~" + tueClose;
                String wedOpen = (String)dataSnapshot.child("Open_Days").child("Wed").child("Open_At").getValue();
                String wedClose = (String)dataSnapshot.child("Open_Days").child("Wed").child("Close_At").getValue();
                String wed = wedOpen + "~" + wedClose;
                String thuOpen = (String)dataSnapshot.child("Open_Days").child("Thu").child("Open_At").getValue();
                String thuClose = (String)dataSnapshot.child("Open_Days").child("Thu").child("Close_At").getValue();
                String thu = thuOpen + "~" + thuClose;
                String friOpen = (String)dataSnapshot.child("Open_Days").child("Fri").child("Open_At").getValue();
                String friClose = (String)dataSnapshot.child("Open_Days").child("Fri").child("Close_At").getValue();
                String fri = friOpen + "~" + friClose;
                String satOpen = (String)dataSnapshot.child("Open_Days").child("Sat").child("Open_At").getValue();
                String satClose = (String)dataSnapshot.child("Open_Days").child("Sat").child("Close_At").getValue();
                String sat = satOpen + "~" + satClose;
                String sunOpen = (String)dataSnapshot.child("Open_Days").child("Sun").child("Open_At").getValue();
                String sunClose = (String)dataSnapshot.child("Open_Days").child("Sun").child("Close_At").getValue();
                String sun = sunOpen + "~" + sunClose;
                String address = (String)dataSnapshot.child("Location").child("Address").getValue();
                String story = (String)dataSnapshot.child("Information").child("Introduction").getValue();
                */

                String pic = imgurURL + picId + ".jpg";
                vendorPicList.add(pic);
                System.out.println(pic);
                pic = imgurURL;

                vendorTitleList.add(title);
                vendorPhoneList.add(phone);
                vendorAddressList.add(address);
                /*
                vendorPhoneList.add(phone);
                timeRemarkList.add(remark);
                monTimeList.add(mon);
                tueTimeList.add(tue);
                wedTimeList.add(wed);
                thurTimeList.add(thu);
                friTimeList.add(fri);
                satTimeList.add(sat);
                sunTimeList.add(sun);
                vendorAddressList.add(address);
                vendorStoryList.add(story);
                */

                focusListView.setAdapter(adapter);
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

        vendorTitleList.clear();
        vendorPicList.clear();
        vendorPhoneList.clear();
        vendorAddressList.clear();
        /*
        vendorPhoneList.clear();
        timeRemarkList.clear();
        monTimeList.clear();
        tueTimeList.clear();
        wedTimeList.clear();
        thurTimeList.clear();
        friTimeList.clear();
        satTimeList.clear();
        sunTimeList.clear();
        vendorAddressList.clear();
        vendorStoryList.clear();
        */
    }

    public interface OnFocusSelected{
        void onFocusSelected(String vendorTitle/*, String vendorURL, String vendorPhone, String timeRemark,
                             String monTime, String tueTime, String wedTime, String thuTime, String friTime,
                             String satTime, String sunTime, String vendorAddress, String vendorStory*/);
    }

    public class CustomAdapter extends BaseAdapter {

        Context c;

        ArrayList<String> vendorTitle;

        ArrayList<String> vendorPic;
        ArrayList<String> vendorPhone;
        ArrayList<String> vendorAddress;
        /*

        ArrayList<String> vendorPhone;

        ArrayList<String> timeRemark;
        ArrayList<String> monTime;
        ArrayList<String> tueTime;
        ArrayList<String> wedTime;
        ArrayList<String> thuTime;
        ArrayList<String> friTime;
        ArrayList<String> satTime;
        ArrayList<String> sunTime;

        ArrayList<String> vendorAddress;

        ArrayList<String> vendorStory;
        */

        public CustomAdapter(Context context, ArrayList<String> vendorTitle, ArrayList<String> vendorPic,
                             ArrayList<String> vendorPhone, ArrayList<String> vendorAddress/*,
                             ArrayList<String> vendorPhone, ArrayList<String> timeRemark, ArrayList<String> monTime,
                             ArrayList<String> tueTime, ArrayList<String> wedTime, ArrayList<String> thuTime,
                             ArrayList<String> friTime, ArrayList<String> satTime, ArrayList<String> sunTime,
                             ArrayList<String> vendorAddress, ArrayList<String> vendorStory*/){
            c = context;
            this.vendorTitle = vendorTitle;
            this.vendorPic = vendorPic;
            this.vendorPhone = vendorPhone;
            this.vendorAddress = vendorAddress;
            /*
            this.vendorPhone = vendorPhone;
            this.timeRemark = timeRemark;
            this.monTime = monTime;
            this.tueTime = tueTime;
            this.wedTime = wedTime;
            this.thuTime = thuTime;
            this.friTime = friTime;
            this.satTime = satTime;
            this.sunTime = sunTime;
            this.vendorAddress = vendorAddress;
            this.vendorStory = vendorStory;
            */

        }

        @Override
        public int getCount() {
            return vendorTitle.size();
        }

        @Override
        public Object getItem(int position) {
            return vendorTitle.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View list;
            LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            list = inflater.inflate(R.layout.custom_listview, null);

            TextView listTextView = (TextView)list.findViewById(R.id.listTextView);
            ImageView listImageView = (ImageView)list.findViewById(R.id.listImageView);
            TextView listPhoneView = (TextView)list.findViewById(R.id.focusPhoneTextView);
            TextView listAddressView = (TextView)list.findViewById(R.id.focusAddressTextView);

            final String title = vendorTitle.get(position);
            listTextView.setText(title);

            final String vendorURL = vendorPic.get(position);
            //Do bitmapTask
            DownloadImageTask downloadImage = new DownloadImageTask(listImageView);
            downloadImage.execute(vendorURL);

            final String phone = vendorPhone.get(position);
            listPhoneView.setText(phone);

            final String address = vendorAddress.get(position);
            listAddressView.setText(address);

            /*
            final String phone = vendorPhone.get(position);

            final String remark = timeRemark.get(position);

            final String mon = monTime.get(position);

            final String tue = tueTime.get(position);

            final String wed = wedTime.get(position);

            final String thur = thuTime.get(position);

            final String fri = friTime.get(position);

            final String sat = satTime.get(position);

            final String sun = sunTime.get(position);

            final String address = vendorAddress.get(position);

            final String story = vendorStory.get(position);
            */

            list.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    mListener.onFocusSelected(title/*, vendorURL, phone, remark, mon, tue, wed, thur,
                            fri, sat, sun, address, story*/);
                }
            });


            return list;
        }
    }

}
