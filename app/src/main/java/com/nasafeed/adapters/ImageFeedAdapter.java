package com.nasafeed.adapters;

import android.graphics.Color;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nasafeed.R;
import com.nasafeed.domain.ImageInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe Yu on 2015/10/18.
 */
public class ImageFeedAdapter extends BaseAdapter {
    private List<ImageInfo> imageInfos;
    private Point size;
    private View previous;
    public ImageFeedAdapter() {
        selected = 0;
        size = null;
        imageInfos = new ArrayList<>();
        previous = null;
    }

    public void setSize(Point size) {
        this.size = size;
    }

    private int selected;

    @Override
    public int getCount() {
        return imageInfos.size();
    }

    // if position less than image's size, return image, otherwise return null;
    @Override
    public Object getItem(int position) {
        return imageInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.image_container_view, parent, false);
        }
        ImageInfo imageInfo = imageInfos.get(position);

        TextView title = (TextView) convertView.findViewById(R.id.imageTitle);
        title.setText(imageInfo.getTitle());

        TextView pubDate = (TextView) convertView.findViewById(R.id.imageDate);
        pubDate.setText(imageInfo.getPubDate());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageDisplay);

        // use Picasso to load image.
        // should resize the image with device size.
        Picasso.with(parent.getContext())
                .load(imageInfo.getUrl())
                .placeholder(R.drawable.loading)
                .resize(this.size.y, this.size.x)
                .centerInside()
                .into(imageView);

        TextView description = (TextView) convertView.findViewById(R.id.imageDescription);
        description.setText(imageInfo.getDescription());

        return convertView;
    }

    public void setSelectedItemBackground(View view, AdapterView parent, int position) {
        if (previous != null) {
            previous.setBackgroundColor(Color.WHITE);
        }
        if (view != null) {
            view.setBackgroundColor(Color.GRAY);
        }
        previous = view;
    }

    // if have selected default value is 0, which means if no one is selected, the first one will
    // be set as wallpaper as default.
    public Object getSelectedItem() {
        return previous;
    }

    public List<ImageInfo> getImageInfos() {
        return imageInfos;
    }

    public void setImageInfos(List<ImageInfo> imageInfos) {
        this.imageInfos = imageInfos;
    }
}
