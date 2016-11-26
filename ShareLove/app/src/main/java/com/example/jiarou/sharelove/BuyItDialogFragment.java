package com.example.jiarou.sharelove;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by chiayi on 16/8/15.
 */
public class BuyItDialogFragment extends DialogFragment {



    interface Listener{
        void onPositiveClick();
        void onNegativeClick();
    }

    private Listener mlistener;



    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        try {
            mlistener = (Listener) getTargetFragment();

        }catch(ClassCastException e){
            e.printStackTrace();
        }

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

        builder.setView(R.layout.buyit_dialog_fragment)
                .setPositiveButton("確定", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mlistener.onPositiveClick();

                    }
                })
                .setNegativeButton("取消", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mlistener.onNegativeClick();

                    }
                });


        return builder.create();
    }


}
