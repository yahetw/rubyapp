package com.example.jiarou.sharelove;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by chiayi on 16/8/14.
 */
public class LotteryDisplayFragment extends Fragment {


    //  還沒做restart fragment 的部分

    final static String LOTTO_DB_URL = "https://lottery-72c58.firebaseio.com/"; //用在開獎
    final static String MEMBER_DB_URL = "https://member-139bd.firebaseio.com/";
    GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
    Long facebookID = Long.parseLong(globalVariable.getUserId());
    Object w_n1, w_n2, w_n3, w_n4, w_n5;
    String owned_period;
    String win_period;



    //Nov 5
    String memberID;
    ArrayList<String> lotto_period_array = new ArrayList<>();
    ArrayList<String> member_lottoID_array = new ArrayList<>();
    ArrayList<String> win_period_array = new ArrayList<>();
    ArrayList<String> past_lotto_num = new ArrayList<>();
    ArrayList<Boolean> checked_array = new ArrayList<>();
    ArrayList<Long> lottoNum = new ArrayList<>();
    ArrayList<Long> lottoWinNum = new ArrayList<>();
    ArrayList<Long> lottoPastWinNum = new ArrayList<>();
    Long owned_points;
    String message;
    TextView pnum,past_period2;
    String plotto_period;
    Object plotto_result;

    TextView cnum1, cnum2, cnum3, cnum4, cnum5, winning_num, check_y_n, latest_period, collect_period,past_period;
    Button gameRule_btn, check_btn;
    LottoRuleDialogFragment lottoRuleFragment = new LottoRuleDialogFragment();

    public static LotteryDisplayFragment newInstance() {
        return new LotteryDisplayFragment();
    }

    public LotteryDisplayFragment() {


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.lottery_display_fragment, container, false);
        gameRule_btn = (Button) view.findViewById(R.id.gameRule_btn);
        check_y_n = (TextView) view.findViewById(R.id.check_y_n);
        check_btn = (Button) view.findViewById(R.id.check_btn);
        winning_num = (TextView) view.findViewById(R.id.winning_num);
        latest_period = (TextView) view.findViewById(R.id.win_period);
        collect_period = (TextView) view.findViewById(R.id.collect_period);



        //取得目前蒐集的樂透號碼
        final Firebase member_db = new Firebase(MEMBER_DB_URL);
        final Query query = member_db.orderByChild("Facebook_ID").equalTo(facebookID);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String member_lottoID;
                String member_lottoPeriod;
                Boolean checked;
                final Long lottoColumn = dataSnapshot.child("Lottery_Numbers").getChildrenCount();

                final String lotto_period;
                final Object lotto_num1;
                final Object lotto_num2;
                final Object lotto_num3;
                final Object lotto_num4;
                final Object lotto_num5;
                final Object plotto_num1;
                final Object plotto_num2;
                final Object plotto_num3;
                final Object plotto_num4;
                final Object plotto_num5;
                final Object lotto_result;



                memberID = dataSnapshot.getKey();

                cnum1 = (TextView) getView().findViewById(R.id.current_num1);
                cnum2 = (TextView) getView().findViewById(R.id.current_num2);
                cnum3 = (TextView) getView().findViewById(R.id.current_num3);
                cnum4 = (TextView) getView().findViewById(R.id.current_num4);
                cnum5 = (TextView) getView().findViewById(R.id.current_num5);
                pnum = (TextView) getView().findViewById(R.id.past_owned_number);
                past_period = (TextView) getView().findViewById(R.id.past_period);
                past_period2 = (TextView) getView().findViewById(R.id.past_period2);


