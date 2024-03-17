package com.unipy.asaris.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
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

public class Query extends AppCompatActivity {

    private PieChart pie_age,pie_gender,pie_active;
    private TextView avgAgeV, over65V;
    //SHARED PREFERENCES
    SharedPreferences preferences;
    String date;
    long sum_age=0;
    List<String> cases= new ArrayList<String>();
    int years010=0, years1020=0, years2030=0, years3040=0, years4050=0, years5060=0, years6070=0, years7080=0, years8090=0, years90plus=0;
    int loop=0,genderM=0,genderF=0,old=0,active_cases=0,inactive_cases=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        avgAgeV = findViewById(R.id.avgAgeView);
        over65V = findViewById(R.id.over65View);

        //Age Pie
        pie_age = findViewById(R.id.pie_age);
        pie_age.getDescription().setEnabled(false);
        pie_age.setUsePercentValues(true);
        pie_age.setExtraOffsets(5,10,5,5);
        pie_age.setDragDecelerationFrictionCoef(0.95f);
        pie_age.setCenterText(generateCenterSpannableText("Age Distribution Pie"));
        pie_age.setDrawHoleEnabled(true);
        pie_age.setHoleColor(Color.WHITE);
        pie_age.setTransparentCircleColor(Color.WHITE);
        pie_age.setTransparentCircleAlpha(0);
        pie_age.setHoleRadius(45f);
        pie_age.setTransparentCircleRadius(61f);
        pie_age.setDrawCenterText(true);
        pie_age.setRotationAngle(0);
        pie_age.setRotationEnabled(true);
        pie_age.setHighlightPerTapEnabled(true);
        // add a selection listener
        pie_age.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                Log.i("VAL SELECTED",
                        "Value: " + e.getY() + ", index: " + h.getX()
                                + ", DataSet index: " + h.getDataSetIndex());
                if(h.getX() == 0){
                    Toast.makeText(Query.this, "0-10 year old cases : "+years010, Toast.LENGTH_SHORT).show();
                }else if(h.getX() == 1){
                    Toast.makeText(Query.this, "10-20 year old cases : "+years1020, Toast.LENGTH_SHORT).show();
                }else if(h.getX() == 2){
                    Toast.makeText(Query.this, "20-30 year old cases : "+years2030, Toast.LENGTH_SHORT).show();
                }else if(h.getX() == 3){
                    Toast.makeText(Query.this, "30-40 year old cases : "+years3040, Toast.LENGTH_SHORT).show();
                }else if(h.getX() == 4){
                    Toast.makeText(Query.this, "40-50 year old cases : "+years4050, Toast.LENGTH_SHORT).show();
                }else if(h.getX() == 5){
                    Toast.makeText(Query.this, "50-60 year old cases : "+years5060, Toast.LENGTH_SHORT).show();
                }else if(h.getX() == 6){
                    Toast.makeText(Query.this, "60-70 year old cases : "+years6070, Toast.LENGTH_SHORT).show();
                }else if(h.getX() == 7){
                    Toast.makeText(Query.this, "70-80 year old cases : "+years7080, Toast.LENGTH_SHORT).show();
                }else if(h.getX() == 8){
                    Toast.makeText(Query.this, "80-90 year old cases : "+years8090, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Query.this, "90+ year old cases : "+years90plus, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected() {
                Log.i("PieChart", "nothing selected");
            }
        });
        pie_age.animateY(1400, Easing.EaseInOutQuad);
        // entry label styling
        pie_age.setEntryLabelColor(Color.WHITE);
        pie_age.setEntryLabelTextSize(8f);



