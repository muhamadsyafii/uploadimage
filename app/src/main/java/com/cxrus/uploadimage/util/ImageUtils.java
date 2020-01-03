package com.cxrus.uploadimage.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtils {

    public static String encodeToBase64(String path) {
        if (path == null) return "";
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return encodeToBase64(bitmap);
    }

    public static String encodeToBase64(Bitmap bitmap) {
        String base64 = "";
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            byte[] imageByteArray = stream.toByteArray();
            base64 = Base64.encodeToString(imageByteArray, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64;
    }

    public static Bitmap decodeBase64(String base64) {
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}