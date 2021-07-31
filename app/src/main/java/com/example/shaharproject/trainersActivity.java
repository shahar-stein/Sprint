package com.example.shaharproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class trainersActivity extends baseActivity
{
    private EditText groupid,price,maxtrainee;

    private Button register, btnchoose;

    private ProgressDialog prg=null;

    private trainers tr=new trainers();

    private groups gr=new groups();

    private groupdeatails gdt = new groupdeatails();

    private MobileServiceClient mclient;

    private MobileServiceTable<trainers> mtable;

    private MobileServiceTable<groups> gtable;

    private MobileServiceTable<groupdeatails> gdtable;

    private int myday,mymonth,myyear;

    private TextView dateofstart;

    private String choosenDate="";

    private final static int CODE=666;

    private  String pic_name="picsi.png",pic_name_path="https://shaharstr.blob.core.windows.net/mypics/picsi.png",old_pic_name ;

    private CircleImageView img;

    private String storageConnection="DefaultEndpointsProtocol=http;AccountName=shaharstr;AccountKey=UHJ3WHkggYn+0PzvZuCFjSY9zQDiYVh1rWi7oF+F4EDY/a0AXoKsPfTMCeHZw/ydlAoDl3RvYYqN1AhRPC2IQA==";




    private void loadImage() {

        AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {

                Bitmap my_bitmap = null;

                String picpathname = "https://shaharstr.blob.core.windows.net/mypics/" + StaticVariables.GetClient().getPic();

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
                            Toast.makeText(trainersActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                return null;
            }
        }.execute();
    }



    private void Reg(final trainers newTrainer,final groups newGroup)
    {
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg=new ProgressDialog(trainersActivity.this);

                prg.setTitle("Register");

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
                    trainers c = mtable.insert(newTrainer).get();

                    StaticVariables.SetTrainer(c);

                    groups g = gtable.insert(newGroup).get();

                    StaticVariables.SetGroup(g);

                    int x=9;
                } catch (Exception e)
                {
                    final String message=e.getMessage().toString();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(trainersActivity.this, message, Toast.LENGTH_SHORT).show();
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

    protected Dialog onCreateDialog(int id)
    {


        return new DatePickerDialog(trainersActivity.this,DatePickListener,myyear,mymonth,myday);

    }

    private DatePickerDialog.OnDateSetListener DatePickListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
        {
            myday=dayOfMonth;

            mymonth=month +1;

            myyear = year;

            choosenDate=myday + "/" + mymonth + "/" + myyear;

            dateofstart.setText(choosenDate);


        }
    };

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
        setContentView(R.layout.activity_trainers);

        dateofstart=findViewById(R.id.dateofstart);

        groupid=findViewById(R.id.groupid);

        price=findViewById(R.id.price);

        maxtrainee=findViewById(R.id.maxtrainee);

        register=findViewById(R.id.register);

        java.util.Calendar c = java.util.Calendar.getInstance();

        myday= c.get(java.util.Calendar.DAY_OF_MONTH);

        mymonth=c.get(java.util.Calendar.MONTH);

        myyear=c.get(java.util.Calendar.YEAR);

        choosenDate=myday + "/" + mymonth + "/" + myyear;

        img=findViewById(R.id.img);

        btnchoose=findViewById(R.id.btnchoose);

        loadImage();


        try
        {
            mclient= new MobileServiceClient("https://shaharfinalproject.azurewebsites.net",trainersActivity.this);

            mtable=mclient.getTable(trainers.class);

            gtable=mclient.getTable(groups.class);

            gdtable=mclient.getTable(groupdeatails.class);
        }
        catch (MalformedURLException e)
        {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

        btnchoose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(it,CODE);

            }
        });




        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tr=new trainers();
                gr=new groups();
                java.util.Calendar c = java.util.Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
                String datetime = dateformat.format(c.getTime());
                tr.setUsern(StaticVariables.GetClient().getUsern());
                tr.setDateofstart(dateofstart.getText().toString());
                tr.setGroupid(groupid.getText().toString());
                gr.setGroupcode(groupid.getText().toString());
                gr.setPrice(price.getText().toString());
                gr.setOpendate(datetime);//new Date(System.currentTimeMillis()).toString());
                gr.setMax(maxtrainee.getText().toString());
                gr.setPic(pic_name);
                AddPicture();
                Reg(tr,gr);
                startActivity(new Intent(getApplicationContext(), Startapp.class));
            }
        });

        dateofstart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog(666);
            }
        });
        
    }
}
