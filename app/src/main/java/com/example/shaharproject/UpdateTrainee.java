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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlobDirectory;
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
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateTrainee extends baseActivity
{
    private EditText height,weight,lastn,email,phone;

    private Button update, btnchoose;

    private Spinner groupid;

    private final static int CODE=666;

    private  String pic_name="picsi.png",pic_name_path="https://shaharstr.blob.core.windows.net/mypics/picsi.png", old_pic_name;

    private tblclients cl = new tblclients();

    private groups gr = new groups();

    private trainee tr=new trainee();

    private groupdeatails gdt = new groupdeatails();

    private String pref="";

    private CircleImageView img;

    private static String item="";

    //---------------------------------

    private MobileServiceClient mclient;

    private MobileServiceTable<tblclients> mclients;

    private MobileServiceTable<groups> mgroups;

    private MobileServiceTable<trainee> mtrainee;

    private MobileServiceTable<groupdeatails> mgroupdeatails;

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


   /* private void loadImage(String img_path)
    {
        img_path=StaticVariables.GetClient().getPic();
        old_pic_name=img_path;
        Picasso.with(this).load("https://shaharstr.blob.core.windows.net/mypics/" + img_path).placeholder(R.mipmap.ic_launcher).into(img, new Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(UpdateTrainee.this, "Good", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(UpdateTrainee.this, "Bad", Toast.LENGTH_SHORT).show();

            }
        });
    }*/




    private void GetData()
    {
        AsyncTask<Void,Void,List<groups>> task = new AsyncTask<Void, Void, List<groups>>()
        {
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg= new ProgressDialog(UpdateTrainee.this);

                prg.setTitle("UpdateTrainee");

                prg.setIcon(R.drawable.loading);

                prg.setMessage("Searching for options...");

                prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                prg.show();

            }


            @Override
            protected List<groups> doInBackground(Void... voids)
            {
                try {

                    List<trainee> mytr = mtrainee.where().field("usern").eq(StaticVariables.GetClient().getUsern()).execute().get();

                    if (mytr.size() != 0) {
                        StaticVariables.SetTrainee(mytr.get(0));

                    }

                    List<groups> myGroups = mgroups.where().field("max").gt("0").execute().get();

                    return  myGroups;

                }catch (Exception e) {
                    final String message = e.getMessage().toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UpdateTrainee.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                return null;
            }

            @Override
            protected void onPostExecute(List<groups> lGroups)
            {
                super.onPostExecute(lGroups);
                List<String> ls= new ArrayList<String>();

                prg.dismiss();
                lastn.setText(StaticVariables.GetClient().getLastn());
                email.setText(StaticVariables.GetClient().getEmail());
                phone.setText(StaticVariables.GetClient().getPhone());
                height.setText(StaticVariables.GetTrainee().getHeight());
                weight.setText(StaticVariables.GetTrainee().getWeight());
                for (int i = 0; i < lGroups.size(); i++)
                    ls.add(lGroups.get(i).getGroupcode().toString());

                if(StaticVariables.GetGroup().getMax().equals("0"))
                    ls.add(StaticVariables.GetGroup().getGroupcode());

                ArrayAdapter<String> adapters= new ArrayAdapter<String>(UpdateTrainee.this,android.R.layout.simple_spinner_dropdown_item,ls);

                groupid.setAdapter(adapters);
                groupid.setSelection(adapters.getPosition(StaticVariables.GetGroup().getGroupcode()));


            }
        }.execute();
    }

    private void UpdateMe(final tblclients newcl, final trainee newtr, final groupdeatails newGd)
    {
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg= new ProgressDialog(UpdateTrainee.this);

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
                    trainee t = mtrainee.update(newtr).get();
                    StaticVariables.SetTrainee(t);
                    if(gdt!=null)
                    {
                        groups g = StaticVariables.GetGroup();
                        int temp=Integer.parseInt(g.getMax())+1;
                        g.setMax((String.valueOf(temp)));
                        g=mgroups.update(g).get();
                        groupdeatails dg = mgroupdeatails.where().field("usern").eq(StaticVariables.GetClient().getUsern()).and().field("groupcode").eq(g.getGroupcode()).execute().get().get(0);
                        dg.setEnddate(newGd.getStartdate());
                        dg=mgroupdeatails.update(dg).get();
                        mgroupdeatails.insert(newGd);
                        g= mgroups.where().field("groupcode").eq(newGd.getGroupcode()).execute().get().get(0);
                        temp=Integer.parseInt(g.getMax())-1;
                        g.setMax((String.valueOf(temp)));
                        g=mgroups.update(g).get();
                        StaticVariables.SetGroup(g);
                    }
                    int x=9;
                } catch (Exception e)
                {
                    final String message=e.getMessage().toString();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(UpdateTrainee.this, message, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(UpdateTrainee.this, message, Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_update_trainee);

        lastn=findViewById(R.id.lastn);

        email=findViewById(R.id.email);

        phone=findViewById(R.id.phone);

        height=findViewById(R.id.height);

        weight=findViewById(R.id.weight);

        groupid=findViewById(R.id.groupid);

        update=findViewById(R.id.update);

        img=findViewById(R.id.img);

        btnchoose=findViewById(R.id.btnchoose);

        loadImage();

        try
        {
            mclient= new MobileServiceClient("https://shaharfinalproject.azurewebsites.net",UpdateTrainee.this);

            mclients=mclient.getTable(tblclients.class);

            mtrainee=mclient.getTable(trainee.class);

            mgroups=mclient.getTable(groups.class);

            mgroupdeatails=mclient.getTable(groupdeatails.class);
        }
        catch (MalformedURLException e)
        {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

        GetData();
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
                tr=StaticVariables.GetTrainee();
                cl.setLastn(lastn.getText().toString());
                cl.setPic(pic_name);
                cl.setEmail(email.getText().toString());
                cl.setPhone(phone.getText().toString());
                tr.setHeight(height.getText().toString());
                tr.setWeight(weight.getText().toString());
                if(groupid.getSelectedItem().toString().equals(StaticVariables.GetGroup().getGroupcode()))
                {
                    gdt=null;
                }
                else
                {
                    gdt=new groupdeatails();
                    java.util.Calendar c = java.util.Calendar.getInstance();
                    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
                    String datetime = dateformat.format(c.getTime());
                    gdt.setGroupcode(groupid.getSelectedItem().toString());
                    gdt.setUsern(StaticVariables.GetClient().getUsern());
                    gdt.setStartdate(datetime);
                    gdt.setEnddate("");
                    gdt.setGroupgrade("0");
                    gdt.setTrainergrade("0");
                }
                AddPicture();
                UpdateMe(cl,tr,gdt);
                startActivity(new Intent(getApplicationContext(), Startapp.class));


            }
        });

    }
}
