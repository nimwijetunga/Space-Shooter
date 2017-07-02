package com.example.user.spaceshooter;

import android.graphics.Canvas;
import android.view.Surface;
import android.view.SurfaceHolder;

/**
 * Created by User on 2017-07-01.
 */

public class MainThread extends Thread{

    public static final int MAX_FPS = 30;
    private double avgFPS;
    private SurfaceHolder holder;
    private GamePanel panel;
    private boolean running;

    public static Canvas canvas;

    public void setRunning(boolean running){
        this.running = running;
    }

    public MainThread(SurfaceHolder holder, GamePanel panel){
        super();
        this.holder = holder;
        this.panel = panel;
    }

    @Override
    public void run(){
        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while(running){
            startTime = System.nanoTime();
            canvas = null;

            try{
              canvas = this.holder.lockCanvas();
                synchronized (holder){
                    this.panel.update(canvas.getWidth(), canvas.getHeight());
                    this.panel.draw(canvas);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(canvas != null){
                    try{
                      holder.unlockCanvasAndPost(canvas);
                    }catch(Exception e){e.printStackTrace();}
                }
            }
            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;
            try{
               if(waitTime > 0)
                   Thread.sleep(waitTime);
            }catch(Exception e){e.printStackTrace();}

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == MAX_FPS){
                avgFPS = 1000/((totalTime/frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
            }
        }
    }
}
