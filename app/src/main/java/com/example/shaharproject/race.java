package com.example.shaharproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class race extends baseActivity
{
    private TextView time,txtdate;

    private EditText racename,adress,description,phone;

    private Button btnchoose,send;

    private CircleImageView img;

    private int myday,mymonth,myyear;

    private String choosenDate="";

    private final static int CODE=666;

    private  String pic_name="picsi.png",pic_name_path="https://shaharstr.blob.core.windows.net/mypics/picsi.png", old_pic_name;

    private MobileServiceClient mclient;

    private MobileServiceTable<competitions> mtable;

    private competitions co;

    private ProgressDialog prg=null;

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
                            Toast.makeText(race.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return null;
            }
        }.execute();
    }


    protected Dialog onCreateDialog(int id)
    {
        return new DatePickerDialog(race.this,DatePickListener,myyear,mymonth,myday);
    }

    private DatePickerDialog.OnDateSetListener DatePickListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
        {
            myday=dayOfMonth;

            mymonth=month +1;

            myyear = year;

            choosenDate=String.format("%02d/%02d/%d",myday,mymonth,myyear);

            txtdate.setText(choosenDate);
        }
    };

    private void OpenRace(final competitions newClient )
    {
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg=new ProgressDialog(race.this);

                prg.setTitle("Create a Race");

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
                    competitions co = mtable.insert(newClient).get();

                    StaticVariables.setMycompetition(co);

                    int x=9;
                } catch (Exception e)
                {
                    final String message=e.getMessage().toString();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(race.this, message, Toast.LENGTH_SHORT).show();
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

                AddPicture();
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
        setContentView(R.layout.activity_race);

        time = findViewById(R.id.time);

        racename=findViewById(R.id.racename);

        adress = findViewById(R.id.adress);

        description=findViewById(R.id.description);

        phone=findViewById(R.id.phone);

        btnchoose=findViewById(R.id.btnchoose);

        send=findViewById(R.id.send);

        img=findViewById(R.id.img);

        txtdate=findViewById(R.id.txtdate);

        Calendar c =Calendar.getInstance();

        myday= c.get(Calendar.DAY_OF_MONTH);

        mymonth=c.get(Calendar.MONTH);

        myyear=c.get(Calendar.YEAR);

        choosenDate=myday + "/" + mymonth + "/" + myyear;



        try
        {
            mclient= new MobileServiceClient("https://shaharfinalproject.azurewebsites.net",race.this);

            mtable=mclient.getTable(competitions.class);
        }
        catch (MalformedURLException e)
        {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

        txtdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog(666);
            }
        });

        time.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Calendar mycalendar = Calendar.getInstance();
                int hour = mycalendar.get(Calendar.HOUR_OF_DAY);
                final int mintues = mycalendar.get(Calendar.MINUTE);

                TimePickerDialog mydialog = new TimePickerDialog(race.this, new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {

                        String mm=hourOfDay+":"+minute;
                        String[] k = mm.split(":");

                        int j=Integer.parseInt(k[k.length-1]);

                        if(j==0)
                            mm+="0";

                       time.setText(mm);
                    }
                },hour,mintues,false);
                mydialog.setTitle("Select Time!!!");
                mydialog.show();
            }
        });

        btnchoose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(it,CODE);

            }
        });

        send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!Check())
                {
                    Toast.makeText(race.this, "Correct Errors", Toast.LENGTH_SHORT).show();
                }else {
                    co = new competitions();
                    co.setCompetitionname(racename.getText().toString());
                    co.setAdress(adress.getText().toString());
                    co.setDescription(description.getText().toString());
                    co.setCompetitionhour(time.getText().toString());
                    co.setCompetitiondate(txtdate.getText().toString());
                    co.setTrainercode(StaticVariables.GetClient().getUsern());
                    co.setLogo(pic_name);
                    //co.set(phone.getText().toString());
                    AddPicture();
                    OpenRace(co);
                    startActivity(new Intent(getApplicationContext(), Startapp.class));
                }
            }
        });

        racename.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(racename.getText().toString().length()<3)
                    racename.setError("Race Name must be at least 3 letters");
            }
        });

        phone.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(phone.getText().toString().length()<10)
                    phone.setError("Phone Number must be at least 10 letters");
            }
        });

        adress.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(adress.getText().toString().length()<2)
                    adress.setError("Adress must be at least 2 letters");
            }
        });

        description.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(description.getText().toString().length()<7)
                    description.setError("Description must be at least 8 letters");
            }
        });
    }

    private boolean Check()
    {
        return RaceName()&&TxtDate()&&Adress()&&Description()&&Phone();
    }

    private boolean TxtDate()
    {
        if(txtdate.getText().equals("Select Day of Birth"))
        {
            txtdate.setError("Select date of birth");
            return false;
        }
        return true;
    }

    private boolean Phone()
    {
        if(phone.getText().toString().length()<10)
        {
            phone.setError("Phone Number must be at least 10 letters");
            return false;
        }
        return true;
    }

    private boolean RaceName()
    {
        if(racename.getText().toString().length()<3) {
            racename.setError("Race Name must be at least 3 letters");
            return false;
        }
        return true;
    }

    private boolean Adress()
    {
        if(adress.getText().toString().length()<2) {
            adress.setError("Adress must be at least 2 letters");
            return false;
        }
        return true;
    }

    private boolean Description()
    {
        if(description.getText().toString().length()<7) {
            description.setError("Description must be at least 8 letters");
            return false;
        }
        return true;
    }
}
