package com.playground.ipondal.fraz.addcandidate;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class AddCandidateImageViewBindings {
    @BindingAdapter("app:bitmap")
    public static void setBitmap(ImageView imageView, Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }
}
