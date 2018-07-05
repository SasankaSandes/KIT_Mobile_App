package lk.sasadev.kitapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegistrationActivity2 extends AppCompatActivity {

    Spinner vehicleMakeSpinner;
    DatabaseReference databaseOBDCompatible;
    ArrayList<String> vehicleMakeArray;
    String[] vehicleModelArray;
    String[] mfYearArray;

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        databaseOBDCompatible = FirebaseDatabase.getInstance().getReference().child("OBD Compatible Vehicles"
        );
        Button nextButton = findViewById(R.id.buttonNext);
        vehicleMakeSpinner = findViewById(R.id.spinnerVehicleMake);

        setVehicleSpinner();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoRegitration3 = new Intent(RegistrationActivity2.this,RegistrationActivity3.class);
                startActivity(gotoRegitration3
                );
            }
        });
    }

    private void setVehicleSpinner() {
        databaseOBDCompatible.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()){
                    vehicleMakeArray.add(String.valueOf(dsp.getKey()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(vehicleMakeArray != null) {
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicleMakeArray);
            vehicleMakeSpinner.setAdapter(arrayAdapter);
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
