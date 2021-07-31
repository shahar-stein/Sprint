package com.example.shaharproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class traineeActivity extends baseActivity
{
    private EditText height,weight;

    private Spinner groupid;

    private List<groups> groupIds;

    private Button register;

    private ProgressDialog prg=null;

    private trainee te=new trainee();

    private groups gr=new groups();

    private groupdeatails gdt=new groupdeatails();

    private MobileServiceClient mclient;

    private MobileServiceTable<trainee> mtable;

    private MobileServiceTable<groups> gtable;

    private MobileServiceTable<groupdeatails> gdtable;

    private static  String item="";

    private TextView txtdateofstart2;

    private int myday,mymonth,myyear;

    private String choosenDate="";




    private void Reg(final trainee newTrainee, final groupdeatails newgdt)
    {
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg=new ProgressDialog(traineeActivity.this);

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
                    groups gl=(gtable.where().field("groupcode").eq(gdt.getGroupcode()).execute().get()).get(0);

                    int temp=Integer.parseInt(gl.getMax())-1;

                    gl.setMax(String.valueOf(temp));

                    gl=gtable.update(gl).get();

                    trainee te = mtable.insert(newTrainee).get();

                    groupdeatails gd = gdtable.insert(newgdt).get();

                    StaticVariables.SetTrainee(te);

                    StaticVariables.SetGroup(gl);

                    int x=9;
                } catch (Exception e)
                {
                    final String message=e.getMessage().toString();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(traineeActivity.this, message, Toast.LENGTH_SHORT).show();
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

    private void GroupSelect()
    {
        AsyncTask<Void,Void,List<groups>> task = new AsyncTask<Void, Void, List<groups>>()
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                prg=new ProgressDialog(traineeActivity.this);

                prg.setTitle("GroupSelect");

                prg.setIcon(R.drawable.loading);

                prg.setMessage("Searching for options...");

                prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                prg.show();
            }

            @Override
            protected List<groups> doInBackground(Void... voids)
            {
                try
                {
                    List<groups> myGroups = gtable.where().field("max").gt("0").execute().get();

                    return  myGroups;
                } catch (Exception e)
                {
                    final String message=e.getMessage().toString();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(traineeActivity.this, message, Toast.LENGTH_SHORT).show();
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

                for (int i = 0; i < lGroups.size(); i++)

                    ls.add(lGroups.get(i).getGroupcode().toString());

                ArrayAdapter<String> adapters= new ArrayAdapter<String>(traineeActivity.this,android.R.layout.simple_spinner_dropdown_item,ls);

                groupid.setAdapter(adapters);


            }
        }.execute();

    }

    protected Dialog onCreateDialog(int id)
    {


        return new DatePickerDialog(traineeActivity.this,DatePickListener,2019,4,3);//myyear,mymonth,myday);

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

            txtdateofstart2.setText(choosenDate);


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee);
        txtdateofstart2=findViewById(R.id.dateofstart2);
        groupid=(Spinner) findViewById(R.id.groupid);
        height=findViewById(R.id.height);
        weight=findViewById(R.id.weight);
        register=findViewById(R.id.register);
        choosenDate=myday + "/" + mymonth + "/" + myyear;
        java.util.Calendar c = java.util.Calendar.getInstance();

        try
        {
            mclient= new MobileServiceClient("https://shaharfinalproject.azurewebsites.net",traineeActivity.this);

            mtable=mclient.getTable(trainee.class);

            gtable=mclient.getTable(groups.class);

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

        GroupSelect();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
                String datetime = dateformat.format(c.getTime());
                te=new trainee();
                gdt=new groupdeatails();
                te.setUsern(StaticVariables.GetClient().getUsern());
                te.setDateofstart(txtdateofstart2.getText().toString());
                te.setHeight(height.getText().toString());
                te.setWeight(weight.getText().toString());
                gdt.setGroupcode(groupid.getSelectedItem().toString());
                gdt.setUsern(StaticVariables.GetClient().getUsern());
                gdt.setStartdate(datetime);
                gdt.setEnddate("");
                gdt.setGroupgrade("0");
                gdt.setTrainergrade("0");
                Reg(te,gdt);
                startActivity(new Intent(getApplicationContext(), Startapp.class));
            }
        });

        txtdateofstart2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog(666);
            }
        });

        height.setOnFocusChangeListener(new View.OnFocusChangeListener()
    {
        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            if(height.getText().toString().length()<3)
                height.setError("Insert Your Height in centimeter ");
        }
    });

        weight.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(weight.getText().toString().length()<1)
                    weight.setError("Insert Your Weight in kilograms ");
            }
        });


/*          groupid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                item= (String) parent.getItemAtPosition(position);

                Toast.makeText(UpdateTrainee.this, "You selected this group", Toast.LENGTH_SHORT).show();
            }
        });
*/
    }
}
