package com.example.user.spaceshooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

/**
 * Created by User on 2017-07-02.
 */

public class Button {

    private Rect rect;
    private Context ctx;
    private String txt;
    private boolean clicked;
    private int color;


    public Button(Context ctx, Rect rect, String txt, int color){
        this.ctx = ctx;
        this.rect = rect;
        this.txt = txt;
        this.color = color;
        clicked = false;
    }

    public boolean getClicked(){
        return clicked;
    }

    public String getText(){
        return txt;
    }

    public void clicked(int x, int y){
        if(rect.contains(x,y))
            clicked = true;
        else
            clicked = false;
    }

    public void drawButton(Canvas c){
        Typeface type = Typeface.createFromAsset(ctx.getAssets(),"fonts/space_font.ttf");
        Paint paint = new Paint();
        paint.setTypeface(type);
        paint.setColor(color);
        paint.setTextSize(70);

        //Draw Button
        if(!clicked) {
            paint.setStyle(Paint.Style.STROKE);
            c.drawRect(rect, paint);
        }else{
            paint.setStyle(Paint.Style.FILL);
            c.drawRect(rect,paint);
        }

        //***CENTER BUTTONS
        RectF bounds = new RectF(rect);
        // measure text width
        bounds.right = paint.measureText(txt, 0, txt.length());
        // measure text height
        bounds.bottom = paint.descent() - paint.ascent();

        bounds.left += (rect.width() - bounds.right) / 2.0f;
        bounds.top += (rect.height() - bounds.bottom) / 2.0f;

        paint.setColor(Color.YELLOW);
        //Draw Centered Text
        c.drawText(txt, bounds.left, bounds.top - paint.ascent(), paint);
    }
}
