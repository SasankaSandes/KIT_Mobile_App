package lk.sasadev.kitapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class LogInActivity extends AppCompatActivity {

    DatabaseReference driverDatabase;
    String email;
    String password;
    String username;
    Log log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme();

        setContentView(R.layout.activity_log_in);

        final EditText emailText = findViewById(R.id.editTextEmail);
        final EditText passwordText = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.buttonLogIn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailText!=null && passwordText!=null){
                    Log.e("debug", "a");
                    email = emailText.toString();
                    password =passwordText.toString();
                    driverDatabase.orderByChild("Email").equalTo(email).addChildEventListener(new ChildEventListener() {

                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            username = dataSnapshot.getKey();
                            Log.e("debug", "a");
                            Toast.makeText(LogInActivity.this, "User found :", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }


                    });


                }
            }
        });

    }

    public void goToRegistrationActivity(View view){
        Intent registrationIntent = new Intent(LogInActivity.this,RegistrationActivity1.class);
        startActivity(registrationIntent);
    }

    public void setTheme() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            //night_mode_yes set appTheme
            setTheme(R.style.AppThemeNoActionBar);
        } else {
            //night_mode_no set appThemeDay
            setTheme(R.style.AppThemeDayNoActionBar);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        driverDatabase = FirebaseDatabase.getInstance().getReference("Driver");
    }
}
