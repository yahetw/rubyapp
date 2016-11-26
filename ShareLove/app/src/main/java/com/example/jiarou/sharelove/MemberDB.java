package com.example.jiarou.sharelove;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by chiayi on 16/8/27.
 */
public class MemberDB {


    GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
    Long facebookID = Long.parseLong(globalVariable.getUserId());
    final static String MEMBER_DB_URL = "https://member-139bd.firebaseio.com/";
    final Firebase member_db = new Firebase(MEMBER_DB_URL);
    Query query = member_db.orderByChild("Facebook_ID").equalTo(facebookID);
    final String LOTTO_DB_URL = "https://lottery-72c58.firebaseio.com/";
    final Firebase lotto_db = new Firebase(LOTTO_DB_URL);
    Integer get_lotto_check;
    String memberID;


    String time;
    final ArrayList<String> lotto_period_array = new ArrayList<>();
    final ArrayList<String> win_period_array = new ArrayList<>();
    final ArrayList<Boolean> checked_array = new ArrayList<>();
    final ArrayList<String> member_lottoID_array = new ArrayList<>();

    Long lottoColumn;
    private static Context mContext;


    public MemberDB(Activity activity, Context context) {
        Firebase.setAndroidContext(activity);
        mContext = context;

    }


    public void getRecordDate() {


        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        //取得星期幾
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        //Sun=1 Mon=2 Tue=3 Wed=4 Thur=5 Fri=6 Sat=7
        switch (dayOfWeek) {

            case Calendar.SUNDAY:

                calendar.add(Calendar.DATE, 5);

                break;
            case Calendar.MONDAY:

                calendar.add(Calendar.DATE, 4);
                break;
            case Calendar.TUESDAY:

                calendar.add(Calendar.DATE, 3);
                break;
            case Calendar.WEDNESDAY:

                calendar.add(Calendar.DATE, 2);
                break;
            case Calendar.THURSDAY:

                calendar.add(Calendar.DATE, 1);
                break;
            case Calendar.FRIDAY:

                calendar.add(Calendar.DATE, 0);
                break;
            case Calendar.SATURDAY:

                calendar.add(Calendar.DATE, 6);

        }


        Date tdt = calendar.getTime();
        // time => 本週五的日期
        time = formatter.format(tdt);


    }


    public int random_nums() {

        Random random = new Random();
        int random_nums = random.nextInt(10);

        return random_nums;
    }


