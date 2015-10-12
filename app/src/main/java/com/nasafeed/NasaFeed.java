package com.nasafeed;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;

public class NasaFeed extends AppCompatActivity {
    Handler handler;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_feed);
        handler = new Handler();
        refreshFromFeed();
    }

    // click method for refresh button
    public void onRefresh(View view) {
        refreshFromFeed();
    }

    // click method for Set Wallpaper button
    public void onSetWallpaper(View view) {
        Thread thread = new Thread() {
            public void run() {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(NasaFeed.this);
                try {
                    wallpaperManager.setBitmap(image);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NasaFeed.this, "Wallpaper set", Toast.LENGTH_SHORT).show();

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NasaFeed.this, "Error setting wallpaper", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        };
        thread.start();
    }

    public void refreshFromFeed() {
        final ProgressDialog dialog = ProgressDialog.show(
                this,
                "Loading",
                "Loading the image of the Day");
        final IotHandler iotHandler = new IotHandler();
        Thread thread = new Thread() {
            public void run() {
                iotHandler.processFeed();
                image = iotHandler.getImage();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        resetDisplay(iotHandler.getTitle(),
                                iotHandler.getDate(),
                                iotHandler.getImage(),
                                iotHandler.getDescription().toString());
                        dialog.dismiss();
                    }
                });
            }
        };
        thread.start();
    }

    /*
    class RetriveRssFeed extends AsyncTask<IotHandler, Void, Void> {

        protected  Void doInBackground(IotHandler... handlers) {
            final IotHandler handler = handlers[0];

            handler.processFeed();
            // UI should be updated in this thread.
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            return null;
        }
    }
    */
    public void resetDisplay(String title, String date, Bitmap image, String description) {
        TextView titleView = (TextView) findViewById(R.id.imageTitle);
        titleView.setText(title);

        TextView dateView = (TextView) findViewById(R.id.imageDate);
        dateView.setText(date);
        if (image != null) {
            ImageView imageView = (ImageView) findViewById(R.id.imageDisplay);
            // rescale image
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            float sacleHt = (float) size.x / image.getWidth();
            image = Bitmap.createScaledBitmap(image, size.x, (int) (image.getWidth() * sacleHt), true);
            imageView.setImageBitmap(image);
        }

        TextView descriptionView = (TextView) findViewById(R.id.imageDescription);
        descriptionView.setText(description);
    }
}
