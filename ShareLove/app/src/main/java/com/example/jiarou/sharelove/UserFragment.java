package com.example.jiarou.sharelove;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String imgurURL = "http://i.imgur.com/";
    GlobalVariable globalVariable = (GlobalVariable)getApplicationContext();
    Long facebookID = Long.parseLong(globalVariable.getUserId());
    Button collect_store_btn, change_name, finish_name;
    final Firebase user_ref = new Firebase("https://member-139bd.firebaseio.com/");

    //Oct 14 update
    private GridView gridView;
    private int[] navigatebtn = {
            R.mipmap.lotto,R.mipmap.my_coupon, R.mipmap.buy_coupon,
            R.mipmap.add_vendor};
    private String[] navigateText = {
            "樂透", "優惠券商店", "我的優惠券", "新增店家"};
    //

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(){
        Bundle args = new Bundle();

        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_user, container, false);



        //Oct 14 update
        List<Map<String, Object>> items = new ArrayList<>();
        for (int i = 0; i < navigatebtn.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("navigatebtn", navigatebtn[i]);
            item.put("text", navigateText[i]);
            items.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this.getContext(), items, R.layout.user_gridview, new String[]{"navigatebtn", "text"}, new int[]{R.id.imageView5, R.id.textView25});


        gridView = (GridView)view.findViewById(R.id.gridView2);
        gridView.setNumColumns(2);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Intent intent = new Intent();

                switch (position) {

                    case 0:
                        intent.setClass(getActivity(), LotteryMainActivity.class);
                        startActivity(intent);
                        break;


                    case 1:
                        intent.setClass(getActivity(), CouponMainActivity.class);
                        startActivity(intent);
                        break;


                    case 2:
                        intent.setClass(getActivity(), OwnedCouponMainActivity.class);
                        startActivity(intent);
                        break;

                    case 3:
                        intent.setClass(getActivity(), AddVendorMainActivity.class);
                        startActivity(intent);
                        break;

                    default:
                        break;


                }


            }

        });


        //


        //跳到 collect_store的按鈕
        collect_store_btn=(Button)view.findViewById(R.id.collect_store_btn);
        collect_store_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setClass(getActivity(), CollectStoreMainActivity.class);
                startActivity(intent);

            }
        });


        //連結ＸＭＬ文字跟圖片
        final TextView user_name = (TextView)view.findViewById(R.id.name);
        final TextView owned_point= (TextView)view.findViewById(R.id.owned_point);
        final ImageView photo= (ImageView)view.findViewById(R.id.photo);
        //firebase 取資料
        Firebase.setAndroidContext(this.getActivity());

        /*之後要加這段半別是哪一個使用者*/
        //  Query focusVendor = vendor2.orderByChild("Information/Name").equalTo(mark);
//        /* focusVendor.*/ChildEventListener childEventListener = user_ref.addChildEventListener(new ChildEventListener()

        Query query = user_ref.orderByChild("Facebook_ID").equalTo(facebookID);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String username= (String)dataSnapshot.child("Nickname").getValue();
                Long owned_points = (Long)dataSnapshot.child("Owned_Points").getValue();
                final String key = (String)dataSnapshot.getKey();
                user_name.setText(username);
                owned_point.setText(String.valueOf(owned_points));



                //Change Nickname
                change_name = (Button) view.findViewById(R.id.change_name_btn);
                change_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewSwitcher switcher = (ViewSwitcher) view.findViewById(R.id.my_switcher1);
                        gridView.setEnabled(false);
                        collect_store_btn.setEnabled(false);
                        switcher.showNext();
                    }
                });


                //Finish Changing
                finish_name = (Button) view.findViewById(R.id.finish_name_btn);
                finish_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewSwitcher switcher = (ViewSwitcher) view.findViewById(R.id.my_switcher1);
                        EditText edit_name = (EditText) view.findViewById(R.id.hidden_edit_view);
                        String name = edit_name.getText().toString();

                        if (name.isEmpty()){

                            switcher.showPrevious();
                            Toast.makeText(getContext(),"No Change", Toast.LENGTH_LONG).show();

                        }else{

                            user_ref.child(key).child("Nickname").setValue(name);
                            user_name.setText(name);
                            switcher.showPrevious();
                            Toast.makeText(getContext(), "更新成功", Toast.LENGTH_LONG).show();

                        }

                        collect_store_btn.setEnabled(true);
                        gridView.setEnabled(true);


                    }
                });



                //大頭照取的地方
                String picId = (String)dataSnapshot.child("Photos").child("Photo_ID").getValue();
                String pic = "https://graph.facebook.com/" + picId + "/picture?type=normal";
                DownloadHttpsImageTask downloadImage = new DownloadHttpsImageTask(photo);
                downloadImage.execute(pic);

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
        })   ;




        return  view;




    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
