package com.example.user.spaceshooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by User on 2017-07-01.
 */

public class Obstacle extends Entity {

    public Obstacle(Rect rect, Level level, Context ctx){
       super(rect, level, ctx);
    }

    @Override
    public void draw(Canvas c) {
        //Asteroid Obstacles
        Drawable d = ctx.getResources().getDrawable(R.drawable.asteroid);
        d.setBounds(rect.left, rect.top, rect.right, rect.bottom);
        d.draw(c);

    }
}
