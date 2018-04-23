package com.example.lukehogan.friendtrack;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class CustomAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Users> friendList;


    public CustomAdapter(Context context,ArrayList<Users> friendList) {
        this.context = context;
        this.friendList = friendList;
    }

    @Override
    public int getCount(){
        return friendList.size();
    }
    @Override
    public Object getItem(int pos){
        return friendList.get(pos);
    }
    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){


        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_custom_layout,parent,false);
        }

        TextView myText = (TextView) convertView.findViewById(R.id.myText);
        TextView myText2 = (TextView) convertView.findViewById(R.id.myEmail);
        ImageView myImage = (ImageView) convertView.findViewById(R.id.image_neutral);

        Users friend = (Users) friendList.get(pos);

        myText.setText(friendList.get(pos).getName());
        myText2.setText(friendList.get(pos).getEmail());
        PicassoClient.downloadUrl(context,friendList.get(pos).getUrl(),myImage);
        //myImage.setImageResource(friend.getUrl());
        // );

        return convertView;
    }
}