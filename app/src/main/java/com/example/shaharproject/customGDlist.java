
package com.example.shaharproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class customGDlist extends BaseAdapter
{
        private Activity context=null;

        private String[] usernames=null;

        private String[] pics=null;

        public customGDlist(Activity context, String[] names, String[] pics)
        {
            this.context = context;
            this.usernames = names;
            this.pics = pics;
        }

        @Override
        public int getCount() {
            return usernames.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

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


    @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {

            LayoutInflater inflater=context.getLayoutInflater();

            View rowView=inflater.inflate(R.layout.listview_trainee_group,null,true);

            TextView txtgrade = rowView.findViewById(R.id.txtname);
            CircleImageView img =rowView.findViewById(R.id.img);

            txtgrade.setText(usernames[i]);
            loadImage(pics[i],img);

            return rowView;
        }
    }
