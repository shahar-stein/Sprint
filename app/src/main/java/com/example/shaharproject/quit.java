package com.example.shaharproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;

public class quit extends baseActivity {
    private Button yes,no;

    private MobileServiceClient mclient;

    private MobileServiceTable<trainee> mtable;

    private MobileServiceTable<groups> gtable;

    private MobileServiceTable<tblclients> clients;

    private MobileServiceTable<groupdeatails> gdtable;

    private ProgressDialog prg=null;

    private String storageConnection="DefaultEndpointsProtocol=http;AccountName=shaharstr;AccountKey=UHJ3WHkggYn+0PzvZuCFjSY9zQDiYVh1rWi7oF+F4EDY/a0AXoKsPfTMCeHZw/ydlAoDl3RvYYqN1AhRPC2IQA==";

    private void Del(final tblclients name,final trainee t, final groups groupCode)
    {
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg=new ProgressDialog(quit.this);

                prg.setTitle("Delete");

                prg.setIcon(R.drawable.loading);

                prg.setMessage("Inserting your Data...");

                prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                prg.show();
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                try
                {
                     groupdeatails gd = (gdtable.where().field("groupcode").eq(groupCode.getGroupcode()).and().field("usern").eq(name.getUsern()).execute().get()).get(0);

                    groups gl=(gtable.where().field("groupcode").eq(gd.getGroupcode()).execute().get()).get(0);

                    int temp=Integer.parseInt(gl.getMax())+1;

                    gl.setMax(String.valueOf(temp));

                    gl=gtable.update(gl).get();

                    mtable.delete(t);

                    java.util.Calendar c = java.util.Calendar.getInstance();
                    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
                    String datetime = dateformat.format(c.getTime());

                    gd.setEnddate(datetime);

                    gdtable.update(gd);

                    clients.delete(name);

                    int x=9;
                } catch (Exception e)
                {
                    final String message=e.getMessage().toString();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(quit.this, message, Toast.LENGTH_SHORT).show();
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

    private void DelPicture(final String pic_name)
    {
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                try
                {
                    CloudStorageAccount storageAccount=CloudStorageAccount.parse(storageConnection);

                    CloudBlobClient blobClient=storageAccount.createCloudBlobClient();

                    CloudBlobContainer container = blobClient.getContainerReference("mypics");

                    CloudBlockBlob blob=container.getBlockBlobReference(pic_name);

                     if(!pic_name.equals("picsi.png"))
                    {
                        blob.deleteIfExists();
                    }

                } catch (Exception e)
                {
                    final String message=e.getMessage();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(quit.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                return null;
            }
        }.execute();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quit);

        yes=findViewById(R.id.yes);
        no=findViewById(R.id.no);

        try
        {
            mclient= new MobileServiceClient("https://shaharfinalproject.azurewebsites.net",quit.this);

            mtable=mclient.getTable(trainee.class);

            gtable=mclient.getTable(groups.class);

            clients=mclient.getTable(tblclients.class);

            gdtable=mclient.getTable(groupdeatails.class);
        }
        catch (MalformedURLException e)
        {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

        no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(getApplicationContext(),Startapp.class));

            }
        });

        yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                DelPicture(StaticVariables.GetClient().getPic());
                Del(StaticVariables.GetClient(),StaticVariables.GetTrainee(),StaticVariables.GetGroup());
                StaticVariables.SetClient(null);
                StaticVariables.SetTrainer(null);
                StaticVariables.SetTrainee(null);
                startActivity(new Intent(getApplicationContext(),Startapp.class));

            }
        });

    }

}
