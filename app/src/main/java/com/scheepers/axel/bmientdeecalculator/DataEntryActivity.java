package com.scheepers.axel.bmientdeecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;


public class DataEntryActivity extends AppCompatActivity {
    private Spinner spinnerActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);

        spinnerActivity = findViewById(R.id.spinnerActivity);
        spinnerActivity.setSelection(1);

        Button buttonActivityDataNext = findViewById(R.id.buttonActivityDataNext);
        buttonActivityDataNext.setOnClickListener(this::onNextButtonClick);
    }

    public void onNextButtonClick(View view) {
        Intent intent = new Intent(this, ResultActivity.class);
        EditText editTextNumberAge = findViewById(R.id.editTextNumberAge);
        EditText editTextNumberHeight = findViewById(R.id.editTextNumberHeight);
        EditText editTextNumberWeight = findViewById(R.id.editTextNumberWeight);
        RadioButton radioButtonMale = findViewById(R.id.radioButtonMale);
        long age, height, weight;
        boolean inputError = false;

        try {
            age = Long.parseLong(editTextNumberAge.getText().toString());
            height = Long.parseLong(editTextNumberHeight.getText().toString());
            weight = Long.parseLong(editTextNumberWeight.getText().toString());
        } catch (Exception e) {
            editTextNumberAge.setText("");
            editTextNumberHeight.setText("");
            editTextNumberWeight.setText("");
            return;
        }

        if (age < 10 || age > 100) {
            editTextNumberAge.setText("");
            inputError = true;
        }

        if (height < 100 || height > 300) {
            editTextNumberHeight.setText("");
            inputError = true;
        }

        if (weight < 20 || weight > 700) {
            editTextNumberWeight.setText("");
            inputError = true;
        }

        if (inputError) {
            return;
        }

        intent.putExtra("AGE", age);
        intent.putExtra("HEIGHT", height);
        intent.putExtra("WEIGHT", weight);
        intent.putExtra("IS_MALE", radioButtonMale.isChecked());
        intent.putExtra("SPINNER_INDEX", spinnerActivity.getSelectedItemPosition());
        startActivity(intent);
    }
}