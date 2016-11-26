package com.example.chiayi.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class CouponMainActivity extends AppCompatActivity implements CouponTypesFragment.OnCouponSelected {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout, CouponTypesFragment.newInstance(), "CouponTypes")
                    .commit();
        }

    }


    @Override
    public void onCouponSelected(String CouponName, String Price, String couponURL, String Info) {

        final CouponDetailsFragment detailsFragment =
                CouponDetailsFragment.newInstance(CouponName,Price,couponURL,Info);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_layout, detailsFragment, "CouponDetails")
                .addToBackStack(null)
                .commit();
    }





    }

