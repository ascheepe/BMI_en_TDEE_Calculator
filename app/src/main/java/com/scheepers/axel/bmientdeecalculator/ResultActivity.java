package com.scheepers.axel.bmientdeecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView textViewBMIResult = findViewById(R.id.textViewBMIResult);
        TextView textViewTDEEResult = findViewById(R.id.textViewTDEEResult);
        TextView textViewTargetWeightResult = findViewById(R.id.textViewTargetWeightResult);
        TextView textViewDetails = findViewById(R.id.textViewDetails);

        Intent intent = getIntent();
        long age = intent.getLongExtra("AGE", -1);
        long height = intent.getLongExtra("HEIGHT", -1);
        long weight = intent.getLongExtra("WEIGHT", -1);
        boolean isMale = intent.getBooleanExtra("IS_MALE", false);
        int spinnerIndex = intent.getIntExtra("SPINNER_INDEX", -1);
        double bmi, tdee, targetWeight;

        if (age == -1 || height == -1 || weight == -1) {
            textViewDetails.setText(R.string.data_retrieve_error);
            return;
        }

        tdee = 10.0 * weight + 6.25 * height - 5.0 * age;
        if (isMale) {
            tdee += 5;
        } else {
            tdee -= 161;
        }
        switch (spinnerIndex) {
            case 0: tdee *= 1.200; break;
            case 1: tdee *= 1.375; break;
            case 2: tdee *= 1.550; break;
            case 3: tdee *= 1.725; break;
            case 4: tdee *= 1.900; break;
        }

        bmi = (double) weight / ((height / 100.0) * (height / 100.0));
        targetWeight = 21.75 * (height / 100.0) * (height / 100.0);

        textViewBMIResult.setText(String.format(new Locale("NL", "nl"),
                "%d", Math.round(bmi)));
        textViewTDEEResult.setText(String.format(new Locale("NL", "nl"),
                "%d", Math.round(tdee)));
        textViewTargetWeightResult.setText(String.format(new Locale("NL", "nl"),
                "%d", Math.round(targetWeight)));

        if (bmi < 16) {
            textViewDetails.setText(R.string.extreme_underweight);
        } else if (bmi >= 16 && bmi < 17) {
            textViewDetails.setText(R.string.much_underweight);
        } else if (bmi >= 17 && bmi < 18.5) {
            textViewDetails.setText(R.string.underweight);
        } else if (bmi >= 18.5 && bmi < 25) {
            textViewDetails.setText(R.string.normal);
        } else if (bmi >= 25 && bmi < 30) {
            textViewDetails.setText(R.string.overweight);
        } else if (bmi >= 30 && bmi < 35) {
            textViewDetails.setText(R.string.much_overweight);
        } else if (bmi >= 35 && bmi < 40) {
            textViewDetails.setText(R.string.extreme_overweight);
        } else {
            textViewDetails.setText(R.string.morbid_obesity);
        }
    }
}