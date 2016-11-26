package com.example.peter.focus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by chiayi on 16/8/27.
 */
public class MemberDB {


    Long facebookID = 111111111111111l; //到時候應該是可以透過什麼管道取得的
    final static String MEMBER_DB_URL = "https://member-139bd.firebaseio.com/";
    final Firebase member_db = new Firebase(MEMBER_DB_URL);
    Query query = member_db.orderByChild("Facebook_ID").equalTo(facebookID);
    String time;
    private static Context mContext;



    public MemberDB (Activity activity, Context context){
        Firebase.setAndroidContext(activity);
        mContext = context;

    }





    public void  getCurrentDate(){


        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        //取得星期幾
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        //Sun=1 Mon=2 Tue=3 Wed=4 Thur=5 Fri=6 Sat=7
        switch (dayOfWeek){

            case Calendar.SUNDAY:

                calendar.add(Calendar.DATE,5);

                break;
            case Calendar.MONDAY:

                calendar.add(Calendar.DATE,4);
                break;
            case Calendar.TUESDAY:

                calendar.add(Calendar.DATE,3);
                break;
            case Calendar.WEDNESDAY:

                calendar.add(Calendar.DATE,2);
                break;
            case Calendar.THURSDAY:

                calendar.add(Calendar.DATE,1);
                break;
            case Calendar.FRIDAY:

                calendar.add(Calendar.DATE,0);
                break;
            case Calendar.SATURDAY:

                calendar.add(Calendar.DATE,6);

        }


        Date tdt = calendar.getTime();
        // time => 本週五的日期
        time = formatter.format(tdt);


    }



    public int random_nums(){

        Random random = new Random();
        int random_nums = random.nextInt(10);

        return random_nums;
    }




