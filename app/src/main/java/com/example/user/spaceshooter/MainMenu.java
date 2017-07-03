package com.example.user.spaceshooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by User on 2017-07-03.
 */

public class MainMenu{

    private Context ctx;
    private boolean dispose, play, exit;
    private ArrayList<Button> buttons;
    Sound sfx;

    public MainMenu(Context ctx){
        this.ctx = ctx;
        dispose = false;
        exit = false;
        play = false;
        buttons = new ArrayList<Button>();
        sfx = new Sound(ctx);
        sfx.playSound("menu");
    }

    public void setSfx(Sound sfx){
       this.sfx = sfx;
    }

    public Sound getSfx(){
        return sfx;
    }

    public boolean isDispose(){
        return dispose;
    }

    public void setDispose(boolean dispose){
        this.dispose = dispose;
    }

    public boolean isPlay(){
        return play;
    }

    public boolean getExit(){
        return exit;
    }

    private void createButtons(Canvas c){
        int width = (int) (c.getWidth() / 4), height = c.getHeight() / 2;
        buttons.add(new Button(ctx, new Rect(width, height, width + 600, height + 100), "Play", Color.RED));
        height = c.getHeight() / 4;
        buttons.add(new Button(ctx, new Rect(width, height, width + 600, height + 100), "High Score", Color.RED));
        height = (int) (c.getHeight() / 1.3);
        buttons.add(new Button(ctx, new Rect(width, height, width + 600, height + 100), "Exit", Color.RED));
    }

    public void update(){
        for(Button i : buttons){
            i.clicked(Surface.x, Surface.y);
            if(i.getClicked()){
                String tmp = i.getText();
                if(tmp.equals("Play"))
                    play = true;
                if(tmp.equals("Exit"))
                    exit = true;
            }
        }
    }

    public void draw(Canvas c){
        createButtons(c);

        Drawable d = ctx.getResources().getDrawable(R.drawable.menu_background);
        d.setBounds(0, 0, c.getWidth(), c.getHeight());
        d.draw(c);

        Typeface type = Typeface.createFromAsset(ctx.getAssets(),"fonts/space_font.ttf");
        Paint paint = new Paint();
        paint.setTypeface(type);
        paint.setColor(Color.YELLOW);
        paint.setTextSize(70);

        c.drawText("Space Shooter", c.getWidth() / 4, 200, paint);

        for(Button i : buttons)
            i.drawButton(c);
    }


}
