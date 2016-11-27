
package com.example.jiarou.sharelove;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jiarou.sharelove.GlobalVariable;
import com.example.jiarou.sharelove.R;
import com.example.jiarou.sharelove.Start_GameFragment;
import com.facebook.AccessToken;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link Game_areaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Game_areaFragment extends Fragment  implements FragmentBackHandler {

    private String[] type = new String[] {"新北市", "台北市"
            ,"台中市","高雄市"};
    private String[] address001 = new String[]{"板橋區"};
    private String[][] type2 = new String[][]{{"板橋區"},{"文山區", "中正區", "信義區", "萬華區","大安區", "大同區","松山區"},{"西屯區"},{"橋頭區", "三民區"}};

    final static ArrayList<String> address = new ArrayList<>();
    final static ArrayList<String> city01 = new ArrayList<>();
    int num;
    Spinner city,area;
    Set set =new HashSet();
    ListView list;
    final static String DB_URL = "https://vendor-5acbc.firebaseio.com/Vendors";
    String get_city;
    String get_area;
    String get_address;
    ArrayAdapter<String> area_adapter,adapter,test_adapter;
    String get_location;
    Button start;
    Long userLongId;
    String one,two,three,four;
    String check_key;



    private Choose_area mListener;

    public Game_areaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment Game_areaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Game_areaFragment newInstance() {
        Game_areaFragment fragment = new Game_areaFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Start_GameFragment start_gameFragment =
                Start_GameFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("location", get_location);
        start_gameFragment.setArguments(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_game_area, container, false);


        GlobalVariable globalVariable = (GlobalVariable) getActivity().getApplicationContext();
        String userId =  globalVariable.setUserId(AccessToken.getCurrentAccessToken().getUserId());
        userLongId = Long.parseLong(userId, 10);

        //選擇城市的spinner
        city=(Spinner) view.findViewById(R.id.city);
        final  ArrayAdapter<String> city_adapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_dropdown_item,type);
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(city_adapter);



        city.setOnItemSelectedListener(selectListener);


        area_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, address001);
        area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        area = (Spinner) view.findViewById(R.id.area);
        area.setAdapter(area_adapter);

        area.setOnItemSelectedListener(zipListener);





        list = (ListView) view.findViewById(R.id.check);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked,address001);
        list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        list.setAdapter(test_adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                get_location = (String) list.getItemAtPosition(position);
                get_area();
                put_now_location(get_location);


                Log.d("location", "location" + get_location);

            }
        });





        start =(Button) view.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(get_location==null) {
                    Toast.makeText(getActivity(), "尚未選取地址", Toast.LENGTH_LONG).show();

                }else if(check_key=="1"){
                    Toast.makeText(getActivity(), "此攤販已挑戰過囉！請換攤販吧！", Toast.LENGTH_LONG).show();
                }
                else {

                    AlertDialog.Builder bdr = new AlertDialog.Builder(getActivity());
                    bdr.setMessage("確認後就無法返回重新選擇囉！");
                    bdr.setTitle("提醒");
                    bdr.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mListener.Choose_area(get_location);
                            put_now_location(get_location);
                            final Firebase myFirebaseRef = new Firebase("https://member-activity.firebaseio.com/Activity");
                            // Map<String, Object> used_shop= new HashMap<String, Object>();
                            // used_shop.put("used",location);
                            Query memberQuery = myFirebaseRef.orderByChild("Facebook_ID").equalTo(userLongId);
                            memberQuery.addChildEventListener(new ChildEventListener() {


                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    String memberKey = dataSnapshot.getKey();
                                    myFirebaseRef.child(memberKey).child("condition").setValue("true");



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
                    });
                    bdr.setNegativeButton("返回", null);
                    bdr.show();








                }
            }
        });






