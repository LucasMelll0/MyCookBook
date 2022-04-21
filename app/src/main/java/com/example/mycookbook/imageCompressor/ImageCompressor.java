package com.example.mycookbook.imageCompressor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageCompressor {

        private static int calculaInSampleSize(BitmapFactory.Options options, int reqHeight, int reqWidth){
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth){
                int halfHeight = height / 2;
                int halfWidth = width / 2;

                while((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth ){
                        inSampleSize *= 2;
                }
            }
            return inSampleSize;

        }

        public static Bitmap comprimeImagemEmBitMap(byte[] imagemEmBytes){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(imagemEmBytes, 0, imagemEmBytes.length, options);
            options.inSampleSize = calculaInSampleSize(options, 1080, 720);

            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeByteArray(imagemEmBytes, 0, imagemEmBytes.length, options);
        }

}
