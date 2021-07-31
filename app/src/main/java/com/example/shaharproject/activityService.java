package com.example.shaharproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class activityService extends baseActivity
{

    private static Intent itplay=null;

    private GridLayout grd=null;

    private static boolean flag=false;

    private  static String sonong="";


    private  void Check(final GridLayout mygrd)
    {
        for (int i = 0; i < mygrd.getChildCount(); i++)
        {
            final CardView cardView=(CardView)mygrd.getChildAt(i);

            cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    CardView temp;

                    CardView cc = (CardView)v;

                    for(int k=0; k<mygrd.getChildCount();k++ )
                    {
                        temp =(CardView)mygrd.getChildAt(k);

                        temp.setCardBackgroundColor(Color.WHITE);

                    }

                    if(flag==true){

                        stopService(itplay);

                    }

                    ViewGroup viewGroup = ((ViewGroup)cc.getChildAt(0));

                    for(int j=0;j<viewGroup.getChildCount();j++)
                    {
                        if (viewGroup.getChildAt(j) instanceof TextView)
                        {
                            String mytext = ((TextView) viewGroup.getChildAt(j)).getText().toString();

                            if(!mytext.equals(StaticVariables.GetMyMusic())){

                                StaticVariables.setMusic(mytext);

                                itplay = new Intent(activityService.this, PlayService.class);

                                startService(itplay);

                                flag=true;

                                cc.setCardBackgroundColor(Color.YELLOW);

                            }

                            else
                            {

                                flag=false;

                            }

                        }

                    }

                }

            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service2);

        grd=findViewById(R.id.grd);

        Check(grd);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if(itplay!= null)
            stopService(itplay);
    }
}

