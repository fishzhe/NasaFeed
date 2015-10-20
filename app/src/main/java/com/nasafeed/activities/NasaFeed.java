package com.nasafeed.activities;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import com.nasafeed.R;
import com.nasafeed.adapters.ImageFeedAdapter;
import com.nasafeed.domain.ImageContainer;
import com.nasafeed.handlers.IotHandler;

import java.io.IOException;

public class NasaFeed extends AppCompatActivity {
    Handler handler;
    Bitmap image;
    ImageFeedAdapter imageFeedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_feed);
        handler = new Handler();
        final ListView imageContainerView = (ListView)findViewById(R.id.image_container_list_view);



        imageFeedAdapter = new ImageFeedAdapter();
        imageContainerView.setAdapter(imageFeedAdapter);
        imageContainerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageFeedAdapter.setSelectedItemBackground(view, parent, position);
            }
        });
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
                    ImageContainer imageContainer = (ImageContainer)imageFeedAdapter.getSelectedItem();
                    wallpaperManager.setBitmap(imageContainer.getImage());
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
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final IotHandler iotHandler = new IotHandler(size);
        Thread thread = new Thread() {
            public void run() {
                iotHandler.processFeed();
                imageFeedAdapter.setImages(iotHandler.getImages());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageFeedAdapter.notifyDataSetChanged();
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
//    public void resetDisplay(String title, String date, Bitmap image, String description) {
//        TextView titleView = (TextView) findViewById(R.id.imageTitle);
//        titleView.setText(title);
//
//        TextView dateView = (TextView) findViewById(R.id.imageDate);
//        dateView.setText(date);
//        if (image != null) {
//            ImageView imageView = (ImageView) findViewById(R.id.imageDisplay);
//            imageView.setImageBitmap(image);
//        }
//
//        TextView descriptionView = (TextView) findViewById(R.id.imageDescription);
//        descriptionView.setText(description);
//    }
}
