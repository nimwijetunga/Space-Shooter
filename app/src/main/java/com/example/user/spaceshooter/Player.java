package com.example.user.spaceshooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by User on 2017-07-01.
 */

public class Player extends Entity{

    private int lives = 3, ammo = 5, score = 0;
    private int highScore = 0, index = 0;
    private boolean dead = false;
    private long deadTimer, fireTimer;
    private ArrayList<Laser> lasers;
    private boolean canFire = true;
    private Sound sfx;
    private HighScore hs;

    public Player(Rect rect, Level level, Context ctx){
        super(rect, level,ctx);
        sfx = new Sound(ctx);
        lasers = new ArrayList<Laser>();
        hs = new HighScore(ctx);
        sfx.playSound("game");
    }

    public boolean isDead(){
        return dead;
    }

    public int getLives(){
        return lives;
    }

    @Override
    public void draw(Canvas c) {
        //Player Image
        Drawable d = ctx.getResources().getDrawable(R.drawable.space_ship);
        d.setBounds(rect.left, rect.top, rect.right, rect.bottom);
        d.draw(c);
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
                sfx.playSound(sfx.getExplosion());
                lives--;
                obs.remove(i);
            }
        }
        level.setObs(obs);
    }

    public void restart(){
        if(lives == 0){
            dead = true;
            deadTimer = System.nanoTime();
            lives = 3;
            ammo = 5;
            String [] name = {"Bob", "Jones", "Billion", "Kred", "Lodu", "Priash"};
            if(highScore == highScore) {
                highScore = score;
                hs.addScore(name[index] , highScore);
                index++;
            }
            score = 0;
        }
    }

    public void fireLaser(){
        if(ammo > 0 && canFire){
            lasers.add(new Laser(new Rect(rect.left, rect.top + 10, rect.right, rect.bottom + 10), ctx, level));
            sfx.playSound(sfx.getLaser());
            ammo--;
            canFire = false;
            fireTimer = System.nanoTime();
        }
    }

    public void update(Point p){
        rect.set(p.x - rect.width()/2, p.y - rect.height()/2, p.x + rect.width()/2, p.y + rect.height()/2);
        restart();

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
                sfx.playSound(sfx.getExplosion());
                lasers.remove(i);
            }
        }
    }
}
