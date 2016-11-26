package com.example.jiarou.sharelove;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements FocusFragment.OnFocusSelected
        , VendedInfoFragment.OnCommentSelected, VendedInfoFragment.OnNavigationSelected,NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.focus_main);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.focus_root, FocusFragment.newInstance(), "Focus")
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
    public void onFocusSelected(String vendorTitle/*, String vendorURL, String vendorPhone, String timeRemark,
                                String monTime, String tueTime, String wedTime, String thuTime, String friTime,
                                String satTime, String sunTime, String vendorAddress, String vendorStory*/) {

        final VendedInfoFragment infoFragment =
                VendedInfoFragment.newInstance(vendorTitle/*, vendorURL, vendorPhone, timeRemark, monTime,
                        tueTime, wedTime, thuTime, friTime, satTime, sunTime, vendorAddress, vendorStory*/);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.focus_root, infoFragment, "VendorInfo")
                .addToBackStack(null)
                .commit();



    }

    @Override
    public void onCommentSelected(String vendorTitle) {
        final CommentFragment commentFragment = CommentFragment.newInstance(vendorTitle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.focus_root, commentFragment, "Comment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onNavigationSelected(String vendorTitle) {
        final NavigationMapFragment navigationMapFragment = NavigationMapFragment.newInstance(vendorTitle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.focus_root, navigationMapFragment, "Venderlist2")
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.focus_root);
        fragment.onActivityResult(requestCode, resultCode, data);
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
                finish();
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
}
