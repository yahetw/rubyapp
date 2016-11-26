package com.example.jiarou.sharelove;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Peter on 2016/8/18.
 */
public class Login extends AppCompatActivity{
    final static String DB_URL = "https://member-139bd.firebaseio.com/";

    /*
    final String[] userId = {""};
    final String[] userName = {""};
    final String[] userPic = {""};
    final String[] userLocale = {""};
    final String[] userBirth = {""};
    */


    Long userLongId ;
    String userName;
    String userPic ;
    String userLocale ;
    String userBirth ;
    Long zip2;


    CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState){
        //初始化臉書
        FacebookSdk.sdkInitialize(getApplicationContext());

        Firebase.setAndroidContext(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);



        //建立callbackManager處理login回呼
        callbackManager = CallbackManager.Factory.create();

        final LoginButton loginButton = (LoginButton)findViewById(R.id.loginButton);

        loginButton.setReadPermissions("public_profile", "user_birthday");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                final Firebase member = new Firebase(DB_URL);

                Double fbId = Double.parseDouble(loginResult.getAccessToken().getUserId());

                Query member2 = member.orderByChild("Facebook_ID").equalTo(fbId);

                member2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {



                        if (dataSnapshot.exists()) {
                            GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
                            //globalVariable.setUserId(loginResult.getAccessToken().getUserId());
                            globalVariable.setUserId(loginResult.getAccessToken().getUserId());

                            //成功登入，跳轉至地圖頁面
                            Intent intent;
                            intent = new Intent();
                            intent.setClass(Login.this, IndexActivity.class);

                            //傳值給首頁

                            startActivity(intent);




                        }else{
                            final View item = LayoutInflater.from(Login.this).inflate(R.layout.login_dialog, null);
                            new AlertDialog.Builder(Login.this)
                                    .setTitle("請輸入所在郵遞區號(三碼)")
                                    .setView(item)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            EditText editText = (EditText)item.findViewById(R.id.editText4);
                                            final String zip = editText.getText().toString();
                                            Long longZip = Long.parseLong(zip, 10);
                                            GlobalVariable globalVariable4 = (GlobalVariable)getApplicationContext();
                                            globalVariable4.setZip(longZip);

                                            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken()
                                                    , new GraphRequest.GraphJSONObjectCallback(){

                                                @Override
                                                public void onCompleted(JSONObject object, GraphResponse response) {

                                                   String userId = object.optString("id");
                                                    userLongId = Long.parseLong(userId, 10);
                                                    userName = object.optString("name");
                                                     userPic = object.optString("picture/data/url");
                                                    userLocale = object.optString("locale");
                                                    userBirth = object.optString("birthday");

                                                    GlobalVariable globalVariable5 = (GlobalVariable)getApplicationContext();
                                                    zip2 = globalVariable5.getZip();

                                                    Map<String, Object> newMember = new HashMap<String, Object>();
                                                    //Map<String, String> favVendor = new HashMap<String, String>();
                                                    //favVendor.put("Vendor_ID", "");
                                                    newMember.put("Birthday", userBirth);
                                                    newMember.put("Default_Zone", zip2);
                                                    newMember.put("Facebook_ID", userLongId);
                                                    //newMember.put("Facebook_ID", loginResult.getAccessToken().getUserId());
                                                    newMember.put("Favorite_Vendors", "");
                                                    newMember.put("Lottery_Numbers", "");
                                                    newMember.put("Nickname", userName);
                                                    newMember.put("Owned_Coupons", "");
                                                    newMember.put("Owned_Points", 0);
                                                    Map<String, String> photo = new HashMap<String, String>();
                                                    photo.put("Photo_ID", userId);
                                                    newMember.put("Photos", photo);
                                                    newMember.put("Share_Times", 0);
                                                    newMember.put("Suspended", false);

                                                    member.push().setValue(newMember);

                                                    GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
                                                    //globalVariable.setUserId(loginResult.getAccessToken().getUserId());
                                                    globalVariable.setUserId(userId);

                                                   // Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_LONG).show();
                                                    final Firebase FirebaseRef = new Firebase("https://member-activity.firebaseio.com/Activity");
                                                    Map mParent = new HashMap();
                                                    mParent.put("one", "");
                                                    mParent.put("two", "");
                                                    mParent.put("three", "");
                                                    mParent.put("four", "");
                                                    mParent.put("times", "one");
                                                    mParent.put("done", "");
                                                    mParent.put("Facebook_ID", userLongId);
                                                    FirebaseRef.push().setValue(mParent);





                                                }
                                            });

                                            Bundle parameters = new Bundle();
                                            parameters.putString("fields", "id,name,picture,locale,birthday");
                                            request.setParameters(parameters);
                                            request.executeAsync();

                                            //成功登入，跳轉至地圖頁面
                                            Intent intent;
                                            intent = new Intent();
                                            intent.setClass(Login.this, IndexActivity.class);
                                            startActivity(intent);
                                        }
                                    }).show();


                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });



            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    //接收callbackManager回傳資料
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
