package com.example.user.spaceshooter.menus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.example.user.spaceshooter.handlers.Button;
import com.example.user.spaceshooter.main.Surface;

import java.util.ArrayList;

/**
 * Created by User on 2017-07-12.
 */

public class UserName {

    private int cw, ch;
    private ArrayList<Button> buttons;
    private Context ctx;
    private char [] userName;
    private char[] alph;
    private boolean enter;

    public UserName(int cw, int ch, Context ctx){
        this.cw = cw;
        this.ch = ch;
        this.ctx = ctx;
        userName = new char [] {'A', 'B', 'C'};
        buttons = new ArrayList<Button>();
        alph = new char[26];
        createButtons();
        genAlph();
    }

    public void genAlph(){
        int k = 0;
        for(int i = 0; i < 26; i++)
            alph[i] = (char)(65 + (k++));
    }

    public boolean isEnter() { return enter; }

    public void setEnter(boolean enter) { this.enter = enter; }

    public String getUn(){ return String.valueOf(userName); }

    public void createButtons(){
        int x = cw / 4, x2 = cw / 2, x3 = cw - 250;
        int y = ch / 2, y2 = ch / 4;
        //Down Arrows
        buttons.add(new Button(ctx, new Rect(x,y,x + 100, y + 100), "↓", Color.RED, "DL"));
        buttons.add(new Button(ctx, new Rect(x2,y,x2 + 100, y + 100), "↓", Color.RED, "DC"));
        buttons.add(new Button(ctx, new Rect(x3,y,x3 + 100, y + 100), "↓", Color.RED, "DR"));
        //Up Arrows
        buttons.add(new Button(ctx, new Rect(x,y2,x + 100, y2 + 100), "↑", Color.RED, "UL"));
        buttons.add(new Button(ctx, new Rect(x2,y2,x2 + 100, y2 + 100), "↑", Color.RED, "UC"));
        buttons.add(new Button(ctx, new Rect(x3,y2,x3 + 100, y2 + 100), "↑", Color.RED, "UR"));
        //Submit Button
        y += 250;
        x2 -= 100;
        buttons.add(new Button(ctx, new Rect(x2,y,x2 + 350, y + 100), "Enter", Color.RED, "Enter"));

    }

    public void draw(Canvas c){
        //Draw Buttons
        for(Button i : buttons){
            i.clicked(Surface.x, Surface.y);
            i.drawButton(c);
        }
        //Draw Text
        Typeface type = Typeface.createFromAsset(ctx.getAssets(),"fonts/space_font.ttf");
        Paint paint = new Paint();
        paint.setTypeface(type);
        paint.setColor(Color.YELLOW);
        paint.setTextSize(100);
        int ty = (ch / 2) - 150;
        c.drawText(String.valueOf(userName[0]), cw / 4, ty, paint);
        c.drawText(String.valueOf(userName[1]), cw / 2, ty, paint);
        c.drawText(String.valueOf(userName[2]), cw - 250, ty, paint);

        update();
    }

    public void update(){
        for(Button i : buttons){
            if(i.getClicked()){
                char c1 = userName[0], c2 = userName[1], c3 = userName[2];
                for(char c : userName)
                    System.out.println(c);
                if(i.getId().equalsIgnoreCase("DL"))
                    userName[0] = down(c1);
                if(i.getId().equalsIgnoreCase("DC"))
                    userName[1] = down(c2);
                if(i.getId().equalsIgnoreCase("DR"))
                    userName[2] = down(c3);
                if(i.getId().equalsIgnoreCase("UL"))
                    userName[0] = up(c1);
                if(i.getId().equalsIgnoreCase("UC"))
                    userName[1] = up(c2);
                if(i.getId().equalsIgnoreCase("UR"))
                    userName[2] = up(c3);
                if(i.getId().equalsIgnoreCase("Enter"))
                    enter = true;
                i.setClicked(false);
                Surface.x = 0;
                Surface.y = 0;
            }
        }
    }

    public char up(char curChar){
        if(curChar == 'A')
            return 'Z';
        int index = -1;
        for(int i = 0; i < alph.length; i++){
            if(curChar == alph[i])
                index = i;
        }
        return alph[index - 1];
    }

    public char down(char curChar){
        if(curChar == 'Z')
            return 'A';
        int index = -1;
        for(int i = 0; i < alph.length; i++){
            if(curChar == alph[i])
                index = i;
        }
        return alph[index + 1];
    }

}
