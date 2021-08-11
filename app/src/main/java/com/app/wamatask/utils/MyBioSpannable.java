package com.app.wamatask.utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class MyBioSpannable extends ClickableSpan {
    private boolean isUnderline = true;

    /**
     * Constructor
     */
    public MyBioSpannable(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(isUnderline);
        ds.setColor(Color.parseColor("#000000"));
        ds.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onClick(View widget) {


    }
}
