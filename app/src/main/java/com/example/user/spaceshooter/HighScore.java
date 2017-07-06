package com.example.user.spaceshooter;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by User on 2017-07-05.
 */

public class HighScore {

    public ArrayList<String> scores;
    public Context ctx;

    public HighScore(Context ctx){
        this.ctx = ctx;
        scores = new ArrayList<String>();
        getScores();
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
}