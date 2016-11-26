package com.example.peter.focus;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Peter on 2016/9/29.
 */
public class CommentFragment extends Fragment {
    private static final String ARGUMENT_TITLE = "VendorTitle";

    ListView commentListView;

    final static ArrayList<String> memberPicList = new ArrayList<>();
    final static ArrayList<String> memberNameList = new ArrayList<>();
    final static ArrayList<String> memberCommentList = new ArrayList<>();
    final static ArrayList<String> memberDateList = new ArrayList<>();

    final static String DB_VENDOR_URL = "https://vendor-5acbc.firebaseio.com/Vendors/";
    final static String DB_MEMBER_URL = "https://member-139bd.firebaseio.com/";

    String imgurURL = "http://i.imgur.com/";


    public static CommentFragment newInstance(String vendorTitle){
        Bundle args = new Bundle();
        args.putString(ARGUMENT_TITLE, vendorTitle);
        CommentFragment commentFragment = new CommentFragment();
        commentFragment.setArguments(args);
        return commentFragment;
    }

    public CommentFragment(){

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle SavedInstanceState){
        final View view = layoutInflater.inflate(R.layout.comment_fragment, container, false);

        commentListView = (ListView)view.findViewById(R.id.commentListView);

        final Bundle args = getArguments();

        final EditText editText = (EditText)view.findViewById(R.id.editText);

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    final String note = editText.getText().toString();

                    final Firebase findMemberId = new Firebase(DB_MEMBER_URL);

                    GlobalVariable globalVariable = (GlobalVariable)getActivity().getApplicationContext();
                    String userId =globalVariable.getUserId();

                    Long userLongId = Long.parseLong(userId, 10);

                    Query memberKey = findMemberId.orderByChild("Facebook_ID").equalTo(userLongId);

                    memberKey.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            final String key = dataSnapshot.getKey();

                            final Firebase writeComment = new Firebase(DB_VENDOR_URL);

                            final Query writeVendorComment = writeComment.orderByChild("Information/Name")
                                    .equalTo(args.getString(ARGUMENT_TITLE));

                            Date mDate = new Date();
                            Integer month = mDate.getMonth() + 1;
                            String monthth = month.toString();
                            Integer year = mDate.getYear() + 1900;
                            String yearar = year.toString();
                            final String date = yearar + "/" + monthth + "/" + mDate.getDay() + " " + mDate.getHours() + ":" + mDate.getMinutes();


                            writeVendorComment.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    String key2 = dataSnapshot.getKey();
                                    if(dataSnapshot.child("Comments").exists()){
                                        Map<String, String> addComment = new HashMap<String, String>();
                                        addComment.put("Member_ID", key);
                                        addComment.put("Content", note);
                                        addComment.put("Date", date);
                                        final Firebase confuse = new Firebase(DB_VENDOR_URL + key2);
                                        confuse.child("Comments").push().setValue(addComment);
                                    }else{
                                        final Firebase confuse = new Firebase(DB_VENDOR_URL + key2);
                                        Map<String, String> addComment = new HashMap<String, String>();
                                        addComment.put("Member_ID", key);
                                        addComment.put("Content", note);
                                        addComment.put("Date", date);
                                        Map<String, String> addComment2 = new HashMap<String, String>();
                                        /*
                                        addComment2.put("Member_ID", "");
                                        addComment2.put("Content", "");
                                        addComment2.put("Date", "");
                                        */
                                        Map<String, Object> finalComment = new HashMap<String, Object>();
                                        finalComment.put("Comments", "");
                                        confuse.updateChildren(finalComment);
                                        confuse.child("Comments").push().setValue(addComment);

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

                    editText.setText("");

                }
                return false;
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    final String note = editText.getText().toString();

                    final Firebase findMemberId = new Firebase(DB_MEMBER_URL);

                    GlobalVariable globalVariable = (GlobalVariable) getActivity().getApplicationContext();
                    String userId = globalVariable.getUserId();

                    Long userLongId = Long.parseLong(userId, 10);

                    Query memberKey = findMemberId.orderByChild("Facebook_ID").equalTo(userLongId);

                    memberKey.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            final String key = dataSnapshot.getKey();

                            final Firebase writeComment = new Firebase(DB_VENDOR_URL);

                            final Query writeVendorComment = writeComment.orderByChild("Information/Name")
                                    .equalTo(args.getString(ARGUMENT_TITLE));

                            Date mDate = new Date();
                            Integer month = mDate.getMonth() + 1;
                            String monthth = month.toString();
                            Integer year = mDate.getYear() + 1900;
                            String yearar = year.toString();
                            final String date = yearar + "/" + monthth + "/" + mDate.getDay() + " " + mDate.getHours() + ":" + mDate.getMinutes();


                            writeVendorComment.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    String key2 = dataSnapshot.getKey();
                                    if (dataSnapshot.child("Comments").exists()) {
                                        Map<String, String> addComment = new HashMap<String, String>();
                                        addComment.put("Member_ID", key);
                                        addComment.put("Content", note);
                                        addComment.put("Date", date);
                                        final Firebase confuse = new Firebase(DB_VENDOR_URL + key2);
                                        confuse.child("Comments").push().setValue(addComment);
                                    } else {
                                        final Firebase confuse = new Firebase(DB_VENDOR_URL + key2);
                                        Map<String, String> addComment = new HashMap<String, String>();
                                        addComment.put("Member_ID", key);
                                        addComment.put("Content", note);
                                        addComment.put("Date", date);
                                        /*
                                        Map<String, String> addComment2 = new HashMap<String, String>();
                                        addComment2.put("Member_ID", "");
                                        addComment2.put("Content", "");
                                        addComment2.put("Date", "");
                                        */
                                        Map<String, Object> finalComment = new HashMap<String, Object>();
                                        finalComment.put("Comments", "");
                                        confuse.updateChildren(finalComment);
                                        confuse.child("Comments").push().setValue(addComment);

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

                    editText.setText("");

                }
                return false;
            }
        });

        final CustomAdapter adapter = new CustomAdapter(this.getActivity(), memberPicList, memberNameList
                , memberCommentList, memberDateList);

        final Firebase commentFirebase = new Firebase(DB_VENDOR_URL);

        Query specificVendor = commentFirebase.orderByChild("Information/Name").equalTo(args.getString(ARGUMENT_TITLE));

        specificVendor.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.child("Comments").exists()) {
                    for (DataSnapshot exDataSnapshot : dataSnapshot.child("Comments").getChildren()) {

                        String comment = (String) exDataSnapshot.child("Content").getValue();
                        String date = (String) exDataSnapshot.child("Date").getValue();
                        String memberId = (String) exDataSnapshot.child("Member_ID").getValue();

                        memberCommentList.add(comment);
                        memberDateList.add(date);

                        final Firebase member = new Firebase(DB_MEMBER_URL);

                        Query findMember = member.orderByKey().equalTo(memberId);
                        findMember.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                String name = (String) dataSnapshot.child("Nickname").getValue();

                                String picId = (String)dataSnapshot.child("Photos/Photo_ID").getValue();
                                String pic = "https://graph.facebook.com/" + picId + "/picture?type=normal";
                                memberPicList.add(pic);

                                memberNameList.add(name);

                                commentListView.setAdapter(adapter);

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

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                memberNameList.clear();
                memberDateList.clear();
                memberCommentList.clear();
                memberPicList.clear();

                if(dataSnapshot.child("Comments").exists()) {
                    for (DataSnapshot exDataSnapshot : dataSnapshot.child("Comments").getChildren()) {

                        String comment = (String) exDataSnapshot.child("Content").getValue();
                        String date = (String) exDataSnapshot.child("Date").getValue();
                        String memberId = (String) exDataSnapshot.child("Member_ID").getValue();

                        memberCommentList.add(comment);
                        memberDateList.add(date);

                        final Firebase member = new Firebase(DB_MEMBER_URL);

                        Query findMember = member.orderByKey().equalTo(memberId);
                        findMember.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                String name = (String) dataSnapshot.child("Nickname").getValue();


                                String picId = (String)dataSnapshot.child("Photos/Photo_ID").getValue();
                                String pic = "https://graph.facebook.com/" + picId + "/picture?type=small";
                                memberPicList.add(pic);


                                memberNameList.add(name);

                                commentListView.setAdapter(adapter);

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


        memberNameList.clear();
        memberDateList.clear();
        memberCommentList.clear();
        memberPicList.clear();


        return view;
    }




    public class CustomAdapter extends BaseAdapter {

        Context c;
        ArrayList<String> memberPic;
        ArrayList<String> memberName;
        ArrayList<String> memberComment;
        ArrayList<String> memberDate;

        public CustomAdapter(Context context, ArrayList<String> memberPic, ArrayList<String> memberName
                , ArrayList<String> memberComment, ArrayList<String> memberDate){
            c = context;
            this.memberPic = memberPic;
            this.memberName = memberName;
            this.memberComment = memberComment;
            this.memberDate = memberDate;
        }


        @Override
        public int getCount() {
            return memberComment.size();
        }

        @Override
        public Object getItem(int position) {
            return memberComment.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View list;
            LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            list = inflater.inflate(R.layout.comment_listview, null);

            ImageView memberImageView = (ImageView)list.findViewById(R.id.memberImageView);
            TextView nameTextView = (TextView)list.findViewById(R.id.nameTextView);
            TextView commentTextView = (TextView)list.findViewById(R.id.commentTextView);
            TextView dateTextView = (TextView)list.findViewById(R.id.dateTextView);


            final String pic = memberPic.get(position);
            DownloadHttpsImageTask downloadHttpsImageTask = new DownloadHttpsImageTask(memberImageView);
            downloadHttpsImageTask.execute(pic);


            final String name = memberName.get(position);
            nameTextView.setText(name);

            final String comment = memberComment.get(position);
            commentTextView.setText(comment);

            final String date = memberDate.get(position);
            dateTextView.setText(date);

            return list;
        }
    }


}
