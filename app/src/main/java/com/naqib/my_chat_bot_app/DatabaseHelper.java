package com.naqib.my_chat_bot_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chatbot.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "chat_responses";
    private static final String COLUMN_QUESTION = "question";
    private static final String COLUMN_RESPONSE = "response";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_QUESTION + " TEXT PRIMARY KEY, "
                + COLUMN_RESPONSE + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String getResponse(String question) {
        String response = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_RESPONSE + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_QUESTION + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{question});
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_RESPONSE);
            if (columnIndex != -1) {
                response = cursor.getString(columnIndex);
            } else {
                Log.e("DatabaseHelper", "Column not found: " + COLUMN_RESPONSE);
            }
        }

        cursor.close();
        db.close();
        return response;
    }
}
