package com.example.user.spaceshooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.constraint.solver.widgets.Rectangle;

import java.util.ArrayList;

/**
 * Created by User on 2017-07-02.
 */

public class Laser{
    private Rect rect;
    private Context ctx;
    private int speed;
    private boolean remove = false;
    private Level level;

    public Laser(Rect rect, Context ctx, Level level){
       speed = 7;
       this.rect = rect;
       this.ctx = ctx;
       this.level = level;
    }

    public void drawLaser(Canvas c){
        Drawable d = ctx.getResources().getDrawable(R.drawable.laser);
        d.setBounds(rect.left, rect.top, rect.right, rect.bottom);
        d.draw(c);
    }

    public void fire(Canvas c){
        for(int i = 0; i < speed; i++){
            drawLaser(c);
            basicAttack();
        }
    }

    public boolean shouldRemove(){
        return remove;
    }

    public void basicAttack(){
        rect.top += 3;
        rect.bottom += 3;
        hitObs();
    }

    public void hitObs(){
        ArrayList<Obstacle> obs = level.getObs();
        for(int i = 0; i < obs.size(); i++){
            if(Rect.intersects(this.rect, obs.get(i).getRect())) {
                obs.remove(i);
                remove = true;
            }
        }
    }

}
