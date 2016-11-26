package com.example.jiarou.sharelove;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by chiayi on 16/8/16.
 */
public class OwnedCouponFragment extends Fragment implements UseCouponDialogFragment.Listener {


    Integer POSITION = null;
    ListView owned_coupon_list;
    GlobalVariable globalVariable = (GlobalVariable)getApplicationContext();
    Long facebookID = Long.parseLong(globalVariable.getUserId());
    String imgurURL = "http://i.imgur.com/";
    String member_ID;
    private CustomAdapter adapter = null;
    UseCouponDialogFragment dialog = new UseCouponDialogFragment();
    final static String MEMBER_DB_URL = "https://member-139bd.firebaseio.com/";
    final static String COUPON_DB_URL = "https://coupon-da649.firebaseio.com/";
    final static ArrayList<String> couponIDList = new ArrayList<>();
    final static ArrayList<String> couponDueDateList = new ArrayList<>();
    final static ArrayList<String> couponInfoList = new ArrayList<>();
    final static ArrayList<String> couponkeyList = new ArrayList<>();
    ArrayList<String> couponNameList = new ArrayList<>();
    final static ArrayList<String> photoIDList = new ArrayList<>();


    public static OwnedCouponFragment newInstance() {
        return new OwnedCouponFragment();
    }

    public OwnedCouponFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.owned_coupon_fragment, container, false);
        owned_coupon_list = (ListView) view.findViewById(R.id.listView);
        connectToMemberFirebase();
        return view;
    }



    public void checkDate(){


        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        Date current = new Date();
        String c_time = sdformat.format(current);
        Date date1 = null;
        Date date2 = null;
        int i =0;

        for(int d=0 ; d<couponkeyList.size() ; d++){


            try {
                date1 = sdformat.parse(couponDueDateList.get(d));
                date2 = sdformat.parse(c_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }


         if (date1.compareTo(date2)<0){

             i++;
             final Firebase member_db = new Firebase(MEMBER_DB_URL);
             member_db.child(member_ID).child("Owned_Coupons").child(couponkeyList.get(d)).removeValue();
             couponkeyList.remove(d);
             couponIDList.remove(d);
             couponInfoList.remove(d);
             couponDueDateList.remove(d);
             Toast.makeText(getContext(), "您的部分優惠券已過期，系統已刪除。", Toast.LENGTH_LONG).show();


         }else {



         }


        }
    }



    public void connectToMemberFirebase() {
        Firebase.setAndroidContext(this.getActivity());
        final Firebase member_db = new Firebase(MEMBER_DB_URL);

        Query query = member_db.orderByChild("Facebook_ID").equalTo(facebookID);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                HashMap<String, Map<String, String>> id = (HashMap<String, Map<String, String>>) dataSnapshot.child("Owned_Coupons").getValue();


                    if(id==null){




                    }else {

                        if(dataSnapshot.child("Owned_Coupons").getValue()==""){


                            //Do nothing


                        }else{

                            for (Map.Entry<String, Map<String, String>> entry : id.entrySet()) {
                                String key = entry.getKey();
                                String coupon_id = id.get(key).get("Coupon_ID");
                                String due_date = id.get(key).get("Due_Date");
                                String coupon_info = id.get(key).get("Information");

                                couponkeyList.add(key);
                                couponIDList.add(coupon_id);
                                couponInfoList.add(coupon_info);
                                couponDueDateList.add(due_date);
                                member_ID = dataSnapshot.getKey();
                                checkDate();
                                getCouponNameandPic(coupon_id);

                            }


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

        couponkeyList.clear();
        couponDueDateList.clear();
        couponInfoList.clear();
        couponIDList.clear();
        couponNameList.clear();
        photoIDList.clear();

    }




    public void getCouponNameandPic(final String couponID){

        adapter = new CustomAdapter(this.getActivity(), couponIDList, couponDueDateList, couponInfoList, couponkeyList, couponNameList, photoIDList);
        final Firebase coupon_db = new Firebase(COUPON_DB_URL);
        coupon_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = (String) dataSnapshot.child(couponID).child("Name").getValue();
                String picId = (String) dataSnapshot.child(couponID).child("Photos").child("Photo_ID").getValue();
                String pic = imgurURL + picId + ".jpg";
                photoIDList.add(pic);
                couponNameList.add(name);
                owned_coupon_list.setAdapter(adapter);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }









    private void UseCouponDialog(){
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "UseCouponDialog");

    }



    @Override
    public void onPositiveClick() {
        adapter.remove(POSITION);
    }

    @Override
    public void onNegativeClick() {

        dialog.dismiss();
    }


    public class CustomAdapter extends BaseAdapter {

        Context c;

        ArrayList<String> couponID;

        ArrayList<String> couponDueDate;

        ArrayList<String> couponInfo;

        ArrayList<String> couponkey;

        ArrayList<String> couponName;

        ArrayList<String> photoID;


        public CustomAdapter(Context context, ArrayList<String> couponIDList, ArrayList<String> couponDueDateList, ArrayList<String> couponInfoList, ArrayList<String> couponkeyList, ArrayList<String> couponNameList, ArrayList<String> photoIDList) {
            c = context;
            this.couponID = couponIDList;
            this.couponDueDate = couponDueDateList;
            this.couponInfo = couponInfoList;
            this.couponkey = couponkeyList;
            this.couponName = couponNameList;
            this.photoID = photoIDList;
        }


        @Override
        public int getCount() {
            return couponID.size();
        }

        @Override
        public Object getItem(int position) {
            return couponID.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        public void remove(int position){

            System.out.println(position);
            final String key = couponkey.get(position);
            final Firebase member_db = new Firebase(MEMBER_DB_URL);
            member_db.child(member_ID).child("Owned_Coupons").child(key).removeValue();
            couponID.remove(position);
            couponDueDate.remove(position);
            couponkey.remove(position);
            couponName.remove(position);
            photoID.remove(position);
            adapter.notifyDataSetChanged();
            System.out.println(couponName);


        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


                View list;
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                list = inflater.inflate(R.layout.custom_owned_coupon_listview, null);

                ImageView listImageView = (ImageView) list.findViewById(R.id.listImageView);
                TextView coupon_name = (TextView) list.findViewById(R.id.coupon_name);
                TextView coupon_id = (TextView) list.findViewById(R.id.coupon_id);
                TextView due_date = (TextView) list.findViewById(R.id.due_date);
                Button use_btn = (Button) list.findViewById(R.id.use_btn);


                final String id = couponID.get(position);
                coupon_id.setText(id);


                final String date = couponDueDate.get(position);
                due_date.setText(date);


                final String key = couponkey.get(position);


                final String name = couponName.get(position);
                coupon_name.setText(name);


                final String couponURL = photoID.get(position);
                DownloadImageTask downloadImage = new DownloadImageTask(listImageView);
                downloadImage.execute(couponURL);



                use_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        POSITION = position;
                        UseCouponDialog();

                    }
                });




                return list;


        }
    }


}



// OnFocusListener等要點listview進去時再用


