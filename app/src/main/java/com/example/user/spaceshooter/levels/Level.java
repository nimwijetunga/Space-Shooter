package com.example.user.spaceshooter.levels;

import android.content.Context;
import android.graphics.Rect;

import com.example.user.spaceshooter.entities.Obstacle;
import com.example.user.spaceshooter.items.Item;
import com.example.user.spaceshooter.items.ItemType;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by User on 2017-07-01.
 */

public class Level {
    private ArrayList<Obstacle> obs;
    private ArrayList<Item> items;
    private Context ctx;
    public Level(Context ctx, int width, int numObs, int height, int numItems){
        obs = new ArrayList<Obstacle>();
        items = new ArrayList<Item>();
        this.ctx = ctx;
        createMap(width, numObs, height);
        genItems(width, height, numItems);
    }

    public ArrayList<Item> getItems(){
        return items;
    }

    public ArrayList<Obstacle> getObs(){
        return obs;
    }

    public void setObs(ArrayList<Obstacle> obs){
        this.obs = obs;
    }

    public void setItems(ArrayList<Item> items){
        this.items = items;
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

    public void genItems(int width, int height, int numItems){
        Random rand = new Random();
        System.out.println(numItems);
        for(int i = 0; i < numItems; i++){
            boolean contains = true;
            int randX = 0, randY = 0;
            do{
               randX = rand.nextInt(width - 50) + 300;
               randY = rand.nextInt(height - 50) + 300;
               for(Obstacle j : obs){
                   if(j.getRect().left != randX && j.getRect().top != randY)
                       contains = false;
               }
            }while(contains);
            ItemType itemType;
            boolean randType = rand.nextBoolean();
            if(randType)
                itemType = ItemType.AMMO;
            else
                itemType = ItemType.LIVES;
            System.out.println(itemType);
            items.add(new Item(itemType, new Rect(randX, randY, randX + 100, randY + 100), ctx));
        }
    }
}
