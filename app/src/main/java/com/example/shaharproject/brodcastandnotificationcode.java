package com.example.shaharproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class brodcastandnotificationcode extends baseActivity
{


    private MediaPlayer mediaPlayer=null;
    //------------------------------------------------------------------------
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if(mediaPlayer !=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
    //------------------------------------------------------------------------
    @Override
    protected void onPause()
    {
        super.onPause();

        unregisterReceiver(mybroadcastPLAY);
        unregisterReceiver(mybroadcastPAUSE);
    }
    //------------------------------------------------------------------------
    @Override
    protected void onResume()
    {
        super.onResume();

        registerReceiver(mybroadcastPLAY,new IntentFilter(NotificationHelp.ACTION_PLAY));

        registerReceiver(mybroadcastPAUSE,new IntentFilter(NotificationHelp.ACTION_PAUSE));
    }
    //------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brodcastandnotification);

        mediaPlayer= MediaPlayer.create(this,R.raw.bravee);

        //btnstatic=findViewById(R.id.btnstatic);

        /*btnstatic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                int i = 2;

                Intent intent = new Intent(brodcastandnotificationcode.this, brodcastandnotificationcode.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(brodcastandnotificationcode.this, 666, intent, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                        + (i * 1000), pendingIntent);

                Toast.makeText(brodcastandnotificationcode.this, "Alarm set in " + i + " seconds", Toast.LENGTH_LONG).show();
            }
        });*/
    }

    //------------------------------------------------------------------------
    private BroadcastReceiver mybroadcastPLAY= new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Toast.makeText(context, "Using Play", Toast.LENGTH_SHORT).show();

            mediaPlayer.start();

            NotificationHelp.Cancel(brodcastandnotificationcode.this,3);
        }
    };
    //------------------------------------------------------------------------
    private BroadcastReceiver mybroadcastPAUSE= new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Toast.makeText(context, "Using Stop", Toast.LENGTH_SHORT).show();

            if(mediaPlayer !=null)
            {
                mediaPlayer.pause();
            }

            NotificationHelp.Cancel(brodcastandnotificationcode.this,3);
        }
    };
    //------------------------------------------------------------------------
    public void Simple_Not(View v)
    {
        int id=1;

        String title="New Message!!!!";

        String message="Time to Die!!!";

        Intent intent= new Intent(this,Startapp.class);

        intent.putExtra("msg","It is my time");

        NotificationHelp.CreateNotification(this,intent,title,message,id);
    }
    //------------------------------------------------------------------------
    public void HeadUp_Not(View v)
    {
        int id=2;

        String title="New Message!!!!";

        String message="Time to Die!!!";

        Intent intent= new Intent(this,Startapp.class);

        intent.putExtra("msg","It is my time");

        NotificationHelp.CreateHeadUpNotification(this,intent,title,message,id);
    }
    //------------------------------------------------------------------------
    public void Action_Not(View v)
    { int id=3;

        Intent intent= new Intent(this,Startapp.class);

        intent.putExtra("msg","Nice Music");

        String contentitle="Music Name";

        String contentext="Artist nAme";

        NotificationHelp.CreateAction(this,intent,contentitle,contentext,id);
    }
}
