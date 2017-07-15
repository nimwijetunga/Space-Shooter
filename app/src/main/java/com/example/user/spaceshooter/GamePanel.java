package com.example.user.spaceshooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by User on 2017-07-01.
 */

public class GamePanel{

    private Player player;
    private Point playerPoint;
    private Level level;
    private int numObs = 15, numItems = 3;
    private boolean movedDown = false;
    private long moveTimer;
    private Context ctxT;
    private UserName un;

    public GamePanel(Context ctx, int width, int height){
        level = new Level(ctx, 500, numObs, 1000, numItems);
        ctxT = ctx;
        un = new UserName(width, height, ctx);
        player = new Player(new Rect(100,100,200,200), level, ctx, width, height);
        playerPoint = new Point(MainActivity.px,MainActivity.py);
    }

    public Player getPlayer(){
        return player;
    }

    public void reset(int width, int height){
        numObs = 15;
        level = new Level(ctxT, width, numObs, height, numItems);
        MainActivity.px = 150;
        MainActivity.py = 150;
        playerPoint = new Point(MainActivity.px,MainActivity.py);
        player.setLevel(level);
    }

    public void nextLevel(Canvas c){
        if((player.rect.centerY() + 36) >= c.getHeight()) {
            numObs += 4;
            numItems++;
            if(numObs >= 50)
                numObs = 50;//Cap the number of objects
            reset(c.getWidth(), c.getHeight());
            player.setAmmo(5);
        }
    }

    public void checkBounds(Canvas c){
        int px = MainActivity.px;
        if(px < 0)
            px = 0;
        else if(px > c.getWidth())
            px = c.getWidth();
        if(MainActivity.py < 0)
            MainActivity.py = 0;
        MainActivity.px = px;
    }

    public void incrY(){
       if(!movedDown){
           MainActivity.py += 20;
           movedDown = true;
           moveTimer = System.nanoTime();
       }
    }

    public void fireLaser(){
        if(Surface.touch && !player.isDead())
            player.fireLaser();
        Surface.touch = false;
    }

    public void update(int width, int height){
        if(!player.getUnMenu()) {
            playerPoint.set(MainActivity.px, MainActivity.py);
            fireLaser();
            player.update(playerPoint);
            player.hitObs();
            player.getItem();
            if (player.isDead())
                reset(width, height);
            incrY();
            updateTimers();
        }
        if(un.isEnter()){
            player.setUnMenu(false);
            player.addHighScore(un.getUn());
            un.setEnter(false);
        }
    }

    public void updateTimers(){
        //Move Timer
        long elapsed = (System.nanoTime() - moveTimer) / 1000000;
        if(elapsed > 50)
            movedDown = false;
    }

    public void draw(Canvas c){

        //Background Image
        Drawable d = ctxT.getResources().getDrawable(R.drawable.space);
        d.setBounds(0, 0, c.getWidth(), c.getHeight());
        d.draw(c);

        //Custom Font
        Typeface type = Typeface.createFromAsset(ctxT.getAssets(),"fonts/space_font.ttf");
        //Player Lives
        Paint paint = new Paint();
        paint.setTypeface(type);
        paint.setColor(Color.YELLOW);
        paint.setTextSize(70);
        if(!player.getUnMenu()) {
            c.drawText("Lives: " + Integer.toString(player.getLives()), 50, 100, paint);
            //Player Ammo Count
            paint.setColor(Color.RED);
            c.drawText("Ammo: " + Integer.toString(player.getAmmo()), c.getWidth() - 350, 100, paint);
            //Player Score
            paint.setColor(Color.GREEN);
            c.drawText("Score: " + Integer.toString(player.getScore()), 50, c.getHeight() - 100, paint);

            //Draw Obstacles and Player
            for (Obstacle i : level.getObs())
                i.draw(c);
            player.draw(c);

            //Draw Items
            for (Item i : level.getItems())
                i.drawItem(c);

            //Draw Laser
            for (Laser i : player.getLasers())
                i.fire(c);

            nextLevel(c);
            checkBounds(c);
        }else{
           un.draw(c);
        }
    }
}
