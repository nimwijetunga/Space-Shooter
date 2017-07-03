package com.example.user.spaceshooter;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by User on 2017-07-03.
 */

public class Surface extends SurfaceView implements SurfaceHolder.Callback{

    public static int x, y;
    public static boolean touch = false;
    private MainThread thread;
    private Context ctx;


    public Surface(Context ctx){
        super(ctx);

        this.ctx = ctx;

        getHolder().addCallback(this);

        setFocusable(true);

    }

    public Thread getThread(){
        return thread;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        x = (int) e.getX();
        y = (int) e.getY();
        //Main Menu Touch Inputs
        if(thread.getMenu().isPlay() && !thread.getMenu().isDispose()) {
            thread.setGamePanel(new GamePanel(ctx));
            thread.getMenu().setDispose(true);
        }
        if(thread.getMenu().getExit()){
            System.exit(0);
        }
        //Game Touch Inputs
        switch(e.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                touch = true;
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this, ctx);
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
}
