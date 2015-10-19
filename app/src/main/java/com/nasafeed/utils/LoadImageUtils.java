package com.nasafeed.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by Zhe Yu on 2015/10/14.
 * <p/>
 * refer to: http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
 */
public class LoadImageUtils {
    public static Bitmap decodeSampledBitmapFromStream(InputStream inputStream, Rect rect,
                                                       int reqWidth, int reqHeight) throws IOException {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // decodeStream method will consume data, then the second decodeStream @ line35 wont work
        byte[] imageData = streamToArray(inputStream);
        BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);

        return bitmap;
    }

    // convert inputStream to byte array
    public static byte[] streamToArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer, 0, buffer.length)) > 0) {
            baos.write(buffer, 0, len);
        }
        byte[] imageData = baos.toByteArray();
        baos.close();
        return imageData;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
