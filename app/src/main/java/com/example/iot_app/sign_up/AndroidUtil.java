package com.example.iot_app.sign_up;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


public class AndroidUtil {

   public static  void showToast(Context context,String message){
       Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

   /* public static void passUserModelAsIntent(Intent intent, HelperClass model){
       intent.putExtra("username",model.getUsername());
       intent.putExtra("phone",model.getPhoneNumber());
       intent.putExtra("userId",model.getUserId());
        intent.putExtra("fcmToken",model.getFcmToken());

    }

    public static HelperClass getUserModelFromIntent(Intent intent){
        HelperClass userModel = new HelperClass();
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setPhone(intent.getStringExtra("phone"));
        userModel.setUserId(intent.getStringExtra("userId"));
        userModel.setFcmToken(intent.getStringExtra("fcmToken"));
        return userModel;
    }

    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }*/
}
