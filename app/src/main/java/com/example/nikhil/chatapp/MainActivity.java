package com.example.nikhil.chatapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.format.DateFormat;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class MainActivity extends AppCompatActivity {
    private static int SIGN_IN_REQUEST_CODE=1;
    DatabaseReference db;
    RelativeLayout activity_main;
    EmojiconEditText emojiconEditText;
    ImageView emoji_button,submit_button;
    EmojIconActions emojiIconActions;
    ListView listofmsgs;
    ArrayList<ChatMessage>ar;



    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_signout)
        {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                 Snackbar.make(activity_main,"You have been signed out",Snackbar.LENGTH_LONG).show();
                 finish();
                }
            });
        }
        return true;
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;

    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SIGN_IN_REQUEST_CODE)
        {
        if(resultCode==RESULT_OK)
        {
            Snackbar.make(activity_main,"Successfuly signed in WELCOME",Snackbar.LENGTH_LONG).show();
            displaymessage();
        }
        else
        {
            Snackbar.make(activity_main,"We could not signed u in",Snackbar.LENGTH_LONG).show();
            finish();
        }
        }
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity_main=findViewById(R.id.main_activity);
        listofmsgs=findViewById(R.id.listofmsgs);
        db= FirebaseDatabase.getInstance().getReference();
        ar=new ArrayList<ChatMessage>();
        emoji_button=(ImageView)findViewById(R.id.emoji_button);
        submit_button=(ImageView)findViewById(R.id.submit_button);
        emojiconEditText=(EmojiconEditText)findViewById(R.id.Emoji_text);
        emojiIconActions=new EmojIconActions(getApplicationContext(),activity_main,emoji_button,emojiconEditText);
        emojiIconActions.ShowEmojicon();
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(emojiconEditText.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                emojiconEditText.setText(" ");
                emojiconEditText.requestFocus();
            }
        });
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(activity_main,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_LONG).show();

        }
        displaymessage();
    }

    private void displaymessage() {
       db.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
                   for(DataSnapshot chatSnapshot:dataSnapshot.getChildren()) {
                       ChatMessage a = (ChatMessage)chatSnapshot.getValue(ChatMessage.class);
                       ar.add(a);
                       ChatAdapter adapter = new ChatAdapter(MainActivity.this, ar);
                       listofmsgs.setAdapter(adapter);
                   }}

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });


    }
}
