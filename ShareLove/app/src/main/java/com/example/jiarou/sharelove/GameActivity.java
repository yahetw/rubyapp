package com.example.jiarou.sharelove;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;

import java.util.Objects;

public class GameActivity extends AppCompatActivity  implements GameFragment.OpenGame,ChanglleFragment.OnFragmentInteractionListener,Game_areaFragment.Choose_area,Start_GameFragment.Start_game,LocationListener,ChanglleFragment.delete,NavigationView.OnNavigationItemSelectedListener,ChanglleFragment01.ch01,Success.success{
    String get_number;
    String get_check;
    String condition;
    String Address;
    int pass;
    private  Fragment fg;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.game_root, GameFragment.newInstance(), "game")
                    .commit();


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void OpenGame() {






        final ChanglleFragment changlleFragment =
                ChanglleFragment.newInstance();

                 getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.game_root, changlleFragment, "changenge")
                .addToBackStack(null)
                .commit();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.game_root);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFragmentInteraction(String number) {




        get_number = number;

        final Firebase FirebaseRef = new Firebase("https://member-activity.firebaseio.com/Activity");
        Query memberQuery = FirebaseRef.orderByChild("Facebook_ID").equalTo(111111111111111l);
        memberQuery.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String memberKey01 = dataSnapshot.getKey();
                condition = (String) dataSnapshot.child("condition").getValue();
                Address = (String) dataSnapshot.child("now").getValue();
                Log.d("d", "hhhh" + Address);
                if (Objects.equals(condition, "false")) {
                    final Game_areaFragment game_areaFragment =
                            Game_areaFragment.newInstance();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.game_root, game_areaFragment, "game_areaFragment")
                            .addToBackStack(null)
                            .commit();
                } else if (Objects.equals(condition, "true")) {

                    final Game_areaFragment game_areaFragment =
                            Game_areaFragment.newInstance();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.game_root, game_areaFragment, "game_areaFragment")
                            .addToBackStack(null)
                            .commit();

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
    public void ch01(String number) {




        get_number = number;

        final Firebase FirebaseRef = new Firebase("https://member-activity.firebaseio.com/Activity");
        Query memberQuery = FirebaseRef.orderByChild("Facebook_ID").equalTo(111111111111111l);
        memberQuery.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String memberKey01 = dataSnapshot.getKey();
                condition = (String) dataSnapshot.child("condition").getValue();
                Address = (String) dataSnapshot.child("now").getValue();
                Log.d("d", "hhhh" + Address);
                if (Objects.equals(condition, "false")) {
                    final Game_areaFragment game_areaFragment =
                            Game_areaFragment.newInstance();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.game_root, game_areaFragment, "game_areaFragment")
                            .addToBackStack(null)
                            .commit();
                } else if (Objects.equals(condition, "true")) {

                    final Game_areaFragment game_areaFragment =
                            Game_areaFragment.newInstance();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.game_root, game_areaFragment, "game_areaFragment")
                            .addToBackStack(null)
                            .commit();

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
    public void Choose_area(String data) {

/**
        final android.support.v4.app.FragmentManager fragmentManager =
                getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag("changenge");
        getSupportFragmentManager().popBackStack();

        fragmentTransaction.remove(fragment);


        fragmentTransaction.commit();
**/

        if(pass==1) {


        }else {
            pass =1;

        }



        final Start_GameFragment start_gameFragment =
                Start_GameFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("address", data);
        bundle.putString("number", get_number);

        bundle.putString("123", Address);
        start_gameFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.game_root, start_gameFragment, " start_gameFragment")
                .addToBackStack(null)
                .commit();






    }








    @Override
    public void Start_game(String number) {
        final Success success =
              Success.newInstance();

        getSupportFragmentManager()
                .beginTransaction()

                .replace(R.id.game_root, success, "success")
                .addToBackStack(null)
        .commit();



    }




    @Override
    public void delete() {
       final GameFragment  gameFragment =GameFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.game_root, gameFragment, "game")
                .addToBackStack(null)
                .commit();




    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }
    }



    /**
    @Override
    public  boolean onKeyDown(int keyCode, KeyEvent event){
        Log.d("test","event");
        AlertDialog.Builder bdr = new AlertDialog.Builder(this);
        bdr.setMessage("確定離開遊戲嘛？");
        bdr.setTitle("提醒");
        bdr.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GameActivity.this.finish();

            }
        });

        bdr.setNegativeButton("繼續", null);
                 bdr.show();
                 if (fg instanceof Start_GameFragment) {
                     Start_GameFragment.onKeyDown(keyCode, event);

                 }
                 return super.onKeyDown(keyCode, event);
             }

     **/

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        final Intent intent = new Intent();
        switch (id){
            case R.id.nav_home:

                intent.setClass(this, IndexActivity.class);
                startActivityForResult(intent, 2);
                break;
            case R.id.nav_game:
                intent.setClass(this, GameActivity.class);
                startActivityForResult(intent, 2);
                finish();
                break;
            case R.id.nav_focus:
                intent.setClass(this, MainActivity.class);
                startActivityForResult(intent, 2);
                break;
            case R.id.nav_lovecode:
                intent.setClass(this, LoveCodeMainActivity.class);
                startActivityForResult(intent, 2);
                break;
            case R.id.nav_user:
                intent.setClass(this, User_Activity.class);
                startActivityForResult(intent, 2);
                break;
            case R.id.logout:
                intent.setClass(this, Login.class);
                startActivityForResult(intent, 2);

            default:
                break;
        }
        return true;
    }


    @Override
    public void success() {
        final ChanglleFragment changlleFragment =
                ChanglleFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.game_root, changlleFragment, "changenge")
                .addToBackStack(null)
                .commit();


    }
}
