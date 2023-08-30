package com.naqib.my_chat_bot_app;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    EditText userInput;
    RecyclerView chatRecyclerView; // RecyclerView for displaying chat messages

    DatabaseHelper dbHelper;
    ChatAdapter chatAdapter; // Adapter for the RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInput = findViewById(R.id.userInput);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        dbHelper = new DatabaseHelper(this);
        chatAdapter = new ChatAdapter(); // Initialize the adapter

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatAdapter);

        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> onSendMessage());
    }

    public void onSendMessage() {
        String userMessage = userInput.getText().toString();
        if (!userMessage.isEmpty()) {
            String response = dbHelper.getResponse(userMessage.toUpperCase());
            addMessage("User", userMessage);
            if (response != null) {
                addMessage("Chatbot", response);
            } else {
                addMessage("Chatbot", "Sorry, I don't have a response for that \nPlease Click Teach In Menu for Teach Me.");
            }
            userInput.setText(""); // Clear the input field
        }
    }

    private void addMessage(String sender, String message) {
        chatAdapter.addMessage(new ChatMessage(sender, message));
        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
    }

    public void onSendMessage(View view) {
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem chat = menu.findItem(R.id.chat);
        MenuItem teach = menu.findItem(R.id.teach);
        chat.setVisible(false);
        teach.setVisible(true);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){
        int itemId = item.getItemId();
        if (itemId == R.id.teach) {
            Intent intent = new Intent(getApplicationContext(), TeachBot.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }
}