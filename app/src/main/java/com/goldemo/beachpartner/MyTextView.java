package com.goldemo.beachpartner;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by seq-kala on 7/2/18.
 */

public class MyTextView extends TextView{
    public MyTextView(Context context) {
        super(context);
        init();
    }



    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    private void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/yourfont.ttf");
        setTypeface(tf ,1);


    }
}
