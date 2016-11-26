package com.example.jiarou.sharelove;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by chiayi on 16/8/17.
 */
public class CollectStoreMainActivity extends AppCompatActivity  implements CollectStoreFragment.OnFocusSelected
        , VendedInfoFragment.OnCommentSelected, VendedInfoFragment.OnNavigationSelected
        , VenderListFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_store);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout, CollectStoreFragment.newInstance(), "CollectStore")
                    .commit();
        }
    }

    @Override
    public void OnFocusSelected(String vendorTitle) {
        final VendedInfoFragment infoFragment =
                VendedInfoFragment.newInstance(vendorTitle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_layout, infoFragment, "VendorInfo")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCommentSelected(String vendorTitle) {
        final CommentFragment commentFragment = CommentFragment.newInstance(vendorTitle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_layout, commentFragment, "Comment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFocusSelected(String vendorTitle) {

    }

    @Override
    public void onNavigationSelected(String vendorTitle) {
        final NavigationMapFragment navigationMapFragment = NavigationMapFragment.newInstance(vendorTitle);


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_layout, navigationMapFragment, "Venderlist2")
                .addToBackStack(null)
                .commit();
    }
}