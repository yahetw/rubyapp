package com.example.jiarou.sharelove;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by chiayi on 16/8/17.
 */
public class AddVendorMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vender);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.AddVendo_layout, AddVendorFragment.newInstance(), "AddVendor")
                    .commit();
        }
    }
}
