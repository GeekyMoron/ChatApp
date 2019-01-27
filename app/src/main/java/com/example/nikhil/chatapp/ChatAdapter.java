package com.example.nikhil.chatapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.library.bubbleview.BubbleTextView;

import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class ChatAdapter extends ArrayAdapter<ChatMessage> {
    private Activity context;
    private List<ChatMessage>listofmsgs;

    public ChatAdapter(Activity context,List<ChatMessage>listofmsgs) {
        super(context,R.layout.list_item,listofmsgs);
        this.context=context;
        this.listofmsgs=listofmsgs;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater lf=context.getLayoutInflater();
        View v=lf.inflate(R.layout.list_item,null,true);
        TextView msguser,msgtext,msgtime;
        msguser=v.findViewById(R.id.msguser);
        msgtext=(BubbleTextView)v.findViewById(R.id.msgtext);
        msgtime=v.findViewById(R.id.msgtime);
        ChatMessage message= listofmsgs.get(position);
        msgtext.setText(message.getMessageText());
        msguser.setText(message.getMessageUser());
        msgtime.setText(DateFormat.format("dd:MM:yyyy (HH:mm:ss)",message.getMessageTime()));
        return v;
    }
}

