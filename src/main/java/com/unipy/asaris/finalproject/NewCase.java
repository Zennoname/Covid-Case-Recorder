package com.unipy.asaris.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewCase extends AppCompatActivity {

    Spinner spinner;
    EditText fname,lname,bdate,phone,address,email;
    //SHARED PREFERENCES
    SharedPreferences preferences;
    String error="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_case);
        //spinner
        spinner = findViewById(R.id.spinner);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Male");
        categories.add("Female");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        //texts
        fname= findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        bdate=findViewById(R.id.bdate);
        phone=findViewById(R.id.phone);
        address=findViewById(R.id.address);
        email=findViewById(R.id.email);
        
        //shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


    }


    //connect with firebase
    public  void firebase(View view){

        // Get instance of firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String user = preferences.getString("mykey3", "no value yet..");
        int i =Integer.valueOf(preferences.getString("mykey4","1"));
        DatabaseReference myRef = database.getReference().child("users").child(user).child("cases").child("case "+i);
        myRef.setValue(user);
        Date date = new Date();
        //test validation
        //birth date
        Calendar cal = Calendar.getInstance();
        cal.setLenient(false);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        sdf.setLenient(false);
        boolean given_date=false; boolean given_fname=false;boolean given_lname=false; boolean given_address=false;boolean given_phone=false; boolean given_email=false;
        //check for null
        if (bdate.getText().toString().trim().equals(""))
        {
            Log.d("id", "Birth Date Empty");
            error+=" Date Empty ";
        }
        else {
            try {
                Date given=sdf.parse(bdate.getText().toString());
                Date maxDate= new Date();
                Date minDate= sdf.parse("01/01/1900");

                if(given.getTime()>minDate.getTime() && given.getTime()<maxDate.getTime()){
                    Log.d("id", "Valid " +maxDate +" "+ given + " " + minDate);
                    given_date=true;
                }
                else{
                    Log.d("id", "Invalid " +maxDate + " " + minDate);
                    error+=" Invalid Date ";
                }

            }
            catch (Exception e) {
                Log.d("id", "exception= " + e);
            }
        }
        //first name
        if(fname.getText().toString().trim().equals("")){
            Log.d("id", "First Name Empty");
            error+=" First Name Empty ";
        }
        else{
            given_fname=true;
        }
        //last name
        if(lname.getText().toString().trim().equals("")){
            Log.d("id", "Last Name Empty");
            error+=" Last Name Empty ";
        }
        else{
            given_lname=true;
        }
        //phone
        if(phone.getText().toString().trim().equals("")){
            Log.d("id", "Phone Empty");
            error+=" Phone Empty ";
        }
        else{
            if(phone.getText().toString().length()==10){
                given_phone=true;
            }
            else{
                Log.d("id", "Phone Invalid");
                error+=" Phone Invalid ";
            }
        }

        //address
        if(address.getText().toString().trim().equals("")){
            Log.d("id", "Address Empty");
            error+=" Address Empty ";
        }
        else{
            given_address=true;
        }

        //email
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
            given_email=true;
        }
        else{
            error+=" Email invalid ";
        }

        //save each case data to different nodes
        if(given_address && given_date && given_fname && given_lname && given_phone && given_email) {


            myRef.child("Date").setValue(DateFormat.format("dd/MM/yyyy", date.getTime()));
            myRef.child("First Name").setValue(fname.getText().toString());
            myRef.child("Last Name").setValue(lname.getText().toString());
            myRef.child("Birth Date").setValue(bdate.getText().toString());
            myRef.child("Phone").setValue(phone.getText().toString());
            myRef.child("Address").setValue(address.getText().toString());
            myRef.child("Email").setValue(email.getText().toString());
            myRef.child("Gender").setValue(spinner.getSelectedItem().toString());

            Intent intent = new Intent(this, MainMenu.class);
            startActivityForResult(intent, 456);

            i++;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("mykey4", String.valueOf(i));
            editor.apply();
        }
        else{
             Toast.makeText(this, error, Toast.LENGTH_LONG).show();
             error="";
        }
    }

    public void back(View view){
        Intent intent = new Intent(this,MainMenu.class);
        startActivityForResult(intent,456);
    }

}