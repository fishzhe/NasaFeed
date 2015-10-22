package com.nasafeed.activities;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    ListView imageContainerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_feed);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        handler = new Handler();
        imageContainerView = (ListView)findViewById(R.id.image_container_list_view);

        imageFeedAdapter = new ImageFeedAdapter();
        imageContainerView.setAdapter(imageFeedAdapter);
        // long click to set wall paper
        imageContainerView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onSetWallpaper(view);
                return true;
            }
        });
        // image view is in the 2 position of container(title, data, image, description)

        refreshFromFeed();
    }

    // click method for refresh button
    public void onRefresh(View view) {
        refreshFromFeed();
    }

    // click method for Set Wallpaper button
    public void onSetWallpaper(View view){
        // LinearLayout linearLayout = (LinearLayout)imageFeedAdapter.getSelectedItem();
        if (view instanceof LinearLayout){
            Log.d("view", "Is linear layout");
            ImageView imageView = (ImageView)((LinearLayout)view).getChildAt(2);
            Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
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
                Toast.makeText(NasaFeed.this, "Wallpaper set error", Toast.LENGTH_SHORT).show();
            }
        }
        // image view is in the 2 position of container(title, data, image, description)

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
                        imageFeedAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
            }
        };
        thread.start();
       // setImageLongClickListener();
    }

//    public void setImageLongClickListener(){
//        View imageContainerView = findViewById(R.id.image_container_list_view);
//        imageContainerView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                onSetWallpaper(v);
//                return true;
//            }
//        });
//    }
}
