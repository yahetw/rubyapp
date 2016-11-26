package com.example.jiarou.sharelove;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.HashMap;
import java.util.Map;
import java.lang.Long;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by chiayi on 16/8/17.
 */
public class AddVendorFragment extends Fragment{

    final static String MEMBER_DB = "https://member-139bd.firebaseio.com/";
    final static String MEMBER_VENDOR_DB = "https://member-vendor.firebaseio.com/";
    GlobalVariable globalVariable = (GlobalVariable)getApplicationContext();
    Long facebookID = Long.parseLong(globalVariable.getUserId());
    Button add_submit;
    Spinner variety_spinner;
    EditText Name_edit,Address_edit,info_edit;
    String[] variety = {"食物","飲料","其他"};
    ArrayAdapter<String> variety_list;
    final String[] selected_variety = new String[1];


    public static AddVendorFragment newInstance(){
        return new AddVendorFragment();
    }

    public AddVendorFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //將畫面設為coupon_types_fragment.xml
        final View view = inflater.inflate(R.layout.add_vendor_fragment, container, false);
        Firebase.setAndroidContext(this.getActivity());
        final String[] Member_ID = new String[1];
        final Firebase member_db = new Firebase(MEMBER_DB);
        final Firebase member_vendor_db = new Firebase(MEMBER_VENDOR_DB);

        Query query = member_db.orderByChild("Facebook_ID").equalTo(facebookID);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Member_ID[0] = dataSnapshot.getKey();

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



        variety_spinner = (Spinner) view.findViewById(R.id.variety_spinner);
        variety_list = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, variety);
        variety_spinner.setAdapter(variety_list);

        variety_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selected_variety[0] = parent.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        Name_edit = (EditText) view.findViewById(R.id.Name_edit);
        Address_edit = (EditText) view.findViewById(R.id.Address_edit);
        info_edit = (EditText) view.findViewById(R.id.info_edit);
        add_submit = (Button) view.findViewById(R.id.add_submit);
        add_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String name = Name_edit.getText().toString();
                String address = Address_edit.getText().toString();
                String info = info_edit.getText().toString();
                Integer kind_num = null;
                switch (selected_variety[0]){
                    case "食物":

                        kind_num = (Integer) 1;
                        break;

                    case "飲料":
                        kind_num = (Integer) 2;
                        break;

                    case "其他":
                        kind_num = (Integer) 3;
                        break;

                    default:
                        break;
                }




                if (name.isEmpty() || address.isEmpty() || info.isEmpty()){

                    Toast.makeText(getContext(),"請確實填寫每一欄，不可空白", Toast.LENGTH_LONG).show();

                }else{

                    Map<String,Object> member_vendor = new HashMap<>();
                    Map<String,Object> inner_member_vendor = new HashMap<>();
                    Map<String,Object> inner_type_id = new HashMap<>();


                    Boolean f = false;
                    member_vendor.put("Member_ID",Member_ID[0]);
                    member_vendor.put("Vendor_ID","");
                    member_vendor.put("Name",name);
                    member_vendor.put("Check","NO");
                    member_vendor.put("Award",f);
                    inner_member_vendor.put("Address",address);
                    inner_member_vendor.put("Introduction",info);
                    inner_type_id.put("Type_ID",kind_num);
                    member_vendor.put("Information",inner_member_vendor);
                    member_vendor.put("Types",inner_type_id);

                    member_vendor_db.push().setValue(member_vendor);

                    Toast.makeText(getContext(), "您新增的攤販資料將進行審核，審核通過將分發點數予您", Toast.LENGTH_LONG).show();

                    Name_edit.setText("");
                    Address_edit.setText("");
                    info_edit.setText("");
                    variety_spinner.setSelection(0);


                }
            }
        });




        return view;
    }





}
