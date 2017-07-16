package com.example.user.spaceshooter.items;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.example.user.spaceshooter.R;
import com.example.user.spaceshooter.entities.Player;

import java.util.Random;

/**
 * Created by User on 2017-07-09.
 */

public class Item {

    private Context ctx;
    private ItemType type;
    private Rect rect;
    private int amount;

    public Item(ItemType type, Rect rect, Context ctx) {
        this.ctx = ctx;
        this.type = type;
        this.rect = rect;
        genIncr();
    }

    public ItemType getType(){
        return type;
    }

    public void genIncr() {
        Random rand = new Random();
        switch (type) {
            case AMMO:
                amount = rand.nextInt(5) + 1;
                break;
            case LIVES:
                amount = rand.nextInt(3) + 1;
                break;
        }
    }

    public int incrStats(Player player) {
        int ammo = player.getAmmo(), lives = player.getLives();
        int tmpAmount = 0;
        if (Rect.intersects(rect, player.getRect())) {
            if (type == ItemType.AMMO) {
                if (ammo + amount < 5)
                   tmpAmount = ammo + amount;
                else
                    tmpAmount = 5;
            } else if (type == ItemType.LIVES) {
                if (lives + amount < 3)
                    tmpAmount = lives + amount;
                else
                   tmpAmount = 3;
            }
            return tmpAmount;
        }
        return -1;
    }

    public void drawItem(Canvas c) {
        Drawable d = ctx.getResources().getDrawable(R.drawable.powerup);
        d.setBounds(rect.left, rect.top, rect.right, rect.bottom);
        d.draw(c);
    }

}