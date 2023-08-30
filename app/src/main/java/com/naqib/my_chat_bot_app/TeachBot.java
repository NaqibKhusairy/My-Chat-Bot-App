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
import android.widget.TextView;
import android.widget.Toast;

public class TeachBot extends AppCompatActivity {
    EditText questionEditText, answerEditText;
    Button sendQuestionButton, sendAnswerButton;
    TextView HiddenTV;
    SQLiteDatabase mDatabase;
    String Q, A;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach_bot);

        mDatabase = openOrCreateDatabase("chatbot.db", MODE_PRIVATE, null);
        String createTableQuery = "CREATE TABLE IF NOT EXISTS  chat_responses ("
                + "question"+ " TEXT PRIMARY KEY, "
                + "response" + " TEXT)";
        mDatabase.execSQL(createTableQuery);

        questionEditText = findViewById(R.id.userInput);
        sendQuestionButton = findViewById(R.id.sendButton);
        answerEditText = findViewById(R.id.userInput2);
        sendAnswerButton = findViewById(R.id.sendButton2);
        HiddenTV = findViewById(R.id.tv1);

        answerEditText.setVisibility(View.GONE);
        sendAnswerButton.setVisibility(View.GONE);
        HiddenTV.setVisibility(View.GONE);

        sendQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Q = questionEditText.getText().toString();
                if (Q.equals("")) {
                    questionEditText.setError("Please Enter Your Question");
                } else {

                    boolean questionExists = checkQuestionExists(Q);

                    if (questionExists) {
                        Toast.makeText(TeachBot.this, "Dah Ada", Toast.LENGTH_SHORT).show();
                    } else {
                        Q = Q.toUpperCase();
                        HiddenTV.setText(Q);
                        questionEditText.setText("");
                        questionEditText.setVisibility(View.GONE);
                        sendQuestionButton.setVisibility(View.GONE);
                        answerEditText.setVisibility(View.VISIBLE);
                        sendAnswerButton.setVisibility(View.VISIBLE);
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
        });

        sendAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                A = answerEditText.getText().toString();
                Q = HiddenTV.getText().toString();
                if (A.equals("")) {
                    answerEditText.setError("Please Enter The Answer");
                } else {
                    questionEditText.setVisibility(View.VISIBLE);
                    sendQuestionButton.setVisibility(View.VISIBLE);
                    answerEditText.setText("");
                    answerEditText.setVisibility(View.GONE);
                    sendAnswerButton.setVisibility(View.GONE);
                    try {
                        String insertSQL = "INSERT INTO chat_responses (question, response) " +
                                "VALUES (?, ?)";

                        mDatabase.execSQL(insertSQL, new String[]{Q, A});
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem chatMenuItem = menu.findItem(R.id.chat);
        MenuItem teachMenuItem = menu.findItem(R.id.teach);
        chatMenuItem.setVisible(true);
        teachMenuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.chat) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
