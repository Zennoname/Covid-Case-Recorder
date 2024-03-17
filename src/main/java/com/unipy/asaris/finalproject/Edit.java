package com.unipy.asaris.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Edit extends AppCompatActivity {

    Spinner spinner,gender;
    SharedPreferences preferences;
    List<String> cases= new ArrayList<String>();
    int loop=0;
    EditText fname,lname,bdate,phone,address,email;
    String error="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //spinner
        gender = findViewById(R.id.gender);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Male");
        categories.add("Female");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        gender.setAdapter(dataAdapter);

        //texts
        fname= findViewById(R.id.edit_fname);
        lname = findViewById(R.id.edit_lname);
        bdate=findViewById(R.id.edit_bdate);
        phone = findViewById(R.id.edit_phone);
        address = findViewById(R.id.edit_address);
        email = findViewById(R.id.edit_email);

        //shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //get user id
        String user = preferences.getString("mykey3", "no value yet..");
        //get reference
        int i = Integer.valueOf(preferences.getString("mykey4", "0"));
        //spinner
        spinner = findViewById(R.id.spinner2);
        // Spinner Drop down elements
        List<String> categories2 = new ArrayList<String>();

        //get firebase ref
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        for (int x = 1; x < i; x++) {
            categories2.add(String.valueOf(x));
        }

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);
            // Drop down layout style - list view with radio button
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter2);


            //when spinner changes value
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    cases.clear();
                    int x = Integer.valueOf(spinner.getSelectedItem().toString());
                    DatabaseReference newsRef = rootRef.child("users").child(user).child("cases").child("case " + x);
                    Log.d("id", String.valueOf(x));
                    ValueEventListener valueEventListener = new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                //store all values in list cases
                                String desc = ds.getValue(String.class);
                                cases.add(desc);
                                Log.d("id", desc);
                                if (cases.size() == 8) {
                                    bdate.setText(cases.get(1));
                                    fname.setText(cases.get(4));
                                    lname.setText(cases.get(6));
                                    address.setText(cases.get(0));
                                    phone.setText(cases.get(7));
                                    email.setText(cases.get(3));
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d("id", databaseError.getMessage());
                        }
                    };
                    newsRef.addListenerForSingleValueEvent(valueEventListener);
                    Log.d("id", "" + cases.size());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

        }


    public  void firebase(View view){

        // Get instance of firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String user = preferences.getString("mykey3", "no value yet..");
        int x=Integer.valueOf(spinner.getSelectedItem().toString());
        DatabaseReference myRef = database.getReference().child("users").child(user).child("cases").child("case "+x);
        //myRef.setValue(user);
        Date date = new Date();
        //test validation
        //birth date
        Calendar cal = Calendar.getInstance();
        cal.setLenient(false);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        sdf.setLenient(false);
        boolean given_date=false; boolean given_fname=false;boolean given_lname=false; boolean given_address=false;boolean given_phone=false;boolean given_email=false;
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

        //
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


            myRef.child("Date").setValue(cases.get(2));
            myRef.child("First Name").setValue(fname.getText().toString());
            myRef.child("Last Name").setValue(lname.getText().toString());
            myRef.child("Birth Date").setValue(bdate.getText().toString());
            myRef.child("Phone").setValue(phone.getText().toString());
            myRef.child("Address").setValue(address.getText().toString());
            myRef.child("Email").setValue(email.getText().toString());
            myRef.child("Gender").setValue(gender.getSelectedItem().toString());

            Intent intent = new Intent(this, MainMenu.class);
            startActivityForResult(intent, 456);

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