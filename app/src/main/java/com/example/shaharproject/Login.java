package com.example.shaharproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Login extends baseActivity {

    private Button btnsend;

    private EditText edituser, editpass;

    private MobileServiceClient mclient;

    private MobileServiceTable<tblclients> mtable;

    private MobileServiceTable<trainee> traineetable;

    private MobileServiceTable<trainers> trainertable;

    private MobileServiceTable<groups> groupstable;

    private MobileServiceTable<groupdeatails> groupdeatailstable;

    private MobileServiceTable<exercises> exerciselst;

    private MobileServiceTable<competitions> racelst;

    private MobileServiceTable<competitiondeatails> raceDetailslst;

    private ProgressDialog prg = null;


    private void Login(final String name,final String pass) {
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                prg = new ProgressDialog(Login.this);

                prg.setTitle("Login");

                prg.setIcon(R.drawable.loading);

                prg.setMessage("Searching Data....");

                prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                prg.show();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    List<tblclients> myclient = mtable.where().field("usern").eq(name)
                            .and().field("pass").eq(pass).execute().get();

                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_YEAR,1);
                    SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
                    String tommorow=dateformat.format(c.getTime());
                    String trainername;


                    if (myclient.size() != 0) {
                        StaticVariables.SetClient(myclient.get(0));
                        if(myclient.get(0).getRoll().equals("Trainer"))
                        {
                            StaticVariables.SetTrainer(trainertable.where().field("usern").eq(StaticVariables.GetClient().getUsern()).execute().get().get(0));
                            StaticVariables.SetGroup(groupstable.where().field("groupcode").eq(StaticVariables.GetTrainer().getGroupid()).execute().get().get(0));
                            trainername=StaticVariables.GetClient().getUsern();
                        }
                        else
                        {
                            StaticVariables.SetTrainee(traineetable.where().field("usern").eq(StaticVariables.GetClient().getUsern()).execute().get().get(0));
                            groupdeatails g  = groupdeatailstable.where().field("usern").eq(StaticVariables.GetClient().getUsern()).and().field("enddate").eq("").execute().get().get(0);
                            StaticVariables.SetGroup(groupstable.where().field("groupcode").eq(g.getGroupcode()).execute().get().get(0));

                            trainername=trainertable.where().field("groupid").eq(StaticVariables.GetGroup().getGroupcode()).execute().get().get(0).getUsern();

                        }
                        List<exercises> exl=(exerciselst.where().field("trainercode").eq(trainername).execute().get());
                        for (int i=0; i<exl.size();i++)
                        {
                            if(dateformat.parse(exl.get(i).getExercisedate()).equals(dateformat.parse(tommorow)))
                            {
                                int id=3;

                                Intent intent= new Intent(getApplicationContext(), Login.class);

                                intent.putExtra("msg","You Have Exercise Tommorow!!");

                                String contentitle="Exercises Alert";

                                String contentext=getResources().getString(R.string.exnot);

                                NotificationHelp.CreateHeadUpNotification(getApplicationContext(),intent,contentitle,contentext,id);
                            }
                        }

                        return true;
                    }
                    return false;

                } catch (Exception e) {
                    final String message = e.getMessage().toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean answer) {
                super.onPostExecute(answer);

                prg.dismiss();

                if (answer == true) {
                    startActivity(new Intent(getApplicationContext(), Startapp.class));

                } else
                    Toast.makeText(Login.this, "I dont know you", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnsend = findViewById(R.id.btnsend);

        edituser = findViewById(R.id.usern);

        editpass = findViewById(R.id.editpass);

        new Permission(this).verifyPermissions();

        try {
//            mclient = new MobileServiceClient("https://shaharfinalproject.azurewebsites.net", Login.this);

            mclient = new MobileServiceClient("https://shaharfinalproject.azurewebsites.net", Login.this);

            mtable = mclient.getTable(tblclients.class);

            traineetable = mclient.getTable(trainee.class);

            trainertable = mclient.getTable(trainers.class);

            groupstable = mclient.getTable(groups.class);

            groupdeatailstable = mclient.getTable(groupdeatails.class);

            exerciselst = mclient.getTable(exercises.class);

            racelst = mclient.getTable(competitions.class);

            raceDetailslst = mclient.getTable(competitiondeatails.class);

        }
        catch (MalformedURLException e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

        //---------------------------------------------
        btnsend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Login(edituser.getText().toString(),editpass.getText().toString());
            }
        });
    }
}
