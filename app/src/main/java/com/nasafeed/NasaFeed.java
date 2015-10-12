package com.nasafeed;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class NasaFeed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_feed);
        IotHandler handler = new IotHandler();
        new RetriveRssFeed().execute(handler);
    }



    class RetriveRssFeed extends AsyncTask<IotHandler, Void, Void> {

        protected  Void doInBackground(IotHandler... handlers) {
            final IotHandler handler = handlers[0];
            handler.processFeed();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resetDisplay(handler.getTitle(), handler.getDate(), handler.getImage(), handler.getDescription().toString());
                }
            });
            return null;
        }
    }
    public void resetDisplay(String title, String date, Bitmap image, String description) {
        TextView titleView = (TextView)findViewById(R.id.imageTitle);
        titleView.setText(title);

        TextView dateView = (TextView)findViewById(R.id.imageDate);
        dateView.setText(date);
        if(image != null) {
            ImageView imageView = (ImageView) findViewById(R.id.imageDisplay);
            imageView.setImageBitmap(image);
        }

        TextView descriptionView = (TextView)findViewById(R.id.imageDescription);
        descriptionView.setText(description);
    }
}
