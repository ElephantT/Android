package com.example.tasklist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class TaskActivity extends AppCompatActivity {

    EditText nameBox;
    EditText yearBox;
    EditText costBox;
    Button delButton;
    Button saveButton;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        nameBox = (EditText) findViewById(R.id.name);
        yearBox = (EditText) findViewById(R.id.year);
        costBox = (EditText) findViewById(R.id.cost);
        delButton = (Button) findViewById(R.id.deleteButton);
        saveButton = (Button) findViewById(R.id.saveButton);

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }

        if (userId > 0) {
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                    DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();
            nameBox.setText(userCursor.getString(1));
            yearBox.setText(String.valueOf(userCursor.getInt(2)));
            costBox.setText(String.valueOf(userCursor.getInt(3)));
            userCursor.close();
        } else {
            delButton.setVisibility(View.GONE);
        }
    }

    public void save(View view){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, nameBox.getText().toString());
        cv.put(DatabaseHelper.COLUMN_YEAR, Integer.parseInt(yearBox.getText().toString()));
        cv.put(DatabaseHelper.COLUMN_COST, Integer.parseInt(costBox.getText().toString()));

        if (userId > 0) {
            db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "="
                    + String.valueOf(userId), null);
        } else {
            db.insert(DatabaseHelper.TABLE, null, cv);
        }
        toMainPage();
    }
    public void delete(View view){
        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
        toMainPage();
    }
    private void toMainPage(){
        db.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
