
package com.example.shaharproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class customEXlist extends BaseAdapter
{
    private Activity context=null;

    private String[] names=null;

    private String[] places=null;

    private String[] dates=null;

    public customEXlist(Activity context, String[] names, String[] places, String[] dates)
    {
        this.context = context;
        this.names = names;
        this.dates = dates;
        this.places=places;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public String getItem(int i) {
        return places[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {

        LayoutInflater inflater=context.getLayoutInflater();

        View rowView=inflater.inflate(R.layout.listview_exercises,null,true);

        TextView exename = rowView.findViewById(R.id.txtname);
        TextView adress =rowView.findViewById(R.id.txtadress);
        TextView exdate=rowView.findViewById(R.id.txtdate);

        exename.setText(names[i]);
        adress.setText(places[i]);
        exdate.setText(dates[i]);
        return rowView;
    }
}