    public void getLottoNum() {


        //得到應該記錄的星期五的日期
        this.getCurrentDate();

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                String memberID = dataSnapshot.getKey();
                String period = "";
                String k = "";
                String key = "";
                ArrayList<String> p_arrayList = new ArrayList();
                ArrayList<String> k_arrayList = new ArrayList();


                /**
                 * 1. 8/28 (日) 第一次登入APP並分享
                 * 2. 先知道今天的日期並且找出本週五要填入Member-Lottery_Numbers-Period的日期
                 * 3. 找到 A 使用者的整體資料庫架構、得出他的 member_ID
                 * 4. 檢查有沒有存在 Lottery_Numbers 的架構 （如果第一次登入就還沒有產生）
                 * 5. 還沒有 -> 建立此架構並且輸入一個號碼到 First Nums
                 *    有 -> 取出此架構的period 比對 是否 有建立了 （不確定對不對）
                 * 6.
                 */


                /**
                 *
                 * 已經有 Period = 2015-09-02 的架構了
                 * 現在要新增一個 Period = 2015-09-09 的架構
                 *
                 *
                 * 1. 判斷有DB Lottery欄位是不是null -> 否
                 * 2. 抓整體架構出來 (但只會抓到 Period = 2015-09-02 的) 因為目前只有這個
                 * 3. 取出 key 和 period (只有 2015-09-02)
                 * 4. 一一抓出 period 比對 資料庫內的period 是否有記錄了要被記錄的period => (no)
                 * 5. 新增一個新的架構
                 *
                 *
                 * 第一次分享 Period = 09-02
                 * 1. 判斷有DB Lottery欄位是不是null -> yes
                 * 2. 新增一個架構
                 *
                 *
                 *
                 * 當週第二次分享
                 * 1. 判斷有DB Lottery欄位是不是null -> no
                 * 2. 在那個架構繼續新增數字
                 *
                 *
                 * 第六次分享時
                 * 1. 跳出已經分享超過五次了
                 *
                 *
                 *
                 *
                 * Period = 09-09 分享時
                 * 1. 跳出已經分享超過五次了 => 改成: 請先去兌獎，才可以進行下一週的收集樂透活動
                 *
                 *
                 *
                 * ＝＝＝＝＝＝＝＝＝＝＝＝＝＝
                 *
                 * 分享兩次而已
                 * 開獎
                 * Period = 09-09 分享
                 * 兌獎
                 *
                 *
                 *
                 * 分享兩次
                 * 開獎
                 * 兌獎
                 * Period = 09-09分享
                 *
                 *
                 *
                 * 星期六開獎後:
                 * 1. 兌獎 => 將畫面上的東西刪掉
                 *
                 *
                 *
                 *
                 * 第二次分享
                 * 6. 判斷有DB Lottery欄位是不是null -> 否
                 * 7. 取出整體架構 => 抓到 Period = 2015-09-09 、 2015-09-02 的
                 * 8. 取出key [xxxxxx,bbbbb] 和 period [2015-09-09, 2015-09-02]
                 * 9. 先比較資料庫已存取的 period = 2015-09-09 是否 = 要被記錄的 time 2015-09-09 (yes)
                 * 10.新增號碼
                 * 11.後比較資料庫已存取的 period = 2015-09-02 是否 = 要被記錄的 time 2015-09-09 (no)
                 * 12. 新增一個架構
                 */


                if(dataSnapshot.hasChild("Lottery_Numbers") == false){

                    Map <String,Object> lotto_nums = new HashMap<>();
                    Map <String,Object> lotto_period = new HashMap<>();

                    lotto_nums.put("First",random_nums());
                    lotto_nums.put("Second","");
                    lotto_nums.put("Third","");
                    lotto_nums.put("Fourth","");
                    lotto_nums.put("Fifth","");

                    Map<String,Map<String,Object>> lotto = new HashMap<>();
                    lotto.put("Numbers",lotto_nums);
                    lotto_period.put("Period", time);

                    k = member_db.child(memberID).child("Lottery_Numbers").push().getKey();
                    member_db.child(memberID).child("Lottery_Numbers").child(k).setValue(lotto);
                    member_db.child(memberID).child("Lottery_Numbers").child(k).updateChildren(lotto_period);



                }else{

                    HashMap<String, Object> id = (HashMap<String, Object>) dataSnapshot.child("Lottery_Numbers").getValue();
                    for (Map.Entry<String,Object> entry: id.entrySet()){

                        key = entry.getKey();
                        period = (String) dataSnapshot.child("Lottery_Numbers").child(key).child("Period").getValue();


                        p_arrayList.add(period);
                        k_arrayList.add(key);

                    }



                    System.out.println("=========================================");
                    System.out.println(period);


                    if (p_arrayList.get(0).equals(time)){
                        Long num1 = (Long) dataSnapshot.child("Lottery_Numbers").child(k_arrayList.get(0)).child("Numbers").child("First").getValue();
                        Object num2 =  dataSnapshot.child("Lottery_Numbers").child(k_arrayList.get(0)).child("Numbers").child("Second").getValue();
                        Object num3 =  dataSnapshot.child("Lottery_Numbers").child(k_arrayList.get(0)).child("Numbers").child("Third").getValue();
                        Object num4 =  dataSnapshot.child("Lottery_Numbers").child(k_arrayList.get(0)).child("Numbers").child("Fourth").getValue();
                        Object num5 =  dataSnapshot.child("Lottery_Numbers").child(k_arrayList.get(0)).child("Numbers").child("Fifth").getValue();

                        if (num1 != null && num2 == "" ){

                            member_db.child(memberID).child("Lottery_Numbers").child(k_arrayList.get(0)).child("Numbers").child("Second").setValue(random_nums());

                        }else if (num2 != "" && num3 == ""){

                            member_db.child(memberID).child("Lottery_Numbers").child(k_arrayList.get(0)).child("Numbers").child("Third").setValue(random_nums());

                        }else if (num3 != "" && num4 == ""){

                            member_db.child(memberID).child("Lottery_Numbers").child(k_arrayList.get(0)).child("Numbers").child("Fourth").setValue(random_nums());

                        }else if (num4 != "" && num5 == "") {

                            member_db.child(memberID).child("Lottery_Numbers").child(k_arrayList.get(0)).child("Numbers").child("Fifth").setValue(random_nums());

                        }else {

                            System.out.println("=========================");
                            System.out.println("=========================");
                            System.out.println("=========================");
                            System.out.println("CANNOT SHARE ANYMORE");
                            Toast.makeText(mContext,"您本週已分享超過五次，至週五為止將不會再分發樂透給您",Toast.LENGTH_LONG).show();


                        }

                    }else{



                        Toast.makeText(mContext,"[您尚未兌獎]本期已開獎，至星期五午夜前可兌獎，兌獎後才可繼續進行下期樂透活動，若逾期兌獎將不分發獎勵",Toast.LENGTH_LONG).show();




                    }





                }




//                if(dataSnapshot.hasChild("Lottery_Numbers") == false ){
//
//                    Map <String,Object> lotto_nums = new HashMap<>();
//                    Map <String,Object> lotto_period = new HashMap<>();
//
//                    lotto_nums.put("First",random_nums());
//                    lotto_nums.put("Second","");
//                    lotto_nums.put("Third","");
//                    lotto_nums.put("Fourth","");
//                    lotto_nums.put("Fifth","");
//
//                    Map<String,Map<String,Object>> lotto = new HashMap<>();
//                    lotto.put("Numbers",lotto_nums);
//                    lotto_period.put("Period", time);
//
//
//                    String k = member_db.child(memberID).child("Lottery_Numbers").push().getKey();
//                    member_db.child(memberID).child("Lottery_Numbers").child(k).setValue(lotto);
//                    member_db.child(memberID).child("Lottery_Numbers").child(k).updateChildren(lotto_period);
//
//
//
//                }else{
//
//                    HashMap<String, Object> id = (HashMap<String, Object>) dataSnapshot.child("Lottery_Numbers").getValue();
//
//
//
//                    for (Map.Entry<String,Object> entry: id.entrySet()){
//
//                        key = entry.getKey();
//                        period = (String) dataSnapshot.child("Lottery_Numbers").child(key).child("Period").getValue();
//
//
//                        p_arrayList.add(period);
//                        k_arrayList.add(key);
//
//
//
//
//                    }
//
//
//                    // 如果要被記錄的時間 等於 已經被記錄在資料庫的時間 -> 本週已經有分享過一次，繼續分享則依序填入2、3、4、5個號碼
//                    // 如果要被記錄的時間 不等於 被記錄在資料庫的時間 -> ...? 還沒被記錄過
//
//
//
//
//                        int i = 0; // latest data
//                        if (p_arrayList.get(i).equals(time)){
//
//
//                            Long num1 = (Long) dataSnapshot.child("Lottery_Numbers").child(k_arrayList.get(i)).child("Numbers").child("First").getValue();
//                            Object num2 =  dataSnapshot.child("Lottery_Numbers").child(k_arrayList.get(i)).child("Numbers").child("Second").getValue();
//                            Object num3 =  dataSnapshot.child("Lottery_Numbers").child(k_arrayList.get(i)).child("Numbers").child("Third").getValue();
//                            Object num4 =  dataSnapshot.child("Lottery_Numbers").child(k_arrayList.get(i)).child("Numbers").child("Fourth").getValue();
//                            Object num5 =  dataSnapshot.child("Lottery_Numbers").child(k_arrayList.get(i)).child("Numbers").child("Fifth").getValue();
//
//
//
//                            if (num1 != null && num2 == "" ){
//
//                                member_db.child(memberID).child("Lottery_Numbers").child(k_arrayList.get(i)).child("Numbers").child("Second").setValue(random_nums());
//
//                            }else if (num2 != "" && num3 == ""){
//
//                                member_db.child(memberID).child("Lottery_Numbers").child(k_arrayList.get(i)).child("Numbers").child("Third").setValue(random_nums());
//
//                            }else if (num3 != "" && num4 == ""){
//
//                                member_db.child(memberID).child("Lottery_Numbers").child(k_arrayList.get(i)).child("Numbers").child("Fourth").setValue(random_nums());
//
//                            }else if (num4 != "" && num5 == "") {
//
//                                member_db.child(memberID).child("Lottery_Numbers").child(k_arrayList.get(i)).child("Numbers").child("Fifth").setValue(random_nums());
//
//                            }else {
//
//                                System.out.println("=========================");
//                                System.out.println("=========================");
//                                System.out.println("=========================");
//                                System.out.println("CANNOT SHARE ANYMORE");
//                                Toast.makeText(mContext,"您本週已分享超過五次，至週五為止將不會再分發樂透給您",Toast.LENGTH_LONG).show();
//
//
//                            }
//
//
//
//                        } else {
//
//
//
//
//
//                                    Map<String, Object> lotto_number = new HashMap<>();
//                                    Map<String, Object> lotto_periods = new HashMap<>();
//                                    Random random = new Random();
//                                    int random_nums = random.nextInt(10);
//
//                                    lotto_number.put("First", random_nums);
//                                    lotto_number.put("Second", "");
//                                    lotto_number.put("Third", "");
//                                    lotto_number.put("Fourth", "");
//                                    lotto_number.put("Fifth", "");
//
//                                    Map<String, Map<String, Object>> lotto = new HashMap<>();
//                                    lotto.put("Numbers", lotto_number);
//                                    lotto_periods.put("Period", time);
//
//
//                                    String k = member_db.child(memberID).child("Lottery_Numbers").push().getKey();
//                                    member_db.child(memberID).child("Lottery_Numbers").child(k).setValue(lotto);
//                                    member_db.child(memberID).child("Lottery_Numbers").child(k).updateChildren(lotto_periods);
//
//
//
//
//                            }
//
//
//
//
//                    }






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







//        query.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                Long ds = (Long) dataSnapshot.child("Lottery_Numbers").child("Numbers").child("First").getValue();
//
//                System.out.println("=====================================");
//                System.out.println("=====================================");
//                System.out.println("=====================================");
//                System.out.println(ds);
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//
//
//    }

    }

}
