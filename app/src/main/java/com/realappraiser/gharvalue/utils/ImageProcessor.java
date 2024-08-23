package com.realappraiser.gharvalue.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class ImageProcessor {

    private static final String TAG = "ImageProcessor";
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2 MB in bytes

    public File compressImage(File imgFile, Context context) {
        File compressedFile = new File(context.getCacheDir(), "compressed_image.jpg");
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 800, 800); // Example width and height
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);

            // Compress and save to file
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                try (OutputStream os = Files.newOutputStream(compressedFile.toPath())) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, os);
                    os.flush();
                }
            }

            // Check file size and adjust if necessary
            if (compressedFile.length() > MAX_FILE_SIZE) {
                Log.w(TAG, "Compressed image exceeds 2 MB, further compression required.");
                compressedFile = resizeImage(compressedFile);
            }

        } catch (IOException e) {
            Log.e(TAG, "Error compressing image: ", e);
        }
        return compressedFile;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private File resizeImage(File file) {
        // Resize the image to ensure it's under 2 MB
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            File resizedFile = new File(file.getParent(), "resized_image.jpg");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                try (OutputStream os = Files.newOutputStream(resizedFile.toPath())) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
                    os.flush();
                }
            }

            return resizedFile;
        } catch (IOException e) {
            Log.e(TAG, "Error resizing image: ", e);
            return file; // Return original file in case of error
        }
    }

}
