package com.example.tt;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tt.data.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class chat extends Activity {
    private ChatAdapter chatAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;

    Intent intent;
    private boolean side = false;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private ChatMessage chatmessage;
    String sendText ;
    ChatData chatData;
    User user;
    String room;
    String name;
    TextView chat_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_chat);
        chatData = new ChatData();
        room = intent.getStringExtra("room_name");
        //room = "test";
        user = User.getInstance();
        name = user.getUsername();
        chatmessage = new ChatMessage(false, "");

        chat_title = findViewById(R.id.chat_title);
        chat_title.setText(room);

        buttonSend = (Button) findViewById(R.id.buttonSend);

        listView = (ListView) findViewById(R.id.listView1);

        chatAdapter = new ChatAdapter(getApplicationContext(), R.layout.item_message_received);
        listView.setAdapter(chatAdapter);

        chatText = (EditText) findViewById(R.id.chatText);
        chatText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatAdapter);

        chatAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatAdapter.getCount() - 1);
            }
        });

        databaseReference.child(room).addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                chatData = dataSnapshot.getValue(ChatData.class);  // chatData를 가져오고
                if(chatData.getUserName().equals(name)) {
                    side = false;
                }
                else{
                    side = true;
                }
                chatmessage = new ChatMessage(side, chatData.getUserName() + " : " + chatData.getMessage());
                chatAdapter.add(chatmessage);  // adapter에 추가합니다.
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        chatData.setUserName(name);
        chatData.setMessage("attended");
        databaseReference.child(room).push().setValue(chatData);


    }

    private boolean sendChatMessage(){
        sendText =chatText.getText().toString();

        //chatAdapter.add(chatmessage);
        chatData.setUserName(name);
        chatData.setMessage(sendText);
        databaseReference.child(room).push().setValue(chatData);
        chatText.setText("");
        return true;
    }

}