package com.example.pc.firebasetestchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoom extends AppCompatActivity {
    private Button btnSend;
    private EditText msgEditText;
    private TextView msgTextView;

    private String username;
    private String chatroom;

    private DatabaseReference root;
    private String tempKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        btnSend = (Button)findViewById(R.id.btnSend);
        msgEditText = (EditText)findViewById(R.id.msgEdtiText);
        msgTextView = (TextView)findViewById(R.id.msgTextView);

        username = getIntent().getExtras().get("username").toString();
        chatroom = getIntent().getExtras().get("room").toString();

        setTitle("Room: " + chatroom);

        root = FirebaseDatabase.getInstance().getReference().child(chatroom);



        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<String, Object>();
                tempKey = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference msgRoot = root.child(tempKey);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("name", username);
                map2.put("msg", msgEditText.getText().toString());

                msgRoot.updateChildren(map2);
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            //when app conects with the db for the first time
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);
            }

            //when child has new value
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            private String otherUsername;
            private String newMessage;
            private void appendChatConversation(DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while(iterator.hasNext()) {
                    newMessage = (String)((DataSnapshot)iterator.next()).getValue();
                    otherUsername = (String)((DataSnapshot)iterator.next()).getValue();

                    msgTextView.append(otherUsername + "-" + newMessage + "\n");
                }
            }
        });
    }

}
