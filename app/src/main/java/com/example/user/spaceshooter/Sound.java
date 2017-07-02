package com.example.user.spaceshooter;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by User on 2017-07-02.
 */

public class Sound {

    private static SoundPool sp;
    private static int laser, explosion;

    public Sound(Context ctx){
        sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        laser = sp.load(ctx, R.raw.laser, 1);
        explosion = sp.load(ctx, R.raw.explosion, 1);
    }

    public void laserSound(){
        sp.play(laser, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void expSound(){
        sp.play(explosion, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
