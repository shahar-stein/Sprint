package com.example.shaharproject;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class showmyrating extends baseActivity
{
    private RatingBar ratingBar1, ratingBar2;

    private MobileServiceClient mclient;

    private MobileServiceTable<trainers> mtrainers;

    private MobileServiceTable<groups> mgroups;

    private MobileServiceTable<groupdeatails> mgdeatails;

    private MobileServiceTable<tblclients> mtbl;

    private trainers tr;

    private ProgressDialog prg=null;

    private ImageView imgG, imgT;

    private TextView grouprate,trainerrate;

    private void ShowMyRate(final String groupname)
    {
        AsyncTask<Void,Void,String> task = new AsyncTask<Void, Void, String>()
        {
            @Override
            protected String doInBackground(Void... voids)
            {
                try
                {
                    List<groups> mygroup= mgroups.where().field("groupcode").eq(groupname).execute().get();
                    List<groupdeatails>mytrainers=mgdeatails.where().field("groupcode").eq(groupname).execute().get();
                    List<trainers>train=mtrainers.where().field("groupid").eq(groupname).execute().get();
                    List<tblclients>cli=mtbl.where().field("usern").eq(train.get(0).getUsern()).execute().get();
                    if(mygroup.size()>0)
                    {
                        float rate=0;
                        float trainrate=0;
                        if(mytrainers.size()>0)
                        {
                            for(int j=0;j<mytrainers.size();j++)
                            {
                                rate+=Float.parseFloat(mytrainers.get(j).getGroupgrade());
                                trainrate+=Float.parseFloat(mytrainers.get(j).getTrainergrade());
                            }
                            rate=rate/mytrainers.size();
                            trainrate=trainrate/mytrainers.size();
                        }
                        return mygroup.toString()+"#"+rate+"#"+trainrate;
                    }

                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                } catch (ExecutionException e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String group)
            {
                super.onPostExecute(group);
                trainerrate=findViewById(R.id.trainerrate);
                grouprate=findViewById(R.id.grouprate);
                imgG=findViewById(R.id.imgG);
                imgT=findViewById(R.id.imgT);

                final String[]data=group.split("#");
                grouprate.setText(data[5]);
                trainerrate.setText(data[6]);
                if(Double.parseDouble(data[5])<3)
                {
                    imgG.setImageResource(R.drawable.tt);
                }
                else if(Double.parseDouble(data[5])<5)
                {
                    imgG.setImageResource(R.drawable.jj5);
                }
                else
                {
                    imgG.setImageResource(R.drawable.dabb);
                }
                if(Double.parseDouble(data[6])<3)
                {
                    imgT.setImageResource(R.drawable.tt);
                }
                else if(Double.parseDouble(data[6])<5)
                {
                    imgT.setImageResource(R.drawable.jj5);
                }
                else
                {
                    imgT.setImageResource(R.drawable.dabb);
                }
            }
        }.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showmyrating);

        try
        {
            mclient= new MobileServiceClient("https://shaharfinalproject.azurewebsites.net",showmyrating.this);

            mtrainers=mclient.getTable(trainers.class);

            mgroups=mclient.getTable(groups.class);

            mgdeatails=mclient.getTable(groupdeatails.class);

            mtbl=mclient.getTable(tblclients.class);

        }
        catch (MalformedURLException e)
        {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        ShowMyRate(StaticVariables.GetTrainer().getGroupid());
    }
}
