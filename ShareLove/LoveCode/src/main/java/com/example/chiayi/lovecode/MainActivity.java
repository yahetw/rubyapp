package com.example.chiayi.lovecode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Resource: http://stackoverflow.com/questions/28232116/android-using-zxing-generate-qr-code
    final static String DB_URL="https://barcode-29a1e.firebaseio.com/Invoice";
    TextView textView2;
    Spinner spinner;
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lovecode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        //add a array list to retrieve title 0629
        //可用
        final ArrayList<Long> codeList= new ArrayList<>();
        textView2=(TextView)findViewById(R.id.textView2);
        imageView=(ImageView)findViewById(R.id.imageView);
        //July 11
        //MultiFormatWriter writer= new MultiFormatWriter();
        spinner=(Spinner)findViewById(R.id.spinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,android.R.id.text1);
        spinner.setAdapter(adapter);



        //Set fire base
        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase(DB_URL);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String st = (String) dataSnapshot.child("title").getValue();
                adapter.add(st);
                System.out.println(adapter);

                Long cd = (Long) dataSnapshot.child("code").getValue();
                codeList.add(cd);
                System.out.println(codeList);

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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String code = String.valueOf(codeList.get(position));

                //String code= String.valueOf(position+1);
                textView2.setText(code);
                try {
                    BitMatrix bitMatrix= new MultiFormatWriter().encode(code,BarcodeFormat.CODE_128,500,300);
                    Bitmap bitmap= toBitmap(bitMatrix);
                    imageView.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Bitmap toBitmap(BitMatrix bitMatrix) {
//        // 定义位图的款和高
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();

        Bitmap bmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                bmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    }