                if (dataSnapshot.hasChild("Lottery_Numbers") && dataSnapshot.child("Lottery_Numbers").getValue() != "") {

                    HashMap<String, Object> id = (HashMap<String, Object>) dataSnapshot.child("Lottery_Numbers").getValue();
                    for (Map.Entry<String, Object> entry : id.entrySet()) {


                        member_lottoID = entry.getKey();
                        member_lottoPeriod = (String) dataSnapshot.child("Lottery_Numbers").child(member_lottoID).child("Period").getValue();
                        checked = (Boolean) dataSnapshot.child("Lottery_Numbers").child(member_lottoID).child("Checked").getValue();
                        owned_points = (Long) dataSnapshot.child("Owned_Points").getValue();
                        member_lottoID_array.add(member_lottoID);
                        lotto_period_array.add(member_lottoPeriod);
                        checked_array.add(checked);
                        Collections.sort(lotto_period_array, Collections.reverseOrder());
                        Collections.sort(member_lottoID_array, Collections.reverseOrder());
                        Collections.sort(checked_array);

                    }


                    //取得最新期的樂透號碼
                    lotto_num1 = dataSnapshot.child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Numbers").child("First").getValue();
                    lotto_num2 = dataSnapshot.child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Numbers").child("Second").getValue();
                    lotto_num3 = dataSnapshot.child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Numbers").child("Third").getValue();
                    lotto_num4 = dataSnapshot.child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Numbers").child("Fourth").getValue();
                    lotto_num5 = dataSnapshot.child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Numbers").child("Fifth").getValue();
                    lotto_result = dataSnapshot.child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Result").getValue();
                    lotto_period = lotto_period_array.get(0);







                    if (lotto_num2.equals("") && lotto_num3.equals("") && lotto_num4.equals("") && lotto_num5.equals("")){

                        lottoNum.add(null);
                        lottoNum.add(null);
                        lottoNum.add(null);
                        lottoNum.add(null);
                        lottoNum.add((Long) lotto_num1);



                    }else if (lotto_num3.equals("") && lotto_num4.equals("") && lotto_num5.equals("")){

                        lottoNum.add(null);
                        lottoNum.add(null);
                        lottoNum.add(null);
                        lottoNum.add((Long)lotto_num2);
                        lottoNum.add((Long)lotto_num1);

                    }else if (lotto_num4.equals("") && lotto_num5.equals("")){

                        lottoNum.add(null);
                        lottoNum.add(null);
                        lottoNum.add((Long)lotto_num3);
                        lottoNum.add((Long)lotto_num2);
                        lottoNum.add((Long)lotto_num1);


                    }else if (lotto_num5.equals("")){


                        lottoNum.add(null);
                        lottoNum.add((Long)lotto_num4);
                        lottoNum.add((Long)lotto_num3);
                        lottoNum.add((Long)lotto_num2);
                        lottoNum.add((Long)lotto_num1);

                    }else{

                        lottoNum.add((Long)lotto_num5);
                        lottoNum.add((Long)lotto_num4);
                        lottoNum.add((Long)lotto_num3);
                        lottoNum.add((Long)lotto_num2);
                        lottoNum.add((Long)lotto_num1);


                    }

                    //如果有兩期，取得舊的那期樂透號碼
                    if (lottoColumn == 2) {

                        plotto_num1 = dataSnapshot.child("Lottery_Numbers").child(member_lottoID_array.get(1)).child("Numbers").child("First").getValue();
                        plotto_num2 = dataSnapshot.child("Lottery_Numbers").child(member_lottoID_array.get(1)).child("Numbers").child("Second").getValue();
                        plotto_num3 = dataSnapshot.child("Lottery_Numbers").child(member_lottoID_array.get(1)).child("Numbers").child("Third").getValue();
                        plotto_num4 = dataSnapshot.child("Lottery_Numbers").child(member_lottoID_array.get(1)).child("Numbers").child("Fourth").getValue();
                        plotto_num5 = dataSnapshot.child("Lottery_Numbers").child(member_lottoID_array.get(1)).child("Numbers").child("Fifth").getValue();
                        plotto_result = dataSnapshot.child("Lottery_Numbers").child(member_lottoID_array.get(1)).child("Result").getValue();
                        plotto_period = lotto_period_array.get(1);
                        past_lotto_num.add(String.valueOf(plotto_num1));
                        past_lotto_num.add(String.valueOf(plotto_num2));
                        past_lotto_num.add(String.valueOf(plotto_num3));
                        past_lotto_num.add(String.valueOf(plotto_num4));
                        past_lotto_num.add(String.valueOf(plotto_num5));
                        //past_period.setText("\n"+plotto_period);
                        //past_period2.setText("\n"+plotto_period);
                        //check_y_n.setText(String.valueOf(plotto_result));


                    }



                    final Firebase lotto_db = new Firebase(LOTTO_DB_URL);
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

                            if (win_period_array.get(0).equals(lotto_period) || win_period_array.get(0).compareTo(lotto_period)>0) {

                                cnum5.setText("您");
                                cnum4.setText("尚");
                                cnum3.setText("未");
                                cnum2.setText("收");
                                cnum1.setText("集");

                                String past_collect_num = String.valueOf(lotto_num5) + String.valueOf(lotto_num4) + String.valueOf(lotto_num3) + String.valueOf(lotto_num2) + String.valueOf(lotto_num1);
                                pnum.setText(past_collect_num);
                                past_period.setText("\n"+lotto_period);
                                past_period2.setText("\n"+lotto_period);
                                check_y_n.setText(String.valueOf(lotto_result));



                            } else {

                                cnum5.setText(String.valueOf(lotto_num5));
                                cnum4.setText(String.valueOf(lotto_num4));
                                cnum3.setText(String.valueOf(lotto_num3));
                                cnum2.setText(String.valueOf(lotto_num2));
                                cnum1.setText(String.valueOf(lotto_num1));
                                collect_period.setText(lotto_period);


                                if (lottoColumn == 1) {

                                    pnum.setText("目前無上期號碼");
                                    past_period.setText("\n上期");
                                    past_period2.setText("\n上期");
                                    check_y_n.setText("目前無開獎結果");

                                } else if (lottoColumn == 2) {


                                    String p1 = past_lotto_num.get(0);
                                    String p2 = past_lotto_num.get(1);
                                    String p3 = past_lotto_num.get(2);
                                    String p4 = past_lotto_num.get(3);
                                    String p5 = past_lotto_num.get(4);

                                    pnum.setText(p5 + p4 + p3 + p2 + p1);
                                    past_period.setText("\n"+plotto_period);
                                    past_period2.setText("\n"+plotto_period);
                                    check_y_n.setText(String.valueOf(plotto_result));

                                } else {


                                }

                            }


                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });


                } else {


                    cnum5.setText("您");
                    cnum4.setText("尚");
                    cnum3.setText("未");
                    cnum2.setText("收");
                    cnum1.setText("集");

                    past_period.setText("\n上期");
                    past_period2.setText("\n上期");
                    check_y_n.setText("目前無開獎結果");
                    pnum.setText("目前無上期號碼");

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


        //getLastestLottoNum;
        final Firebase lotto_db = new Firebase(LOTTO_DB_URL);
        Query query2 = lotto_db.limitToLast(1);
        query2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                w_n1 = dataSnapshot.child("Numbers").child("First").getValue();
                w_n2 = dataSnapshot.child("Numbers").child("Second").getValue();
                w_n3 = dataSnapshot.child("Numbers").child("Third").getValue();
                w_n4 = dataSnapshot.child("Numbers").child("Fourth").getValue();
                w_n5 = dataSnapshot.child("Numbers").child("Fifth").getValue();
                win_period = (String) dataSnapshot.child("Period").getValue();

                String w1 = String.valueOf(w_n1);
                String w2 = String.valueOf(w_n2);
                String w3 = String.valueOf(w_n3);
                String w4 = String.valueOf(w_n4);
                String w5 = String.valueOf(w_n5);

                lottoWinNum.add((Long) w_n5);
                lottoWinNum.add((Long) w_n4);
                lottoWinNum.add((Long) w_n3);
                lottoWinNum.add((Long) w_n2);
                lottoWinNum.add((Long) w_n1);


                String winning_number = w5 + w4 + w3 + w2 + w1;
                winning_num.setText(winning_number);
                latest_period.setText("\n" + win_period);
                collect_period.setText(owned_period);


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


        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Firebase member_db = new Firebase(MEMBER_DB_URL);
                Long total_points;
                System.out.println(lottoNum);
                System.out.println(lottoWinNum);

                if (lotto_period_array.isEmpty() || win_period_array.isEmpty()) {

                    Toast.makeText(getContext(), "請先分享文章，蒐集樂透", Toast.LENGTH_LONG).show();

                } else {


                    if (win_period_array.get(0).equals(lotto_period_array.get(0))) {

                        if (checked_array.get(0) == false) {

                            if (lottoNum.get(3) == null || lottoNum.get(2) == null) {

                                message = "樂透蒐集不足";
                                Toast.makeText(getContext(), "抱歉! 您收集的樂透數不足三個，故無法兌獎。請繼續參與下週的樂透活動。", Toast.LENGTH_LONG).show();
                                member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Checked").setValue(true);
                                member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Result").setValue(message);
                                check_y_n.setText(message);




                            } else {


                                int m = 0, j, h;

                                for (j = 4; j >= 0; j--) {

                                    if (lottoNum.get(j) == lottoWinNum.get(j)) {

                                        m = m + 1;
                                    } else {

                                        break;
                                    }
                                }


                                if (m >= 3) {

                                    h = m;
                                    //分發點數給中獎者
                                    switch (h) {

                                        case 3:

                                            message = "中三碼";
                                            Toast.makeText(getContext(), "恭喜中獎，將贈予您愛心點數 5 點，請至個人頁面查看", Toast.LENGTH_LONG).show();
                                            total_points = owned_points + 5;
                                            member_db.child(memberID).child("Owned_Points").setValue(total_points);
                                            member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Checked").setValue(true);
                                            member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Result").setValue(message);
                                            check_y_n.setText(message);

                                            break;


                                        case 4:

                                            message = "中四碼";
                                            Toast.makeText(getContext(), "恭喜中獎，將贈予您愛心點數 10 點，請至個人頁面查看", Toast.LENGTH_LONG).show();
                                            total_points = owned_points + 10;
                                            member_db.child(memberID).child("Owned_Points").setValue(total_points);
                                            member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Checked").setValue(true);
                                            member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Result").setValue(message);
                                            check_y_n.setText(message);
                                            break;


                                        case 5:

                                            message = "全中";
                                            Toast.makeText(getContext(), "恭喜中獎，將贈予您愛心點數 30 點，請至個人頁面查看", Toast.LENGTH_LONG).show();
                                            total_points = owned_points + 30;
                                            member_db.child(memberID).child("Owned_Points").setValue(total_points);
                                            member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Checked").setValue(true);
                                            member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Result").setValue(message);
                                            check_y_n.setText(message);
                                            break;

                                    }

                                } else {

                                    message = "可惜沒中獎";
                                    member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Checked").setValue(true);
                                    member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Result").setValue(message);
                                    check_y_n.setText(message);

                                }
                            }


                        } else {

                            Toast.makeText(getContext(), "上期已兌過，請繼續蒐集樂透獎號", Toast.LENGTH_LONG).show();

                        }


                    } else if (win_period_array.get(0).compareTo(lotto_period_array.get(0)) > 0) {


                        final Firebase lotto_db = new Firebase(LOTTO_DB_URL);
                        Query query3 = lotto_db.orderByChild("Period").equalTo(lotto_period_array.get(0));
                        query3.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                Long total_points;
                                Long w1 = (Long) dataSnapshot.child("Numbers").child("First").getValue();
                                Long w2 = (Long) dataSnapshot.child("Numbers").child("Second").getValue();
                                Long w3 = (Long) dataSnapshot.child("Numbers").child("Third").getValue();
                                Long w4 = (Long) dataSnapshot.child("Numbers").child("Fourth").getValue();
                                Long w5 = (Long) dataSnapshot.child("Numbers").child("Fifth").getValue();

                                lottoPastWinNum.add(w5);
                                lottoPastWinNum.add(w4);
                                lottoPastWinNum.add(w3);
                                lottoPastWinNum.add(w2);
                                lottoPastWinNum.add(w1);


                                if (checked_array.get(0)==false){

                                    if (lottoNum.get(3) == null || lottoNum.get(2) == null) {

                                        message = "樂透蒐集不足";
                                        Toast.makeText(getContext(), "抱歉! 您收集的樂透數不足三個，故無法兌獎。請繼續參與下週的樂透活動。", Toast.LENGTH_LONG).show();
                                        member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Checked").setValue(true);
                                        member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Result").setValue(message);
                                        check_y_n.setText(message);


                                    } else {


                                        int m = 0, j, h;

                                        for (j = 4; j >= 0; j--) {

                                            if (lottoNum.get(j) == lottoPastWinNum.get(j)) {

                                                m = m + 1;
                                            } else {

                                                break;
                                            }
                                        }


                                        if (m >= 3) {

                                            h = m;
                                            //分發點數給中獎者
                                            switch (h) {

                                                case 3:

                                                    message = "中三碼";
                                                    Toast.makeText(getContext(), "恭喜中獎，將贈予您愛心點數 5 點，請至個人頁面查看", Toast.LENGTH_LONG).show();
                                                    total_points = owned_points + 5;
                                                    member_db.child(memberID).child("Owned_Points").setValue(total_points);
                                                    member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Checked").setValue(true);
                                                    member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Result").setValue(message);
                                                    check_y_n.setText(message);

                                                    break;


                                                case 4:

                                                    message = "中四碼";
                                                    Toast.makeText(getContext(), "恭喜中獎，將贈予您愛心點數 10 點，請至個人頁面查看", Toast.LENGTH_LONG).show();
                                                    total_points = owned_points + 10;
                                                    member_db.child(memberID).child("Owned_Points").setValue(total_points);
                                                    member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Checked").setValue(true);
                                                    member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Result").setValue(message);
                                                    check_y_n.setText(message);
                                                    break;


                                                case 5:

                                                    message = "全中";
                                                    Toast.makeText(getContext(), "恭喜中獎，將贈予您愛心點數 30 點，請至個人頁面查看", Toast.LENGTH_LONG).show();
                                                    total_points = owned_points + 30;
                                                    member_db.child(memberID).child("Owned_Points").setValue(total_points);
                                                    member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Checked").setValue(true);
                                                    member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Result").setValue(message);
                                                    check_y_n.setText(message);
                                                    break;

                                            }

                                        } else {

                                            message = "可惜沒中獎";
                                            member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Checked").setValue(true);
                                            member_db.child(memberID).child("Lottery_Numbers").child(member_lottoID_array.get(0)).child("Result").setValue(message);
                                            check_y_n.setText(message);

                                        }
                                    }


                                }else{

                                    Toast.makeText(getContext(), "您已兌過，請繼續蒐集樂透獎號", Toast.LENGTH_LONG).show();

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


                    } else if (win_period_array.get(0).compareTo(lotto_period_array.get(0)) < 0) {

                        Toast.makeText(getContext(), "本期尚未開獎", Toast.LENGTH_LONG).show();

                    }
                }

                Fragment fragment = new LotteryDisplayFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.LotteryDisplay_layout, fragment);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        gameRule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLottoRule();


            }
        });





        return view;
    }



    public void showLottoRule (){

        lottoRuleFragment.setTargetFragment(this, 0);
        lottoRuleFragment.show(getFragmentManager(), "Lotto_Rule_dialog");

    }







}

