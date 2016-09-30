package com.nasafeed.activities;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.os.Handler;
import android.widget.Toast;

import com.nasafeed.R;
import com.nasafeed.adapters.ImageFeedAdapter;
import com.nasafeed.handlers.IotHandler;

import java.io.IOException;

public class NasaFeed extends AppCompatActivity {
    Handler handler;
    ImageFeedAdapter imageFeedAdapter;
    ListView imageContainerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_feed);
        handler = new Handler();
        imageContainerView = (ListView)findViewById(R.id.image_container_list_view);

        imageFeedAdapter = new ImageFeedAdapter();
        imageContainerView.setAdapter(imageFeedAdapter);
        // long click to set wall paper
        imageContainerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onShowImageInfo(view);
                return;
            }
        });

        imageContainerView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onSetWallpaper(view);
                return true;
            }
        });
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFromFeed();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        refreshFromFeed();
    }

    public void onShowImageInfo(View view) {
        if (view instanceof LinearLayout) {
            View imageInfoContainer = view.findViewById(R.id.image_info_container);
            int visibility =
                    imageInfoContainer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            imageInfoContainer.setVisibility(visibility);
        }
    }
    // click method for Set Wallpaper button
    public void onSetWallpaper(View view){
        if (view instanceof LinearLayout){
            Log.d("view", "Is linear layout");
            ImageView imageView = (ImageView)((LinearLayout)view).getChildAt(0);
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
    }

    public void refreshFromFeed() {

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
                    }
                });
            }
        };
        thread.start();
    }
}
