package com.example.listofgoods;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ProductActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_item);

        Intent intent = getIntent();
        if (intent.getExtras().getBoolean("create") == false) {
            EditText editName = (EditText) findViewById(R.id.editName);
            editName.setText(intent.getExtras().getString("name"));
            EditText editStore = (EditText) findViewById(R.id.editStore);
            editStore.setText(intent.getExtras().getString("store"));
            EditText editDate = (EditText) findViewById(R.id.editDate);
            editDate.setText(intent.getExtras().getString("date"));
            ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
            imageButton.setImageResource(intent.getExtras().getInt("image"));
            iter = intent.getExtras().getInt("image");
        }
        else {
            Button button = (Button) findViewById(R.id.button2);
            button.setVisibility(View.GONE);
            ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
            imageButton.setImageResource(iter);
        }
    }

    public void onCancelClick(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }

    private int iter = R.drawable.p0;
    private int max = R.drawable.p4;
    public void onImageButtonClick(View v) {
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        if (iter < max) iter++;
        else iter = R.drawable.p0;
        imageButton.setImageResource(iter);
    }

    public void onButton1Click(View v) {
        Intent data = new Intent();
        data.putExtra("delete",false);
        EditText editName = (EditText) findViewById(R.id.editName);
        data.putExtra("name",editName.getText().toString());
        EditText editStore = (EditText) findViewById(R.id.editStore);
        data.putExtra("store",editStore.getText().toString());
        EditText editDate = (EditText) findViewById(R.id.editDate);
        data.putExtra("date",editDate.getText().toString());
        data.putExtra("image", iter);
        setResult(RESULT_OK,data);
        finish();
    }

    public void onButton2Click(View v) {
        Intent data = new Intent();
        data.putExtra("delete",true);
        setResult(RESULT_OK,data);
        finish();
    }
}
