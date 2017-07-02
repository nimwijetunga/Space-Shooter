package com.example.user.spaceshooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by User on 2017-07-01.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{

    private MainThread thread;
    private Player player;
    private Point playerPoint;
    private Level level;
    private int numObs = 15;
    private boolean movedDown = false;
    private long moveTimer;
    private Context ctxT;

    public GamePanel(Context ctx){
        super(ctx);

        ctxT = ctx;

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        level = new Level(ctx, 500, numObs, 1000);

        player = new Player(new Rect(100,100,200,200), level, ctx);
        playerPoint = new Point(MainActivity.px,MainActivity.py);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //does nothing
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        while(retry){
            try{
                thread.setRunning(false);
                thread.join();
            }catch(Exception e){e.printStackTrace();}
            retry = false;
        }
    }

    public void reset(int width, int height){
        numObs = 15;
        level = new Level(ctxT, width, numObs, height);
        MainActivity.px = 150;
        MainActivity.py = 150;
        playerPoint = new Point(MainActivity.px,MainActivity.py);
        player.setLevel(level);
    }

    public void nextLevel(Canvas c){
        if((player.rect.centerY() + 36) >= c.getHeight()) {
            numObs += 4;
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

    @Override
    public boolean onTouchEvent(MotionEvent e){
        switch(e.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                player.fireLaser();
        }
        return true;
    }

    public void update(int width, int height){
        playerPoint.set(MainActivity.px, MainActivity.py);
        player.update(playerPoint);
        player.hitObs();
        if(player.isDead())
            reset(width, height);
        incrY();
        updateTimers();
    }

    public void updateTimers(){
        //Move Timer
        long elapsed = (System.nanoTime() - moveTimer) / 1000000;
        if(elapsed > 50)
            movedDown = false;
    }

    @Override
    public void draw(Canvas c){
        super.draw(c);

        //Background Image
        Drawable d = getResources().getDrawable(R.drawable.space);
        d.setBounds(0, 0, c.getWidth(), c.getHeight());
        d.draw(c);

        //Custom Font
        Typeface type = Typeface.createFromAsset(ctxT.getAssets(),"fonts/space_font.ttf");
        //Player Lives
        Paint paint = new Paint();
        paint.setTypeface(type);
        paint.setColor(Color.YELLOW);
        paint.setTextSize(70);
        c.drawText("Lives: " + Integer.toString(player.getLives()), 50, 100, paint);
        //Player Ammo Count
        paint.setColor(Color.RED);
        c.drawText("Ammo: " + Integer.toString(player.getAmmo()), c.getWidth() - 350, 100, paint);
        //Player Score
        paint.setColor(Color.GREEN);
        c.drawText("Score: " + Integer.toString(player.getScore()), 50, c.getHeight() - 100, paint);


        //Draw Obstacles and Player
        for(Obstacle i : level.getObs())
            i.draw(c);
        player.draw(c);

        //Draw Laser
        for(Laser i : player.getLasers())
            i.fire(c);

        nextLevel(c);
        checkBounds(c);
    }
}
