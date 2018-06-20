package lk.sasadev.kitapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Window;
import android.view.WindowManager;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme();
        super.onCreate(savedInstanceState);

        // remove title and set full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_welcome);

        SharedPreferences userLoginPreferences = PreferenceManager.getDefaultSharedPreferences(WelcomeActivity.this);
        SharedPreferences.Editor spEditor = userLoginPreferences.edit();

        Boolean isLoggedIn = userLoginPreferences.getBoolean("isLoggedIn", false);

        if(isLoggedIn){
            new Handler().postDelayed(new Runnable() {
                public void run() {
            Intent dashBoardIntent = new Intent(WelcomeActivity.this,DashBoardActivity.class);
            startActivity(dashBoardIntent);
            finish();
                }
            },3000);

        }
        else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
            Intent logInIntent = new Intent(WelcomeActivity.this, LogInActivity.class);
            startActivity(logInIntent);
            finish();
                }
            },3000);
        }
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
}
