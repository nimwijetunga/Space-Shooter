package com.example.user.spaceshooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

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

    public HighScore(Context ctx){
        this.ctx = ctx;
        scores = new ArrayList<String>();
        index = 0;
        getScores();
        buttons = new ArrayList<Button>();
        createButtons();
    }

    public void createButtons(){
        buttons.add(new Button(ctx, new Rect(50, 50, 300, 150), "Back", Color.RED, "Back"));
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

        int y = 300;

        for(int i = index; i < scores.size(); i++){
            String line = scores.get(i);
            if(line.length() != 0)
                y += 100;
            c.drawText(scores.get(i), 100, y , paint);
        }

        System.out.println("Here");
    }

    public void update(){
        for(Button i : buttons) {
            i.clicked(Surface.x, Surface.y);
            if(i.getText().equalsIgnoreCase("Back") && i.getClicked()){
                MainMenu.highScore = false;
                MainMenu.dispose = false;
            }
        }
    }
}