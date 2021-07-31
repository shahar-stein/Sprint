package com.example.shaharproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;

public class rate extends baseActivity
{
    private RatingBar ratingBar1, ratingBar2;

    private Button submit;

    private MobileServiceTable<trainers> ttable;

    private MobileServiceTable<groupdeatails> gdttable;

    private MobileServiceClient mclient;

    private trainers tr;

    private ProgressDialog prg=null;


    private TextView groupname,trainername;


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void getData()
    {
        AsyncTask<Void,Void,String> task = new AsyncTask<Void, Void, String>()
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg=new ProgressDialog(rate.this);

                prg.setTitle("rating");

                prg.setIcon(R.drawable.loading);

                prg.setMessage("Inserting your Data...");

                prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                prg.show();
            }

            @Override
            protected String doInBackground(Void... voids)
            {
                try
                {
                    trainers c = ttable.where().field("groupid").eq(StaticVariables.GetGroup().getGroupcode()).execute().get().get(0);

                    return(c.getUsern());

                } catch (Exception e)
                {
                    final String message=e.getMessage().toString();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(rate.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return null;
            }

            @Override
            protected void onPostExecute(String name)
            {
                super.onPostExecute(name);

                prg.dismiss();

                trainername.setText(name);

            }
        }.execute();

    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void rates(final String trainergrade, final String groupgrade)
    {
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg=new ProgressDialog(rate.this);

                prg.setTitle("rating");

                prg.setMessage("Inserting your Data...");

                prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                prg.show();
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                try
                {

                     groupdeatails gd = gdttable.where().field("groupcode").eq(StaticVariables.GetGroup().getGroupcode()).and().field("usern").eq(StaticVariables.GetClient().getUsern()).execute().get().get(0);

                    gd.setGroupgrade(groupgrade);

                    gd.setTrainergrade(trainergrade);

                    gdttable.update(gd);

                    int x=9;
                } catch (Exception e)
                {
                    final String message=e.getMessage().toString();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(rate.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);

                prg.dismiss();

            }
        }.execute();

    }




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        ratingBar1 =  findViewById(R.id.ratingBar1);
        ratingBar2 =  findViewById(R.id.ratingBar2);
        submit=findViewById(R.id.submit);

        groupname=findViewById(R.id.groupname);
        trainername=findViewById(R.id.trainername);
        try
        {
            mclient= new MobileServiceClient("https://shaharfinalproject.azurewebsites.net",rate.this);

            ttable=mclient.getTable(trainers.class);

            gdttable=mclient.getTable(groupdeatails.class);
        }
        catch (MalformedURLException e)
        {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }



        groupname.setText(StaticVariables.GetGroup().getGroupcode());
        getData();



        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rates(String.valueOf(ratingBar1.getRating()),String.valueOf(ratingBar2.getRating()));
                startActivity(new Intent(getApplicationContext(), Startapp.class));
            }
        });


    }
}
