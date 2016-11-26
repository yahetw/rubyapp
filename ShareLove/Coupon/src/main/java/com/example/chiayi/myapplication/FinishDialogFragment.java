package com.example.chiayi.myapplication;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

/**
 * Created by chiayi on 16/8/16.
 */
public class FinishDialogFragment extends DialogFragment {

    interface FListener{
        void OwnedCouponClick();
        void GoBackClick();
    }

    private FListener flistener;



    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        try {
            flistener = (FListener) getTargetFragment();

        }catch(ClassCastException e){
            e.printStackTrace();
        }

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

        builder.setView(R.layout.finish_dialog_fragment)
                .setPositiveButton("查看購買的券", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        flistener.OwnedCouponClick();

                    }
                })
                .setNegativeButton("繼續購買", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        flistener.GoBackClick();

                    }
                });


        return builder.create();
    }


}
