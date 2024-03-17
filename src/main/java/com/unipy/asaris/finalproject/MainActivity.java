package com.unipy.asaris.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText editText2,editText3;
    FirebaseUser currentUser;
    //SHARED PREFERENCES
    SharedPreferences preferences;
    String user_id=null;
    //switch
    Switch lan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        editText2 = findViewById(R.id.editTextTextPassword);
        editText3 = findViewById(R.id.editTextTextPersonName);
        lan= findViewById(R.id.switch1);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String language = preferences.getString("language", "en");
        if (language.equals("el")){
            lan.setChecked(false);
            setLocale("el");
        }
        else{
            lan.setChecked(true);
            setLocale("en");
        }

        lan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setLocale("en");
                } else {
                    setLocale("el");
                }
            }
        });



    }

    private boolean isEmpty(EditText edit) {
        return edit.getText().toString().trim().length() <= 0;
    }

    //Sign up class
    public void signup(View view){
        if( isEmpty(editText2) || isEmpty(editText3)){
            Toast.makeText(this, "Please fill in both your email and password", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(
                //get email and password
                editText3.getText().toString().trim(),editText2.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    //inform user for successful sign up
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Signup success!",Toast.LENGTH_SHORT).show();
                            currentUser = mAuth.getCurrentUser();
                        } else {
                            //show error message
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void signin(View view){
        if( isEmpty(editText2) || isEmpty(editText3)){
            Toast.makeText(this, "Please fill in both your email and password", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(
                //get email and password
                editText3.getText().toString().trim(),editText2.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    //inform for successful sign in
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            currentUser = mAuth.getCurrentUser();
                            user_id=currentUser.getUid();
                            writesp(null);
                            Toast.makeText(getApplicationContext(),"Login success!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MainMenu.class);
                            startActivity(intent);
                        }else {
                            //show error
                            Toast.makeText(getApplicationContext(),
                                    task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //get userid to shared preferences
    public void writesp(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("mykey3", user_id);
        editor.apply();
    }

    //change language
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf,dm);

        String language = preferences.getString("language", "en");
        if (!language.equals(lang)){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("language", lang);
            editor.apply();

            Intent refresh = new Intent(this, MainActivity.class);
            finish();
            startActivity(refresh);
            Toast.makeText(getApplicationContext(),lang,Toast.LENGTH_SHORT).show();
        }

    }

}