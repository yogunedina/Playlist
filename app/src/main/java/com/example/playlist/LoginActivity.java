package com.example.playlist;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class LoginActivity extends AppCompatActivity {
    Button b1;
    EditText ed1;
    EditText ed2;
    TextView attempt;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        b1 = findViewById(R.id.button);
        ed1 = findViewById(R.id.editText5);
        attempt = findViewById(R.id.textView);
        ed2 = findViewById(R.id.editText2);
        attempt.setVisibility(View.GONE);

        attempt.setText(Integer.toString(counter));

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                if(ed1.getText().toString().equals("admin") &&
                ed2.getText().toString().equals("Temp123")) {
                    Toast.makeText(getApplicationContext(),
                            "Successful Login. Now Redirecting...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class) ;
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "Wrong Credentials. Try Again", Toast.LENGTH_LONG).show();

                    attempt.setVisibility(View.VISIBLE);
                    attempt.setBackgroundColor(Color.RED);
                    counter--;
                    attempt.setText(Integer.toString(counter));

                    if (counter == 0) {
                        b1.setEnabled(false);
                    }
                }
            }
        });

    }
}
