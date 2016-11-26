package com.example.peter.focus;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements FocusFragment.OnFocusSelected
        , VendedInfoFragment.OnCommentSelected, VendedInfoFragment.OnNavigationSelected{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.focus_main);
        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.focus_root, FocusFragment.newInstance(), "Focus")
                    .commit();
        }
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.focus_root);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

}
