package com.btloop.moneymanage.ulti;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.Button;

/**
 * Created by thede on 5/7/2016.
 */
public class ConerButton {
    public void setCorner(Button bt,String color)
    {
        final GradientDrawable gdDefault = new GradientDrawable();
        gdDefault.setColor(Color.parseColor(color));
        gdDefault.setCornerRadius(30);
        bt.setBackgroundDrawable(gdDefault);
    }
}
