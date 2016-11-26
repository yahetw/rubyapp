package com.example.jiarou.sharelove;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Test02Activity extends AppCompatActivity {
    Button button1,button2,button3,button4,over;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test02);

        button1=(Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setClass(Test02Activity.this, Test03Activity.class);
                startActivity(intent);



            }
        });

        button2=(Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setClass(Test02Activity.this, Test03Activity.class);
                startActivity(intent);


            }
        });


        button3=(Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setClass(Test02Activity.this, Test03Activity.class);
                startActivity(intent);


            }
        });



        button4=(Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setClass(Test02Activity.this, Test03Activity.class);
                startActivity(intent);


            }
        });


        over=(Button) findViewById(R.id.over_btn);
        over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent intent = new Intent();
                intent.setClass(Test02Activity.this, TestActivity.class);
                startActivity(intent);


            }
        });


    }
}
