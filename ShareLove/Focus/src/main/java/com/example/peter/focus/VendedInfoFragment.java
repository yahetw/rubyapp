package com.example.peter.focus;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Peter on 2016/8/9.
 */
public class VendedInfoFragment extends Fragment {
    final static String DB_URL = "https://vendor-5acbc.firebaseio.com/Vendors";
    final static String DB_MEMBER_URL = "https://member-139bd.firebaseio.com/";
    String imgurURL = "http://i.imgur.com/";

    private static final String ARGUMENT_TITLE = "VendorTitle";
    /*
    private static final String ARGUMENT_VENDERURL = "VendorURL";
    private static final String ARGUMENT_PHONE = "VendorPhone";
    private static final String ARGUMENT_REMARK = "TimeRemark";
    private static final String ARGUMENT_MON = "MonTime";
    private static final String ARGUMENT_TUE = "TueTime";
    private static final String ARGUMENT_WED = "WedTime";
    private static final String ARGUMENT_THU = "ThuTime";
    private static final String ARGUMENT_FRI = "FriTime";
    private static final String ARGUMENT_SAT = "SatTime";
    private static final String ARGUMENT_SUN = "SunTime";
    private static final String ARGUMENT_ADDRESS = "VendorAddress";
    private static final String ARGUMENT_STORY = "VendorStory";
    */

    private OnCommentSelected mListener;
    private OnNavigationSelected mListener2;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    Bitmap bitmap;
    String forShareUse;

    //String key2 = "-KPH9T4n7OuJQcVj_u4B";
    Long counting ;
    Long mathth ;

