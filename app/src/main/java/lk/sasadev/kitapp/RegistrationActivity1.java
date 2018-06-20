package lk.sasadev.kitapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

public class RegistrationActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration1);
    }

    public void goToRegistration2(View view){
        Intent registrationIntent2 = new Intent(RegistrationActivity1.this,RegistrationActivity2.class);
        startActivity(registrationIntent2);
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
