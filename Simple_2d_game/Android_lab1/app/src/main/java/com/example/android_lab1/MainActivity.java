package com.example.android_lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public void Start(View V) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        startActivityForResult(intent, 1);
    }

    public void Exit(View V) {
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                boolean result = data.getExtras().getBoolean("win");
                if (!result) {
                    Toast.makeText(getApplicationContext(), "You've lost :(",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "You've just won!" ,
                            Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "GAME_CANCELED",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
