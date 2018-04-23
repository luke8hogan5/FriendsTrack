package com.example.lukehogan.friendtrack;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoClient {

    public static void downloadUrl(Context context, String url, ImageView img){
        if(url != null){

            Picasso.with(context).load(url).placeholder(R.drawable.neutral).into(img);
        }
        else{
            Picasso.with(context).load(R.drawable.neutral).into(img);
        }
    }
}

