package com.example.shaharproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class register extends baseActivity
{

    private EditText usern,firstn,lastn,pass,phone,email;

    private TextView txtdate;

    private RadioButton female,male;

    private Button btnchoose,trainee,trainer;

    private int myday,mymonth,myyear;

    private String choosenDate="";

    private final static int CODE=666;

    private  String pic_name="picsi.png",pic_name_path="https://shaharstr.blob.core.windows.net/mypics/picsi.png",old_pic_name;

    private tblclients cl=new tblclients();

    private String pref="";

    private CircleImageView img;

    //---------------------------------

    private MobileServiceClient mclient;

    private MobileServiceTable<tblclients> mtable;

    private ProgressDialog prg=null;

    private String storageConnection="DefaultEndpointsProtocol=http;AccountName=shaharstr;AccountKey=UHJ3WHkggYn+0PzvZuCFjSY9zQDiYVh1rWi7oF+F4EDY/a0AXoKsPfTMCeHZw/ydlAoDl3RvYYqN1AhRPC2IQA==";

    private void loadImage() {

        AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {

                Bitmap my_bitmap = null;

                String picpathname =  pic_name_path;

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


        @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
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
                                Toast.makeText(register.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    return null;
                }
            }.execute();
        }


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void Reg(final tblclients newClient )
    {
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg=new ProgressDialog(register.this);

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
                    tblclients c = mtable.insert(newClient).get();

                    StaticVariables.SetClient(c);

                    int x=9;
                } catch (Exception e)
                {
                    final String message=e.getMessage().toString();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(register.this, message, Toast.LENGTH_SHORT).show();
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




    @Override
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
    protected Dialog onCreateDialog(int id)
    {


        return new DatePickerDialog(register.this,DatePickListener,myyear,mymonth,myday);

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

            txtdate.setText(choosenDate);


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Calendar c =Calendar.getInstance();

        myday= c.get(Calendar.DAY_OF_MONTH);

        mymonth=c.get(Calendar.MONTH);

        myyear=c.get(Calendar.YEAR);

        choosenDate=myday + "/" + mymonth + "/" + myyear;

        txtdate=findViewById(R.id.txtdate);

        btnchoose=findViewById(R.id.btnchoose);

        firstn=findViewById(R.id.firstn);

        lastn=findViewById(R.id.lastn);

        pass=findViewById(R.id.pass);

        usern=findViewById(R.id.usern);

        email=findViewById(R.id.email);

        phone=findViewById(R.id.phone);

        female=findViewById(R.id.female);

        male=findViewById(R.id.male);

        img=findViewById(R.id.img);

        trainer=findViewById(R.id.trainer);

        trainee=findViewById(R.id.trainee);


        loadImage();



        try
        {
            mclient= new MobileServiceClient("https://shaharfinalproject.azurewebsites.net",register.this);

            mtable=mclient.getTable(tblclients.class);
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
        //------------------------------------------------------
        btnchoose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(it,CODE);

            }
        });

        //-------------------------------------------------

        trainer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!Check())
                {
                    Toast.makeText(register.this, "Correct Errors", Toast.LENGTH_SHORT).show();
                }else {
                    cl = new tblclients();
                    cl.setUsern(usern.getText().toString());
                    cl.setPass(pass.getText().toString());
                    cl.setFirstn(firstn.getText().toString());
                    cl.setLastn(lastn.getText().toString());
                    cl.setDateborn(txtdate.getText().toString());
                    cl.setPic(pic_name);
                    cl.setEmail(email.getText().toString());
                    cl.setPhone(phone.getText().toString());
                    cl.setGender((female.isChecked() ? "Female" : "Male"));
                    cl.setRoll("Trainer");
                    AddPicture();
                    Reg(cl);
                    startActivity(new Intent(getApplicationContext(), trainersActivity.class));
                }
            }
        });

        trainee.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View v)
            {
                if(!Check())
                {
                    Toast.makeText(register.this, "Correct Errors", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    cl = new tblclients();
                    cl.setUsern(usern.getText().toString());
                    cl.setPass(pass.getText().toString());
                    cl.setFirstn(firstn.getText().toString());
                    cl.setLastn(lastn.getText().toString());
                    cl.setDateborn(txtdate.getText().toString());
                    cl.setPic(pic_name);
                    cl.setEmail(email.getText().toString());
                    cl.setPhone(phone.getText().toString());
                    if (female.isChecked())
                        cl.setGender("Female");
                    else
                        cl.setGender("Male");
                    cl.setRoll("Trainee");
                    AddPicture();
                    Reg(cl);
                    startActivity((new Intent(getApplicationContext(), traineeActivity.class)));
                }
            }
        });

        usern.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(usern.getText().toString().length()<3)
                    usern.setError("Username must be at least 3 letters");
            }
        });

       firstn.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(firstn.getText().toString().length()<2)
                    firstn.setError("First name must be at least 2 letters");
            }
        });

        lastn.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(lastn.getText().toString().length()<2)
                    lastn.setError("Last name must be at least 2 letters");
            }
        });


        pass.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(pass.getText().toString().length()<7)
                    pass.setError("Password must be at least 8 letters");
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


        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!email.getText().toString().matches("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"
                        + "\\@"
                        + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"
                        + "("
                        + "\\."
                        + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"
                        + ")+"))
                    email.setError("Email must be vaild");
            }
        });
    }

    private boolean Check()
    {
        return MaleFemale()&&UserName()&&TxtDate()&&Password()&&FirstName()&&LastName()&&Email()&&Phone();
    }

    private boolean MaleFemale()
    {
        if(!male.isChecked()&&!female.isChecked())
        {
            male.setError("Select Gender");
            return  false;
        }
        return true;
    }

    private boolean UserName()
    {
        if(usern.getText().toString().length()<3) {
            usern.setError("Username must be at least 3 letters");
            return false;
        }
        return true;
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

    private boolean Password()
    {
        if(pass.getText().toString().length()<7) {
            pass.setError("Password must be at least 8 letters");
            return false;
        }
        return true;
    }

    private boolean FirstName()
    {
        if(firstn.getText().toString().length()<2) {
            firstn.setError("First name must be at least 2 letters");
            return false;
        }
        return true;
    }

    private boolean LastName()
    {
        if(lastn.getText().toString().length()<2) {
            lastn.setError("Last name must be at least 2 letters");
            return false;
        }
        return true;
    }

    private boolean Email()
    {
        if
        (!email.getText().toString().matches("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"
                + "\\@"
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"
                + "("
                + "\\."
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"
                + ")+"))
        {
            email.setError("Email must be vaild");
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
}

