package com.example.user.spaceshooter.main;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.example.user.spaceshooter.handlers.Sound;
import com.example.user.spaceshooter.menus.MainMenu;

/**
 * Created by User on 2017-07-01.
 */

public class MainThread extends Thread{

    public static final int MAX_FPS = 30;
    private double avgFPS;
    private SurfaceHolder holder;
    private GamePanel panel;
    private boolean running;
    private MainMenu menu;
    private Context ctx;
    private Surface surface;
    public static Sound sfx;

    public static Canvas canvas;

    public void setRunning(boolean running){
        this.running = running;
    }

    public MainThread(SurfaceHolder holder, Surface surface, Context ctx){
        super();
        this.holder = holder;
        this.surface = surface;
        this.ctx = ctx;
        sfx = new Sound(ctx);
        menu = new MainMenu(ctx);
    }

    public void setGamePanel(GamePanel panel){
        this.panel = panel;
    }

    public MainMenu getMenu(){
        return menu;
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
                    menu.getHighScore().setHeight(canvas.getHeight());
                    menu.getHighScore().setWidth(canvas.getWidth());
                    updateMenu(ctx);
                    if(menu.isPlay()) {
                        panel.update(canvas.getWidth(), canvas.getHeight());
                        panel.draw(canvas);
                    }
                    if(menu.isHighScore()){
                        menu.getHighScore().drawScore(canvas);
                        menu.getHighScore().update();
                    }
                    if(!menu.isDispose()){
                        menu.draw(canvas);
                        menu.update();
                    }
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

    public void updateMenu(Context ctx){
        //Main Menu Touch Inputs
        if(menu.isPlay() && !menu.isDispose()) {//Play
            setGamePanel(new GamePanel(ctx, canvas.getWidth(), canvas.getHeight()));
            menu.dispose = true;
        }
        if(menu.isHighScore())//High Score Menu
            menu.dispose = true;
        if(menu.getExit())//Exit
            System.exit(0);
    }
}
