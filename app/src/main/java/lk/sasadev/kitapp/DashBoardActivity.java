package lk.sasadev.kitapp;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.pires.obd.enums.AvailableCommandNames;



public class DashBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setAppTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        setFontFaces();



    }

    private void setAppTheme(){
        //check for night mode status
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            //night_mode_yes set appTheme
            setTheme(R.style.AppTheme);
        }else {
            //night_mode_no set appThemeDay
            setTheme(R.style.AppThemeDay);
        }
    }//set app theme according to the selected mode, day/night

    private  void setFontFaces(){
        //set special font face from asset ttf font
        TextView speedText = findViewById(R.id.speed);
        TextView rpmText = findViewById(R.id.rpm);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/Digital.ttf");
        speedText.setTypeface(typeface);
        rpmText.setTypeface(typeface);
    }
    //set menu options on action bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //choose icon layout base on selected theme
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES) {
            getMenuInflater().inflate(R.menu.menu_night, menu);
        }
        else {
            getMenuInflater().inflate(R.menu.menu_day, menu);
        }
        return true;

    }
    //action bar menu items on select actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //day night theme switch menu item
            case R.id.action_day_night:
                //check current theme and set opposite theme on select
                if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    restartActivity();
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    restartActivity();
                }
                return true;
                //bluetooth setting menu item
            case R.id.action_bluetooth:
                //intent to bluetooth setting activity
                Intent iBluetooth = new Intent(getApplicationContext(),BluetoothSettingsActivity.class);
                startActivity(iBluetooth);
                return true;
            case R.id.action_schedule:
                //intent to schedule activity
                Intent iShedule = new Intent(getApplicationContext(),ScheduleActivity.class);
                startActivity(iShedule);
                return true;
            case R.id.action_warning:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //restart activity method is required to apply theme changes
    public void restartActivity(){
        Intent i = new Intent(getApplicationContext(),DashBoardActivity.class);
        startActivity(i);
        finish();
    }


}
