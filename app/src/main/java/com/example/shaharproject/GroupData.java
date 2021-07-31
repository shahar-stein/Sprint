package com.example.shaharproject;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import de.hdodenhof.circleimageview.CircleImageView;


public class GroupData extends baseActivity
{

    private MobileServiceClient mclient;

    private MobileServiceTable<trainers> mtrainers;

    private MobileServiceTable<groups> mgroups;

    private MobileServiceTable<groupdeatails> mgdeatails;

    private MobileServiceTable<tblclients> mtbl;

    private ProgressDialog prg;

    private TextView txtgroupname, txttrainer,txtgrouprate, name,gender,phone,email;

    private CircleImageView logo, trainerpic, traineepic;

    private ListView lst;

    private Button phoneT,EmailT,exit;


    private void loadImage(final String picname, final CircleImageView img)
    {
        AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {

                Bitmap my_bitmap = null;

                String picpathname = "https://shaharstr.blob.core.windows.net/mypics/" + picname;

                InputStream in = null;
                try {
                    in = new java.net.URL(picpathname).openStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                my_bitmap = BitmapFactory.decodeStream(in);

                return my_bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);

                img.setImageBitmap(bitmap);
            }
        }.execute();
    }



    private void ShowChooseen(final String username)
    {
        AsyncTask<Void,Void,String> task = new AsyncTask<Void, Void, String>()
        {
            @Override
            protected String doInBackground(Void... voids)
            {
                try
                {
                    tblclients cli=(mtbl.where().field("usern").eq(username).execute().get()).get(0);
                    return cli.toString();


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
            protected void onPostExecute(String trainee)
            {
                super.onPostExecute(trainee);

                View mview = getLayoutInflater().inflate(R.layout.dialog_client,null);

                final AlertDialog.Builder a_builder=new AlertDialog.Builder(GroupData.this);


                name=mview.findViewById(R.id.name);
                gender=mview.findViewById(R.id.gender);
                phone=mview.findViewById(R.id.phone);
                email=mview.findViewById(R.id.email);
                traineepic=mview.findViewById(R.id.traineeimg);
                EmailT=mview.findViewById(R.id.bemail);
                phoneT=mview.findViewById(R.id.bphone);
                exit=mview.findViewById(R.id.exit);

                final String[]data=trainee.split("#");

                name.setText(data[0]+ " " + data[1]);
                gender.setText(data[2]);
                phone.setText(data[3]);
                email.setText(data[4]);
                loadImage(data[5],traineepic);

                a_builder.setView(mview);


                final AlertDialog alert;
                alert=a_builder.create();
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });

                EmailT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Toast.makeText(GroupData.this, "Send email", Toast.LENGTH_SHORT).show();

                        String[] TO = {email.getText().toString()};
                        String[] CC = {"shaharshtein@gmail.com"};
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");


                        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                        emailIntent.putExtra(Intent.EXTRA_CC, CC);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hi Yossi!!!!");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

                        try
                        {
                            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                            //finish();
                        } catch (ActivityNotFoundException ex)
                        {
                            Toast.makeText(GroupData.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                        }



                        /*Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, email.getText().toString());
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

                        try {
                            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                            finish();
                        } catch (android.content.ActivityNotFoundException ex) {
                        }*/
                    }
                });

                phoneT.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+data[3]));

                        if (ActivityCompat.checkSelfPermission(GroupData.this,
                                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(callIntent);
                    }
                });

                alert.show();
            }
        }.execute();


    }

    private void findmembers(final ListView lst, final String groupname,final CircleImageView img)
    {
        AsyncTask<Void, Void, List<String>> task = new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                prg = new ProgressDialog(GroupData.this);

                prg.setTitle("search");

                prg.setIcon(R.drawable.loading);

                prg.setMessage("Searching Data....");

                prg.show();

            }

            @Override
            protected List<String> doInBackground(Void... voids) {
                try {
                    final trainers t=(mtrainers.where().field("groupid").eq(groupname).execute().get()).get(0);
                    final groupdeatails g=(mgdeatails.where().field("groupcode").eq(groupname).execute().get()).get(0);
                    tblclients c=(mtbl.where().field("usern").eq(t.getUsern()).execute().get()).get(0);
                    loadImage(c.getPic(),img);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txttrainer.setText(t.getUsern());
                            txtgrouprate.setText(g.getGroupgrade());
                        }
                    });

                    List<groupdeatails> mycolegs = mgdeatails.where().field("groupcode").eq(groupname).and().field("enddate").eq("").execute().get();

                    List<String> answer=new ArrayList<>();

                    for(int i=0; i<mycolegs.size();i++)
                    {
                      tblclients temp=(mtbl.where().field("usern").eq(mycolegs.get(i).getUsern()).execute().get()).get(0);


                        String pic=(temp.getPic()==null ? "picsi.png" : temp.getPic());
                        answer.add(pic+'#'+temp.getUsern());
                    }

                    return answer;

                } catch (Exception e) {

                    final String message = e.getMessage().toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupData.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                return null;
            }

            protected void onPostExecute(List<String> mgroups) {
                super.onPostExecute(mgroups);

                prg.dismiss();

                if(mgroups!=null) {
                    String[] names = new String[mgroups.size()];
                    String[] grouppic = new String[mgroups.size()];


                    for (int i = 0; i < mgroups.size(); i++) {
                        String[] temp = mgroups.get(i).split("#");
                        grouppic[i] = temp[0];
                        names[i] = temp[1];
                    }

                    customGDlist adapter = new customGDlist(GroupData.
                            this,  names, grouppic);


                    lst.setAdapter(adapter);
                }
            }

        }.execute();
    }





    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_data);

        txtgroupname=findViewById(R.id.groupname);
        txtgrouprate=findViewById(R.id.grouprate);
        txttrainer=findViewById(R.id.trainername);
        lst=findViewById(R.id.list);
        logo=findViewById(R.id.logo);
        trainerpic=findViewById(R.id.trainerpic);


        try {

            mclient = new MobileServiceClient("https://shaharfinalproject.azurewebsites.net",GroupData.this);

            mtrainers=mclient.getTable(trainers.class);

            mgroups=mclient.getTable(groups.class);

            mgdeatails=mclient.getTable(groupdeatails.class);

            mtbl=mclient.getTable(tblclients.class);


        } catch (MalformedURLException e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

        txtgroupname.setText(StaticVariables.GetGroup().getGroupcode());
        loadImage(StaticVariables.GetGroup().getPic(),logo);


        findmembers(lst, StaticVariables.GetGroup().getGroupcode(),trainerpic);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {

                TextView txtuser=view.findViewById(R.id.txtname);

                ShowChooseen(txtuser.getText().toString());
            }
        });

        trainerpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowChooseen(txttrainer.getText().toString());
            }
        });
    }

}
