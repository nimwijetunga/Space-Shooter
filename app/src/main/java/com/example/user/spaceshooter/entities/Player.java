package com.example.user.spaceshooter.entities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.example.user.spaceshooter.handlers.Button;
import com.example.user.spaceshooter.items.Item;
import com.example.user.spaceshooter.items.ItemType;
import com.example.user.spaceshooter.items.Laser;
import com.example.user.spaceshooter.levels.Level;
import com.example.user.spaceshooter.R;
import com.example.user.spaceshooter.menus.MainMenu;
import com.example.user.spaceshooter.main.MainThread;
import com.example.user.spaceshooter.main.Surface;

import java.util.ArrayList;

/**
 * Created by User on 2017-07-01.
 */

public class Player extends Entity {

    private int lives = 3, ammo = 5, score = 0;
    private int highScore = 0;
    private boolean dead = false, un_menu = false;
    private long deadTimer, fireTimer;
    private ArrayList<Laser> lasers;
    private boolean canFire = true;
    private Button menu;
    private int cw, ch;

    public Player(Rect rect, Level level, Context ctx, int cw, int ch){
        super(rect, level,ctx);
        this.cw = cw;
        this.ch = ch;
        lasers = new ArrayList<Laser>();
        MainThread.sfx.playSound("game");
        menu = new Button(ctx, new Rect(cw / 4, 125, cw / 4 + 600, 225), "Main Menu", Color.RED, "Main Menu");
    }

    public boolean isDead(){
        return dead;
    }

    public int getLives(){
        return lives;
    }

    public boolean getUnMenu(){ return un_menu; }

    public void setUnMenu(boolean un_menu){ this.un_menu = un_menu; }

    @Override
    public void draw(Canvas c) {
        //Player Image
        Drawable d = ctx.getResources().getDrawable(R.drawable.space_ship);
        d.setBounds(rect.left, rect.top, rect.right, rect.bottom);
        d.draw(c);

        //Buttons
        menu.drawButton(c);
    }

    public ArrayList<Laser> getLasers(){
        return lasers;
    }

    public int getAmmo(){
        return ammo;
    }

    public void setAmmo(int ammo){
        this.ammo = ammo;
    }

    public int getScore(){return score;}

    public void hitObs(){
        ArrayList<Obstacle> obs = level.getObs();
        for(int i = 0; i < obs.size(); i++){
            if(this.collidesWith(obs.get(i))){
                MainThread.sfx.playSound(MainThread.sfx.getExplosion());
                lives--;
                restart();
                obs.remove(i);
            }
        }
        level.setObs(obs);
    }

    public void addHighScore(String userName){
        MainMenu.getHighScore().addScore(userName , highScore);
    }

    public void getItem(){
        ArrayList<Item> items = level.getItems();
        for(int i = 0; i < items.size(); i++){
            int amount = items.get(i).incrStats(this);
            if(amount != -1) {
                if (items.get(i).getType() == ItemType.AMMO)
                    ammo = amount;
                else if (items.get(i).getType() == ItemType.LIVES)
                    lives = amount;
                items.remove(i);
            }
        }
        level.setItems(items);
    }

    public void restart(){
        if(lives == 0){
            dead = true;
            deadTimer = System.nanoTime();
            lasers.clear();
            lives = 3;
            ammo = 5;
            if(score > highScore) {
                highScore = score;
                un_menu = true;
            }
            score = 0;
        }
    }

    public void fireLaser(){
        if(ammo > 0 && canFire){
            lasers.add(new Laser(new Rect(rect.left, rect.top + 10, rect.right, rect.bottom + 10), ctx, level));
            MainThread.sfx.playSound(MainThread.sfx.getLaser());
            ammo--;
            canFire = false;
            fireTimer = System.nanoTime();
        }
    }

    public void update(Point p){
        rect.set(p.x - rect.width()/2, p.y - rect.height()/2, p.x + rect.width()/2, p.y + rect.height()/2);

        //Respawn Timer
        long elapsed = (System.nanoTime() - deadTimer) / 1000000;
        if(elapsed > 1500)
            dead = false;

        //Fire Timer
        elapsed = (System.nanoTime() - fireTimer) / 1000000;
        if(elapsed > 500)
            canFire = true;

        //Remove Laser
        for(int i = 0; i < lasers.size(); i++){
            if(lasers.get(i).shouldRemove()) {
                score += 3;
                MainThread.sfx.playSound(MainThread.sfx.getExplosion());
                lasers.remove(i);
            }
        }

        //Button
        menu.clicked(Surface.x, Surface.y);
        if(menu.getClicked()){
            MainMenu.play = false;
            MainMenu.dispose = false;
            MainThread.sfx.playSound("Menu");
        }
    }
}
