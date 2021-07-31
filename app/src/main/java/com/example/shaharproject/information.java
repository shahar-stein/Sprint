
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

public class information extends  baseActivity
{
    private MobileServiceClient mclient;

    private MobileServiceTable<trainers> mtrainers;

    private MobileServiceTable<groups> mgroups;

    private MobileServiceTable<groupdeatails> mgdeatails;

    private MobileServiceTable<tblclients> mtbl;

    private ProgressDialog prg;

    private TextView txtgroupname,txtprice,txtopendate,txtrunners,txtrate;

    private CircleImageView imageView;

    private Button btnclose,btncontent;

    private ListView lst;


    private void loadImage(final String picname, final CircleImageView img) {

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





    private void ShowChooseen(final String groupname)
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
                        if(mytrainers.size()>0)
                        {
                            for(int j=0;j<mytrainers.size();j++)
                            {
                                rate+=Float.parseFloat(mytrainers.get(j).getGroupgrade());
                            }
                            rate=rate/mytrainers.size();
                        }
                        return mygroup.toString()+"#"+rate+"#"+cli.get(0).getPhone();
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

                View mview = getLayoutInflater().inflate(R.layout.dialog_groupinformation,null);

                final AlertDialog.Builder a_builder=new AlertDialog.Builder(information.this);


                txtgroupname=mview.findViewById(R.id.txtgroupname);
                txtprice=mview.findViewById(R.id.txtprice);
                txtopendate=mview.findViewById(R.id.txtopendate);
                txtrunners=mview.findViewById(R.id.txtrunners);
                txtrate=mview.findViewById(R.id.txtrate);
                btncontent=mview.findViewById(R.id.btncontent);
                btnclose=mview.findViewById(R.id.btnclose);
                imageView=mview.findViewById(R.id.imageVieww);

                final String[]data=group.split("#");

                txtgroupname.setText(data[0].substring(1));
                txtprice.setText(data[1]);
                txtopendate.setText(data[2]);
                txtrunners.setText(data[3]);
                txtrate.setText(data[5]);

                loadImage(data[4].substring(0,data[4].length()-1),imageView);

                a_builder.setView(mview);


                final AlertDialog alert;
                alert=a_builder.create();
                btnclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });

                btncontent.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+data[6]));

                    if (ActivityCompat.checkSelfPermission(information.this,
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

    private void findmembers(final ListView lst)
    {
        AsyncTask<Void, Void, List<String>> task = new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                prg = new ProgressDialog(information.this);

                prg.setTitle("search");

                prg.setIcon(R.drawable.loading);

                prg.setMessage("Searching Data....");

                prg.show();

            }

            @Override
            protected List<String> doInBackground(Void... voids) {
                try {
                    List<groups> mygroups = mgroups.execute().get();

                    List<String> answer=new ArrayList<>();

                    for(int i=0; i<mygroups.size();i++)
                    {
                        String pic=(mygroups.get(i).getPic()==null ? "picsi.png" : mygroups.get(i).getPic());
                        trainers temp=(mtrainers.where().field("groupid").eq(mygroups.get(i).getGroupcode()).select("usern").execute().get()).get(0);
                        answer.add(pic+'#'+mygroups.get(i).getGroupcode()+'#'+temp.getUsern());
                    }

                    return answer;

                } catch (Exception e) {

                    final String message = e.getMessage().toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(information.this, message, Toast.LENGTH_SHORT).show();

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
                    String[] groupname = new String[mgroups.size()];
                    String[] grouppic = new String[mgroups.size()];


                    for (int i = 0; i < mgroups.size(); i++) {
                        String[] temp = mgroups.get(i).split("#");
                        grouppic[i] = temp[0];
                        groupname[i] = temp[1];
                        names[i] = temp[2];
                    }

                    customlistGroups adapter = new customlistGroups(information.
                            this, groupname, grouppic, names);


                    lst.setAdapter(adapter);
                }
            }

        }.execute();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        try {

            mclient = new MobileServiceClient("https://shaharfinalproject.azurewebsites.net",information.this);

            mtrainers=mclient.getTable(trainers.class);

            mgroups=mclient.getTable(groups.class);

            mgdeatails=mclient.getTable(groupdeatails.class);

            mtbl=mclient.getTable(tblclients.class);


        } catch (MalformedURLException e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

        lst=findViewById(R.id.lstmembers);
        findmembers(lst);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {

                TextView txtuser=view.findViewById(R.id.txtgroupname);

                ShowChooseen(txtuser.getText().toString());
            }
        });

    }
}