        //Gender Pie
        pie_gender = findViewById(R.id.pie_gender);
        pie_gender.getDescription().setEnabled(false);
        pie_gender.setUsePercentValues(true);
        pie_gender.setExtraOffsets(5,10,5,5);
        pie_gender.setDragDecelerationFrictionCoef(0.95f);
        pie_gender.setCenterText(generateCenterSpannableText("Gender Distribution Pie"));
        pie_gender.setDrawHoleEnabled(true);
        pie_gender.setHoleColor(Color.WHITE);
        pie_gender.setTransparentCircleColor(Color.WHITE);
        pie_gender.setTransparentCircleAlpha(0);
        pie_gender.setHoleRadius(45f);
        pie_gender.setTransparentCircleRadius(61f);
        pie_gender.setDrawCenterText(true);
        pie_gender.setRotationAngle(0);
        pie_gender.setRotationEnabled(true);
        pie_gender.setHighlightPerTapEnabled(true);
        // add a selection listener
        pie_gender.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                Log.i("VAL SELECTED",
                        "Value: " + e.getY() + ", index: " + h.getX()
                                + ", DataSet index: " + h.getDataSetIndex());
                if(h.getX() == 0){
                    Toast.makeText(Query.this, "Male Cases : "+genderM, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Query.this, "Female cases : "+genderF, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected() {
                Log.i("PieChart", "nothing selected");
            }
        });
        pie_gender.animateY(1400, Easing.EaseInOutQuad);
        // entry label styling
        pie_gender.setEntryLabelColor(Color.WHITE);
        pie_gender.setEntryLabelTextSize(8f);


        //Activity Pie
        pie_active = findViewById(R.id.pie_active);
        pie_active.getDescription().setEnabled(false);
        pie_active.setUsePercentValues(true);
        pie_active.setExtraOffsets(5,10,5,5);
        pie_active.setDragDecelerationFrictionCoef(0.95f);
        pie_active.setCenterText(generateCenterSpannableText("Active Case Distribution Pie"));
        pie_active.setDrawHoleEnabled(true);
        pie_active.setHoleColor(Color.WHITE);
        pie_active.setTransparentCircleColor(Color.WHITE);
        pie_active.setTransparentCircleAlpha(0);
        pie_active.setHoleRadius(45f);
        pie_active.setTransparentCircleRadius(61f);
        pie_active.setDrawCenterText(true);
        pie_active.setRotationAngle(0);
        pie_active.setRotationEnabled(true);
        pie_active.setHighlightPerTapEnabled(true);
        // add a selection listener
        pie_active.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                Log.i("VAL SELECTED",
                        "Value: " + e.getY() + ", index: " + h.getX()
                                + ", DataSet index: " + h.getDataSetIndex());
                if(h.getX() == 0){
                    Toast.makeText(Query.this, "Active cases : "+active_cases, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Query.this, "Inactive cases : "+inactive_cases, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected() {
                Log.i("PieChart", "nothing selected");
            }
        });
        pie_active.animateY(1400, Easing.EaseInOutQuad);
        // entry label styling
        pie_active.setEntryLabelColor(Color.WHITE);
        pie_active.setEntryLabelTextSize(8f);



