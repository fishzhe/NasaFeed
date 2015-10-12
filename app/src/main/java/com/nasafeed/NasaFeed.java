package com.nasafeed;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
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
            // UI should be updated in this thread.
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
            // rescale image
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            float sacleHt = (float)size.x / image.getWidth();
            image = Bitmap.createScaledBitmap(image, size.x, (int)(image.getWidth() * sacleHt), true);
            imageView.setImageBitmap(image);
        }

        TextView descriptionView = (TextView)findViewById(R.id.imageDescription);
        descriptionView.setText(description);
    }
}
