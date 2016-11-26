package com.example.chiayi.myapplication;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chiayi on 16/7/29.
 */
public class CouponDetailsFragment extends Fragment implements BuyItDialogFragment.Listener, FinishDialogFragment.FListener{

    final static String MEMBER_DB_URL = "https://member-139bd.firebaseio.com/";
    final Firebase memberRef = new Firebase(MEMBER_DB_URL);
    final Firebase couponRef = new Firebase("https://coupon-da649.firebaseio.com/");
    TextView amountTextView;
    BuyItDialogFragment dialog = new BuyItDialogFragment();
    FinishDialogFragment f_dialog = new FinishDialogFragment();
    Long owned_points=null;
    Long coupon_amounts=null;
    Long coupon_amount = null;
    String key; //Coupon_ID
    String info; //使用者購買的優惠券的info 用來更新資料庫的



    private static final String ARGUMENT_NAME = "CouponName";
    private static final String ARGUMENT_PRICE = "Price";
    private static final String ARGUMENT_IMAGE_URL = "ImageURL";
    private static final String ARGUMENT_INFO = "Info";



    public static CouponDetailsFragment newInstance(String CouponName, String Price, String ImageURL, String Info){

        final Bundle args = new Bundle();
        args.putString(ARGUMENT_NAME,CouponName);
        args.putString(ARGUMENT_PRICE,Price);
        args.putString(ARGUMENT_IMAGE_URL,ImageURL);
        args.putString(ARGUMENT_INFO,Info);


        final CouponDetailsFragment fragment= new CouponDetailsFragment();
        fragment.setArguments(args);

        return fragment;

    }

    public CouponDetailsFragment(){
        //empty constructors
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){


        RetrieveMemberData(memberRef);

        final View view = inflater.inflate(R.layout.coupon_details_fragment, container, false);
        final TextView nameTextView = (TextView) view.findViewById(R.id.textView4);
        final TextView priceTextView = (TextView) view.findViewById(R.id.textView6);
        amountTextView = (TextView) view.findViewById(R.id.textView8);
        final TextView infoTextView = (TextView) view.findViewById(R.id.textView12);
        final ImageView couponImage = (ImageView) view.findViewById(R.id.imageView);
        final Button buy_btn= (Button) view.findViewById(R.id.buy_btn);

        final Bundle args = getArguments();
        nameTextView.setText(args.getString(ARGUMENT_NAME));
        priceTextView.setText(args.getString(ARGUMENT_PRICE));
        DownloadImageTask downloadImage = new DownloadImageTask(couponImage);
        downloadImage.execute(args.getString(ARGUMENT_IMAGE_URL));
        info = args.getString(ARGUMENT_INFO);
        infoTextView.setText(args.getString(ARGUMENT_INFO));
        ConnectToCoupon(args.getString(ARGUMENT_NAME));
        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();

            }
        });
        return view;
    }



    //連接到 Member 的 Firebase、取得使用者擁有的點數
    public void RetrieveMemberData(Firebase memberRef){

        Firebase.setAndroidContext(this.getActivity());
        Query memberQuery = memberRef.orderByChild("Facebook_ID").equalTo(111111111111111l);
        memberQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Long owned_point = (Long) dataSnapshot.child("Owned_Points").getValue();
                owned_points = owned_point;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Long owned_point = (Long) dataSnapshot.child("Owned_Points").getValue();
                owned_points = owned_point;
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


    /***
     * buy_it_dialog、CheckOwnedPoints(檢查可不可以購買)、更新使用者的愛心點數、更新優惠券剩餘數量
     */
    // buy_it_dialog
    private void showDialog(){
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "dialog");

    }

    // 按下確定鈕
    @Override
    public void onPositiveClick() {
        CheckOwnedPoints();
    }

    // 按返回鈕
    @Override
    public void onNegativeClick() {
        dialog.dismiss();
    }


    //CheckOwnedPoints(檢查可不可以購買)、儲存新的剩餘數量到資料庫
    public void CheckOwnedPoints() {

        final Bundle args = getArguments();

        String coupon_name = args.getString(ARGUMENT_NAME);
        Long coupon_price = Long.parseLong(args.getString(ARGUMENT_PRICE));

        if (coupon_amount > 0) {

            if (owned_points >= coupon_price) {
                owned_points = owned_points - coupon_price;
                coupon_amounts = coupon_amount - 1;
                ChangeOwnedPoint(memberRef);
                couponRef.child(key).child("Remaining_Amount").setValue(coupon_amounts);
                ConnectToCoupon(coupon_name);
                showFinishDialog();


            } else {
                Toast.makeText(getContext(), "很抱歉，您的點數不足以購買此優惠券", Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(getContext(), "很抱歉，目前此優惠券數量不足無法購買", Toast.LENGTH_LONG).show();
        }

    }


    // 更新使用者擁有的點數到資料庫 Member-Owned_Points
    public void ChangeOwnedPoint(Firebase memberRef){

        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
        Date dt = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.MONTH,1);
        Date tdt = calendar.getTime();
        String time = formatter.format(tdt);

        //要再查一下如何知道他的ID?
        memberRef.child("-KPH9T4n7OuJQcVj_u4B").child("Owned_Points").setValue(owned_points);
        Map <String,String> My_Coupon = new HashMap<String,String>();
        My_Coupon.put("Coupon_ID",key);
        My_Coupon.put("Due_Date",time);
        My_Coupon.put("Information",info);

        memberRef.child("-KPH9T4n7OuJQcVj_u4B").child("Owned_Coupons").push().setValue(My_Coupon);


        //memberRef.child("-KPH9T4n7OuJQcVj_u4B").child("Owned_Coupons").child(key).child("Information").setValue(info);


    }

    //取得最新的剩餘數量並設到畫面上 Coupon-Remaining_Amount
    public void ConnectToCoupon(String coupon_name){

        Query query = couponRef.orderByChild("Name").equalTo(coupon_name);
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    key = ds.getKey();
                    amountTextView.setText(String.valueOf(ds.child("Remaining_Amount").getValue()));
                    coupon_amount = Long.valueOf(String.valueOf(ds.child("Remaining_Amount").getValue()));
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }





    /***
     * final_it_dialog -> 跳轉畫面
     */

    private void showFinishDialog(){
        f_dialog.setTargetFragment(this, 0);
        f_dialog.show(getFragmentManager(), "Finish_dialog");
        Toast.makeText(getContext(), "您剩餘的愛心點數:" + owned_points + "點", Toast.LENGTH_LONG).show();
    }


    @Override
    public void OwnedCouponClick() {
        dialog.dismiss();
    }

    @Override
    public void GoBackClick() {
        dialog.dismiss();
    }








}
