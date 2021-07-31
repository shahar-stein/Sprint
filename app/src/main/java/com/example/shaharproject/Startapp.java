package com.example.shaharproject;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Startapp extends baseActivity
{

    private TextView name;
    private Integer flag=0;

    private TextView txtbattery;

    private BroadcastReceiver BattInfo = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            int level=intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);

            txtbattery.setText(String.valueOf(level) + "%");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startapp);


        Permission p= new Permission(Startapp.this);
        p.verifyPermissions();

        name = findViewById(R.id.name);

        if(StaticVariables.GetClient()==null)
        {
            name.setText("Visitor");
        }
        else
        {
            name.setText(StaticVariables.GetClient().getFirstn()+" " +StaticVariables.GetClient().getLastn());

        }


        //btnstatic=findViewById(R.id.btnstatic);

        txtbattery=findViewById(R.id.txtbattery);

        registerReceiver(this.BattInfo,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


    }

    //---------------------------------------------------------------------------------

    //------------------------------------------------------------------------


    //------------------------------------------------------------------------

    //------------------------------------------------------------------------


}

