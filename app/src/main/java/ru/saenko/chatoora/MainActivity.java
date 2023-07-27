package ru.saenko.chatoora;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static int MAX_MESSAGE_LENGTH = 100;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("messages");

    EditText editTextMessage;
    Button sendMessageButton;
    RecyclerView messagesRecycler;

    DataAdapter dataAdapter;

    ArrayList<String> messages = new ArrayList<>(100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitialVariablesMethod();

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String message = snapshot.getValue(String.class);
                messages.add(message);
                dataAdapter.notifyDataSetChanged();
                messagesRecycler.smoothScrollToPosition(messages.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void InitialVariablesMethod() {
        editTextMessage = findViewById(R.id.message_input_text);
        sendMessageButton = findViewById(R.id.send_message_button);
        messagesRecycler = findViewById(R.id.messages_recycler);

        messagesRecycler.setLayoutManager(new LinearLayoutManager(this));

        dataAdapter = new DataAdapter(this, messages);

        messagesRecycler.setAdapter(dataAdapter);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editTextMessage.getText().toString();
                if(message.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Введите сообщение!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(message.length() > MAX_MESSAGE_LENGTH) {
                    Toast.makeText(getApplicationContext(), "Сообщение слишком длинное!", Toast.LENGTH_SHORT).show();
                    return;
                }

                myRef.push().setValue(message);
                editTextMessage.setText("");
            }
        });
    }
}