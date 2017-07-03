package com.example.user.spaceshooter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * Created by User on 2017-07-02.
 */

public class Sound {

    private static SoundPool sp;
    private static int laser, explosion, menu_music, game_music;
    private Context ctx;
    public static MediaPlayer mp;

    public Sound(Context ctx){
        sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        this.ctx = ctx;

        laser = sp.load(ctx, R.raw.laser, 1);
        explosion = sp.load(ctx, R.raw.explosion, 1);
    }

    public int getLaser(){
        return laser;
    }

    public int getExplosion(){
        return explosion;
    }

    public int getMenuMusic(){
        return menu_music;
    }

    public int getGameMusic(){
        return game_music;
    }

    public void playSound(String type) {
        if(mp != null) {
            Sound.mp.stop();
            Sound.mp.reset();
            Sound.mp.release();
        }
        if (type.equalsIgnoreCase("Menu"))
            mp = MediaPlayer.create(ctx, R.raw.menu_music);
        if(type.equalsIgnoreCase("Game"))
            mp = MediaPlayer.create(ctx, R.raw.game_music);

        mp.start();
        mp.setLooping(true);
    }

    public void playSound(int sound){
        sp.play(sound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void stopSound(int sound){
        sp.stop(sound);
    }
}
