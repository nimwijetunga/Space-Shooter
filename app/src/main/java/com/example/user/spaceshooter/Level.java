package com.example.user.spaceshooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by User on 2017-07-01.
 */

public class Level {
    private ArrayList<Obstacle> obs;
    private int level;
    private Context ctx;
    private int maxLevel;

    public Level(Context ctx, int width, int numObs, int height){
        maxLevel = 2;
        obs = new ArrayList<Obstacle>();
        this.ctx = ctx;
        createMap(width, numObs, height);
    }

    public int getMaxLevel(){
        return maxLevel;
    }

    public ArrayList<Obstacle> getObs(){
        return obs;
    }

    public void setObs(ArrayList<Obstacle> obs){
        this.obs = obs;
    }

    public void createMap(int width, int numObs, int height){
       Random random = new Random();
       int prevIntX = 0, prevIntY = 0,  randIntX = 0, randIntY = 0;
       for(int i = 0; i < numObs; i++){
           do{
              randIntX = random.nextInt(width - 50) + 300;
           }while(randIntX == (prevIntX + 100));
           do{
             randIntY   = random.nextInt(height - 50) + 300;
           }while(randIntY == (prevIntY + 200));
           obs.add(new Obstacle(new Rect(randIntX, randIntY, randIntX + 100,randIntY + 100),this,ctx));
           prevIntX = randIntX;
           prevIntY = randIntY;
       }
    }
}
