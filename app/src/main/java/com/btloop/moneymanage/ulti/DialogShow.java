package com.btloop.moneymanage.ulti;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.btloop.moneymanage.R;
import com.btloop.moneymanage.fragment.AddEventFragment;

/**
 * Created by thede on 5/8/2016.
 */
public class DialogShow {
    public void requestDialog(Context context,String title,String mess)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(mess);
        builder.setPositiveButton("Có",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setNegativeButton("Không",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }
}
