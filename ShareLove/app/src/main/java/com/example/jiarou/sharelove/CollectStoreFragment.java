package com.example.jiarou.sharelove;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
 * Created by chiayi on 16/8/17.
 */
public class CollectStoreFragment extends Fragment {


    ListView collectVendorList;

    final static ArrayList<String> vendorTitleList = new ArrayList<>();
    final static ArrayList<String> vendorPicList = new ArrayList<>();
    final static ArrayList<String> vendorKeyList = new ArrayList<>();

    final String[] key = {""};

    final static String DB_URL = "https://vendor-5acbc.firebaseio.com/Vendors";
    final static String DB_MEMBER_URL = "https://member-139bd.firebaseio.com/";

    String imgurURL = "http://i.imgur.com/";

    private OnFocusSelected mListener;


    public static CollectStoreFragment newInstance(){
        Bundle args = new Bundle();

        CollectStoreFragment collectStoreFragment = new CollectStoreFragment();
        collectStoreFragment.setArguments(args);
        return collectStoreFragment;
    }

    public CollectStoreFragment(){

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.collect_store_fragment,container,false);
        Firebase.setAndroidContext(this.getActivity());
        collectVendorList = (ListView)view.findViewById(R.id.collectListView);

        final CustomAdapter adapter = new CustomAdapter(this.getActivity(), vendorTitleList, vendorPicList);

        GlobalVariable globalVariable = (GlobalVariable)getActivity().getApplicationContext();
        String userId =globalVariable.getUserId();

        final Firebase member = new Firebase(DB_MEMBER_URL);
        Long userLongId = Long.parseLong(userId, 10);
        Query findMember = member.orderByChild("Facebook_ID").equalTo(userLongId);
        findMember.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot exSnapshot: dataSnapshot.child("Favorite_Vendors").getChildren()){
                    String key = (String)exSnapshot.child("Vendor_ID").getValue();
                    final Firebase vendor = new Firebase(DB_URL);
                    Query vendor2 = vendor.orderByKey().equalTo(key);
                    vendor2.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String title = (String)dataSnapshot.child("Information/Name").getValue();
                            String picId = (String)dataSnapshot.child("Photos/Photo_ID").getValue();

                            String pic = imgurURL + picId + ".jpg";
                            vendorPicList.add(pic);
                            vendorTitleList.add(title);
                            collectVendorList.setAdapter(adapter);
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

        vendorPicList.clear();
        vendorTitleList.clear();

        return view;
    }





    public interface OnFocusSelected{
        void OnFocusSelected(String vendorTitle);
    }

    public class CustomAdapter extends BaseAdapter{

        Context c;
        ArrayList<String> vendorTitle;
        ArrayList<String> vendorPic;

        public CustomAdapter(Context context, ArrayList<String> vendorTitle, ArrayList<String> vendorPic){
            c = context;
            this.vendorTitle = vendorTitle;
            this.vendorPic = vendorPic;
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

            list = inflater.inflate(R.layout.collect_store_list, null);

            TextView collectTextView = (TextView)list.findViewById(R.id.collectTextView);
            ImageView collectImageView = (ImageView)list.findViewById(R.id.collectImageView);

            final String title = vendorTitle.get(position);
            collectTextView.setText(title);

            final String vendorURL = vendorPic.get(position);
            DownloadImageTask downloadImageTask = new DownloadImageTask(collectImageView);
            downloadImageTask.execute(vendorURL);

            list.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mListener.OnFocusSelected(title);
                }
            });

            return list;
        }
    }



}
