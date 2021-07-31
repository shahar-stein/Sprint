package com.example.shaharproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogOut extends baseActivity
{
    private Button yes,no;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);

        yes=findViewById(R.id.yes);
        no=findViewById(R.id.no);

        no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(getApplicationContext(),Startapp.class));

            }
        });

        yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                StaticVariables.SetClient(null);
                StaticVariables.SetTrainer(null);
                StaticVariables.SetTrainee(null);
                startActivity(new Intent(getApplicationContext(),Startapp.class));

            }
        });

    }
}
