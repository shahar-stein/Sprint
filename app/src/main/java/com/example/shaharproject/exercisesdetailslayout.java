package com.example.shaharproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class exercisesdetailslayout extends baseActivity
{

    private TextView  txtdate,datedata,timedata,placedata;

    private String excode;

    private Spinner txtname;

    private ListView lst;

    private Button btn_send;

    private MobileServiceClient mclient;

    private MobileServiceTable<exercises> mex;

    private MobileServiceTable<tblclients> mtbl;

    private MobileServiceTable<exercisesdetails> mexd;

    private MobileServiceTable<groupdeatails> mgdetails;

    private MobileServiceTable<groups> mgroups;

    private ProgressDialog prg;

    private List<String> places,dates,times;


    private void select()
    {
        AsyncTask<Void, Void, List<String>> task = new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                prg = new ProgressDialog(exercisesdetailslayout.this);

                prg.setTitle("search");

                prg.setIcon(R.drawable.loading);

                prg.setMessage("Searching Data....");

                prg.show();

            }

            @Override
            protected List<String> doInBackground(Void... voids) {
                try {
                    List<exercises> lstex= mex.where().field("trainercode").eq(StaticVariables.GetClient().getUsern()).execute().get();

                    List<String> answer=new ArrayList<>();

                    answer.add(" # # # ");

                    for(int i=0; i<lstex.size();i++)
                    {
                        answer.add(lstex.get(i).getExercisecode()+'#'+lstex.get(i).getPlace()+'#'+lstex.get(i).getStarthour()+'#'+lstex.get(i).getExercisedate());
                    }

                    return answer;

                } catch (Exception e) {

                    final String message = e.getMessage().toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(exercisesdetailslayout.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                return null;
            }

            protected void onPostExecute(List<String> mgx) {
                super.onPostExecute(mgx);

                prg.dismiss();

                List<String> codes = new ArrayList<>();

                for(int i=0; i<mgx.size(); i++)
                {
                    String[] data = mgx.get(i).split("#");
                    codes.add(data[0]);
                    places.add(data[1]);
                    times.add(data[2]);
                    dates.add(data[3]);
                }

                ArrayAdapter<String> aAdapt=new ArrayAdapter<String>(exercisesdetailslayout.this,android.R.layout.simple_spinner_item,codes);
                aAdapt.setDropDownViewResource(android.R.layout.simple_spinner_item);
                txtname.setAdapter(aAdapt);
            }

        }.execute();
    }

    private void findmembers(final ListView lst, final String groupname, final String excode)
    {
        AsyncTask<Void, Void, List<String>> task = new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                prg = new ProgressDialog(exercisesdetailslayout.this);

                prg.setTitle("search");

                prg.setIcon(R.drawable.loading);

                prg.setMessage("Searching Data....");

                prg.show();

            }

            @Override
            protected List<String> doInBackground(Void... voids) {
                try {

                    List<groupdeatails> mycolegs = mgdetails.where().field("groupcode").eq(groupname).and().field("enddate").eq("").execute().get();

                    List<exercisesdetails> attendies = mexd.where().field("exercisecode").eq(excode).execute().get();
                    List<String> answer=new ArrayList<>();

                    for(int i=0; i<mycolegs.size();i++)
                    {
                        tblclients temp=(mtbl.where().field("usern").eq(mycolegs.get(i).getUsern()).execute().get()).get(0);


                        String pic=(temp.getPic()==null ? "picsi.png" : temp.getPic());
                        String tempAnswer=(pic+'#'+temp.getFirstn()+' '+temp.getLastn());
                        boolean flag=false;
                        for(int z=0; z<attendies.size();z++)
                        {
                            if (attendies.get(z).getTraineecode().equals(temp.getUsern()))
                                flag=true;
                        }
                        if(attendies.size()>0 && flag)
                        {
                            tempAnswer+='#'+"v";
                        }
                        else
                        {
                            tempAnswer+='#'+" ";
                        }
                        answer.add(tempAnswer);
                    }

                    return answer;

                } catch (Exception e) {

                    final String message = e.getMessage().toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(exercisesdetailslayout.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                return null;
            }

            @Override
            protected void onPostExecute(List<String> mgroups) {
                super.onPostExecute(mgroups);
                prg.dismiss();
                if(mgroups.size()>0) {
                    String[] names = new String[mgroups.size()];
                    String[] grouppic = new String[mgroups.size()];
                    String[] attended = new String[mgroups.size()];

                    for (int i = 0; i < mgroups.size(); i++) {
                        String[] temp = mgroups.get(i).split("#");
                        grouppic[i] = temp[0];
                        names[i] = temp[1];
                        attended[i]=temp[2];
                    }

                    customCBlist adapter = new customCBlist(exercisesdetailslayout.
                            this,  names, grouppic,attended);


                    adapter.notifyDataSetChanged();
                    lst.setAdapter(adapter);
                }

            }

        }.execute();
    }


    private void updatepresent(final List<String> presents, final String excode)
    {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                prg = new ProgressDialog(exercisesdetailslayout.this);

                prg.setTitle("search");

                prg.setIcon(R.drawable.loading);

                prg.setMessage("Storing Data....");

                prg.show();

            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {

                    List<exercisesdetails> attendies = mexd.where().field("exercisecode").eq(excode).execute().get();

                    for(int i=0;i<presents.size();i++)
                    {
                        String[] data=presents.get(i).split(" ");
                        tblclients trainee=mtbl.where().field("firstn").eq(data[1]).and().field("lastn").eq(data[2]).execute().get().get(0);
                        exercisesdetails temp=new exercisesdetails();
                        temp.setTrainer(data[3]);
                        temp.setExercisecode(excode);
                        temp.setTraineecode(trainee.getUsern());
                        boolean flag=false;
                        String id = "";
                        for (int n=0; n<attendies.size(); n++)
                        {
                            if(attendies.get(n).getTraineecode().equals(temp.getTraineecode()))
                            {
                                flag=true;

                                id=attendies.get(n).getId();
                            }
                        }
                        if(data[0].equals("true") && flag==false){
                            temp=mexd.insert(temp).get();
                        }
                        else if(data[0].equals("false") && flag==true)
                        {
                            mexd.delete(id).get();
                        }
                    }

               } catch (Exception e) {

                    final String message = e.getMessage().toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(exercisesdetailslayout.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                prg.dismiss();
                startActivity(new Intent(getApplicationContext(), Startapp.class));
            }

        }.execute();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainees_exercise);

        places=new ArrayList<>();
        times=new ArrayList<>();
        dates=new ArrayList<>();
        txtname=findViewById(R.id.txtname);
        txtdate=findViewById(R.id.txtdate);
        lst=findViewById(R.id.list);
        btn_send=findViewById(R.id.send);
        timedata=findViewById(R.id.timedata);
        placedata=findViewById(R.id.placedata);
        datedata=findViewById(R.id.datedata);
        btn_send.setVisibility(View.INVISIBLE);

        try {

            mclient = new MobileServiceClient("https://shaharfinalproject.azurewebsites.net",exercisesdetailslayout.this);

            mex=mclient.getTable(exercises.class);

            mexd=mclient.getTable(exercisesdetails.class);

            mgdetails=mclient.getTable(groupdeatails.class);

            mtbl=mclient.getTable(tblclients.class);

            mgroups=mclient.getTable(groups.class);


        } catch (MalformedURLException e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

        select();

        txtdate.setText(StaticVariables.GetClient().getUsern());

        txtname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                    return;
                excode=parent.getItemAtPosition(position).toString();
                timedata.setText(times.get(position));
                placedata.setText(places.get(position));
                datedata.setText(dates.get(position));
                findmembers(lst,StaticVariables.GetGroup().getGroupcode(),excode);
                btn_send.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> present=new ArrayList<>();
                for(int i=0;i<lst.getChildCount();i++)
                {
                    CheckBox t=(lst.getChildAt(i).findViewById(R.id.present));
                    TextView tn=lst.getChildAt(i).findViewById(R.id.txtname);
                    present.add((t.isChecked()? "true ":"false ")+tn.getText()+" "+txtdate.getText());
                }
                updatepresent(present, excode);

            }
        });

    }
}
