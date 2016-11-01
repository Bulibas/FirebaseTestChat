package com.example.pc.firebasetestchat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private Button addRoomButton;
    private EditText addRoomEditText;

    private ListView roomsListView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> roomsList=new ArrayList<>();

    private String username;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addRoomButton = (Button)findViewById(R.id.addRoomButton);
        addRoomEditText = (EditText)findViewById(R.id.searchRoomEditText);
        roomsListView = (ListView)findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.single_room, R.id.textView, roomsList);
        roomsListView.setAdapter(arrayAdapter);

        requestUsername();

        addRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<String, Object>();
                map.put(addRoomEditText.getText().toString(),"");
                root.updateChildren(map);
            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator roomsIterator =dataSnapshot.getChildren().iterator();

                while(roomsIterator.hasNext()) {
                    set.add(((DataSnapshot)roomsIterator.next()).getKey());
                }
                roomsList.clear();
                roomsList.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        roomsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView)view.findViewById(R.id.textView);
                Intent intent = new Intent(getApplicationContext(),ChatRoom.class);
                intent.putExtra("room",textView.getText().toString());
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }

    private void requestUsername() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter name: ");

        final EditText inputField = new EditText(this);

        builder.setView(inputField);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                username = inputField.getText().toString();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestUsername();
            }
        });
        builder.show();
    }
}
