package com.naqib.my_chat_bot_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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
    RecyclerView chatRecyclerView;

    DatabaseHelper dbHelper;
    ChatAdapter chatAdapter;
    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = openOrCreateDatabase("chatbot.db", MODE_PRIVATE, null);
        String createTableQuery = "CREATE TABLE IF NOT EXISTS chat_responses ("
                + "question TEXT PRIMARY KEY, "
                + "response TEXT)";
        mDatabase.execSQL(createTableQuery);

        userInput = findViewById(R.id.userInput);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        dbHelper = new DatabaseHelper(this);
        chatAdapter = new ChatAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatAdapter);

        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> onSendMessage());

        insertData();
    }

    public void onSendMessage() {
        String userMessage = userInput.getText().toString();
        if (!userMessage.isEmpty()) {
            if (containsInappropriateContent(userMessage)) {
                addMessage("User", userMessage);
                addMessage("Chatbot", "Sorry, you mentioned content that I'm not allowed to discuss.");
            } else {
                String response = dbHelper.getResponse(userMessage.toUpperCase());
                addMessage("User", userMessage);
                if (response != null) {
                    addMessage("Chatbot", response);
                } else {
                    addMessage("Chatbot", "Sorry, I don't have a response for that.\nPlease click Teach in the menu to teach me.");
                }
            }
            userInput.setText("");
        }
    }

    private boolean containsInappropriateContent(String message) {
        String[] inappropriateKeywords = {"porn", "adult", "explicit", "sexual", "sex"};
        for (String keyword : inappropriateKeywords) {
            if (message.toLowerCase().contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private void addMessage(String sender, String message) {
        chatAdapter.addMessage(new ChatMessage(sender, message));
        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem chat = menu.findItem(R.id.chat);
        MenuItem teach = menu.findItem(R.id.teach);
        chat.setVisible(false);
        teach.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.teach) {
            Intent intent = new Intent(getApplicationContext(), TeachBot.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSendMessage(View view) {
    }

    private void insertData() {
        String[] questions = {
                "hi", "hello", "hey",
                "how are you?","how are you",
                "what is your function", "how are you doing", "what's your function", "what your function", "your function?",
                "what's your name", "what is your name", "Your name?", "who are you",
                "tell me a joke", "say something funny",
                "how old are you", "your age?",
                "what is the meaning of life",
                "how does the internet work"
        };
        String[] answers = {
                "Hi!", "Hello!", "Hey there!",
                "I'm doing well, thank you for asking.","I'm doing well, thank you for asking.",
                "I'm just a chatbot, but I'm here to help!","I'm just a chatbot, but I'm here to help!",
                "I'm just a chatbot, but I'm here to help!","I'm just a chatbot, but I'm here to help!","I'm just a chatbot, but I'm here to help!",
                "I'm a simple chatbot. Call me ChatBot.","I'm a simple chatbot. Call me ChatBot.",
                "I'm a simple chatbot. Call me ChatBot.","I'm a simple chatbot. Call me ChatBot.",
                "Why don't scientists trust atoms? Because they make up everything!",
                "Sure, here's one: Why did the scarecrow win an award? Because he was outstanding in his field!",
                "I'm ageless, I'm a creation of code!","I'm ageless, I'm a creation of code!",
                "The answer to the ultimate question of life, the universe, and everything is 42.",
                "The internet is a global network of interconnected computers that communicate using standardized protocols."
        };

        for (int i = 0; i < questions.length; i++) {
            String Q = questions[i].toUpperCase();
            String A = answers[i];

            boolean questionExists = checkQuestionExists(Q);

            if (!questionExists) {
                try {
                    String insertSQL = "INSERT INTO chat_responses (question, response) " +
                            "VALUES (?, ?)";

                    mDatabase.execSQL(insertSQL, new String[]{Q, A});
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private boolean checkQuestionExists(String Q) {
        String query = "SELECT * FROM chat_responses WHERE question = ?";
        try (Cursor cursor = mDatabase.rawQuery(query, new String[]{Q})) {
            return cursor.getCount() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}