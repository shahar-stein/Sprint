package com.example.shaharproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity
{

    private ProgressDialog prg;
    private ProgressBar progressBar;
    private MediaPlayer play;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prg=new ProgressDialog(MainActivity.this);
        progressBar=findViewById(R.id.progressBar);

        play= MediaPlayer.create(MainActivity.this, R.raw.jellyfishjam);

        prg.setIcon(R.drawable.loading);
        prg.setTitle("ברוכים הבאים");
        prg.setMessage("נטען לדף הבא");

        play.start();

       // prg.show();
        new CountDownTimer(5600, 1000) {

            public void onTick(long millisUntilFinished) {
                progressBar.setProgress( progressBar.getProgress()+20);
            }

            public void onFinish() {
            }
        }.start();
        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {

                startActivity(new Intent(MainActivity.this,Startapp.class));

                prg.dismiss();
                play.stop();

               finish();
            }
        },5600);
    }
}
