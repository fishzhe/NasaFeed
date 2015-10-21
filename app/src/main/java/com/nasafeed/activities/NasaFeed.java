package com.nasafeed.activities;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.os.Handler;
import android.widget.Toast;

import com.nasafeed.R;
import com.nasafeed.adapters.ImageFeedAdapter;
import com.nasafeed.domain.ImageInfo;
import com.nasafeed.handlers.IotHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

public class NasaFeed extends AppCompatActivity {
    Handler handler;
    ImageFeedAdapter imageFeedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_feed);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
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
        ImageInfo imageInfo = (ImageInfo)imageFeedAdapter.getSelectedItem();
        SetWallpaperTask task = new SetWallpaperTask();
        task.execute(imageInfo);
    }

    public void refreshFromFeed() {
        final ProgressDialog dialog = ProgressDialog.show(
                this,
                "Loading",
                "Loading the image of the Day");
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        final IotHandler iotHandler = new IotHandler();
        Thread thread = new Thread() {
            public void run() {
                iotHandler.processFeed();
                imageFeedAdapter.setSize(size);
                imageFeedAdapter.setImageInfos(iotHandler.getImageInfos());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //imageFeedAdapter.notify();
                        imageFeedAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
            }
        };
        thread.start();

    }

    class SetWallpaperTask extends AsyncTask<ImageInfo, Void, Void> {

        @Override
        protected Void doInBackground(ImageInfo... params) {
            final WallpaperManager wallpaperManager = WallpaperManager.getInstance(NasaFeed.this);

            final ImageInfo imageInfo = params[0];
            //Bitmap image = imageInfo.getImage();
            final Target t = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    try {
                        Log.d("setBitmap", "" + bitmap.getHeight());
                        wallpaperManager.setBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NasaFeed.this, "Error setting wallpaper", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(getApplicationContext()).load(imageInfo.getUrl()).into(t);
                    Toast.makeText(NasaFeed.this, "Wallpaper set", Toast.LENGTH_SHORT).show();

                }
            });
            return null;
        }
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
