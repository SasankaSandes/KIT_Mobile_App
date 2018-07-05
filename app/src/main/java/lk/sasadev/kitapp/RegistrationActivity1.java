package lk.sasadev.kitapp;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class RegistrationActivity1 extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference driverDatabaseRef;
    Button nextButton;
    EditText firstNameText;
    EditText lastNameText;
    EditText emailText;
    EditText passwordText;
    EditText passwordConfirmText;
    ProgressBar progressBar;

    String firstName;
    String lastName;
    String email;
    String password;
    String confirmPassword;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration1);

        firebaseAuth = FirebaseAuth.getInstance();
        driverDatabaseRef = FirebaseDatabase.getInstance().getReference("Driver");

        firstNameText = findViewById(R.id.regTextFirstName);
        lastNameText = findViewById(R.id.regTextLastName);
        emailText = findViewById(R.id.regTextEmail);
        passwordText = findViewById(R.id.regTextPassword);
        passwordConfirmText = findViewById(R.id.regTextConfirmPassword);
        nextButton = findViewById(R.id.reg1ButtonNext);
        progressBar =findViewById(R.id.reg1ProgressBar);

        progressBar.setVisibility(View.GONE);



    }

    @Override
    protected void onStart() {
        super.onStart();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstName = firstNameText.getText().toString().trim();
                lastName = lastNameText.getText().toString().trim();
                email = emailText.getText().toString().trim();
                password = passwordText.getText().toString().trim();
                confirmPassword = passwordConfirmText.getText().toString().trim();

                if(validation(firstName, lastName, email, password, confirmPassword)){
                    registerUser(email,password);
                }

            }
        });
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

    public boolean validation(String firstName, String lastName, String email, String password, String confirmPassword){
        boolean b=true;
        if(firstName.equals("")) {
            firstNameText.setError("first name is required!");
            firstNameText.requestFocus();
            b = false;
        }
        if(lastName.equals("")) {
            lastNameText.setError("last name is required!");
            lastNameText.requestFocus();
            b = false;
        }
        if(!Pattern.matches("^[a-zA-Z]+$",firstName)) {
            firstNameText.setError("first name should be one name, contain only letters!");
            firstNameText.requestFocus();
            b = false;
        }
        if(!Pattern.matches("^[a-zA-Z]+$",lastName)) {
            lastNameText.setError("last name should be one name, contains only letters!");
            lastNameText.requestFocus();
            b = false;
        }
        if(email.equals("")) {
            emailText.setError("email is required!");
            emailText.requestFocus();
            b = false;
        }

        if(password.equals("")) {
            passwordText.setError("password is required!");
            emailText.requestFocus();
            b = false;
        }

        if(confirmPassword.equals("")) {
            passwordConfirmText.setError("password confirmation is required!");
            emailText.requestFocus();
            b = false;
        }

        if(!password.equals(confirmPassword)){
            passwordConfirmText.setError("passwords did not matched");
            passwordText.requestFocus();
            passwordConfirmText.requestFocus();
            b=false;
        }

        if(!password.equals("") && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("please enter a valid email!");
            emailText.requestFocus();
            b = false;
        }
        /*if(!userNotExist(email)){
            emailText.setError("email is already register!");
            emailText.requestFocus();
            b=false;
        }*/
        return b;
    }

    /*public boolean userNotExist(String email){
        notExist=true;
        if (email.length()>0) {
            firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    notExist = task.getResult().getSignInMethods().isEmpty();
                }
            });

        }
        return notExist;
    }
*/
    public void registerUser(final String email, String password){
        nextButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            doToast("user successfully added");


                        }
                        else {
                            doToast("user sign up failed");
                        }
                        if(task.isCanceled()){
                            doToast("user adding cancelled");
                        }
                        if(task.isComplete()){
                            progressBar.setVisibility(View.GONE);
                            nextButton.setVisibility(View.VISIBLE);
                            String uid = firebaseAuth.getCurrentUser().getUid();
                            if(uid != null){
                                driverDatabaseRef.child(uid).child("FirstName").setValue(firstName);
                                driverDatabaseRef.child(uid).child("LastName").setValue(lastName);
                                driverDatabaseRef.child(uid).child("Email").setValue(email);

                            }
                        }

                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthUserCollisionException) {
                    emailText.setError("email already registered");
                    emailText.requestFocus();
                }
                if(e instanceof FirebaseAuthWeakPasswordException){
                    passwordText.setError("password is weak, must be at least 6 characters");
                }

            }
        });
    }

    public void doToast (String toastText){
        Toast.makeText(this,toastText,Toast.LENGTH_SHORT).show();
    }
}
