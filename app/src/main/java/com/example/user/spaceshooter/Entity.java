package com.example.user.spaceshooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

/**
 * Created by User on 2017-07-01.
 */

public abstract class Entity{

    protected Rect rect;
    protected Level level;
    protected Context ctx;

    public Entity(Rect rect, Level level, Context ctx){
        this.ctx = ctx;
        this.rect = rect;
        this.level = level;
    }

    public void setLevel(Level level){
     this.level = level;
    }

    public Rect getRect(){
        return rect;
    }

    public boolean collidesWith(Entity e2){
        if(Rect.intersects(this.rect, e2.getRect()))
            return true;
        return false;
    }

    public abstract void draw(Canvas c);
}
