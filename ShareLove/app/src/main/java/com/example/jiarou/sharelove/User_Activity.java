package com.example.jiarou.sharelove;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class User_Activity extends AppCompatActivity  implements UserFragment.OnFragmentInteractionListener,NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentMgr = getFragmentManager();
        FragmentTransaction fragmentTrans = fragmentMgr.beginTransaction();
         UserFragment userFrangment = new UserFragment();
        fragmentTrans.add(R.id.user_root, userFrangment, "user");
        fragmentTrans.commit();


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
    public void onFragmentInteraction(Uri uri) {

    }

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
                finish();
                break;
            case R.id.logout:
                intent.setClass(this, Login.class);
                startActivityForResult(intent, 2);

            default:
                break;
        }
        return true;
    }
}
