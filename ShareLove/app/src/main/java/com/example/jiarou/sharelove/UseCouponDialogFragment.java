package com.example.jiarou.sharelove;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by chiayi on 16/9/17.
 */
public class UseCouponDialogFragment extends DialogFragment {

    interface Listener{
        void onPositiveClick();
        void onNegativeClick();
    }

    private Listener ulistener;



    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        try {
            ulistener = (Listener) getTargetFragment();

        }catch(ClassCastException e){
            e.printStackTrace();
        }

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

        builder.setView(R.layout.use_coupon_dialog_fragment)
                .setPositiveButton("使用", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ulistener.onPositiveClick();

                    }
                })
                .setNegativeButton("取消", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ulistener.onNegativeClick();

                    }
                });


        return builder.create();
    }


}
