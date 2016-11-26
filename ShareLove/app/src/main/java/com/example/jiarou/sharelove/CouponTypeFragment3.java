package com.example.jiarou.sharelove;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by chiayi on 16/11/13.
 */
public class CouponTypeFragment3 extends Fragment {

    TextView member_owned_points;
    Button coupon_type1;
    Button coupon_type2;
    Button coupon_type3;
    GridView gridView;
    RadioGroup radioGroup1;

    final  static ArrayList<Long> couponPriceList= new ArrayList<>();
    final static ArrayList<String> couponInfoList= new ArrayList<>();
    final static ArrayList<String> couponNameList= new ArrayList<>();
    final static ArrayList<String> couponImagesList=new ArrayList<>();
    final static String Coupn_DB_URL ="https://coupon-da649.firebaseio.com/";
    final static String MEMBER_DB_URL = "https://member-139bd.firebaseio.com/";
    String imgurURL = "http://i.imgur.com/";
    GlobalVariable globalVariable = (GlobalVariable)getApplicationContext();
    Long facebookID = Long.parseLong(globalVariable.getUserId());
    private OnCouponSelected mListener;



    public static CouponTypesFragment newInstance() {

        CouponTypesFragment fragment = new CouponTypesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);


        return fragment;
    }


    public CouponTypeFragment3(){

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

        connectToCouponFirebase("豪華");
        connectToMemberFirebase();

        //將畫面設為coupon_types_fragment.xml
        final View view = inflater.inflate(R.layout.coupon_types_fragment, container, false);

        //Coupon types page button (change page)
        coupon_type1=(Button)view.findViewById(R.id.coupon_type1);
        coupon_type2=(Button)view.findViewById(R.id.coupon_type2);
        coupon_type3=(Button)view.findViewById(R.id.coupon_type3);

        coupon_type1.setOnClickListener(ChangePage);
        coupon_type2.setOnClickListener(ChangePage);
        coupon_type3.setOnClickListener(ChangePage);


//        radioGroup1 = (RadioGroup) view.findViewById(R.id.radioGroup1);
//        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId){
//                    case R.id.button:
//                        connectToCouponFirebase("平價");
//                        break;
//                    case R.id.button2:
//                        connectToCouponFirebase("中等");
//                        break;
//
//                    case R.id.button3:
//                        connectToCouponFirebase("豪華");
//                        break;
//
//                    default:
//                        break;
//                }
//            }
//        });

        //宣告上方的會員擁有點數
        member_owned_points = (TextView) view.findViewById(R.id.textView2);

        //宣告 GridView
        gridView=(GridView)view.findViewById(R.id.gridView);
        return view;
    }




    //Change Pages Button
    private View.OnClickListener ChangePage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId()== R.id.coupon_type1){
                Fragment fragment = new CouponTypesFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.root_layout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
            else if (v.getId()== R.id.coupon_type2){
                Fragment fragment = new CouponTypeFragment2();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.root_layout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }else {
                Fragment fragment = new CouponTypeFragment3();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.root_layout, fragment);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }


            Fragment fragment3 = new CouponTypeFragment3();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.detach(fragment3);

        }
    };



    //連接到 Member 的 Firebase
    public void connectToMemberFirebase(){
        Firebase.setAndroidContext(this.getActivity());
        final Firebase memberRef = new Firebase(MEMBER_DB_URL);

        Query memberQuery = memberRef.orderByChild("Facebook_ID").equalTo(facebookID);
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
