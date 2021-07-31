package com.example.shaharproject;

import android.app.ProgressDialog;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class exerciselayout extends baseActivity
{
    private EditText txtname,txtadress,txtdescription;

    private Button btnexit, btnsubmit;

    private DatePicker txtdate;

    private TimePicker txttime;

    private TextView txttrainer;

    private MobileServiceClient mclient;

    private MobileServiceTable<exercises> mexercise;

    private MobileServiceTable<trainers> mtrainers;

    private MobileServiceTable<groupdeatails> mgdtbl;

    private MobileServiceTable<tblclients> mtblclients;

    private ProgressDialog prg=null;

    private LinearLayout trainer,trainee;

    private ListView lst;


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void OpenEx(final exercises ex )
    {
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg=new ProgressDialog(exerciselayout.this);

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
                    exercises e = mexercise.insert(ex).get();
                    int x=9;
                } catch (Exception e)
                {
                    final String message=e.getMessage().toString();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(exerciselayout.this, message, Toast.LENGTH_SHORT).show();
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

    private void sendinvite(final exercises ex)
    {
        AsyncTask<Void,Void,Void> task=new AsyncTask<Void,Void,Void>()
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            prg=new ProgressDialog(exerciselayout.this);

            prg.setTitle("Register");

            prg.setIcon(R.drawable.loading);

            prg.setMessage("Sending Invitations...");

            prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            prg.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                List<groupdeatails> g=(mgdtbl.where().field("groupcode").eq(StaticVariables.GetGroup().getGroupcode()).execute().get());
                List<String> mail=new ArrayList<>();

                for(int i=0; i<g.size();i++)
                {
                    tblclients c=(mtblclients.where().field("usern").eq(g.get(i).getUsern()).execute().get()).get(0);
                    mail.add(c.getEmail());
                }

            } catch (Exception e)
            {
                final String message=e.getMessage().toString();

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(exerciselayout.this, message, Toast.LENGTH_SHORT).show();
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

    private void loadplan(final ListView lst,final String name)
    {
        AsyncTask<Void,Void,List<String>> task=new AsyncTask<Void,Void,List<String>>()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg=new ProgressDialog(exerciselayout.this);

                prg.setTitle("Register");

                prg.setIcon(R.drawable.loading);

                prg.setMessage("loading exercise plan...");

                prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                prg.show();
            }

            @Override
            protected List<String> doInBackground(Void... voids)
            {
                try
                {
                    List<String> answer=new ArrayList<>();
                    trainers tr=mtrainers.where().field("groupid").eq(name).execute().get().get(0);
                    List<exercises> ex=(mexercise.where().field("trainercode").eq(tr.getUsern()).execute().get());


                    for(int i=0; i<ex.size();i++)
                    {
                        answer.add(ex.get(i).getExercisedate()+"#"+ex.get(i).getExercisecode()+"#"+ex.get(i).getPlace());
                    }
                    return answer;

                } catch (Exception e)
                {
                    final String message=e.getMessage().toString();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(exerciselayout.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<String> answer)
            {
                super.onPostExecute(answer);

                prg.dismiss();

                String[] names;
                String[] dates;
                String[] places;

                if(answer.size()>0) {
                    names = new String[answer.size()];
                    dates = new String[answer.size()];
                    places = new String[answer.size()];

                    for (int i = 0; i < answer.size(); i++) {
                        String[] data = answer.get(i).split("#");
                        dates[i] = data[0];
                        names[i]=data[1];
                        places[i]=data[2];
                    }

                    customEXlist adapter = new customEXlist(exerciselayout.this,names,places,dates);

                    lst.setAdapter(adapter);
                }

            }
        }.execute();

    }



    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exerciseplan);

        trainer=findViewById(R.id.trainerplan);

        trainee=findViewById(R.id.traineeplan);

        txtname=findViewById(R.id.txtname);

        txtadress=findViewById(R.id.txtadress);

        txtdescription=findViewById(R.id.txtdescription);

        btnexit=findViewById(R.id.btnexit);

        btnsubmit=findViewById(R.id.btnsubmit);

        txtdate=findViewById(R.id.txtdate);

        txttime=findViewById(R.id.txttime);

        txttrainer=findViewById(R.id.txttrainer);

        lst=findViewById(R.id.explan);

        try
        {
            mclient= new MobileServiceClient("https://shaharfinalproject.azurewebsites.net",exerciselayout.this);

            mexercise=mclient.getTable(exercises.class);

            mtrainers=mclient.getTable(trainers.class);

            mgdtbl=mclient.getTable(groupdeatails.class);

            mtblclients=mclient.getTable(tblclients.class);
        }
        catch (MalformedURLException e)
        {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

        if(StaticVariables.GetClient().getRoll().equals("Trainer"))
        {
            trainer.setVisibility(View.VISIBLE);
            trainer.setClickable(true);
            trainee.setVisibility(View.INVISIBLE);
            trainee.setClickable(false);
            txttrainer.setText(StaticVariables.GetClient().getUsern());
            Calendar c = Calendar.getInstance();
            txtdate.setMinDate(c.getTimeInMillis());
            txttime.setIs24HourView(true);
        }
        else
        {
            trainer.setVisibility(View.INVISIBLE);
            trainer.setClickable(false);
            trainee.setVisibility(View.VISIBLE);
            trainee.setClickable(true);
            loadplan(lst,StaticVariables.GetGroup().getGroupcode());
        }

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                exercises ex = new exercises();
                ex.setExercisecode(txtname.getText().toString());
                ex.setExercisedate(String.format("%02d/%02d/%04d",txtdate.getDayOfMonth(),txtdate.getMonth()+1,txtdate.getYear()));
                ex.setStarthour(txttime.getHour()+":"+txttime.getMinute());
                ex.setTrainercode(txttrainer.getText().toString());
                ex.setNotes(txtdescription.getText().toString());
                ex.setPlace(txtadress.getText().toString());
                OpenEx(ex);

                startActivity(new Intent(getApplicationContext(),Startapp.class));
            }
        });

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Uri uri_map = Uri.parse("geo:0.0?q=" + lst.getItemAtPosition(position).toString());

                Intent itmap = new Intent(Intent.ACTION_VIEW,uri_map);

                startActivity(itmap);

            }
        });

        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });
    }
}
