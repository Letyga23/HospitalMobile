package com.example.hospitalmobile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class WorkingWithImage {
    //0.8мб = 819200
    //0.1мб = 819200
    private static final int MAX_SIZE_BYTES = 102400;


    public static String resizeBase64Image(String base64Image) {
        if (base64Image == null || base64Image.isEmpty())
            return "";

        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        int quality = 100; // Начальное качество JPEG
        while (imageBytes.length > MAX_SIZE_BYTES && quality >= 0) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            imageBytes = outputStream.toByteArray();
            Log.d("TAG", "Quality: " + quality + ", Size: " + imageBytes.length);
            quality -= 5;
        }

        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) {
            return "";
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public static Bitmap base64ToBitmap(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return null;
        }
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
