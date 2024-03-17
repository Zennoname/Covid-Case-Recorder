package com.unipy.asaris.finalproject;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainMenu extends AppCompatActivity implements LocationListener{


    private static final int REC_RESULT = 653;
    //for shared preferences and gps
    LocationManager locationManager;
    //SHARED PREFERENCES
    SharedPreferences preferences;
    //
    MyTts myTts;
    boolean alerted = false;
    int loop=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);



        // speaking class
        myTts = new MyTts(this);
        // arxikopoihsh gia gps
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //shared pref
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //activate gps
        gps(null);
        Correctnum();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REC_RESULT && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String order = matches.get(0);

            //voice commands
            if (order.toUpperCase().equals("ADD")) {
                addcase(null);
                Toast.makeText(this, order, Toast.LENGTH_LONG).show();
            }
            else if (order.toUpperCase().equals("STATISTICS")){
                makequery(null);
                Toast.makeText(this, order, Toast.LENGTH_LONG).show();
            }
            else if (order.toUpperCase().equals("MAP")){
                map(null);
                Toast.makeText(this, order, Toast.LENGTH_LONG).show();
            }
            else if (order.toUpperCase().equals("EDIT")){
                edit(null);
                Toast.makeText(this, order, Toast.LENGTH_LONG).show();
            }
            /*else if (order.toUpperCase().equals("EXIT")){
                exit(null);
                Toast.makeText(this, order, Toast.LENGTH_LONG).show();
            }*/
            //color changing for voice recognition
            if (matches.contains("red"))
                getWindow().getDecorView().setBackgroundColor(Color.RED);
            if (matches.contains("blue"))
                getWindow().getDecorView().setBackgroundColor(Color.BLUE);
            if (matches.contains("yellow"))
                getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
        }

    }

    //voice recognizer
    public void recognize(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please say something!");
        startActivityForResult(intent, REC_RESULT);

    }


    //next activity
    public void addcase(View view){
        Intent intent = new Intent(this,NewCase.class);
        startActivityForResult(intent,456);
    }
    //STATISTICS
    public void makequery(View view){
        Intent intent = new Intent(this,Query.class);
        startActivityForResult(intent,456);
    }

    //close app
    public void exit(View view){
       // finish();
       // System.exit(0);
    }

    public  void map (View view){
        Intent intent = new Intent(this,Map.class);
        startActivityForResult(intent,456);
    }

    public  void edit (View view){
        Intent intent = new Intent(this,Edit.class);
        startActivityForResult(intent,456);
    }

    //gps
    public void gps(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.
                    requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 234);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, this);
    }

    double x=0,y=0;

    //get location attributes
    //γγππ συντεταγμενες 38.033337083719445, 23.79366099312106
    @Override
    public void onLocationChanged(Location location) {
        x = location.getLatitude();
        y = location.getLongitude();

        if(!alerted){
            if ((38>x || x>38.06) || (y<23.5||y>23.8)){
                Toast.makeText(this, "Go back to work", Toast.LENGTH_LONG).show();

                myTts.speak("Go back to work");
            }
            alerted = true;
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void Correctnum(){

        //LOAD DEFAULT STATISTICS
        // Get instance of firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //get user id
        String user = preferences.getString("mykey3", "no value yet..");

        //get reference



        //get firebase ref
        DatabaseReference newsRef = FirebaseDatabase.getInstance().getReference().child("users").child(user).child("cases");
        //int i =Integer.valueOf(preferences.getString("mykey4","0"));
        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    loop++;


                }
                Log.d("id", String.valueOf(loop));
                int i =Integer.parseInt(preferences.getString("mykey4","0"));
                if(i!=loop){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("mykey4", String.valueOf(loop+1));
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("id", databaseError.getMessage());
            }
         };
         newsRef.addListenerForSingleValueEvent(valueEventListener);

    }



}