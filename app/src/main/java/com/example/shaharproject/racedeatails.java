package com.example.shaharproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;


enum btnName{Update,Delete,Register;}

public class racedeatails extends baseActivity
{
    private MobileServiceClient mclient;

    private MobileServiceTable<competitions> mrace;

    private MobileServiceTable<competitiondeatails> mrdetails;

    private ProgressDialog prg;

    private TextView txtracename,txttrainercode, txtcompetitiondate, txtadress, txtdescription, txthour, result;

    private EditText txtresult;

    private CircleImageView imageView;

    private Button btnclose,btnregister;//,btndelete;

    private ListView lst;

    private Spinner all,my,past;


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

    private void Reg(final competitiondeatails newDetails )
    {
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg=new ProgressDialog(racedeatails.this);

                prg.setTitle("Register To Race");

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
                    competitiondeatails tc = mrdetails.insert(newDetails).get();

                } catch (Exception e)
                {
                    final String message=e.getMessage().toString();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(racedeatails.this, message, Toast.LENGTH_SHORT).show();
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

    private void UnReg(final competitiondeatails newDetails )
    {
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg=new ProgressDialog(racedeatails.this);

                prg.setTitle("Register To Race");

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
                    competitiondeatails md= mrdetails.where().field("competitionname").eq(newDetails.getCompetitionname()).and().field("traineecode").eq(StaticVariables.GetClient().getUsern()).execute().get().get(0);
                    mrdetails.delete(md).get();

                } catch (Exception e)
                {
                    final String message=e.getMessage().toString();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(racedeatails.this, message, Toast.LENGTH_SHORT).show();
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

    private void Delcomp(final competitiondeatails newDetails )
    {
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg=new ProgressDialog(racedeatails.this);

                prg.setTitle("Register To Race");

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
                    List<competitiondeatails> md= mrdetails.where().field("competitionname").eq(newDetails.getCompetitionname()).execute().get();
                    for(int i=0; i<md.size(); i++)
                    {
                        mrdetails.delete(md.get(i)).get();
                    }
                    competitions c=mrace.where().field("competitionname").eq(newDetails.getCompetitionname()).execute().get().get(0);
                    mrace.delete(c);

                } catch (Exception e)
                {
                    final String message=e.getMessage().toString();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(racedeatails.this, message, Toast.LENGTH_SHORT).show();
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

    private void Update(final competitiondeatails newDetails )
    {
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg=new ProgressDialog(racedeatails.this);

                prg.setTitle("Update Data");

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
                    competitiondeatails tc = mrdetails.update(newDetails).get();

                } catch (Exception e)
                {
                    final String message=e.getMessage().toString();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(racedeatails.this, message, Toast.LENGTH_SHORT).show();
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

    private void ShowChooseen(final String racename)
    {
        AsyncTask<Void,Void,String> task = new AsyncTask<Void, Void, String>()
        {
            @Override
            protected String doInBackground(Void... voids)
            {
                try
                {
                    List<competitions> mycomp= mrace.where().field("competitionname").eq(racename).execute().get();
                    List<competitiondeatails> md= mrdetails.where().field("deleted").eq(false).and().field("competitionname").eq(racename).and().field("traineecode").eq(StaticVariables.GetClient().getUsern()).execute().get();
                    return mycomp.get(0).toString()+"#"+md.size()+"#"+StaticVariables.GetClient().getRoll()+(md.size()>0? "#"+md.get(0).getID()+"#"+md.get(0).getResults(): "");
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
            protected void onPostExecute(String comp)
            {
                super.onPostExecute(comp);
                final String[] data=comp.split("#");
                int size=Integer.parseInt(data[7]);

                View mview = getLayoutInflater().inflate(R.layout.dialog_raceinformation,null);

                final AlertDialog.Builder a_builder=new AlertDialog.Builder(racedeatails.this);

                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
                String now=dateformat.format(c.getTime());

                txtracename=mview.findViewById(R.id.txtracename);
                txttrainercode=mview.findViewById(R.id.txttrainercode);
                txtcompetitiondate=mview.findViewById(R.id.txtcompetitiondate);
                txtadress=mview.findViewById(R.id.txtadress);
                txtdescription=mview.findViewById(R.id.txtdescription);
                txthour=mview.findViewById(R.id.txthour);
                result=mview.findViewById(R.id.result);
                txtresult=mview.findViewById(R.id.txtresult);
                btnregister=mview.findViewById(R.id.btnregister);
                btnclose=mview.findViewById(R.id.btnclose);
                //btndelete=mview.findViewById(R.id.btndelete);
                imageView=mview.findViewById(R.id.imageVieww);
                if(size>0 || data[8].equals("Trainer"))
                {
                    try{
                        if(dateformat.parse(data[4]).before(dateformat.parse(now))){
                            btnregister.setText("Update");
                            result.setVisibility(View.VISIBLE);
                            txtresult.setVisibility(View.VISIBLE);
                        }
                        else{
                            btnregister.setText("Delete");
                            result.setVisibility(View.INVISIBLE);
                            txtresult.setVisibility(View.INVISIBLE);
                        }
                    }
                    catch(Exception e){}
                }
                else
                {
                    btnregister.setText("Register");
                    result.setVisibility(View.INVISIBLE);
                    txtresult.setVisibility(View.INVISIBLE);
                }

                txtracename.setText(data[0]);
                txttrainercode.setText(data[1]);
                txtadress.setText(data[2]);
                txtadress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri_map = Uri.parse("geo:0.0?q=" + txtadress.getText());

                        Intent itmap = new Intent(Intent.ACTION_VIEW,uri_map);

                        startActivity(itmap);

                    }
                });
                txtdescription.setText(data[3]);
                txtcompetitiondate.setText(data[4]);
                txthour.setText(data[5]);
                if(size>0){
                    txtresult.setText(data[10]);
                }

                loadImage(data[6],imageView);

                a_builder.setView(mview);

                final competitiondeatails temp = new competitiondeatails();
                temp.setCompetitionname(data[0]);
                temp.setTraineecode(StaticVariables.GetClient().getUsern());
                temp.setRegisterdate(dateformat.format(c.getTime()));

                final AlertDialog alert;
                alert=a_builder.create();
                btnclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });

                btnregister.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        try
                        {
                            switch(btnName.valueOf(btnregister.getText().toString()))
                            {
                                case Register:
                                    Reg(temp);
                                    break;
                                case Delete:
                                    if(data[8].equals("Trainee")) {
                                        UnReg(temp);
                                    }
                                    else
                                    {
                                        Delcomp(temp);
                                    }
                                    break;
                                case Update:
                                    temp.setId(data[9]);
                                    temp.setResults(txtresult.getText().toString());
                                    Update(temp);
                                    break;
                            }
                            alert.dismiss();
                            startActivity(new Intent(getApplicationContext(), racedeatails.class));
                        }
                        catch(Exception e)
                        {
                            final String message = e.getMessage().toString();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(racedeatails.this, message, Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                });

/*
                btndelete.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        try
                        {
                            if(data[8].equals("Trainee")) {
                                UnReg(temp);
                                alert.dismiss();
                            }
                            else
                            {
                                Delcomp(temp);
                            }
                        }
                        catch(Exception e)
                        {
                            final String message = e.getMessage().toString();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(racedeatails.this, message, Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                });

*/
                alert.show();

            }
        }.execute();
    }
    private void RaceSelect(final Spinner my, final Spinner all, final Spinner past)//ListView lst)
    {
        AsyncTask<Void, Void, List<List<String>>> task = new AsyncTask<Void, Void, List<List<String>>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                prg = new ProgressDialog(racedeatails.this);

                prg.setTitle("search");

                prg.setIcon(R.drawable.loading);

                prg.setMessage("Searching Data....");

                prg.show();

            }

            @Override
            protected List<List<String>> doInBackground(Void... voids) {
                try {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
                    String now=dateformat.format(c.getTime());
                    List<competitions>mycomp;
                    if(StaticVariables.GetClient().getRoll().equals("Trainee")){
                        mycomp = mrace.execute().get();
                    }else{
                        mycomp = mrace.where().field("trainercode").eq(StaticVariables.GetClient().getUsern()).execute().get();
                    }




                    //List<String> answer=new ArrayList<>();

                    List<String> all=new ArrayList<>();
                    all.add("");
                    List<String> my=new ArrayList<>();
                    my.add("");
                    List<String> past=new ArrayList<>();
                    past.add("");
//
                    for(int i=0; i<mycomp.size();i++)
                    {
                        List<competitiondeatails>compdet= mrdetails.where().field("competitionname").eq(mycomp.get(i).getCompetitionname()).and().field("traineecode").eq(StaticVariables.GetClient().getUsern()).execute().get();
                        if(dateformat.parse(mycomp.get(i).getCompetitiondate()).before(dateformat.parse(now)) && compdet.size()>0) {
                            past.add(mycomp.get(i).getCompetitionname());
                        }
                        if(dateformat.parse(mycomp.get(i).getCompetitiondate()).after(dateformat.parse(now))&& compdet.size()>0){
                            my.add( mycomp.get(i).getCompetitionname());
                        }
                        if(dateformat.parse(mycomp.get(i).getCompetitiondate()).after(dateformat.parse(now))&&compdet.size()==0) {
                            all.add(mycomp.get(i).getCompetitionname());
                        }
                    }

                    List<List<String>> answer = new ArrayList<>();
                    answer.add(all);
                    answer.add(my);
                    answer.add(past);

                    return answer;//answer;

                } catch (Exception e) {

                    final String message = e.getMessage().toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(racedeatails.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                return null;
            }

            protected void onPostExecute(List<List<String>> comps) {
                super.onPostExecute(comps);

                prg.dismiss();

                ArrayAdapter<String> aAdapt=new ArrayAdapter<String>(racedeatails.this,android.R.layout.simple_spinner_item,comps.get((0)));
                aAdapt.setDropDownViewResource(android.R.layout.simple_spinner_item);
                all.setAdapter(aAdapt);
                ArrayAdapter<String> mAdapt=new ArrayAdapter<String>(racedeatails.this,android.R.layout.simple_spinner_item,comps.get((1)));
                mAdapt.setDropDownViewResource(android.R.layout.simple_spinner_item);
                my.setAdapter(mAdapt);
                ArrayAdapter<String> pAdapt=new ArrayAdapter<String>(racedeatails.this,android.R.layout.simple_spinner_item,comps.get((2)));
                pAdapt.setDropDownViewResource(android.R.layout.simple_spinner_item);
                past.setAdapter(pAdapt);

/*
                if(comps!=null) {
                    String[] names = new String[comps.size()];
                    String[] trainername = new String[comps.size()];
                    String[] complogo = new String[comps.size()];


                    for (int i = 0; i < comps.size(); i++) {
                        String[] temp = comps.get(i).split("#");
                        complogo[i] = temp[0];
                        trainername[i] = temp[2];
                        names[i] = temp[1];
                    }

                    customlistGroups adapter = new customlistGroups(racedeatails.
                            this, names, complogo, trainername);


                    lst.setAdapter(adapter);
*/
                }

        }.execute();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_racedeatails);

        try {

            mclient = new MobileServiceClient("https://shaharfinalproject.azurewebsites.net",racedeatails.this);

            mrace=mclient.getTable(competitions.class);

            mrdetails =mclient.getTable(competitiondeatails.class);

        } catch (MalformedURLException e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

        my=findViewById(R.id.my);
        all=findViewById(R.id.all);
        past=findViewById(R.id.past);

        RaceSelect(my,all,past);

        all.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    return;
                }
                String txt=parent.getItemAtPosition(position).toString();
                ShowChooseen(txt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        my.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    return;
                }
                String txt=parent.getItemAtPosition(position).toString();
                ShowChooseen(txt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        past.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    return;
                }
                String txt=parent.getItemAtPosition(position).toString();
                ShowChooseen(txt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
 /*       lst=findViewById(R.id.lstraces);
        RaceSelect(lst);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {

                TextView txtuser=view.findViewById(R.id.txtgroupname);

                ShowChooseen(txtuser.getText().toString());
            }
        });*/



    }
}
