package by.bsuir.shop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ImageLoder {

    public static void loadImageOnImageView(ImageView imageView,String imgUrl){

        Picasso.get().load(imgUrl).into(imageView);

    }


    public static byte[] loadBitmap(String url){
        try {
            Bitmap bmp = Picasso.get().load(url).get();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            bmp.recycle();
            return byteArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void loadFromBytes(ImageView imageView,byte[] img){
        Bitmap bmp = BitmapFactory.decodeByteArray(img, 0, img.length);
        imageView.setImageBitmap(bmp);
    }
}
