package com.example.user.spaceshooter.menus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.example.user.spaceshooter.handlers.Button;
import com.example.user.spaceshooter.R;
import com.example.user.spaceshooter.main.Surface;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by User on 2017-07-05.
 */

public class HighScore {

    private ArrayList<String> scores;
    private Context ctx;
    private int index;
    private ArrayList<Button> buttons;
    private int width, height;
    private boolean canScroll = false;
    private int max_display;

    public HighScore(Context ctx){
        this.ctx = ctx;
        scores = new ArrayList<String>();
        index = 0;
        width = Surface.getScreenWidth();
        height = Surface.getScreenHeight();
        getScores();
        buttons = new ArrayList<Button>();
        createButtons();
        max_display = setMax_display();
    }

    public int setMax_display(){
        int disp = 1, start = 350;
        while(height - 200 > start){
            start += 100;
            disp++;
        }
        return disp;
    }

    public void createButtons(){
        buttons.add(new Button(ctx, new Rect(50, 50, 300, 150), "Back", Color.RED, "Back"));
        buttons.add(new Button(ctx, new Rect(50, 250, 150, 350), "↓", Color.RED, "Down"));
        buttons.add(new Button(ctx, new Rect(200, 250, 300, 350), "↑", Color.RED, "Up"));
    }

    public void addScore(String name, int score){
        scores.add(name + " " + Integer.toString(score));
        saveScores();
    }

    public void printScores(){
        for(String i : scores)
            System.out.println(i);
    }

    public void getScores(){
        try{
            FileInputStream fis = ctx.openFileInput("Score.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line = reader.readLine();

            while(line != null){
                scores.add(line);
                line = reader.readLine();
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public void saveScores(){
        try{
            FileOutputStream fos = ctx.openFileOutput("Score.txt",Context.MODE_PRIVATE);
            Writer writer = new OutputStreamWriter(fos);
            for(String s : scores){
                writer.write(s);
                writer.write("\n\r");
            }
            printScores();
            writer.flush();
            writer.close();
        }catch(Exception e){e.printStackTrace();}
    }

    public void drawScore(Canvas c){
        //Background Image
        Drawable d = ctx.getResources().getDrawable(R.drawable.space);
        d.setBounds(0, 0, c.getWidth(), c.getHeight());
        d.draw(c);

        //Draw Buttons
        for(Button i : buttons)
            i.drawButton(c);

        //Set Font
        Typeface type = Typeface.createFromAsset(ctx.getAssets(),"fonts/space_font.ttf");
        Paint paint = new Paint();
        paint.setTypeface(type);
        paint.setColor(Color.YELLOW);
        paint.setTextSize(70);

        //Remove 0 length scores
        for(int i = 0; i < scores.size(); i++){
            if(scores.get(i).length() == 0)
                scores.remove(i);
        }

        int y = 350;
        int ti = 1;

        for(int i = index; i < scores.size(); i++){
            String line = scores.get(i);
            y += 100;
            c.drawText(scores.get(i), 100, y , paint);
            if(ti == max_display)
                break;
            ti++;
        }
    }

    public void update(){
        for(Button i : buttons) {
            i.clicked(Surface.x, Surface.y);
            if(i.getClicked()) {
                if (i.getText().equalsIgnoreCase("Back")) {
                    MainMenu.highScore = false;
                    MainMenu.dispose = false;
                }
                if (i.getId().equalsIgnoreCase("Down"))
                    moveDown();
                if (i.getId().equalsIgnoreCase("Up"))
                    moveUp();
                Surface.x = -1;
                Surface. y = -1;
                i.setClicked(false);
            }
        }
    }

    public void moveDown(){
        if(scores.size() - index <= max_display)
            return;
        index += max_display;
    }

    public void moveUp(){
       if(index >= max_display)
           index -= max_display;
    }
}