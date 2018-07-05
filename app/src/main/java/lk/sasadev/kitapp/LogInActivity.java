package lk.sasadev.kitapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {


    String email;
    String password;
    EditText emailText;
    EditText passwordText;
    Button loginButton;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    BroadcastReceiver internetConnectionBcr;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme();

        setContentView(R.layout.activity_log_in);

        firebaseAuth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.editTextEmail);
        passwordText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogIn);
        progressBar = findViewById(R.id.loginProgressBar);
        progressBar.setVisibility(View.GONE);


    }


    @Override
    protected void onStart() {
        super.onStart();

        checkInternetConnection();

        currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(getApplicationContext(),"user already signed in as " + currentUser.getEmail(), Toast.LENGTH_LONG).show();
            Intent i = new Intent(LogInActivity.this,DashBoardActivity.class);
            startActivity(i);
            finish();
        }
        //use current user  check not null  set things according to user

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = emailText.getText().toString().trim();
                password = passwordText.getText().toString().trim();

                hideKeyboard(view);
                if (validation(email,password)){
                    loginButton.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(email,password);
                }

            }
        });
    }

    private void hideKeyboard(View view) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(internetConnectionBcr);
    }

    private void checkInternetConnection() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
        builder.setMessage("Please turn on mobile data or Wifi")
                .setTitle("No Internet Connection");
        final AlertDialog dialog = builder.create();

        internetConnectionBcr = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = manager.getActiveNetworkInfo();

                if(ni!=null && ni.isConnectedOrConnecting() && dialog.isShowing()){
                    dialog.dismiss();
                }
                if(ni == null || !ni.isConnectedOrConnecting()){
                    dialog.show();
                }
            }
        };

        registerReceiver(internetConnectionBcr, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        /*ConnectivityManager connectivityManager= (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();*/



        /*if(!isConnected) {
            dialog.show();
        }
        if(isConnected){
            dialog.dismiss();
        }*/

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

    public boolean validation(String email, String password){
        boolean b=true;
        if(email.equals("")){
            emailText.setError("please enter your email!");
            emailText.requestFocus();
            b = false;
        }

        if(password.equals("")) {
            passwordText.setError("please enter password!");
            emailText.requestFocus();
            b = false;
        }
        /*if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("please enter a valid email!");
            emailText.requestFocus();
            b = false;
        }*/
        return b;
        };

    public void loginUser(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = firebaseAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(),"user successfully signed in" + currentUser.getEmail(), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(LogInActivity.this,DashBoardActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"failed to sign in", Toast.LENGTH_LONG).show();
                        }
                        if(task.isComplete()){
                            loginButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }


    }