/**

 Log.d("set", "set" + set);


 Firebase.setAndroidContext(this.getActivity());
 final Firebase vendor = new Firebase(DB_URL);
 Query gameQuery = vendor.orderByChild("Game").equalTo(true);
 gameQuery.addChildEventListener(new ChildEventListener() {

@Override
public void onChildAdded(DataSnapshot dataSnapshot, String s) {

String title = ((String) dataSnapshot.child("Location/Address").getValue());
//                city_adapter.add(title.substring(0, 3));


/**

set.add(title.substring(0, 3));
Log.d("set", "set" + set);

num = set.size();
Iterator it;
it = set.iterator();

if (set.contains( set.add(title.substring(0, 3)))) {
while (it.hasNext()) {
city01.add((String) it.next());
Log.d("test", "test" + city01);

}


}else {

}

Log.d("set", "set" + set);

Log.d("set", "size" + num);

Log.d("test", "test" + city01);


switch (num) {
case 1:


case 7:

case 8:

case 9:
//city.setAdapter((SpinnerAdapter) city_adapter);
break;


}


//Toast.makeText(getActivity(),num , Toast.LENGTH_LONG).show();


//  city.setAdapter(city_adapter);
 **/
        /**
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

         **/
        return view;
    }


    private AdapterView.OnItemSelectedListener selectListener = new AdapterView.OnItemSelectedListener(){
        public void onItemSelected(AdapterView<?> parent, View v, int position,long id){
            //讀取第一個下拉選單是選擇第幾個
            get_city  = city.getSelectedItem().toString();
            int pos = city.getSelectedItemPosition();

            area_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, type2[pos]);
            //載入第二個下拉選單Spinner
            area.setAdapter(area_adapter);


/**
 final Firebase vendor = new Firebase(DB_URL);

 vendor.addChildEventListener(new ChildEventListener() {

@Override
public void onChildAdded(DataSnapshot dataSnapshot, String s) {
String areas = (String) dataSnapshot.child("Location/Address").getValue();


//找到台北市取出區域
if (areas.contains(get_city)) {
get_area = areas.substring(3, 6);


// area_adapter.add(get_area);

city01.add(get_area);


Log.d("123", "123" + city01);
Log.d("222", "222" + city01.size());


switch (city01.size()) {
case 1:
area_adapter.add(get_area);
break;

case 2:
area_adapter.add(get_area);
break;
case 3:
area_adapter.add(get_area);
break;
case 4:
area_adapter.add(get_area);
break;
case 5:
area_adapter.add(get_area);
break;


case 17:
case 18:
case 19:
case 20:
case 21:
city01.remove(city01.get(0));
city01.remove(city01.get(1));
city01.remove(city01.get(5));
city01.remove(city01.get(6));

for (int i = 0; i < city01.size(); i++) {

area_adapter.add(city01.get(i));

Log.d("test", "ha" + city01.get(i));

}


Log.d("hello", "ha" + city01);
break;

case 22:
city01.remove(city01.get(0));
city01.remove(city01.get(1));
city01.remove(city01.get(5));
city01.remove(city01.get(6));

for (int i = 0; i < city01.size(); i++) {

area_adapter.add(city01.get(i));

Log.d("test", "ha" + city01.get(i));

}


Log.d("hello", "ha" + city01);
break;


}



/**
 *
if(city01.size()>=10){
Log.d("2", "2" + city01.get(1));
Log.d("3", "3" + city01.get(1));
city01.remove(city01.get(0));
city01.remove(city01.get(1));
city01.remove(city01.get(5));
city01.remove(city01.get(6));

}


if(city01.size()>=2) {
for (int i = 1; i <= city01.size(); i++) {

Log.d("1", "1" + "here");
if (city01.get(i)== city01.get(i-1)) {
city01.remove(city01.get(i));
Log.d("1", "1" + i);

Log.d("1", "1" + city01.get(0) );

}else {
city01.add(get_area);

}


}
}

 **/
/**

 }else {

 Log.d("area", "area" + "失敗");
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


 city01.clear();

 }

 public void onNothingSelected(AdapterView<?> arg0){

 }

 **/

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    };









    private AdapterView.OnItemSelectedListener zipListener = new AdapterView.OnItemSelectedListener(){
        public void onItemSelected(AdapterView<?> parent, View v, int position,long id){


            final String select_address  = area.getSelectedItem().toString();


            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, android.R.id.text1);
            list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            list.setAdapter(adapter);



            final Firebase myFirebaseRef = new Firebase("https://vendor-5acbc.firebaseio.com/Vendors");
            ChildEventListener childEventListener = myFirebaseRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String address = (String) dataSnapshot.child("Location/Address").getValue();
                    String  city_area=get_city+select_address;


                    if (address.contains(city_area)) {
                        get_address = address;;
                        Log.d("area", "area" +  get_address);
                        adapter.add(get_address);

                    }else {

                        Log.d("area", "area" + "失敗");
                    }
                    //有問題的code


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

        public void onNothingSelected(AdapterView<?> arg0){

        }

    };



    private   void  put_now_location(final String location){
        final Firebase myFirebaseRef = new Firebase("https://member-activity.firebaseio.com/Activity");
        // Map<String, Object> used_shop= new HashMap<String, Object>();
        // used_shop.put("used",location);
        Query memberQuery = myFirebaseRef.orderByChild("Facebook_ID").equalTo(userLongId);
        memberQuery.addChildEventListener(new ChildEventListener() {



            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String memberKey = dataSnapshot.getKey();



                myFirebaseRef.child(memberKey).child("now").setValue(location);



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


    //測試不重複



    private  void get_area(){

        final Firebase FirebaseRef = new Firebase("https://member-activity.firebaseio.com/Activity");
        Query memberQuery = FirebaseRef.orderByChild("Facebook_ID").equalTo( userLongId);
        ChildEventListener childEventListener = memberQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                one = (String) dataSnapshot.child("one").getValue();
                two = (String) dataSnapshot.child("two").getValue();
                three = (String) dataSnapshot.child("three").getValue();
                four = (String) dataSnapshot.child("four").getValue();

              if(get_location==one){
                 // Toast.makeText(getActivity(), "已選取過囉！", Toast.LENGTH_LONG).show();
                  check_key="1";


              }else if(get_location==two){
                 // Toast.makeText(getActivity(), "已選取過囉！", Toast.LENGTH_LONG).show();
                  check_key="1";


              }else if(get_location==three){
                //  Toast.makeText(getActivity(), "已選取過囉！", Toast.LENGTH_LONG).show();

                  check_key="1";

              }else if(get_location==four){
                 // Toast.makeText(getActivity(), "已選取過囉！", Toast.LENGTH_LONG).show();
                  check_key="1";

              }else {
                  check_key="0";
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


    /**
     // TODO: Rename method, update argument and hook method into UI event
     public void onButtonPressed(Uri uri) {
     if (mListener != null) {
     mListener.Choose_area();
     }
     }**/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Choose_area) {
            mListener = (Choose_area) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Choose_area");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Choose_area {
        // TODO: Update argument type and name
        void Choose_area(String data);


        void delete();
    }

    @Override
    public boolean onBackPressed() {


        return BackHandlerHelper.handleBackPress(this);
    }
}