    @Override
    public void onCreate(Bundle savedInstanceState){
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);


    }

    public static VendedInfoFragment newInstance(String vendorTitle/*, String vendorURL, String vendorPhone, String timeRemark,
                                                 String monTime, String tueTime, String wedTime, String thuTime, String friTime,
                                                 String satTime, String sunTime, String vendorAddress, String vendorStory*/){
        final Bundle args = new Bundle();
        args.putString(ARGUMENT_TITLE, vendorTitle);
        /*
        args.putString(ARGUMENT_VENDERURL, vendorURL);
        args.putString(ARGUMENT_PHONE, vendorPhone);
        args.putString(ARGUMENT_REMARK, timeRemark);
        args.putString(ARGUMENT_MON, monTime);
        args.putString(ARGUMENT_TUE, tueTime);
        args.putString(ARGUMENT_WED, wedTime);
        args.putString(ARGUMENT_THU, thuTime);
        args.putString(ARGUMENT_FRI, friTime);
        args.putString(ARGUMENT_SAT, satTime);
        args.putString(ARGUMENT_SUN, sunTime);
        args.putString(ARGUMENT_ADDRESS, vendorAddress);
        args.putString(ARGUMENT_STORY, vendorStory);
        */

        final VendedInfoFragment fragment = new VendedInfoFragment();
        fragment.setArguments(args);

        return fragment;

    }

    public VendedInfoFragment(){

    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnCommentSelected){
            mListener = (OnCommentSelected)context;
        }else{
            throw new ClassCastException(context.toString() + "must implement OnCommentSelected");
        }
        if(context instanceof OnNavigationSelected){
            mListener2 = (OnNavigationSelected)context;
        }else{
            throw new ClassCastException(context.toString() + "must implement OnNavigationSelected");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.vender_info_fragment, container, false);

        final TextView titleTextView = (TextView)view.findViewById(R.id.titleTextView);

        final ImageView vendorImageView = (ImageView)view.findViewById(R.id.vendorImageView);

        final TextView phoneTextView = (TextView)view.findViewById(R.id.phoneTextView);
        final TextView remarkTextView = (TextView)view.findViewById(R.id.remarkTextView);
        final TextView monTextView = (TextView)view.findViewById(R.id.monTextView);
        final TextView tueTextView = (TextView)view.findViewById(R.id.tueTextView);
        final TextView wedTextView = (TextView)view.findViewById(R.id.wedTextView);
        final TextView thuTextView = (TextView)view.findViewById(R.id.thuTextView);
        final TextView friTextView = (TextView)view.findViewById(R.id.friTextView);
        final TextView satTextView = (TextView)view.findViewById(R.id.satTextView);
        final TextView sunTextView = (TextView)view.findViewById(R.id.sunTextView);
        final TextView addressTextView = (TextView)view.findViewById(R.id.addressTextView);
        final TextView storyTextView = (TextView)view.findViewById(R.id.storyTextView);

        final ImageView fbShare = (ImageView)view.findViewById(R.id.fbShareImageView);
        final ImageView collect = (ImageView)view.findViewById(R.id.imageView3);
        final TextView count = (TextView)view.findViewById(R.id.textView12);
        final ImageView comment = (ImageView)view.findViewById(R.id.imageView2);
        final ImageView navigation = (ImageView)view.findViewById(R.id.imageView4);
        fbShare.bringToFront();

        final Bundle args = getArguments();
        titleTextView.setText(args.getString(ARGUMENT_TITLE));
        /*
        DownloadImageTask downloadImage = new DownloadImageTask(vendorImageView);
        downloadImage.execute(args.getString(ARGUMENT_VENDERURL));
        phoneTextView.setText(args.getString(ARGUMENT_PHONE));
        remarkTextView.setText(args.getString(ARGUMENT_REMARK));
        monTextView.setText(args.getString(ARGUMENT_MON));
        tueTextView.setText(args.getString(ARGUMENT_TUE));
        wedTextView.setText(args.getString(ARGUMENT_WED));
        thuTextView.setText(args.getString(ARGUMENT_THU));
        friTextView.setText(args.getString(ARGUMENT_FRI));
        satTextView.setText(args.getString(ARGUMENT_SAT));
        sunTextView.setText(args.getString(ARGUMENT_SUN));
        addressTextView.setText(args.getString(ARGUMENT_ADDRESS));
        storyTextView.setText(args.getString(ARGUMENT_STORY));
        */

        Firebase.setAndroidContext(this.getActivity());
        final Firebase vendor2 = new Firebase(DB_URL);
        String mark = args.getString(ARGUMENT_TITLE);

        Query focusVendor2 = vendor2.orderByChild("Information/Name").equalTo(mark);
        final String[] key = {""};

        focusVendor2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                key[0] = dataSnapshot.getKey();
                //Toast.makeText(getContext(), key[0], Toast.LENGTH_LONG).show();

                String picId = (String)dataSnapshot.child("Photos").child("Photo_ID").getValue();
                String pic = imgurURL + picId + ".jpg";
                DownloadImageTask downloadImage = new DownloadImageTask(vendorImageView);
                downloadImage.execute(pic);

                forShareUse = pic;

                String phone = (String) dataSnapshot.child("Information").child("Phone").getValue();
                String remark = (String) dataSnapshot.child("Open_Days").child("Remark").getValue();
                String monOpen = (String) dataSnapshot.child("Open_Days").child("Mon").child("Open_At").getValue();
                String monClose = (String) dataSnapshot.child("Open_Days").child("Mon").child("Close_At").getValue();
                String mon = monOpen + "~" + monClose;
                String tueOpen = (String) dataSnapshot.child("Open_Days").child("Tue").child("Open_At").getValue();
                String tueClose = (String) dataSnapshot.child("Open_Days").child("Tue").child("Close_At").getValue();
                String tue = tueOpen + "~" + tueClose;
                String wedOpen = (String) dataSnapshot.child("Open_Days").child("Wed").child("Open_At").getValue();
                String wedClose = (String) dataSnapshot.child("Open_Days").child("Wed").child("Close_At").getValue();
                String wed = wedOpen + "~" + wedClose;
                String thuOpen = (String) dataSnapshot.child("Open_Days").child("Thu").child("Open_At").getValue();
                String thuClose = (String) dataSnapshot.child("Open_Days").child("Thu").child("Close_At").getValue();
                String thu = thuOpen + "~" + thuClose;
                String friOpen = (String) dataSnapshot.child("Open_Days").child("Fri").child("Open_At").getValue();
                String friClose = (String) dataSnapshot.child("Open_Days").child("Fri").child("Close_At").getValue();
                String fri = friOpen + "~" + friClose;
                String satOpen = (String) dataSnapshot.child("Open_Days").child("Sat").child("Open_At").getValue();
                String satClose = (String) dataSnapshot.child("Open_Days").child("Sat").child("Close_At").getValue();
                String sat = satOpen + "~" + satClose;
                String sunOpen = (String) dataSnapshot.child("Open_Days").child("Sun").child("Open_At").getValue();
                String sunClose = (String) dataSnapshot.child("Open_Days").child("Sun").child("Close_At").getValue();
                String sun = sunOpen + "~" + sunClose;
                String address = (String) dataSnapshot.child("Location").child("Address").getValue();
                String story = (String) dataSnapshot.child("Information").child("Introduction").getValue();
                counting = (Long)dataSnapshot.child("Popularity").getValue();

                phoneTextView.setText(phone);
                remarkTextView.setText(remark);
                monTextView.setText(mon);
                tueTextView.setText(tue);
                wedTextView.setText(wed);
                thuTextView.setText(thu);
                friTextView.setText(fri);
                satTextView.setText(sat);
                sunTextView.setText(sun);
                addressTextView.setText(address);
                storyTextView.setText(story);
                String countingS = counting.toString();
                count.setText(countingS);

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

        fbShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {

                        /*
                        !!!在這裡做分享完成後想要做的動作，幫助樂透運行
                         */


                        //8/27變動部分
                        MemberDB memberDB = new MemberDB(getActivity(), getContext());
                        memberDB.getLottoNum();


                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getContext(), "cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
                    }
                });

                if (ShareDialog.canShow(SharePhotoContent.class)) {
                    //Bitmap image = getBitmap(forShareUse);

                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentTitle(args.getString(ARGUMENT_TITLE))
                            .setImageUrl(Uri.parse(forShareUse))
                            .build();

                    /*
                    SharePhoto photo = new SharePhoto.Builder().setImageUrl(Uri.parse(forShareUse)).build();
                    SharePhotoContent content = new SharePhotoContent
                            .Builder().addPhoto(photo).build();
                            */

                    shareDialog.show(content);

                } else {
                    Toast.makeText(getContext(), "QQ", Toast.LENGTH_LONG).show();
                }

            }
        });

        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] key2 = {""};
                GlobalVariable globalVariable = (GlobalVariable)getActivity().getApplicationContext();
                String userId =globalVariable.getUserId();
                //Toast.makeText(getContext(), userId, Toast.LENGTH_LONG).show();
                Long userLongId = Long.parseLong(userId, 10);

                final Firebase member = new Firebase(DB_MEMBER_URL);
                Query  findMember = member.orderByChild("Facebook_ID").equalTo(userLongId);
                findMember.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        key2[0] = dataSnapshot.getKey();
                        //Toast.makeText(getContext(), key2[0], Toast.LENGTH_LONG).show();
                        final Firebase member3 = new Firebase(DB_MEMBER_URL + key2[0]);

                        //member3.child("Favorite_Vendors/Vendor_ID").push().setValue(key[0]);

                        //GlobalVariable globalVariable2 = (GlobalVariable)getActivity().getApplicationContext();
                        //globalVariable2.setCollectedVendor(key[0]);

                        Map<String, Object> favVendor = new HashMap<String, Object>();
                        favVendor.put("Vendor_ID", key[0]);
                        member3.child("Favorite_Vendors").push().setValue(favVendor);

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

                final Firebase collection = new Firebase(DB_URL + "/" + key[0]);
                mathth = counting + 1L;
                collection.child("Popularity").setValue(mathth);
                String mathS = mathth.toString();
                count.setText(mathS);






            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCommentSelected(args.getString(ARGUMENT_TITLE));
            }
        });

        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener2.onNavigationSelected(args.getString(ARGUMENT_TITLE));
            }
        });

        return view;
    }

    public interface OnCommentSelected{
        void onCommentSelected(String vendorTitle);
    }

    public interface OnNavigationSelected{
        void onNavigationSelected(String vendorTitle);
    }

    /*
    public Bitmap getBitmap(String... params){
        HttpURLConnection connection;
        final Bitmap myBitmap;

        try{
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);
            this.bitmap = myBitmap;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    */

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