    //取得使用者蒐集的期數、蒐集多少期
    public void getMemberLottoInfo() {

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String member_lottoID;
                Boolean checked;
                String member_lottoPeriod;
                memberID = dataSnapshot.getKey();


                if (dataSnapshot.child("Lottery_Numbers").getValue() == "" || dataSnapshot.hasChild("Lottery_Numbers") == false) {

                    get_lotto_check = 1;

                } else {

                    HashMap<String, Object> id = (HashMap<String, Object>) dataSnapshot.child("Lottery_Numbers").getValue();
                    for (Map.Entry<String, Object> entry : id.entrySet()) {

                        member_lottoID = entry.getKey();
                        checked = (Boolean) dataSnapshot.child("Lottery_Numbers").child(member_lottoID).child("Checked").getValue();
                        member_lottoPeriod = (String) dataSnapshot.child("Lottery_Numbers").child(member_lottoID).child("Period").getValue();
                        lottoColumn = dataSnapshot.child("Lottery_Numbers").getChildrenCount();
                        checked_array.add(checked);
                        lotto_period_array.add(member_lottoPeriod);
                        member_lottoID_array.add(member_lottoID);
                        Collections.sort(lotto_period_array, Collections.reverseOrder());
                        Collections.sort(checked_array);
                        Collections.sort(member_lottoID_array, Collections.reverseOrder());

                    }

                    System.out.println(lotto_period_array);
                    System.out.println(checked_array);
                    System.out.println(member_lottoID_array);


                    if (checked_array.get(0) == false) {


                        lotto_db.addValueEventListener(new ValueEventListener() {
                            String win_lottoID;
                            String win_lottoPeriod;

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                HashMap<String, Object> id = (HashMap<String, Object>) dataSnapshot.getValue();
                                for (Map.Entry<String, Object> entry : id.entrySet()) {

                                    win_lottoID = entry.getKey();
                                    win_lottoPeriod = (String) dataSnapshot.child(win_lottoID).child("Period").getValue();
                                    win_period_array.add(win_lottoPeriod);
                                    Collections.sort(win_period_array, Collections.reverseOrder());

                                }

                                System.out.println(win_period_array);
                                System.out.println(lotto_period_array);
                                int check_num = 0; //一樣:1 不一樣:0

                                for (int i = 0; i < win_period_array.size(); i++) {

                                    //如果最新蒐集的期數==開獎期數-> 以開獎請兌獎
                                    if (win_period_array.get(i).equals(lotto_period_array.get(0))) {
                                        check_num = check_num + 1;
                                        break;

                                    } else {

                                        if (win_period_array.get(i).compareTo(lotto_period_array.get(0))>0){
                                            check_num = check_num + 1;

                                        }else{

                                            check_num = check_num + 0;

                                        }

                                    }
                                }


                                switch (check_num) {

                                    case 0:
                                        //繼續新增那期的樂透
                                        get_lotto_check = 2;
                                        break;

                                    case 1:
                                        Toast.makeText(mContext, win_period_array.get(0) + " 期獎號已開出，請先前去兌獎，否則分享將不發予號碼", Toast.LENGTH_LONG).show();
                                        get_lotto_check = 3;
                                        break;

                                    default:
                                        break;
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    } else {

                        if (lottoColumn == 1) {

                            //都checked過了，接下來新增另一期樂透
                            get_lotto_check = 1;

                        } else {

                            //都checked過了，接下來新增另一期樂透，刪除
                            member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(1)).removeValue();
                            get_lotto_check = 1;


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


    }


    //分享後，比對可否發放樂透，以及比對發放第幾號的樂透

    public void getLottoNum() {

        this.getRecordDate();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String lotto_key;


                switch (get_lotto_check) {
                    case 1:
                        Map<String, Object> lotto_nums = new HashMap<>();
                        Map<String, Object> lotto_period = new HashMap<>();
                        Map<String, Object> lotto_checked = new HashMap<>();
                        Map<String, Object> lotto_result = new HashMap<>();

                        lotto_nums.put("First", random_nums());
                        lotto_nums.put("Second", "");
                        lotto_nums.put("Third", "");
                        lotto_nums.put("Fourth", "");
                        lotto_nums.put("Fifth", "");

                        Map<String, Map<String, Object>> lotto = new HashMap<>();
                        lotto.put("Numbers", lotto_nums);
                        lotto_period.put("Period", time);
                        lotto_checked.put("Checked", false);
                        lotto_result.put("Result", "");

                        lotto_key = member_db.child(memberID).child("Lottery_Numbers").push().getKey();
                        member_db.child(memberID).child("Lottery_Numbers").child(lotto_key).setValue(lotto);
                        member_db.child(memberID).child("Lottery_Numbers").child(lotto_key).updateChildren(lotto_period);
                        member_db.child(memberID).child("Lottery_Numbers").child(lotto_key).updateChildren(lotto_checked);
                        member_db.child(memberID).child("Lottery_Numbers").child(lotto_key).updateChildren(lotto_result);
                        Toast.makeText(mContext, "分享成功，可前去樂透頁面查看新增的樂透號碼", Toast.LENGTH_LONG).show();
                        break;
                    case 2:

                        Long num1 = (Long) dataSnapshot.child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Numbers").child("First").getValue();
                        Object num2 = dataSnapshot.child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Numbers").child("Second").getValue();
                        Object num3 = dataSnapshot.child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Numbers").child("Third").getValue();
                        Object num4 = dataSnapshot.child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Numbers").child("Fourth").getValue();
                        Object num5 = dataSnapshot.child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Numbers").child("Fifth").getValue();
                        if (num1 != null && num2 == "") {

                            member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Numbers").child("Second").setValue(random_nums());
                            Toast.makeText(mContext, "分享成功，成功取得第二個樂透", Toast.LENGTH_LONG).show();

                        } else if (num2 != "" && num3 == "") {

                            member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Numbers").child("Third").setValue(random_nums());
                            Toast.makeText(mContext, "分享成功，成功取得第三個樂透", Toast.LENGTH_LONG).show();

                        } else if (num3 != "" && num4 == "") {

                            member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Numbers").child("Fourth").setValue(random_nums());
                            Toast.makeText(mContext, "分享成功，成功取得第四個樂透", Toast.LENGTH_LONG).show();

                        } else if (num4 != "" && num5 == "") {

                            member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Numbers").child("Fifth").setValue(random_nums());
                            Toast.makeText(mContext, "分享成功，成功取得第五個樂透", Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(mContext, "您本週已分享超過五次，至週五為止將不會再分發樂透給您", Toast.LENGTH_LONG).show();

                        }
                        break;
                    case 3:
                        Toast.makeText(mContext, "分享成功，但在您兌換樂透前將不新增任何號碼", Toast.LENGTH_LONG).show();
                    default:
                        break;
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
    }


}
