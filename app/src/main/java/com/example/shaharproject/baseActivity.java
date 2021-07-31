package com.example.shaharproject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Locale;

public class baseActivity extends AppCompatActivity
{

    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        Locale locale=Resources.getSystem().getConfiguration().locale;
        Locale.setDefault(locale);
    }

    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        Permission p= new Permission(this);
        p.verifyPermissions();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuex, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(StaticVariables.GetClient()==null)
        {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
            menu.getItem(3).setVisible(true);
            menu.getItem(4).setVisible(false);
            menu.getItem(5).setVisible(false);
            menu.getItem(6).setVisible(false);
            menu.getItem(7).setVisible(false);
            menu.getItem(8).setVisible(false);
            menu.getItem(9).setVisible(false);
            menu.getItem(10).setVisible(false);
            menu.getItem(11).setVisible(false);
            menu.getItem(12).setVisible(false);
            menu.getItem(13).setVisible(false);
            menu.getItem(14).setVisible(false);
        }
        else
        {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
            menu.getItem(4).setVisible(true);
            menu.getItem(5).setVisible(true);
            menu.getItem(6).setVisible(true);
            menu.getItem(7).setVisible(true);
            if(StaticVariables.GetClient().getRoll().equals("Trainer"))
                menu.getItem(8).setVisible(true);
            else
                menu.getItem(8).setVisible(false);
            menu.getItem(9).setVisible(true);
            if(StaticVariables.GetClient().getRoll().equals("Trainer"))
                menu.getItem(10).setVisible(true);
            else
                menu.getItem(10).setVisible(false);
            menu.getItem(11).setVisible(true);
            menu.getItem(12).setVisible(true);
            menu.getItem(13).setVisible(true);
            menu.getItem(14).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getTitleCondensed().toString())
        {
            case "Home":
                startActivity(new Intent(this,Startapp.class));
                break;
            case "Group Data":
                startActivity(new Intent(this,GroupData.class));
                break;
            case "About Us":
                startActivity(new Intent(this,information.class));
                break;
            case "Utility":
                     startActivity(new Intent(this,brodcastandnotificationcode.class));
                break;
            case "Log In":
                startActivity(new Intent(this,Login.class));
                break;
            case "Register":
                startActivity(new Intent(this,register.class));
                break;
            case "User Update":
                if(StaticVariables.GetClient().getRoll().equals("Trainer"))
                    startActivity(new Intent(this,UpdateTrainer.class));
                else
                    startActivity(new Intent(this,UpdateTrainee.class));
                finish();
                break;
            case "Log Out":
                startActivity(new Intent(this,LogOut.class));
                break;
            case "Rating":
                if(StaticVariables.GetClient().getRoll().equals("Trainer"))
                    startActivity(new Intent(this,showmyrating.class));
                else
                    startActivity(new Intent(this,rate.class));
                finish();
                break;
            case "Quit":
                startActivity(new Intent(this,quit.class));
                break;
            case "Open Race":
                startActivity(new Intent(this,race.class));
                break;
            case "Show races":
                startActivity(new Intent(this,racedeatails.class));
                break;
            case "Exercises":
                startActivity(new Intent(this, exerciselayout.class));
                break;
            case "Exercises attendancy":
                startActivity(new Intent(this,exercisesdetailslayout.class));
                break;
            case "Music":
                startActivity(new Intent(this,activityService.class));
                break;

        }
        return true;
    }
}