        //shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //LOAD DEFAULT STATISTICS
        // Get instance of firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //get user id
        String user = preferences.getString("mykey3", "no value yet..");
        //get reference
        int i =Integer.parseInt(preferences.getString("mykey4","0"));
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);


        if (i!=0) {
            for (int x = 1; x < i; x++) {
                //get firebase ref
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference newsRef = rootRef.child("users").child(user).child("cases").child("case " + x);
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //store all values in list cases
                            String desc = ds.getValue(String.class);
                            cases.add(desc);
                        }

                        try {
                            //get date values from database
                            //birth date
                            Date dateObj1 = sdf.parse(cases.get(loop*8+1));
                            //case date
                            Date dateObj2 = sdf.parse(cases.get(loop*8+2));
                            //find age
                            long case_age = 0;
                            if (dateObj2 != null && dateObj1 != null) {
                                case_age = (dateObj2.getTime() - dateObj1.getTime()) / (24 * 60 * 60 * 1000L *365);
                            }
                            //sum ages
                            if(case_age <= 10 ){
                                years010++;
                            }else if(case_age <= 20){
                                years1020++;
                            }else if(case_age <= 30){
                                years2030++;
                            }else if(case_age <= 40){
                                years3040++;
                            }else if(case_age <= 50){
                                years4050++;
                            }else if(case_age<= 60){
                                years5060++;
                            }else if(case_age <= 70){
                                years6070++;
                            }else if(case_age <= 80){
                                years7080++;
                            }else if(case_age <= 90){
                                years8090++;
                            }else{
                                years90plus++;
                            }
                            sum_age+=case_age;
                            //get current time
                            Date now = new Date();
                            long days_past= 0;
                            if (dateObj2 != null) {
                                days_past = ((now.getTime()-dateObj2.getTime())/(1000*60*60*24))+1;
                            }
                            Log.d("id", "days= " + now +"-"+ dateObj2 + " "+ days_past);
                            //find gender percentage
                            if(cases.get(loop*8+5).contains("Male")){
                                genderM++;
                            }else{
                                genderF++;
                            }
                            //find older than 65 percentage
                            if (case_age>65){
                                old++;
                            }
                            if(days_past<15){
                                active_cases++;
                            }
                            else{
                                inactive_cases++;
                            }
                            //fill lists

                            ArrayList<PieEntry> entries = new ArrayList<>();
                            if(years010 != 0){
                                entries.add(new PieEntry( (float) years010, "0-10"));
                            }else{
                                entries.add(new PieEntry(0));
                            }

                            if(years1020 != 0){
                                entries.add(new PieEntry( (float) years1020, "10-20"));
                            }else{
                                entries.add(new PieEntry(0));
                            }

                            if(years2030 != 0){
                                entries.add(new PieEntry( (float) years2030, "20-30"));
                            }else{
                                entries.add(new PieEntry(0));
                            }

                            if(years3040 != 0){
                                entries.add(new PieEntry( (float) years3040, "30-40"));
                            }else{
                                entries.add(new PieEntry(0));
                            }

                            if(years4050 != 0){
                                entries.add(new PieEntry( (float) years4050, "40-50"));
                            }else{
                                entries.add(new PieEntry(0));
                            }

                            if(years5060 != 0){
                                entries.add(new PieEntry( (float) years5060, "50-60"));
                            }else{
                                entries.add(new PieEntry(0));
                            }

                            if(years6070 != 0){
                                entries.add(new PieEntry( (float) years6070, "60-70"));
                            }else{
                                entries.add(new PieEntry(0));
                            }

                            if(years7080 != 0){
                                entries.add(new PieEntry( (float) years7080, "70-80"));
                            }else{
                                entries.add(new PieEntry(0));
                            }

                            if(years8090 != 0){
                                entries.add(new PieEntry( (float) years8090, "80-90"));
                            }else{
                                entries.add(new PieEntry(0));
                            }

                            if(years90plus != 0){
                                entries.add(new PieEntry( (float) years90plus, "90+"));
                            }else{
                                entries.add(new PieEntry(0));
                            }
                            PieDataSet dataSet = new PieDataSet(entries, "Ages");
                            setData(dataSet, pie_age);
                            //genders
                            entries = new ArrayList<>();
                            entries.add(new PieEntry((float)genderM,"Male"));
                            entries.add(new PieEntry((float)genderF, "Female"));
                            dataSet = new PieDataSet(entries, "Genders");
                            setData(dataSet, pie_gender);
                            //active cases
                            entries = new ArrayList<>();
                            entries.add(new PieEntry((float)active_cases, "Active"));
                            entries.add(new PieEntry((float)inactive_cases, "Inactive"));
                            dataSet = new PieDataSet(entries, "Virus Activity");
                            setData(dataSet, pie_active);


                            //show values
                            avgAgeV.setText("Average Age= "+((float)sum_age/(i-1)));
                            //test2.setText("Males: "+ ((float)gender/(i-1)*100)+"%"+ System.lineSeparator() +"Females: "+ (100-(((float) gender/(i-1))*100))+"%");
                            over65V.setText("Older than 65: "+ ((float)old/(i-1))*100+"%");
                            //test4.setText("Active cases: "+active_cases);
                            //test5.setText("Closed cases: "+ (i-active_cases-1));
                            float test=((float) old/(i-1))*100;

                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.d("id", e.getMessage());
                        }
                        loop++;
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("id", databaseError.getMessage());
                    }
                };
                newsRef.addListenerForSingleValueEvent(valueEventListener);
            }
        }
    }

    private void setData(PieDataSet pieDataSet, PieChart chart){
        pieDataSet.setDrawIcons(false);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setIconsOffset(new MPPointF(0,40));
        pieDataSet.setSelectionShift(5f);
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        pieDataSet.setColors(colors);
        PieData data = new PieData(pieDataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        chart.setData(data);
        // undo all highlights
        chart.highlightValues(null);
        chart.invalidate();
    }


    private SpannableString generateCenterSpannableText(String message) {
        SpannableString s = new SpannableString(message);
        s.setSpan(new RelativeSizeSpan(1.5f), 0, message.length(), 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 0,  20, 0);
        return s;
    }

    public void back(View view){
        Intent intent = new Intent(this,MainMenu.class);
        startActivityForResult(intent,456);
    }


}