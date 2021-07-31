package com.example.shaharproject;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class PlayService extends Service
{
    private MediaPlayer mediaPlayer=null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }



    @Override
    public void onCreate()
    {
        super.onCreate();

        String music=StaticVariables.GetMyMusic();

        if(music.equals("Sia"))

            mediaPlayer= MediaPlayer.create(this,R.raw.sia);

        else if(music.equals("Coldplay"))

            mediaPlayer= MediaPlayer.create(this,R.raw.coldplayhymn);

        else if(music.equals("Imagine Dragons"))

            mediaPlayer= MediaPlayer.create(this,R.raw.imagine);

        else if(music.equals("Beyonce"))

            mediaPlayer= MediaPlayer.create(this,R.raw.runnin);

        else if(music.equals("One Republic"))

            mediaPlayer= MediaPlayer.create(this,R.raw.onerepublic);

        else if (music.equals("Queen"))

            mediaPlayer= MediaPlayer.create(this,R.raw.bravee);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        mediaPlayer.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        mediaPlayer.release();
    }
}
