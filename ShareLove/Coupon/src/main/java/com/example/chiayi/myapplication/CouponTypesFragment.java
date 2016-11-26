package com.example.chiayi.myapplication;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;

import static com.example.chiayi.myapplication.R.id.button;
import static com.example.chiayi.myapplication.R.id.button2;
import static com.example.chiayi.myapplication.R.id.button3;
import static com.example.chiayi.myapplication.R.id.textView2;

/**
 * Created by chiayi on 16/7/27.
 */
public class CouponTypesFragment extends Fragment{


    TextView member_owned_points;
    Button coupon_type1;
    Button coupon_type2;
    Button coupon_type3;
    GridView gridView;

    final  static ArrayList<Long> couponPriceList= new ArrayList<>();
    final static ArrayList<String> couponInfoList= new ArrayList<>();
    final static ArrayList<String> couponNameList= new ArrayList<>();
    final static ArrayList<String> couponImagesList=new ArrayList<>();
    final static String Coupn_DB_URL ="https://coupon-da649.firebaseio.com/";
    final static String MEMBER_DB_URL = "https://member-139bd.firebaseio.com/";
    String imgurURL = "http://i.imgur.com/";
    private OnCouponSelected mListener;



    public static CouponTypesFragment newInstance() {

        Bundle args = new Bundle();
        CouponTypesFragment fragment = new CouponTypesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public CouponTypesFragment(){

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof OnCouponSelected) {
            mListener = (OnCouponSelected) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnCouponSelected.");
        }




    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        connectToCouponFirebase("平價");
        connectToMemberFirebase();



        //將畫面設為coupon_types_fragment.xml
        final View view = inflater.inflate(R.layout.coupon_types_fragment,container,false);

        //Coupon types page button (change page)
        coupon_type1=(Button)view.findViewById(button);
        coupon_type2=(Button)view.findViewById(button2);
        coupon_type3=(Button)view.findViewById(button3);

        coupon_type1.setOnClickListener(ChangePage);
        coupon_type2.setOnClickListener(ChangePage);
        coupon_type3.setOnClickListener(ChangePage);

        //宣告上方的會員擁有點數
        member_owned_points = (TextView) view.findViewById(textView2);

        //宣告 GridView
        gridView=(GridView)view.findViewById(R.id.gridView);
        return view;
    }


    //Change Pages Button
    private View.OnClickListener ChangePage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId()== button){
                connectToCouponFirebase("平價");
            }
            else if (v.getId()== button2){
                connectToCouponFirebase("中等");
            }else {
                connectToCouponFirebase("豪華");
            }
        }
    };



    //連接到 Member 的 Firebase
    public void connectToMemberFirebase(){
        Firebase.setAndroidContext(this.getActivity());
        final Firebase memberRef = new Firebase(MEMBER_DB_URL);

        Query memberQuery = memberRef.orderByChild("Facebook_ID").equalTo(111111111111111l);
        memberQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Long owned_points= (Long) dataSnapshot.child("Owned_Points").getValue();
                member_owned_points.setText(String.valueOf(owned_points));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Long owned_points= (Long) dataSnapshot.child("Owned_Points").getValue();
                member_owned_points.setText(String.valueOf(owned_points));

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




    //連接Firebase取出名字、價錢、照片，置入ArrayList中
    public void connectToCouponFirebase(String couponType){
        Firebase.setAndroidContext(this.getActivity());
        //Adapter宣告
        final CustomAdapter adapter= new CustomAdapter(this.getActivity(), couponNameList, couponPriceList, couponImagesList, couponInfoList);
        //Connection Firebase
        final Firebase couponRef = new Firebase(Coupn_DB_URL);
        //Query
        Query budgetRef = couponRef.orderByChild("Type").equalTo(couponType);
        budgetRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Long price = (Long) dataSnapshot.child("Price").getValue();
                String name = (String) dataSnapshot.child("Name").getValue();
                String photoId = (String) dataSnapshot.child("Photos").child("Photo_ID").getValue();
                String information = (String) dataSnapshot.child("Information").getValue();

                //CouponURL是完整url
                String couponURL = imgurURL + photoId + ".jpg";
                couponImagesList.add(couponURL);
                couponNameList.add(name);
                couponPriceList.add(price);
                couponInfoList.add(information);


                gridView.setAdapter(adapter);


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

        couponImagesList.clear();
        couponNameList.clear();
        couponPriceList.clear();
        couponInfoList.clear();
    }


    //Interface
    public interface OnCouponSelected{
        void onCouponSelected(String CouponName, String Price, String couponURL, String info);
    }


    //Adapter
    public class CustomAdapter extends BaseAdapter {

        final ArrayList<String> couponName;
        final ArrayList<Long> couponPrice;
        Context context;
        ArrayList<String> couponImage;
        ArrayList<String> couponInfo;


        public CustomAdapter(Context c, final ArrayList<String> couponName, final ArrayList<Long> couponPrice,  ArrayList<String> couponImage, ArrayList<String> couponInfo){

            context = c;
            this.couponImage=couponImage;
            this.couponPrice=couponPrice;
            this.couponName=couponName;
            this.couponInfo=couponInfo;


        }


        @Override
        public int getCount() {
            return couponName.size();
        }

        @Override
        public Object getItem(int position) {
            return couponName.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View grid;
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            grid = inflater.inflate(R.layout.custom_gridview, null);
            TextView textView = (TextView) grid.findViewById(R.id.textView1);
            TextView textView2 = (TextView) grid.findViewById(R.id.textView2);
            ImageView imageView = (ImageView)grid.findViewById(R.id.imageView1);


            final String name = couponName.get(position);
            textView.setText(name);


            final String price = String.valueOf(couponPrice.get(position));
            textView2.setText(price);


            final String couponURL = couponImage.get(position);
            DownloadImageTask downloadImage = new DownloadImageTask(imageView);
            downloadImage.execute(couponURL);


            final String info = couponInfo.get(position);





            grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCouponSelected(name, price, couponURL, info);

                }
            });
            return grid;
        }



    }







}


