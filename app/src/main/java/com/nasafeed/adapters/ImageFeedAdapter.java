package com.nasafeed.adapters;

import android.graphics.Point;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nasafeed.R;
import com.nasafeed.domain.ImageContainer;
import com.nasafeed.handlers.IotHandler;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe Yu on 2015/10/18.
 */
public class ImageFeedAdapter extends BaseAdapter{
    private List<ImageContainer> images = new ArrayList<ImageContainer>();

    public ImageFeedAdapter(){}

    @Override
    public int getCount() {
        return images.size();
    }
    // if position less than image's size, return image, otherwise return null;
    @Override
    public Object getItem(int position) {
        return images.get(position);
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

        ImageContainer image = images.get(position);

        TextView title = (TextView)convertView.findViewById(R.id.imageTitle);
        title.setText(image.getTitle());

        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageDisplay);
        imageView.setImageBitmap(image.getImage());

        TextView pubDate = (TextView)convertView.findViewById(R.id.imageDate);
        pubDate.setText(image.getPubDate());

        TextView description = (TextView)convertView.findViewById(R.id.imageDescription);
        description.setText(image.getDescription());

        return convertView;
    }

    public List<ImageContainer> getImages() {
        return images;
    }

    public void setImages(List<ImageContainer> images) {
        this.images = images;
    }
}
