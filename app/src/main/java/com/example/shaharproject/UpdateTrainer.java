package com.example.shaharproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateTrainer extends baseActivity
{
    private EditText lastn,price,phone,email;

    private Button update,btnchoose;

    private final static int CODE=666;

    private  String pic_name="picsi.png",pic_name_path="https://shaharstr.blob.core.windows.net/mypics/picsi.png", old_pic_name;

    private tblclients cl=new tblclients();

    private trainers tr = new trainers();

    private groups gr = new groups();

    private String pref="";

    private CircleImageView img;



    //---------------------------------

    private MobileServiceClient mclient;

    private MobileServiceTable<tblclients> mclients;

    private MobileServiceTable<trainers> mtrainer;

    private MobileServiceTable<groups> mgroups;

    private ProgressDialog prg=null;

    private String storageConnection="DefaultEndpointsProtocol=http;AccountName=shaharstr;AccountKey=UHJ3WHkggYn+0PzvZuCFjSY9zQDiYVh1rWi7oF+F4EDY/a0AXoKsPfTMCeHZw/ydlAoDl3RvYYqN1AhRPC2IQA==";

    private void loadImage()
    {

        AsyncTask<Void,Void,Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {

                Bitmap my_bitmap = null;

                String picpathname="https://shaharstr.blob.core.windows.net/mypics/" + StaticVariables.GetClient().getPic();

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
            protected void onPostExecute(Bitmap bitmap)
            {
                super.onPostExecute(bitmap);

                img.setImageBitmap(bitmap);
            }
        }.execute();



      /*  old_pic_name=img_path;
        Picasso.with(UpdateTrainer.this).load("https://shaharstr.blob.core.windows.net/mypics/" + img_path).placeholder(R.mipmap.ic_launcher).into(img, new Callback()
        {
            @Override
            public void onSuccess() {
                Toast.makeText(UpdateTrainer.this, "Good", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(UpdateTrainer.this, "Bad", Toast.LENGTH_SHORT).show();

            }
        });*/
    }

    private void GetData()
    {
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>()
        {
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg= new ProgressDialog(UpdateTrainer.this);

                prg.setTitle("UpdateTrainer");

                prg.setIcon(R.drawable.loading);

                prg.setMessage("Searching for options...");

                prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                prg.show();

            }


            @Override
            protected Void doInBackground(Void... voids)
            {
                try {

                    List<trainers> mytr = mtrainer.where().field("usern").eq(StaticVariables.GetClient().getUsern()).execute().get();

                    if (mytr.size() != 0) {
                        StaticVariables.SetTrainer(mytr.get(0));

                    }

                    List<groups> myg = mgroups.where().field("groupcode").eq(mytr.get(0).getGroupid()).execute().get();

                    if (myg.size() != 0) {
                        StaticVariables.SetGroup(myg.get(0));

                    }
                }catch (Exception e) {
                    final String message = e.getMessage().toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UpdateTrainer.this, message, Toast.LENGTH_SHORT).show();

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
                lastn.setText(StaticVariables.GetClient().getLastn());
                email.setText(StaticVariables.GetClient().getEmail());
                phone.setText(StaticVariables.GetClient().getPhone());
                price.setText(StaticVariables.GetGroup().getPrice());
            }
        }.execute();
    }


    private void UpdateMe(final tblclients newcl, final groups newgr)
    {
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg= new ProgressDialog(UpdateTrainer.this);

                prg.setTitle("Update");

                prg.setIcon(R.drawable.loading);

                prg.setMessage("Updating Data....");

                prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                prg.show();

            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                try
                {
                    tblclients c = mclients.update(newcl).get();

                    StaticVariables.SetClient(c);
                    groups g = mgroups.update(newgr).get();
                    int x=9;
                } catch (Exception e)
                {
                    final String message=e.getMessage().toString();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(UpdateTrainer.this, message, Toast.LENGTH_SHORT).show();
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

    private void AddPicture()
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

                    File source = new File(pic_name_path);

                    if(source.exists())
                    {
                        blob.upload(new FileInputStream(source),source.length());
                        if(!old_pic_name.equals("picsi.png"))
                        {
                            blob=container.getBlockBlobReference(old_pic_name);
                            blob.deleteIfExists();
                        }
                    }


                } catch (Exception e)
                {
                    final String message=e.getMessage();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UpdateTrainer.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                return null;
            }
        }.execute();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CODE)
        {
            Uri myImage= data.getData();

            String[] filePathColumn={MediaStore.Images.Media.DATA};

            Cursor cursor=getContentResolver().query(myImage,filePathColumn,null,null,null);

            cursor.moveToFirst();

            int columnIndex=cursor.getColumnIndex(filePathColumn[0]);

            pic_name_path=cursor.getString(columnIndex);

            pic_name=pic_name_path.substring(pic_name_path.lastIndexOf("/") + 1);

            img.setImageBitmap(BitmapFactory.decodeFile(pic_name_path));
            img.setMaxWidth(30);
            img.setMaxHeight(30);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_trainer);

        update=findViewById(R.id.update);

        btnchoose=findViewById(R.id.btnchoose);

        lastn=findViewById(R.id.lastn);

        price=findViewById(R.id.price);

        email=findViewById(R.id.email);

        phone=findViewById(R.id.phone);

        img=findViewById(R.id.img);

        loadImage();

        try
        {
            mclient= new MobileServiceClient("https://shaharfinalproject.azurewebsites.net",UpdateTrainer.this);

            mclients=mclient.getTable(tblclients.class);

            mtrainer=mclient.getTable(trainers.class);

            mgroups=mclient.getTable(groups.class);
        }
        catch (MalformedURLException e)
        {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }


        GetData();

        //-------------------------------------------------
        btnchoose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(it,CODE);

            }
        });

        update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cl=StaticVariables.GetClient();
                gr=StaticVariables.GetGroup();
                cl.setLastn(lastn.getText().toString());
                cl.setPic(pic_name);
                cl.setEmail(email.getText().toString());
                cl.setPhone(phone.getText().toString());
                gr.setPrice(price.getText().toString());
                AddPicture();
                UpdateMe(cl,gr);
                startActivity(new Intent(getApplicationContext(), Startapp.class));
            }
        });
    }
}
